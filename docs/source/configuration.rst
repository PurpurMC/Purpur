=============
Configuration
=============

This page details the various configuration settings exposed by Purpur in the purpur.yml file.

.. |br| raw:: html

.. contents::
   :depth: 1
   :local:

If you want information on settings in paper.yml, spigot.yml, and bukkit.yml you should see
their respective documentation pages.

* `Bukkit Configuration (bukkit.yml) <https://bukkit.gamepedia.com/Bukkit.yml>`_

* `Spigot Configuration (spigot.yml) <https://www.spigotmc.org/wiki/spigot-configuration/>`_

* `Paper Configuration (paper.yml) <https://paper.readthedocs.io/en/stable/server/configuration.html>`_

.. warning::
    Configuration values change frequently at times. It is possible for the
    information here to be incomplete. If you cannot find what you're looking for
    or think something may be wrong, :doc:`contact`

Global Settings
===============

Global settings affect all worlds on the server as well as the core server
functionality itself.

verbose
~~~~~~~
* **default**: false
* **description**: Sets whether the server should dump all configuration values to the server log on startup.

lagging-threshold:
* **default**: 19.0
* **description**: Purpur keeps track of when it is lagging in order to have the ability to change behaviors accordingly. This value is that threshold when you want to consider the server to be lagging. Right now this is only used for mob.villager.brain-ticks setting.

disable-drops-on-cramming-death
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Stops entities from dropping loot on death, if killed by cramming gamerule

dont-send-useless-entity-packets
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Skips sending relative move packets for entities that didn't really move

.. warning::
    The `dont-send-useless-entity-packets` option is highly experimental! Only enable this if you are feeling brave.

fix-item-position-desync
~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Update item positions according to the rounding the client does

.. warning::
    The `fix-item-position-desync` option is highly experimental! Only enable this if you are feeling brave.

fix-zero-tick-farms
~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Prevents crops from growing using the zero-tick trick (fixes MC-113809)

large-ender-chests
~~~~~~~~~~~~~~
* **default**: true
* **description:** Use large size ender chests (6 rows, aka 54 slots)

packed-barrels
~~~~~~~~~~~~~~
* **default**: true
* **description:** Use large size barrels (6 rows, aka 54 slots)

suppress-unknown-attribute-warnings
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description:** Suppress warnings about unknown attributes in console and logs

use-better-mending
~~~~~~~~~~~~~~~~~~
* **default**: true
* **description:** Set to true for mending enchantment to always repair the most damaged equipment first

use-alternate-keepalive
~~~~~~~~~~~~~~~~~~
* **default**: false
* **description:** Uses a different approach to keepalive ping timeouts. Enabling this sends a keepalive packet once per second to a player, and only kicks for timeout if none of them were responded to in 30 seconds. Responding to any of them in any order will keep the player connected. AKA, it won't kick your players because 1 packet gets dropped somewhere along the lines.

update-perms-on-world-change
~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description:** When a player changes worlds the server recalculates their permissions and resends their available commands. This can be laggy, so the option is disabled by default

slime-blocks-not-pushable
~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description:** Prevent slime blocks from being pushable by pistons. Useful for preventing multiple types of duplication machines.

infinite-lava
~~~~~~~~~~~~~
* **default:** false
* **description:** Allow lava to take on infinite supply properties similar to water (two source blocks flowing together creates a new source block)

grindstone
~~~~~~~~~~~~~~~~~~~~~~
* disallow-placement
    - **default**: true
    - **description**: Disallow placing blacklisted items into the grindstone UI slots

* returns-zero-exp
    - **default**: true
    - **description**: Return 0 exp for blacklisted items in the grindstone

* blacklisted-items
    - **default**: {}
    - **description**: List of blacklisted items for grindstone

.. note::
    Example of blacklisted-items:
      * blacklisted-items:
         - minecraft:tripwire_hook
         - minecraft:stone
         - minecraft:grass_block

logger
~~~~~~
* show-duplicate-entity-uuid-errors
    - **default**: true
    - **description**:: Controls if errors about duplicate entity uuids are shown in console/logs

* show-unknown-attribute-warnings
    - **default**: true
    - **description**:: Controls if warnings about unknown attributes are shown in console/logs

armorstand
~~~~~~~~~~
* step-height
    - **default**: 0.0
    - **description**:: Set the default step height of armorstands. Useful for plugins that utilize armorstands as vehicles to be able to drive over blocks without jumping, etc.

mobs
~~~~
* require-shift-to-mount
    - **default**: true
    - **description**: Require shift click to mount otherwise non-ridable mobs

