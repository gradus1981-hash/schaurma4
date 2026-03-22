@echo off
setlocal enabledelayedexpansion

set DIR=%~dp0
set PROPS_FILE=%DIR%gradle\wrapper\gradle-wrapper.properties
set CACHE_DIR=%DIR%.gradle-dist

if not exist "%PROPS_FILE%" (
  echo gradle-wrapper.properties not found: %PROPS_FILE%
  exit /b 1
)

for /f "tokens=1,* delims==" %%A in ('type "%PROPS_FILE%" ^| findstr /b "distributionUrl="') do set DIST_URL=%%B
set DIST_URL=%DIST_URL:\:=%

for %%F in ("%DIST_URL%") do set ZIP_NAME=%%~nxF
set BASE_NAME=%ZIP_NAME:.zip=%
set DIST_DIR=%CACHE_DIR%\%BASE_NAME%
set ZIP_PATH=%CACHE_DIR%\%ZIP_NAME%

if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"

if not exist "%DIST_DIR%" (
  if not exist "%ZIP_PATH%" (
    echo Downloading Gradle distribution: %DIST_URL%
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -UseBasicParsing -Uri '%DIST_URL%' -OutFile '%ZIP_PATH%'"
    if errorlevel 1 exit /b 1
  )

  set TMP_DIR=%CACHE_DIR%\_extract
  if exist "%TMP_DIR%" rmdir /s /q "%TMP_DIR%"
  mkdir "%TMP_DIR%"

  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ZIP_PATH%' -DestinationPath '%TMP_DIR%' -Force"
  if errorlevel 1 exit /b 1

  for /d %%D in ("%TMP_DIR%\*") do (
    move "%%~fD" "%DIST_DIR%" >nul
    goto moved
  )
  echo Failed to extract Gradle distribution
  exit /b 1
)

:moved
call "%DIST_DIR%\bin\gradle.bat" %*
