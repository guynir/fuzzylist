@ECHO OFF

SET SCRIPT_DIR=%~dp0
SET PROJECT_NAME=fuzzylist

IF "%~1"=="" (
  REM Run this batch file while suppressing "Terminate batch job (Y/N)?" when pressing CTRL+C.
  REM Exit batch upon completion.
  SET CURRENT_DIR=%CD%
  CALL <NUL %0 SUPPRESS_TERMINATION_QUESTION
  CD %CURRENT_DIR%
  EXIT /B
)

ECHO ***********************************************************
ECHO *                                                         *
ECHO * Starting Docker compose for development environment.    *
ECHO *                                                         *
ECHO ***********************************************************
ECHO.

REM Change location to the director where the script is located at.
REM This is the same location as the docker-compose files.
CD %~dp0

REM Run docker compose.
docker-compose -p %PROJECT_NAME% -f docker-compose-windows-mac.yaml up --build
