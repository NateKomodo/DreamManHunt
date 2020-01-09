# DreamManHunt
A recreation of the man hunt / assassin game mode featured in some of Dreams videos.

## Installation
Download the latest releast from the releases tab and put it in your plugins folder.

## Usage
The main command is /assassin <assassin/speedrunner> <add/remove> <player>. This adds a particular player to a given role, either assassin or speedrunner.

The config is located in Plugins/Assassin/config.yml. It has a toggle for options. Below is a description of what each entry does:

`assassins-insta-kill-speedrunner` - true/false, changes if the assassin can one shot the speedrunner

`compass-tracking` - true/false, changes if the assassins compass (If they have one) points to the closeset speedrunner

`freeze-assassin-when-seen` - true/false, changes if the assassin will be frozen in place if the speedrunner puts their crosshair over the assassin. Note if frozen in the air, it may trigger a "Flying is not enabled on the server disconnect", so it is recommended to disable it.

Typing /assassin will reload the config and display debug information.
