@echo off
title Smart Pharmacy Supply Chain System - Spring Boot Backend
echo ===================================================================
echo   Starting Smart Pharmacy Supply Chain System Backend...
echo ===================================================================
cd /d "%~dp0backend"
where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    call mvn spring-boot:run
) else if exist "..\maven\apache-maven-3.9.6\bin\mvn.cmd" (
    call "..\maven\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
) else (
    echo [ERROR] Apache Maven was not found on PATH.
    echo Please install Maven or add Maven bin folder to your PATH.
    pause
)
