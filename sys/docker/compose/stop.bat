@ECHO OFF

ECHO ***********************************************************
ECHO *                                                         *
ECHO * Stopping Docker compose for development environment.    *
ECHO *                                                         *
ECHO ***********************************************************
ECHO.

SET PROJECT_NAME=fuzzylist

docker-compose -p %PROJECT_NAME% -f docker-compose-windows-mac.yaml down
