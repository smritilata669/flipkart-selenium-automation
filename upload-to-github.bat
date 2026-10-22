@echo off
setlocal
cd /d "%~dp0"

echo ========================================================
echo   Uploading Flipkart Automation to GitHub
echo ========================================================
echo.

set "GH_PATH=%LOCALAPPDATA%\Microsoft\WinGet\Packages\GitHub.cli_Microsoft.Winget.Source_8wekyb3d8bbwe\bin\gh.exe"

REM Step 1: Check GitHub Authentication
"%GH_PATH%" auth status >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ACTION] Please log into your GitHub account:
    echo 1. Select: GitHub.com
    echo 2. Select: HTTPS
    echo 3. Authenticate Git with your GitHub credentials? Yes
    echo 4. Login with: Login with a web browser
    echo.
    "%GH_PATH%" auth login -h github.com -p https -w
)

echo.
echo [INFO] Creating GitHub repository and pushing code...
"%GH_PATH%" repo create flipkart-selenium-automation --public --source=. --remote=origin --push

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================================
    echo [SUCCESS] Your project has been uploaded to GitHub!
    echo ========================================================
) else (
    echo.
    echo [NOTE] If repository already exists, pushing directly:
    git push -u origin main
)

pause
