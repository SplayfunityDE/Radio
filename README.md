# Radio

<img align="right" src="https://avatars.githubusercontent.com/u/108355696?s=200&v=4" height="200" width="200">

> [!Note]
> Be careful! Any changes from `master` will be deployed directly to the production system via rsync!

"Radio" combines all features related to 24/7 audio features on the [SPLAYFUNITY](https:splayfer.de) Discord server.
It uses sedmelluq/lavaplayer as it's main api. [Link](https://github.com/sedmelluq/lavaplayer)

## Features
The following features are fully included:
- 24/7 radio music
- 100% grafical config options for administrators

## Backend
**Database**

This repository uses the default SPLAYFUNITY MongoDB database.

**Deployment**

Deployment is accessing varios open source ressources and interfaces.

- Github Actions
- Maven shade plugin
- ssh
- rsync
- docker compose
