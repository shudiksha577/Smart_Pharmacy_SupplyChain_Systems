$ErrorActionPreference = "Stop"
$git = "c:\Users\Shudiksha M\Desktop\project\mingit\cmd\git.exe"
$repoUrl = "https://github.com/shudiksha577/Smart_Pharmacy_SupplyChain_Systems.git"
$cloneDir = "c:\Users\Shudiksha M\Desktop\Smart_Pharmacy_SupplyChain_Systems"
$sourceDir = "c:\Users\Shudiksha M\Desktop\project"

# 1. Clean existing clone directory if present
if (Test-Path $cloneDir) {
    Remove-Item $cloneDir -Recurse -Force
}

# 2. Clone the repository
Write-Host "Cloning $repoUrl into $cloneDir..."
& $git clone $repoUrl $cloneDir

Set-Location $cloneDir

# 3. Configure Git user
& $git config user.name "Shudiksha Sugeerthanaa"
& $git config user.email "shudiksha577@users.noreply.github.com"

# 4. Create base commit on main branch
Write-Host "Setting up main branch with initial project setup..."
Copy-Item "$sourceDir\.gitignore" "$cloneDir\.gitignore"
Copy-Item "$sourceDir\README.md" "$cloneDir\README.md"

& $git add .gitignore README.md
& $git commit -m "Initial commit: Project setup and documentation"

# 5. Create dev branch from main
Write-Host "Creating and switching to dev branch..."
& $git checkout -b dev

# 6. Copy all project files into dev branch
Write-Host "Copying project files into dev branch..."
$excludeItems = @("mingit", "maven", ".git", "setup_git.ps1", "git_push_test.ps1", "make_pdf.ps1", "count_pdf_pages.ps1", "test_ppt.ps1", "verify_pptx.ps1", "verify_slide1.ps1", "generate_powerpoint.ps1")

Get-ChildItem -Path $sourceDir | Where-Object { $excludeItems -notcontains $_.Name } | ForEach-Object {
    if ($_.PSIsContainer) {
        if ($_.Name -eq "backend") {
            # Copy backend excluding target
            Copy-Item $_.FullName $cloneDir -Recurse -Force
            if (Test-Path "$cloneDir\backend\target") {
                Remove-Item "$cloneDir\backend\target" -Recurse -Force
            }
        } else {
            Copy-Item $_.FullName $cloneDir -Recurse -Force
        }
    } else {
        Copy-Item $_.FullName $cloneDir -Force
    }
}

# 7. Stage and commit on dev branch
Write-Host "Staging and committing full-stack system implementation on dev branch..."
& $git add -A
& $git status --short
& $git commit -m "feat: complete implementation of Smart Pharmacy Supply Chain System"

Write-Host "Local repository setup complete on branches 'main' and 'dev'!"
& $git log --oneline --graph --all