* chicken
    * eggs-hatch-when-despawned
        - **default:** false
        - **description:** When chicken egg despawns a chicken is hatched in its place

* cow
    * feed-mushrooms-for-mooshroom
        - **default**: 0
        - **description**: Number of mushrooms to feed a cow to make it transform into a mooshroom. Value of 0 disables feature.

* creeper
    * naturally-charged-chance
        - **default:** 0.0
        - **description:** Chance creepers are charged (powered) when spawning

* ender-dragon
    * always-drop-egg-block
        - **default**: false
        - **description:** When true all valid ender dragon deaths will place an ender egg block on top of the portal
    * always-drop-full-exp
        - **default**: false
        - **description:** When true all valid ender dragon deaths will drop the full amount of experience orbs as if it were the first dragon death

* fox
    * tulips-change-type
        - **default**: true
        - **description**: Feeding a white/orange tulip changes type snow/regular.

* giant
    * naturally-spawn
        - **default**: true
        - **description**: Control if giant zombies naturally spawn in the game

    * have-ai
        - **default**: true
        - **description**: Control if giant zombies have AI instead of just standing there

* illusioner
    * naturally-spawn
        - **default**: true
        - **description**: Control if illusioners naturally spawn in the game

* iron_golem
    * swims
        - **default**: true
        - **description**: Set whether iron golems can swim or not
    * can-spawn-in-air
        - **default**: false
        - **description**: Set whether iron golems can spawn in the air, like in 1.12 and below

* phantom
    * crystals-attack-range
        - **default**: 0.0
        - **description**: Radius crystals scan for phantoms to attack. Value of 0 disables feature
    * crystals-attack-damage
        - **default**: 1.0F
        - **description**: Amount of damage per second crystals deal to phantoms. Value of 1.0 is half a heart
    * orbit-crystal-radius
        - **default**: 0.0
                - **description**: Radius which phantoms scan for crystals to orbit. Value of 0 disables feature
    * spawn-in-the-end
        - **default**: false
        - **description**: Set whether phantoms spawn naturally in the end
    * only-attack-insomniacs
        - **default:** false
        - **description:** Make phantoms only attack insomniac players. Players that have slept recently will be ignored.

* pigmen
    * dont-target-unless-hit
        - **default**: false
        - **description**: Prevent pigmen from targetting players unless they are hit. (fixes MC-56653)

* rabbit
    * spawn-killer-rabbit-chance
        - **default**: 0.0
        - **description**: Percent chance (0.0-1.0) the killer rabbit naturally spawns
    * spawn-toast-chance
        - **default**: 0.0
        - **description**: Percent chance (0.0-1.0) to naturally spawn a rabbit named Toast

* snow_golem
    * drops-pumpkin-when-sheared
        - **default**: false
        - **description**: Control if shearing a snowman makes the pumpkin drop to the ground

    * pumpkin-can-be-added-back
        - **default**: false
        - **description**: Control if pumpkins can be placed back onto snowmen

* villager
    * use-brain-ticks-only-when-lagging
        - **default**: true
        - **description**: Only use the brain ticks setting when the server is lagging (see lagging-threshold above). If set to false, the brain ticks setting is always used.
    * brain-ticks
        - **default**: 2
        - **description**: How often (in ticks) should villager's tick their brain logic. Vanilla value is to tick every tick (1). Higher amounts makes them tick less often to reduce lag, but setting it too high could result is unresponsive villagers.
    * allow-leashing
        - **default**: false
        - **description**: Allow players to use leads on villagers (trader not included)
    * follow-emerald-blocks
        - **default:** false
        - **description:** Villagers will be tempted by emerald blocks and follow players holding them

* zombie
    * target-turtle-eggs
        - **default**: true
        - **description**: Should zombies target/stomp turtle eggs

* zombie_horse
    * spawn-chance
        - **default**: 0
        - **description**: Percent chance (0.0 - 1.0) a zombie horse will spawn instead of a skeleton horse (natural spawns during thunderstorms)

ridable
~~~~~~~
* <mob string id here>
    - **default**: true
    - **description**: When true this mob is ridable by right clicking it while holding shift

controllable-minecarts
~~~~~~~~~~~~~~~~~~~~~~
* enabled
    - **default**: true
    - **description**: Whether minecarts can be controlled with WASD when not on rails

* base-speed
    - **default**: 0.2
    - **description**: Base speed of minecart when controlled with WASD

* block-speed
    - **default**: {}
    - **description**: List of speed overrides per block type

.. note::
    Example of block-speed overrides:
      * block-speed:
         - minecraft:sand: 0.1
         - minecraft:stone: 0.6
         - minecraft:black_concrete: 1.0

