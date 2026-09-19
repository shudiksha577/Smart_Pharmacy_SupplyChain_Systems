# Build PowerPoint Presentation via COM Automation from slides_data.json
$ErrorActionPreference = "Stop"

$jsonPath = Join-Path $PSScriptRoot "slides_data.json"
$outputPath = Join-Path $PSScriptRoot "Smart_Pharmacy_Supply_Chain_System.pptx"

if (-not (Test-Path $jsonPath)) {
    Write-Error "slides_data.json not found at $jsonPath"
    exit 1
}

$rawJson = Get-Content $jsonPath -Raw -Encoding UTF8
$slides = ConvertFrom-Json $rawJson

Write-Host "Loaded $($slides.Count) slides from slides_data.json"

# Color constants (BGR format for Office COM: R + G*256 + B*65536)
function Color-RGB($r, $g, $b) {
    return [int]($r + ($g * 256) + ($b * 65536))
}

$cBg          = Color-RGB 248 250 252
$cCardBg      = Color-RGB 255 255 255
$cCardBorder  = Color-RGB 226 232 240
$cPrimary     = Color-RGB 13 148 136    # Teal #0d9488
$cPrimaryDark = Color-RGB 15 118 110    # Teal Dark #0f766e
$cNavy        = Color-RGB 15 23 42      # Slate 900 #0f172a
$cBlue        = Color-RGB 37 99 235     # Blue #2563eb
$cTextMain    = Color-RGB 30 41 59      # Slate 800
$cTextMuted   = Color-RGB 100 116 139   # Slate 500
$cHighlightBg = Color-RGB 240 253 250   # Teal Light #f0fdfa
$cHighlightBd = Color-RGB 153 246 228   # Teal Border
$cBadgeBg     = Color-RGB 241 245 249   # Slate 100
$cPassBg      = Color-RGB 220 252 231   # Green 100
$cPassText    = Color-RGB 22 101 52     # Green 800

# Start PowerPoint
Write-Host "Initializing PowerPoint Application..."
$ppt = New-Object -ComObject PowerPoint.Application
$pres = $ppt.Presentations.Add(0) # 0 = msoFalse (window not visible during batch)

# Set 16:9 Widescreen dimensions: 960 x 540 pt (13.33 x 7.5 inches)
$pres.PageSetup.SlideWidth = 960
$pres.PageSetup.SlideHeight = 540

function Add-Header($s, $category, $title, $slideNum) {
    # Top accent bar
    $bar = $s.Shapes.AddShape(1, 0, 0, 960, 4)
    $bar.Fill.Solid()
    $bar.Fill.ForeColor.RGB = $cPrimary
    $bar.Line.Visible = 0

    # Category badge
    $catBox = $s.Shapes.AddTextbox(1, 40, 16, 500, 18)
    $catBox.TextFrame.MarginLeft = 0
    $catBox.TextFrame.MarginTop = 0
    $catBox.TextFrame.TextRange.Text = $category.ToUpper()
    $catBox.TextFrame.TextRange.Font.Name = "Segoe UI"
    $catBox.TextFrame.TextRange.Font.Size = 10
    $catBox.TextFrame.TextRange.Font.Bold = 1
    $catBox.TextFrame.TextRange.Font.Color.RGB = $cPrimary

    # Slide Title
    $titleBox = $s.Shapes.AddTextbox(1, 40, 36, 750, 34)
    $titleBox.TextFrame.MarginLeft = 0
    $titleBox.TextFrame.MarginTop = 0
    $titleBox.TextFrame.TextRange.Text = $title
    $titleBox.TextFrame.TextRange.Font.Name = "Segoe UI"
    $titleBox.TextFrame.TextRange.Font.Size = 20
    $titleBox.TextFrame.TextRange.Font.Bold = 1
    $titleBox.TextFrame.TextRange.Font.Color.RGB = $cNavy

    # Slide Badge
    $badge = $s.Shapes.AddShape(1, 820, 22, 100, 24)
    $badge.Fill.Solid()
    $badge.Fill.ForeColor.RGB = $cBadgeBg
    $badge.Line.Visible = 1
    $badge.Line.ForeColor.RGB = $cCardBorder
    $badge.TextFrame.TextRange.Text = "Slide $slideNum / 20"
    $badge.TextFrame.TextRange.Font.Name = "Segoe UI"
    $badge.TextFrame.TextRange.Font.Size = 10
    $badge.TextFrame.TextRange.Font.Bold = 1
    $badge.TextFrame.TextRange.Font.Color.RGB = $cTextMuted

    # Footer
    $foot = $s.Shapes.AddTextbox(1, 40, 514, 880, 20)
    $foot.TextFrame.MarginLeft = 0
    $foot.TextFrame.TextRange.Text = "Smart Pharmacy Supply Chain System | Final Year Capstone Project"
    $foot.TextFrame.TextRange.Font.Name = "Segoe UI"
    $foot.TextFrame.TextRange.Font.Size = 9
    $foot.TextFrame.TextRange.Font.Color.RGB = $cTextMuted
}

