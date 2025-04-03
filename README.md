# Ninkai - Day Night Cycle Plugin

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
![latest build](https://img.shields.io/github/actions/workflow/status/poulpy2k/ninkaidaynightcycle/gradle.yml
)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PoulpY2K_ninkaidaynightcycle&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PoulpY2K_ninkaidaynightcycle)
![latest tag](https://img.shields.io/github/v/tag/poulpy2k/ninkaidaynightcycle)

![Github last commit](https://img.shields.io/github/last-commit/poulpy2k/ninkaidaynightcycle
)
![Github commit activity](https://img.shields.io/github/commit-activity/w/poulpy2k/ninkaidaynightcycle
)


A project I built while learning Minecraft Paper/Spigot plugin development.

The plugin is made for a roleplay server called "Ninkai".

It enables real time syncing to IRL time and weather.

## Commands

- /daynightcycle init - Initializes the plugin
- /daynightcycle start - Start the day night cycle
- /daynightcycle stop - Stop the day night cycle
- /daynightcycle set time <time> - Set the time to a specific value and freeze it
- /daynightcycle set timezone <timezone> - Set the timezone
- /daynightcycle set weather <weather> - Set the weather to a specific value
- /daynightcycle status - Gives the plugin status (initialized, running, timezone, time, weather, etc)
- /daynightcycle reload - Reload the config

## Features

- Real time syncing

## Run Locally

### Clone the project

```bash
  git clone https://github.com/PoulpY2K/ninkaidaynightcycle.git
```

### Go to the project directory

```bash
  cd ninkaidaynightcycle
```

### Run the project

#### With local gradle
```bash
  ./gradlew clean build runServer
```

#### With global gradle
```bash
  gradle clean build runServer
```

## Authors

- [@poulpy2k](https://www.github.com/poulpy2k)

