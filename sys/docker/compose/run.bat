@ECHO OFF

SET SCRIPT_DIR=%~dp0
SET PROJECT_NAME=fuzzylist

REM IF "%~1"=="" (
REM   REM Run this batch file while suppressing "Terminate batch job (Y/N)?" when pressing CTRL+C.
REM   REM Exit batch upon completion.
REM   SET CURRENT_DIR=%CD%
REM   CALL <NUL %0 SUPPRESS_TERMINATION_QUESTION
REM   CD %CURRENT_DIR%
REM   EXIT /B
REM )

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
docker-compose -p %PROJECT_NAME% -f docker-compose-windows-mac.yaml up -d --build
