# List of Patched Bugs
**Info**
- *(fabric only)* probably means that the bug is already patched in forge
## Unpatched in vanilla
### Client Side
| Bug ID                                                | Name                                                                                                                                 |
|-------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| [MC-4490](https://bugs.mojang.com/browse/MC-4490)     | Fishing line not attached to fishing rod in third person while crouching                                                             |
| [MC-46776](https://bugs.mojang.com/browse/MC-46766)   | Mining a block in Survival, then changing to Spectator creates a breaking animation and sound                                        |
| [MC-53312](https://bugs.mojang.com/browse/MC-53312)   | Illager/(zombie) villager/witch robes don't render the last two rows of pixels                                                       |
| [MC-79545](https://bugs.mojang.com/browse/MC-79545)   | The experience bar disappears when too many levels are given to the player                                                           |
| [MC-80859](https://bugs.mojang.com/browse/MC-80859)   | Starting to drag item stacks over other compatible stacks makes the latter invisible until appearance change (stack size increases)  |
| [MC-93384](https://bugs.mojang.com/browse/MC-93384)   | Bubbles appear at the feet of drowning mobs                                                                                          |
| [MC-108948](https://bugs.mojang.com/browse/MC-108948) | Boat on top of slime blocks hover over block                                                                                         |
| [MC-111516](https://bugs.mojang.com/browse/MC-111516) | Player flickers/turns invisible when flying at high speeds                                                                           |
| [MC-116379](https://bugs.mojang.com/browse/MC-116379) | Punching with a cast fishing rod in the off-hand detaches fishing line from rod                                                      |
| [MC-121772](https://bugs.mojang.com/browse/MC-121772) | Can't scroll while holding SHIFT on macOS                                                                                            |
| [MC-122627](https://bugs.mojang.com/browse/MC-122627) | Tab suggestion box has missing padding on right side                                                                                 |
| [MC-123739](https://bugs.mojang.com/browse/MC-123739) | Recipe book entries are not sorted in any meaningful manner. In fact, the order can even change randomly after a reload.             |
| [MC-122477](https://bugs.mojang.com/browse/MC-122477) | Linux/GNU: Opening chat sometimes writes 't                                                                                          |
| [MC-127970](https://bugs.mojang.com/browse/MC-127970) | Using riptide on a trident with an item in your off-hand causes visual glitch with the item in your offhand                          |
| [MC-140646](https://bugs.mojang.com/browse/MC-140646) | Text fields don't scroll while selecting text with Shift                                                                             |
| [MC-145929](https://bugs.mojang.com/browse/MC-145929) | Actionbar text may be difficult to read without text background enabled                                                              |
| [MC-148149](https://bugs.mojang.com/browse/MC-148149) | Linux game crash when opening links                                                                                                  |
| [MC-151412](https://bugs.mojang.com/browse/MC-151412) | "Edit Server Info" window does not focus "Server Name" text field automatically                                                      |
| [MC-159163](https://bugs.mojang.com/browse/MC-159163) | Quickly pressing the sneak key causes the sneak animation to play twice                                                              |
| [MC-162253](https://bugs.mojang.com/browse/MC-162253) | Lag spike when crossing certain chunk borders                                                                                        |
| [MC-165381](https://bugs.mojang.com/browse/MC-165381) | Block breaking can be delayed by dropping/throwing the tool while breaking a block                                                   |
| [MC-165595](https://bugs.mojang.com/browse/MC-165595) | Guardian beam does not render when over a certain "Time" in level.dat                                                                |
| [MC-176559](https://bugs.mojang.com/browse/MC-176559) | Breaking process resets when a pickaxe enchanted with Mending mends by XP / Mending slows down breaking blocks again *(fabric only)* |
| [MC-215531](https://bugs.mojang.com/browse/MC-215531) | The carved pumpkin overlay isn't removed when switching into spectator mode                                                          |
| [MC-217716](https://bugs.mojang.com/browse/MC-217716) | The green nausea overlay isn't removed when switching into spectator mode                                                            |
| [MC-231097](https://bugs.mojang.com/browse/MC-231097) | Holding the "Use" button continues to slow down the player even after the used item has been dropped                                 |
| [MC-233042](https://bugs.mojang.com/browse/MC-233042) | Server Address field isn't focused when Direct Connection menu is opened                                                             |
| [MC-234898](https://bugs.mojang.com/browse/MC-234898) | The "Get a trial!" button isn't consistently displayed within the realms menu                                                        |
| [MC-235035](https://bugs.mojang.com/browse/MC-235035) | Sleeping in a custom dimension with "natural" set to false causes crash *(fabric only)*                                              |
| [MC-249059](https://bugs.mojang.com/browse/MC-249059) | Loading terrain screen cannot close before 2 seconds have passed                                                                     |

### Server Side (Both)
| Bug ID                                                | Name                                                                                               |
|-------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| [MC-14923](https://bugs.mojang.com/browse/MC-14923)   | Players can be kicked for spamming in a singleplayer world with cheats disabled                    |
| [MC-69216](https://bugs.mojang.com/browse/MC-69216)   | Switching to spectator mode while fishing keeps rod cast                                           |
| [MC-81773](https://bugs.mojang.com/browse/MC-81773)   | Bows and tridents drawn in survival/creative/adventure mode can be released in spectator mode      |
| [MC-84873](https://bugs.mojang.com/browse/MC-84873)   | DeathTime values 20+ cause corrupted mobs                                                          |
| [MC-90084](https://bugs.mojang.com/browse/MC-90084)   | When sitting in boats and boats with chest mobs legs penetrate the hull                            |
| [MC-93018](https://bugs.mojang.com/browse/MC-93018)   | Wild wolves show breeding hearts but do not breed                                                  |
| [MC-119417](https://bugs.mojang.com/browse/MC-119417) | A spectator can occupy a bed if they enter it and then are switched to spectator mode              |
| [MC-119754](https://bugs.mojang.com/browse/MC-119754) | Firework boosting on elytra continues in spectator mode                                            |
| [MC-121903](https://bugs.mojang.com/browse/MC-121903) | Command block minecarts do not save execution cooldown to NBT                                      |
| [MC-124177](https://bugs.mojang.com/browse/MC-124177) | Teleporting to another dimension loses some client states                                          |
| [MC-129909](https://bugs.mojang.com/browse/MC-129909) | Players in spectator mode continue to consume foods and liquids shortly after switching game modes |
| [MC-132878](https://bugs.mojang.com/browse/MC-132878) | Armor stands destroyed by explosions/lava/fire don't produce particles                             |
| [MC-193343](https://bugs.mojang.com/browse/MC-193343) | Soul Speed effect remains after switching to spectator mode                                        |
| [MC-199467](https://bugs.mojang.com/browse/MC-199467) | Certain entity animations stop after they've existed in world for too long                         |
| [MC-200418](https://bugs.mojang.com/browse/MC-200418) | Cured baby zombie villagers stay as jockey variant                                                 |
| [MC-206705](https://bugs.mojang.com/browse/MC-206705) | Spyglasses stay in use in spectator mode                                                           |
| [MC-215530](https://bugs.mojang.com/browse/MC-215530) | The freezing effect isn't immediately removed when switching into spectator mode                   |
| [MC-223153](https://bugs.mojang.com/browse/MC-223153) | Block of Raw Copper uses stone sounds instead of copper sounds                                     |
| [MC-231743](https://bugs.mojang.com/browse/MC-231743) | minecraft.used:minecraft.POTTABLE_PLANT doesn't increase when placing plants into flower pots      |

## Patched in snapshots
To delete when next version comes out.
### Client Side
### Server Side (Both)

## Previously patched
Bugs that this mod has patched but has been superseded by a vanilla update.
### Client Side
### Server Side (Both)