World Settings
==============

World settings are on a per-world basis. The child-node `default` is used for all worlds that do not have their own specific settings.

editable-signs
~~~~~~~~~~~~~~
* **default**: true
* **description**: Ability to edit signs by right clicking them with another sign in hand

bamboo
~~~~~~
* max-height:
    - **default**: 16
    - **description**: Maximum height bamboo may grow to

* small-height:
    - **default**: 10
    - **description**: Maximum height bamboo may be small thickness

* boat-eject-players-on-land
    - **default**: false
    - **description**: Whether or not boats eject players when on land

block-tick-events
~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Enable block tick events

fluid-tick-events
~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Enable fluid tick events

campfire-obeys-gravity
~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: When true, campfires will fall to the ground (like anvils do) instead of floating in the air

campfire-regen
~~~~~~~~~~~~~~
* interval
    - **default**: 40
    - **description**: Time (in ticks) that campfires scan for player and apply regen on. Regen buff only gets applied if campfire is lit. Set to 0 to disable

* duration
    - **default**: 80
    - **description**: How long (in ticks) the regen buff lasts

* range
    - **default**: 5
    - **description**: Distance (in blocks) a player must be within to receive the regen buff

* amplifier
    - **default**: 0
    - **description**: The amplifier on the regen buff. `0` for level 1, `1` for level 2

* require-line-of-sight
    - **default**: true
    - **description**: Only players within line of sight of the campfire will receive the regen buff

* boost-duration
    - **default**: 80
    - **description**: How long (in ticks) the regen buff lasts when the campfire is in smoke signal mode

* boost-range
    - **default**: 10
    - **description**: Distance (in blocks) a player must be within to receive the regen buff when the campfire is in smoke signal mode

* boost-amplifier
    - **default**: 1
    - **description**: The amplifier on the regen buff when the campfire is in smoke signal mode

* boost-require-line-of-sight
    - **default**: false
    - **description**: Only players within line of sight of the campfire will receive the regen buff when the campfire is in smoke signal mode

campfires-go-out-in-rain
~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Campfires burn out in the rain

dispenser-apply-cursed-armor-slots
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Should dispensers apply armor to armor slots if enchanted with curse of binding

allow-moist-soil-from-water-below
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Allow soil to moisten from water directly below it

allow-sign-colors
~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Allow players to use color codes on signs

items-can-break-turtle-eggs
~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Allow dropped items to damage/break turtle eggs

milk-cures-bad-omen
~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Allow players to drink milk to cure bad omen status effect

block-tick-events
~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Fire plugin events when blocks tick

fluid-tick-events
~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: Fire plugin events when fluids tick

limit-pillager-outpost-spawns
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: 10
* **description**: Limit the number of pillagers allowed to spawn at an outpost at any given time

radius-villager-iron-golem-spawns
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: 0
* **description**: Radius villagers search for existing iron golems before spawning more. Value of 0 disables features

limit-villager-iron-golem-spawns
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: 5
* **description**: Maximum amount of iron golems villagers can spawn in configured radius

idle-timeout
~~~~~~~~~~~~
* kick-if-idle
    - **default**: true
    - **description**: Kick players if they become idle (see server.properties for player-idle-timeout time)

* tick-nearby-entities
    - **default**: false
    - **description**: Should entities tick normally when nearby players are afk. False will require at least 1 non-afk player in order to tick.

* count-as-sleeping
    - **default**: false
    - **description**: Should AFK players count as sleeping? (allows active players to skip night by sleeping, even if AFK players are not in bed)

* update-tab-list
    - **default**: true
    - **description**: Should AFK players have their name updated in the tab list (puts `[AFK]` in front of their name)

* broadcast
    * away
        - **default**: "&e&o{player} is now AFK"
        - **description**: The message to broadcast server-wide when a player goes afk. Set to empty string ("") to disable
    * back
        - **default**: "&e&o{player} is no longer AFK"
        - **description**: The message to broadcast server-wide when a player comes back from being afk. Set to empty string ("") to disable

elytra
~~~~~~
* damage-per-second
    - **default**: 1
    - **description**: How much damage an elytra takes during flight each second

* damage-multiplied-by-speed
    - **default**: 0
    - **description**: Damage is multiplied by speed if flight is faster than set speed. Value of 0 disables this multiplier.

* ignore-unbreaking
    - **default**: false
    - **description**: Should elytras ignore the unbreaking enchantment

* damage-per-boost
    * firework
        - **default**: 0
        - **description**: How much damage to deal to the elytra when firework boost activates

    * trident
        - **default**: 0
        - **description**: How much damage to deal to the elytra when trident riptide boost activates
