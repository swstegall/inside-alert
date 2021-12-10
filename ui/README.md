# Inside Alert UI

## Requirements

In order to run this application, users must have the following software installed.

- Node (v 12 or higher)
- Yarn

## Installing Dependencies and Configuration

- In a terminal, navigate to this directory.
- Issue the command `yarn`.
- In the `src/utilities` directory, issue the command `cp constantsTemplate.js constants.js`.

## Running Locally

- Using a text editor, modify `constants.js` and input the IP address and port that the API is using. If on the same system running the API, this should be `const host = "localhost";` and `const port = "8080";`.
- From the `ui` folder, issue the command `yarn dev`. You can then view the UI by opening a browser and navigating to `http://localhost:3000`.

## Building for Production

- Using a text editor, modify `constants.js` and input the IP address and port that the API is using.
- From the `ui` folder, issue the command `yarn build`.

## Deploying on Linux (systemd)

- Create a shell script to run the UI. This may be something like:

`/home/username/inside-alert-ui`:
```bash
#!/usr/bin/bash
cd ~/inside-alert/ui
npx next start
```

- Create a systemd service file as root to initiate your runscript. This may be something like:

`/etc/systemd/system/inside-alert-ui.service`:
```s
[Unit]
Description=Start inside-alert-ui

[Service]
Type=oneshot
ExecStart=/home/username/inside-alert-ui

[Install]
WantedBy=multi-user.target
```

- Enable the service with `systemctl enable inside-alert-ui.service`, then reboot the host with `reboot`.