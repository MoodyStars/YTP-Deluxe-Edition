@echo off
REM Example Windows build script. Edit paths for launch4j.exe as needed.

echo Building JAR...
mvn clean package

IF %ERRORLEVEL% NEQ 0 (
  echo Maven build failed.
  exit /b 1
)

REM path to launch4j executable
set LAUNCH4J_EXE="C:\Program Files\Launch4j\launch4j.exe"
if not exist %LAUNCH4J_EXE% (
  echo Launch4j not found at %LAUNCH4J_EXE%. Please install Launch4j and update this script.
  exit /b 1
)

echo Running Launch4j...
%LAUNCH4J_EXE% launch4j\ytp_plus_launch4j.xml

echo Done. Output in dist\YTP-Plus-Deluxe.exe
pause