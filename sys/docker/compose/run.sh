#! /bin/bash

# Preserve the current working directory so we can back to it after this script terminates.
CURRENT_DIR=$(pwd)
PROJECT_NAME=fuzzylist

# Switch to the compose directory. It needs to be executed from there.
cd "$(dirname "${BASH_SOURCE[0]}")" || exit

# Based on the OS type, activate the relevant docker compose descriptor.
if [[ "$OSTYPE" == "linux-gnu" || "$OSTYPE" == "freebsd" ]]; then
  echo Detected Linux-class OS.
  docker-compose -p $PROJECT_NAME -f ./docker-compose-linux.yaml up --build
elif [[ "$OSTYPE" == "darwin" || "$OSTYPE" == "darwin20" || "$OSTYPE" == "darwin21" ]]; then
  echo Detected MacOS.
  docker-compose -p $PROJECT_NAME -f ./docker-compose-windows-mac.yaml up --build
elif [[ "$OSTYPE" == "cygwin" || "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
  docker-compose -p $PROJECT_NAME -f ./docker-compose-windows-mac.yaml up --build
  echo Detected Windows 32 sub system.
else
  echo Unknown OS type \(OSTYPE: \'$OSTYPE\'\). Aborting.
fi

# After all is done, return to the original directory.
cd "$CURRENT_DIR" || exit
