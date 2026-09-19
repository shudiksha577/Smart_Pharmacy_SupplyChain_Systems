@echo off
title Smart Pharmacy Supply Chain System - Spring Boot Backend
echo ===================================================================
echo   Starting Smart Pharmacy Supply Chain System Backend...
echo ===================================================================
cd /d "%~dp0backend"
call "..\maven\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
pause
