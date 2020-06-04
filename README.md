# DreamManHunt
A recreation of the man hunt / assassin game mode featured in some of Dreams videos.

## Installation
Download the latest releast from the releases tab and put it in your plugins folder.

## Usage
##### Commands
Use command `/assassin <player> [remove]` to add player to (or remove from) assassin's group.

Use command `/speedrunner <player> [remove]` to add player to (or remove from) speedrunner's group.

`/assassin` without parameters will reload the config.

##### Config
The config is located in Plugins/Assassin/config.yml. It has toggles for different options. Below is a description of what each entry does:

`compass-tracking` - true/false, changes if the assassins compass (if they have one) points to the closest speedrunner

`compass-give` - true/false, if true gives a compass to assassins on group assignment and on respawn

`compass-particle` - true/false, if true draws a yellow particle near assassin (if he holds a compass in main hand) in the direction of closest speedrunner

`compass-particle-in-nether` - true/false, the previous option also works in nether if this option is set to true
 
`compass-random-different-worlds` - true/false, if true compass will go crazy if there is no speedrunners  __in the same world__

`assassins-insta-kill-speedrunner` - true/false, changes if the assassin can one shot the speedrunner

`freeze-assassin-when-seen` - true/false, changes if the assassin will be frozen in place if the speedrunner puts their crosshair over the assassin. Note if frozen in the air, it may trigger a "Flying is not enabled on the server disconnect", so it is recommended to disable it.