function Add-Card($s, $left, $top, $width, $height, $title, $bodyText, $borderRgb = $cCardBorder, $fillRgb = $cCardBg) {
    $card = $s.Shapes.AddShape(1, $left, $top, $width, $height)
    $card.Fill.Solid()
    $card.Fill.ForeColor.RGB = $fillRgb
    $card.Line.Visible = 1
    $card.Line.ForeColor.RGB = $borderRgb
    $card.Line.Weight = 1

    $tb = $s.Shapes.AddTextbox(1, $left + 14, $top + 10, $width - 28, $height - 20)
    $tb.TextFrame.WordWrap = 1
    $tb.TextFrame.MarginLeft = 0
    $tb.TextFrame.MarginRight = 0
    $tb.TextFrame.MarginTop = 0
    $tb.TextFrame.MarginBottom = 0

    $fullText = "$title`n$bodyText"
    $tb.TextFrame.TextRange.Text = $fullText
    
    $pCount = $tb.TextFrame.TextRange.Paragraphs().Count
    if ($pCount -ge 1) {
        $p1 = $tb.TextFrame.TextRange.Paragraphs(1)
        $p1.Font.Name = "Segoe UI"
        $p1.Font.Size = 13
        $p1.Font.Bold = 1
        $p1.Font.Color.RGB = $cNavy
    }
    for ($i = 2; $i -le $pCount; $i++) {
        $pi = $tb.TextFrame.TextRange.Paragraphs($i)
        $pi.Font.Name = "Segoe UI"
        $pi.Font.Size = 11
        $pi.Font.Bold = 0
        $pi.Font.Color.RGB = $cTextMain
    }
}

function Add-HighlightBox($s, $left, $top, $width, $height, $text) {
    $box = $s.Shapes.AddShape(1, $left, $top, $width, $height)
    $box.Fill.Solid()
    $box.Fill.ForeColor.RGB = $cHighlightBg
    $box.Line.Visible = 1
    $box.Line.ForeColor.RGB = $cHighlightBd
    $box.Line.Weight = 1

    $tb = $s.Shapes.AddTextbox(1, $left + 14, $top + 8, $width - 28, $height - 16)
    $tb.TextFrame.WordWrap = 1
    $tb.TextFrame.MarginLeft = 0
    $tb.TextFrame.MarginTop = 0
    $tb.TextFrame.TextRange.Text = $text
    $tb.TextFrame.TextRange.Font.Name = "Segoe UI"
    $tb.TextFrame.TextRange.Font.Size = 11.5
    $tb.TextFrame.TextRange.Font.Color.RGB = $cPrimaryDark
}

function Add-TableHelper($s, $left, $top, $width, $height, $headers, $colWidths, $rows) {
    $rowCount = $rows.Count + 1
    $colCount = $headers.Count
    $tblShape = $s.Shapes.AddTable($rowCount, $colCount, $left, $top, $width, $height)
    $tbl = $tblShape.Table

    # Column widths
    for ($c = 0; $c -lt $colCount; $c++) {
        $tbl.Columns($c + 1).Width = $colWidths[$c]
    }

    # Header row
    for ($c = 0; $c -lt $colCount; $c++) {
        $cell = $tbl.Cell(1, $c + 1)
        $cell.Shape.Fill.Solid()
        $cell.Shape.Fill.ForeColor.RGB = Color-RGB 241 245 249
        $tr = $cell.Shape.TextFrame.TextRange
        $tr.Text = $headers[$c]
        $tr.Font.Name = "Segoe UI"
        $tr.Font.Size = 11
        $tr.Font.Bold = 1
        $tr.Font.Color.RGB = $cNavy
        $cell.Shape.TextFrame.MarginLeft = 8
        $cell.Shape.TextFrame.MarginRight = 8
    }

    # Body rows
    for ($r = 0; $r -lt $rows.Count; $r++) {
        $bg = if ($r % 2 -eq 0) { Color-RGB 255 255 255 } else { Color-RGB 248 250 252 }
        for ($c = 0; $c -lt $colCount; $c++) {
            $cell = $tbl.Cell($r + 2, $c + 1)
            $cell.Shape.Fill.Solid()
            $cell.Shape.Fill.ForeColor.RGB = $bg
            $tr = $cell.Shape.TextFrame.TextRange
            $tr.Text = [string]$rows[$r][$c]
            $tr.Font.Name = "Segoe UI"
            $tr.Font.Size = 10
            $tr.Font.Color.RGB = $cTextMain
            if ($c -eq 0) { $tr.Font.Bold = 1 }
            if ($headers[$c] -eq "Result" -and $rows[$r][$c] -eq "PASS") {
                $tr.Font.Bold = 1
                $tr.Font.Color.RGB = Color-RGB 22 101 52
            }
            $cell.Shape.TextFrame.MarginLeft = 8
            $cell.Shape.TextFrame.MarginRight = 8
        }
    }
}

