#!/bin/bash

# Username on remote server.
USERNAME=guy

# Remote host address.
REMOTE_HOST=167.71.136.117

# Port on remote host that is forwarded to this host.
REMOTE_HTTP_FORWARDING_PORT=8080

# SSH port on remote host. Used as a control channel.
REMOTE_SSH_PORT=22

# Local port that accepts FuzzyList application requests.
LOCAL_HTTP_PORT=80

# Local port that forwards requests to remote SSH service.
LOCAL_SSH_TUNNEL_PORT=11122

DELAY_ON_FAILURE=5
DELAY_ON_KEEPALIVE=5

function log () {
  dt=$(date '+%d/%m/%Y %H:%M:%S');
  echo "$dt  [INFO ] $1"
  logger -p user.info --id -t tunnel "$1"
}

function logError () {
  dt=$(date '+%d/%m/%Y %H:%M:%S');
  echo "$dt [ERROR $1"
  logger -p user.err --id -t tunnel "$1"
}

#
# Execute command on remote host via SSH.
#
function remoteCall () {
  ssh -f $USERNAME@$REMOTE_HOST "$@" > /dev/null 2>&1
}

#
# Attempt to execute a command on remote host via local port tunneled.
#
function localCall () {
  ssh -f $USERNAME@localhost -p $LOCAL_SSH_TUNNEL_PORT "$@" > /dev/null 2>&1
}

#
# Creates a 2 tunnels between local and remote host.
#
function createTunnel() {
  ssh -f -N \
            -R $REMOTE_HTTP_FORWARDING_PORT:localhost:$LOCAL_HTTP_PORT \
            -L $LOCAL_SSH_TUNNEL_PORT:localhost:$REMOTE_SSH_PORT \
            $USERNAME@$REMOTE_HOST
}

#
# Check that remote host is available.
#
log "Checking if remote host is available..."
while ! remoteCall ls
  do
    log "Remote host unavailable. Waiting for $DELAY seconds."
    sleep $DELAY_ON_FAILURE
    log "Checking if remote host is available..."
  done

#
# Check if our control channel is available.
#
if ! localCall ls
  then
    log "Setting up new tunnel."
    if createTunnel
    then
      log "Successfully set a tunnel."
    fi
  else
    log "Tunnel active."
fi

log "Testing tunnel for validity."
if ! remoteCall wget -q -O /dev/null http://localhost:8080
  then
    log "Tunnel not working correctly. Trying to kill existing tunnel."
    remoeCall pkill -U $USERNAME
  else
    log "Tunnel active !"
  fi

while true
  do
    sleep $DELAY_ON_KEEPALIVE
    if ! localCall ls
      then
        logError "Error while testing tunnel."
      else
        log "Tunnel OK."
      fi
  done

