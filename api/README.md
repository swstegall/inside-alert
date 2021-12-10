# Inside Alert API

## Requirements

In order to run this application, users must have the following software installed.

- Amazon OpenJDK 16

## Installing Dependencies and Configuration

- In a terminal, navigate to this directory.
- Issue the command `./gradlew`.

## Running

- Issue the command `./gradlew run`.

## Deploying on Linux (systemd)

- Create a shell script to run the API. This may be something like:

`/home/username/inside-alert-api`:
```bash
#!/usr/bin/bash
cd ~/inside-alert/api
./gradlew run
```

- Create a systemd service file as root to initiate your runscript. This may be something like:

`/etc/systemd/system/inside-alert-api.service`:
```s
[Unit]
Description=Start inside-alert-api

[Service]
Type=oneshot
ExecStart=/home/username/inside-alert-api

[Install]
WantedBy=multi-user.target
```

- Enable the service with `systemctl enable inside-alert-api.service`, then reboot the host with `reboot`.