# Iterate through each slide definition
foreach ($sl in $slides) {
    Write-Host "Creating Slide $($sl.num): $($sl.title)..."
    $s = $pres.Slides.Add($sl.num, 12) # 12 = ppLayoutBlank
    $s.Background.Fill.Solid()
    $s.Background.Fill.ForeColor.RGB = $cBg

    if ($sl.type -eq "title") {
        # Top badge
        $tbBadge = $s.Shapes.AddShape(1, 280, 45, 400, 28)
        $tbBadge.Fill.Solid()
        $tbBadge.Fill.ForeColor.RGB = Color-RGB 204 251 241
        $tbBadge.Line.Visible = 1
        $tbBadge.Line.ForeColor.RGB = $cPrimary
        $tbBadge.TextFrame.TextRange.Text = $sl.badge
        $tbBadge.TextFrame.TextRange.Font.Name = "Segoe UI"
        $tbBadge.TextFrame.TextRange.Font.Size = 11
        $tbBadge.TextFrame.TextRange.Font.Bold = 1
        $tbBadge.TextFrame.TextRange.Font.Color.RGB = $cPrimaryDark

        # Main Title
        $tBox = $s.Shapes.AddTextbox(1, 50, 85, 860, 60)
        $tBox.TextFrame.TextRange.Text = $sl.title
        $tBox.TextFrame.TextRange.Font.Name = "Segoe UI"
        $tBox.TextFrame.TextRange.Font.Size = 30
        $tBox.TextFrame.TextRange.Font.Bold = 1
        $tBox.TextFrame.TextRange.Font.Color.RGB = $cNavy
        $tBox.TextFrame.TextRange.ParagraphFormat.Alignment = 2 # Center

        # Subtitle
        $subBox = $s.Shapes.AddTextbox(1, 50, 150, 860, 35)
        $subBox.TextFrame.TextRange.Text = $sl.subtitle
        $subBox.TextFrame.TextRange.Font.Name = "Segoe UI"
        $subBox.TextFrame.TextRange.Font.Size = 15
        $subBox.TextFrame.TextRange.Font.Color.RGB = $cBlue
        $subBox.TextFrame.TextRange.ParagraphFormat.Alignment = 2 # Center

        # Student Details Card (Left)
        $card1 = $s.Shapes.AddShape(1, 60, 210, 400, 260)
        $card1.Fill.Solid()
        $card1.Fill.ForeColor.RGB = $cCardBg
        $card1.Line.Visible = 1
        $card1.Line.ForeColor.RGB = $cCardBorder
        $card1.Line.Weight = 1.5

        $sText = "Student Details`n`n" +
                 "Student 1: $($sl.students[0].name)`n" +
                 "Register Number: $($sl.students[0].reg)`n`n" +
                 "Student 2: $($sl.students[1].name)`n" +
                 "Register Number: $($sl.students[1].reg)`n`n" +
                 "Team ID: $($sl.teamId)"

        $stBox = $s.Shapes.AddTextbox(1, 80, 225, 360, 230)
        $stBox.TextFrame.WordWrap = 1
        $stBox.TextFrame.TextRange.Text = $sText
        $stBox.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $stBox.TextFrame.TextRange.Paragraphs(1).Font.Size = 16
        $stBox.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $stBox.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cPrimary
        for ($i = 2; $i -le $stBox.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $stBox.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 12
            $p.Font.Color.RGB = $cTextMain
        }

        # Academic Details Card (Right)
        $card2 = $s.Shapes.AddShape(1, 500, 210, 400, 260)
        $card2.Fill.Solid()
        $card2.Fill.ForeColor.RGB = $cCardBg
        $card2.Line.Visible = 1
        $card2.Line.ForeColor.RGB = $cCardBorder
        $card2.Line.Weight = 1.5

        $aText = "Academic Details`n`n" +
                 "Department:`n$($sl.department)`n`n" +
                 "Institution:`n$($sl.institution)`n`n" +
                 "Academic Year:`n$($sl.academicYear)"

        $atBox = $s.Shapes.AddTextbox(1, 520, 225, 360, 230)
        $atBox.TextFrame.WordWrap = 1
        $atBox.TextFrame.TextRange.Text = $aText
        $atBox.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $atBox.TextFrame.TextRange.Paragraphs(1).Font.Size = 16
        $atBox.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $atBox.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cBlue
        for ($i = 2; $i -le $atBox.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $atBox.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 12
            $p.Font.Color.RGB = $cTextMain
        }

        # Footer
        $foot = $s.Shapes.AddTextbox(1, 50, 490, 860, 25)
        $foot.TextFrame.TextRange.Text = "B.E. Computer Science & Engineering (Artificial Intelligence) Capstone Viva"
        $foot.TextFrame.TextRange.Font.Name = "Segoe UI"
        $foot.TextFrame.TextRange.Font.Size = 10
        $foot.TextFrame.TextRange.Font.Color.RGB = $cTextMuted
        $foot.TextFrame.TextRange.ParagraphFormat.Alignment = 2
    }
    elseif ($sl.type -eq "cards_and_list") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 40 $sl.highlight

        # 3 cards
        $cardW = 280
        $gap = 20
        for ($c = 0; $c -lt 3; $c++) {
            $cd = $sl.cards[$c]
            $left = 40 + $c * ($cardW + $gap)
            Add-Card $s $left 125 $cardW 160 $cd.title $cd.body
        }

        # List at bottom
        $lBox = $s.Shapes.AddShape(1, 40, 300, 880, 195)
        $lBox.Fill.Solid()
        $lBox.Fill.ForeColor.RGB = $cCardBg
        $lBox.Line.Visible = 1
        $lBox.Line.ForeColor.RGB = $cCardBorder

        $lt = $s.Shapes.AddTextbox(1, 55, 310, 850, 175)
        $lt.TextFrame.WordWrap = 1
        $listContent = "$($sl.listTitle)`n`n" + ($sl.listItems -join "`n`n")
        $lt.TextFrame.TextRange.Text = $listContent
        $lt.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $lt.TextFrame.TextRange.Paragraphs(1).Font.Size = 13
        $lt.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $lt.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cNavy
        for ($i = 2; $i -le $lt.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $lt.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 11
            $p.Font.Color.RGB = $cTextMain
        }
    }
    elseif ($sl.type -eq "grid_cards") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 34 $sl.intro

        # 4 grid cards (2x2)
        $w = 430
        $h = 130
        Add-Card $s 40 120 $w $h $sl.gridCards[0].title $sl.gridCards[0].body
        Add-Card $s 490 120 $w $h $sl.gridCards[1].title $sl.gridCards[1].body
        Add-Card $s 40 260 $w $h $sl.gridCards[2].title $sl.gridCards[2].body
        Add-Card $s 490 260 $w $h $sl.gridCards[3].title $sl.gridCards[3].body

        # Full card
        Add-Card $s 40 400 880 95 $sl.fullCard.title $sl.fullCard.body
    }
    elseif ($sl.type -eq "table_slide") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 34 $sl.highlight
        Add-TableHelper $s 40 120 880 370 $sl.table.headers $sl.table.colWidths $sl.table.rows
    }
    elseif ($sl.type -eq "flow_and_cards") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 34 $sl.intro

        # 3 Step boxes
        $stepW = 260
        $sGap = 50
        for ($i = 0; $i -lt 3; $i++) {
            $st = $sl.steps[$i]
            $sLeft = 40 + $i * ($stepW + $sGap)
            $sBox = $s.Shapes.AddShape(1, $sLeft, 120, $stepW, 90)
            $sBox.Fill.Solid()
            $sBox.Fill.ForeColor.RGB = Color-RGB 240 253 250
            $sBox.Line.Visible = 1
            $sBox.Line.ForeColor.RGB = $cPrimary
            $sBox.Line.Weight = 1.5

            $sTb = $s.Shapes.AddTextbox(1, $sLeft + 10, 125, $stepW - 20, 80)
            $sTb.TextFrame.WordWrap = 1
            $sTb.TextFrame.TextRange.Text = "$($st.num)`n$($st.tech)`n$($st.desc)"
            $sTb.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
            $sTb.TextFrame.TextRange.Paragraphs(1).Font.Size = 12
            $sTb.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
            $sTb.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cPrimaryDark
            $sTb.TextFrame.TextRange.Paragraphs(2).Font.Size = 11
            $sTb.TextFrame.TextRange.Paragraphs(2).Font.Bold = 1
            $sTb.TextFrame.TextRange.Paragraphs(2).Font.Color.RGB = $cNavy
            $sTb.TextFrame.TextRange.Paragraphs(3).Font.Size = 9.5
            $sTb.TextFrame.TextRange.Paragraphs(3).Font.Color.RGB = $cTextMuted

            # Arrow between steps
            if ($i -lt 2) {
                $arr = $s.Shapes.AddTextbox(1, $sLeft + $stepW + 10, 150, 30, 30)
                $arr.TextFrame.TextRange.Text = "->"
                $arr.TextFrame.TextRange.Font.Name = "Segoe UI"
                $arr.TextFrame.TextRange.Font.Size = 16
                $arr.TextFrame.TextRange.Font.Bold = 1
                $arr.TextFrame.TextRange.Font.Color.RGB = $cPrimary
            }
        }

        # 2 bottom cards
        $b1Text = ($sl.cards[0].items -join "`n- ")
        $b1Text = "- " + $b1Text
        Add-Card $s 40 230 430 265 $sl.cards[0].title $b1Text

        $b2Text = ($sl.cards[1].items -join "`n- ")
        $b2Text = "- " + $b2Text
        Add-Card $s 490 230 430 265 $sl.cards[1].title $b2Text
    }
    elseif ($sl.type -eq "grid_8") {
        Add-Header $s $sl.category $sl.title $sl.num

        # 8 cards: 4 columns x 2 rows
        $w = 210
        $h = 195
        $gapX = 13
        $gapY = 15

        for ($idx = 0; $idx -lt 8; $idx++) {
            $f = $sl.features[$idx]
            $row = [Math]::Floor($idx / 4)
            $col = $idx % 4
            $left = 40 + $col * ($w + $gapX)
            $top = 85 + $row * ($h + $gapY)
            Add-Card $s $left $top $w $h $f.title $f.desc
        }
    }
    elseif ($sl.type -eq "grid_tech") {
        Add-Header $s $sl.category $sl.title $sl.num
        if ($sl.intro) {
            Add-HighlightBox $s 40 75 880 32 $sl.intro
            $topOffset = 115
            $hCard = 180
        } else {
            $topOffset = 85
            $hCard = 200
        }

        if ($sl.cards.Count -eq 4) {
            # 2x2 grid
            $w = 430
            # Card 1
            $b1 = if ($sl.cards[0].items) { "• " + ($sl.cards[0].items -join "`n• ") } else { $sl.cards[0].body }
            Add-Card $s 40 $topOffset $w $hCard $sl.cards[0].title $b1

            # Card 2
            $b2 = if ($sl.cards[1].items) { "• " + ($sl.cards[1].items -join "`n• ") } else { $sl.cards[1].body }
            Add-Card $s 490 $topOffset $w $hCard $sl.cards[1].title $b2

            # Card 3
            $b3 = if ($sl.cards[2].items) { "• " + ($sl.cards[2].items -join "`n• ") } else { $sl.cards[2].body }
            Add-Card $s 40 ($topOffset + $hCard + 15) $w $hCard $sl.cards[2].title $b3

            # Card 4
            $b4 = if ($sl.cards[3].items) { "• " + ($sl.cards[3].items -join "`n• ") } else { $sl.cards[3].body }
            Add-Card $s 490 ($topOffset + $hCard + 15) $w $hCard $sl.cards[3].title $b4
        }
        elseif ($sl.cards.Count -eq 6) {
            # 3x2 grid
            $w = 280
            $h = 180
            $gapX = 20
            $gapY = 15
            for ($idx = 0; $idx -lt 6; $idx++) {
                $c = $sl.cards[$idx]
                $row = [Math]::Floor($idx / 3)
                $col = $idx % 3
                $left = 40 + $col * ($w + $gapX)
                $top = $topOffset + $row * ($h + $gapY)
                Add-Card $s $left $top $w $h $c.title $c.body
            }
        }
    }
    elseif ($sl.type -eq "architecture") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 34 $sl.intro

        # 3 Tier boxes
        $boxW = 260
        $gap = 50
        $cYellow = Color-RGB 202 138 4
        $colors = @($cBlue, $cPrimary, $cYellow)

        for ($i = 0; $i -lt 3; $i++) {
            $t = $sl.tiers[$i]
            $left = 40 + $i * ($boxW + $gap)
            $bShape = $s.Shapes.AddShape(1, $left, 130, $boxW, 160)
            $bShape.Fill.Solid()
            $bShape.Fill.ForeColor.RGB = Color-RGB 255 255 255
            $bShape.Line.Visible = 1
            $bShape.Line.ForeColor.RGB = $colors[$i]
            $bShape.Line.Weight = 2

            $tb = $s.Shapes.AddTextbox(1, $left + 15, 145, $boxW - 30, 130)
            $tb.TextFrame.WordWrap = 1
            $tb.TextFrame.TextRange.Text = "$($t.title)`n`n$($t.subtitle)`n`n$($t.badge)"
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Size = 14
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $colors[$i]
            $tb.TextFrame.TextRange.Paragraphs(2).Font.Size = 11
            $tb.TextFrame.TextRange.Paragraphs(2).Font.Color.RGB = $cTextMuted
            $tb.TextFrame.TextRange.Paragraphs(3).Font.Size = 11
            $tb.TextFrame.TextRange.Paragraphs(3).Font.Bold = 1
            $tb.TextFrame.TextRange.Paragraphs(3).Font.Color.RGB = $cNavy

            # Arrow
            if ($i -lt 2) {
                $arr = $s.Shapes.AddTextbox(1, $left + $boxW + 10, 190, 30, 30)
                $arr.TextFrame.TextRange.Text = "<->"
                $arr.TextFrame.TextRange.Font.Name = "Segoe UI"
                $arr.TextFrame.TextRange.Font.Size = 16
                $arr.TextFrame.TextRange.Font.Bold = 1
                $arr.TextFrame.TextRange.Font.Color.RGB = $cPrimary
            }
        }

        # Data flow box
        Add-HighlightBox $s 40 310 880 185 $sl.flowText
    }
    elseif ($sl.type -eq "database") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 34 $sl.highlight

        # 4 tables (2x2)
        $w = 430
        $h = 160
        for ($i = 0; $i -lt 4; $i++) {
            $tbl = $sl.tables[$i]
            $row = [Math]::Floor($i / 2)
            $col = $i % 2
            $left = 40 + $col * 450
            $top = 120 + $row * 175
            $body = "Columns: $($tbl.columns)`n`nPurpose: $($tbl.purpose)"
            Add-Card $s $left $top $w $h $tbl.name $body
        }

        # Bottom relationship
        $rBox = $s.Shapes.AddTextbox(1, 40, 480, 880, 25)
        $rBox.TextFrame.TextRange.Text = $sl.relationship
        $rBox.TextFrame.TextRange.Font.Name = "Segoe UI"
        $rBox.TextFrame.TextRange.Font.Size = 10.5
        $rBox.TextFrame.TextRange.Font.Bold = 1
        $rBox.TextFrame.TextRange.Font.Color.RGB = $cPrimaryDark
    }
    elseif ($sl.type -eq "dfd") {
        Add-Header $s $sl.category $sl.title $sl.num

        # Left: DFD 0
        $d0 = $sl.dfd0
        $card0 = $s.Shapes.AddShape(1, 40, 85, 430, 410)
        $card0.Fill.Solid()
        $card0.Fill.ForeColor.RGB = $cCardBg
        $card0.Line.Visible = 1
        $card0.Line.ForeColor.RGB = $cCardBorder

        $tb0 = $s.Shapes.AddTextbox(1, 55, 95, 400, 390)
        $tb0.TextFrame.WordWrap = 1
        $d0Content = "$($d0.title)`n`n" +
                     "[External Entity]`n" +
                     "  $($d0.actor)`n" +
                     "       |`n" +
                     "       v  ($($d0.flow1))`n`n" +
                     "[Main System Process]`n" +
                     "  $($d0.system)`n" +
                     "       |`n" +
                     "       v  ($($d0.flow2))`n`n" +
                     "[Data Store]`n" +
                     "  $($d0.db)"
        $tb0.TextFrame.TextRange.Text = $d0Content
        $tb0.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $tb0.TextFrame.TextRange.Paragraphs(1).Font.Size = 14
        $tb0.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $tb0.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cNavy
        for ($i = 2; $i -le $tb0.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $tb0.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 11
            $p.Font.Color.RGB = $cTextMain
        }

        # Right: DFD 1
        $d1 = $sl.dfd1
        $card1 = $s.Shapes.AddShape(1, 490, 85, 430, 410)
        $card1.Fill.Solid()
        $card1.Fill.ForeColor.RGB = $cCardBg
        $card1.Line.Visible = 1
        $card1.Line.ForeColor.RGB = $cCardBorder

        $tb1 = $s.Shapes.AddTextbox(1, 505, 95, 400, 390)
        $tb1.TextFrame.WordWrap = 1
        $d1Content = "$($d1.title)`n`n- " + ($d1.processes -join "`n`n- ")
        $tb1.TextFrame.TextRange.Text = $d1Content
        $tb1.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $tb1.TextFrame.TextRange.Paragraphs(1).Font.Size = 14
        $tb1.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $tb1.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cNavy
        for ($i = 2; $i -le $tb1.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $tb1.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 11
            $p.Font.Color.RGB = $cTextMain
        }
    }
    elseif ($sl.type -eq "workflow") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 32 $sl.intro

        # 8-step flow horizontally
        $stepW = 95
        $stepGap = 17
        for ($i = 0; $i -lt 8; $i++) {
            $stText = $sl.steps[$i]
            $stLeft = 40 + $i * ($stepW + $stepGap)
            $sBox = $s.Shapes.AddShape(1, $stLeft, 115, $stepW, 60)
            $sBox.Fill.Solid()
            $sBox.Fill.ForeColor.RGB = Color-RGB 241 245 249
            $sBox.Line.Visible = 1
            $sBox.Line.ForeColor.RGB = $cCardBorder

            $tb = $s.Shapes.AddTextbox(1, $stLeft + 4, 120, $stepW - 8, 50)
            $tb.TextFrame.WordWrap = 1
            $tb.TextFrame.TextRange.Text = $stText
            $tb.TextFrame.TextRange.Font.Name = "Segoe UI"
            $tb.TextFrame.TextRange.Font.Size = 9.5
            $tb.TextFrame.TextRange.Font.Bold = 1
            $tb.TextFrame.TextRange.Font.Color.RGB = $cNavy

            if ($i -lt 7) {
                $arr = $s.Shapes.AddTextbox(1, $stLeft + $stepW + 2, 130, 15, 25)
                $arr.TextFrame.TextRange.Text = "->"
                $arr.TextFrame.TextRange.Font.Name = "Segoe UI"
                $arr.TextFrame.TextRange.Font.Size = 11
                $arr.TextFrame.TextRange.Font.Bold = 1
                $arr.TextFrame.TextRange.Font.Color.RGB = $cPrimary
            }
        }

        # FEFO card
        $fefo = $sl.fefoCard
        $fc = $s.Shapes.AddShape(1, 40, 190, 880, 300)
        $fc.Fill.Solid()
        $fc.Fill.ForeColor.RGB = Color-RGB 240 253 250
        $fc.Line.Visible = 1
        $fc.Line.ForeColor.RGB = $cHighlightBd
        $fc.Line.Weight = 1.5

        $ftb = $s.Shapes.AddTextbox(1, 60, 205, 840, 270)
        $ftb.TextFrame.WordWrap = 1
        $fText = "$($fefo.title)`n`n" +
                 "$($fefo.rule)`n`n" +
                 "[Batch A]: $($fefo.boxA.title) -> Status: $($fefo.boxA.badge)`n`n" +
                 "[Batch B]: $($fefo.boxB.title) -> Status: $($fefo.boxB.badge)`n`n" +
                 "$($fefo.explanation)"
        $ftb.TextFrame.TextRange.Text = $fText
        $ftb.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $ftb.TextFrame.TextRange.Paragraphs(1).Font.Size = 15
        $ftb.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $ftb.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cPrimaryDark
        for ($i = 2; $i -le $ftb.TextFrame.TextRange.Paragraphs().Count; $i++) {
            $p = $ftb.TextFrame.TextRange.Paragraphs($i)
            $p.Font.Name = "Segoe UI"
            $p.Font.Size = 12
            $p.Font.Color.RGB = $cTextMain
        }
    }
    elseif ($sl.type -eq "frontend") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 32 $sl.intro

        # Top 3 cards
        $w3 = 280
        for ($i = 0; $i -lt 3; $i++) {
            $c = $sl.topCards[$i]
            $left = 40 + $i * 300
            Add-Card $s $left 115 $w3 180 $c.title $c.body
        }

        # Bottom 2 cards
        $w2 = 430
        for ($i = 0; $i -lt 2; $i++) {
            $c = $sl.bottomCards[$i]
            $left = 40 + $i * 450
            Add-Card $s $left 310 $w2 185 $c.title $c.body
        }
    }
    elseif ($sl.type -eq "backend_arch") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 32 $sl.intro

        # 3 Layers
        $layerW = 260
        for ($i = 0; $i -lt 3; $i++) {
            $ly = $sl.layers[$i]
            $left = 40 + $i * 310
            $bShape = $s.Shapes.AddShape(1, $left, 115, $layerW, 90)
            $bShape.Fill.Solid()
            $bShape.Fill.ForeColor.RGB = $cCardBg
            $bShape.Line.Visible = 1
            $bShape.Line.ForeColor.RGB = $cPrimary
            $bShape.Line.Weight = 1.5

            $tb = $s.Shapes.AddTextbox(1, $left + 10, 120, $layerW - 20, 80)
            $tb.TextFrame.WordWrap = 1
            $tb.TextFrame.TextRange.Text = "$($ly.num)`n$($ly.annotation)`n$($ly.desc)"
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Size = 12
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
            $tb.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cNavy
            $tb.TextFrame.TextRange.Paragraphs(2).Font.Size = 11
            $tb.TextFrame.TextRange.Paragraphs(2).Font.Bold = 1
            $tb.TextFrame.TextRange.Paragraphs(2).Font.Color.RGB = $cPrimary
            $tb.TextFrame.TextRange.Paragraphs(3).Font.Size = 9.5
            $tb.TextFrame.TextRange.Paragraphs(3).Font.Color.RGB = $cTextMuted

            if ($i -lt 2) {
                $arr = $s.Shapes.AddTextbox(1, $left + $layerW + 15, 145, 25, 25)
                $arr.TextFrame.TextRange.Text = "->"
                $arr.TextFrame.TextRange.Font.Name = "Segoe UI"
                $arr.TextFrame.TextRange.Font.Size = 14
                $arr.TextFrame.TextRange.Font.Bold = 1
                $arr.TextFrame.TextRange.Font.Color.RGB = $cPrimary
            }
        }

        # Real Execution Example
        Add-HighlightBox $s 40 215 880 150 $sl.example

        # Exception Card
        Add-Card $s 40 375 880 120 $sl.exceptionCard.title $sl.exceptionCard.body
    }
    elseif ($sl.type -eq "results") {
        Add-Header $s $sl.category $sl.title $sl.num
        Add-HighlightBox $s 40 75 880 32 $sl.intro

        # 3 Cards
        $w = 280
        # Card 1: Dashboard items
        $c1Text = "- " + ($sl.cards[0].items -join "`n- ")
        Add-Card $s 40 115 $w 280 $sl.cards[0].title $c1Text

        # Card 2: Dispense
        Add-Card $s 340 115 $w 280 $sl.cards[1].title $sl.cards[1].body

        # Card 3: Cold chain
        Add-Card $s 640 115 $w 280 $sl.cards[2].title $sl.cards[2].body

        # Bottom highlight
        Add-HighlightBox $s 40 405 880 90 $sl.highlight
    }
    elseif ($sl.type -eq "conclusion") {
        Add-Header $s $sl.category $sl.title $sl.num

        # Left: Project Conclusion
        $cText = "- " + ($sl.conclusionItems -join "`n`n- ")
        Add-Card $s 40 85 430 260 "Project Conclusion" $cText

        # Right: What We Learned
        $lText = "- " + ($sl.learningItems -join "`n`n- ")
        Add-Card $s 490 85 430 260 "What We Learned" $lText

        # Bottom Thank you card
        $tyCard = $s.Shapes.AddShape(1, 40, 360, 880, 135)
        $tyCard.Fill.Solid()
        $tyCard.Fill.ForeColor.RGB = Color-RGB 240 253 250
        $tyCard.Line.Visible = 1
        $tyCard.Line.ForeColor.RGB = $cHighlightBd
        $tyCard.Line.Weight = 2

        $tyTb = $s.Shapes.AddTextbox(1, 60, 385, 840, 90)
        $tyTb.TextFrame.WordWrap = 1
        $tyTb.TextFrame.TextRange.Text = "$($sl.thankYou.title)`n`n$($sl.thankYou.body)"
        $tyTb.TextFrame.TextRange.ParagraphFormat.Alignment = 2 # Center
        $tyTb.TextFrame.TextRange.Paragraphs(1).Font.Name = "Segoe UI"
        $tyTb.TextFrame.TextRange.Paragraphs(1).Font.Size = 24
        $tyTb.TextFrame.TextRange.Paragraphs(1).Font.Bold = 1
        $tyTb.TextFrame.TextRange.Paragraphs(1).Font.Color.RGB = $cPrimaryDark
        $tyTb.TextFrame.TextRange.Paragraphs(2).Font.Size = 14
        $tyTb.TextFrame.TextRange.Paragraphs(2).Font.Bold = 1
        $tyTb.TextFrame.TextRange.Paragraphs(2).Font.Color.RGB = $cNavy
    }
}

# Save presentation
if (Test-Path $outputPath) {
    Remove-Item $outputPath -Force
}

Write-Host "Saving presentation to $outputPath..."
$pres.SaveAs($outputPath)
$pres.Close()
$ppt.Quit()
[System.Runtime.InteropServices.Marshal]::ReleaseComObject($ppt) | Out-Null

Write-Host "SUCCESS: Generated Smart_Pharmacy_Supply_Chain_System.pptx with $($slides.Count) slides."
