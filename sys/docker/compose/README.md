NOTICE
======

**NOTE:** Due to bug in Docker installation process, some data may be missing and may require a manual fix.

Prior to starting Docker compose, make sure the following entries exists within the OS's _hosts_ file:
```
host.docker.internal
gateway.docker.internal
```

e.g.:

```
172.17.0.1      host.docker.internal
172.17.0.1      gateway.docker.internal
```

Known IP addresses:
* For Microsoft Windows platform: 192.168.1.31
* For Linux-based platforms: 172.17.0.1
