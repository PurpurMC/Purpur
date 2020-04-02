=============
Configuration
=============

This page details the various configuration settings exposed by Purpur in the purpur.yml file.

.. |br| raw:: html

.. contents::
   :depth: 1
   :local:

If you are looking for details on biome specific settings found in biomes.yml click here :doc:`biomes`.

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
* **description**: Sets whether the server should dump all configuration values to the server log on startup

config-version
~~~~~~~~~~~~~~
* **Do not change this for any reason!** This is used internally to help automatically update your config

use-alternate-keepalive
~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Uses a different approach to keepalive ping timeouts. Enabling this sends a keepalive packet once per second to a player, and only kicks for timeout if none of them were responded to in 30 seconds. Responding to any of them in any order will keep the player connected. AKA, it won't kick your players because 1 packet gets dropped somewhere along the lines

tps-catchup
~~~~~~~~~~~
* **default**: true
* **description**: Control tps catch-up

.. note::
    TPS catchup makes your server tick faster than 20 TPS after any period of time that below 20. This is an attempt at keeping the average TPS as close to 20 as possible, but does come with its own set of side effects

dont-send-useless-entity-packets
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Skips sending relative move packets for entities that didn't really move

.. warning::
    The `dont-send-useless-entity-packets` option is highly experimental! Only enable this if you are feeling brave

fix-item-position-desync
~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Update item positions according to the rounding the client does

.. warning::
    The `fix-item-position-desync` option is highly experimental! Only enable this if you are feeling brave

lagging-threshold:
* **default**: 19.0
* **description**: Purpur keeps track of when it is lagging in order to have the ability to change behaviors accordingly. This value is that threshold when you want to consider the server to be lagging. Right now this is only used for mob.villager.brain-ticks setting

upnp-port-forwarding
~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Attempt to automatically port forward using UPnP

recalculate-perms-on-world-change
~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description**: When a player changes worlds the server recalculates their permissions and resends their available commands

logger
~~~~~~
* suppress-unknown-attribute-warnings
    - **default**: false
    - **description**: Suppress warnings about unknown attributes in console and logs

* suppress-init-legacy-material-errors
    - **default**: false
    - **description**: Suppress warnings about plugins initializing the legacy material api

* suppress-world-gen-feature-deserialization-errors
    - **default**: false
    - **description**: Suppress errors about world gen unable to deserialize unknown features

packet-limiter
~~~~~~~~~~~~~~
* packets-per-second
    - **default**: 250
    - **description**: Maximum allowed packets per second

* packet-spam-interval
    - **default**: 10.0
    - **description**: How long (in seconds) to track packets for

* kick-message
    - **default**: "Sent too many packets"
    - **description**: The message player sees when being kicked from packet limiter

* enchantment
    * <enchantment id>
        * max-level
            - **default**: 1 through 5
            - **description**: Maximum level the enchantment can be
.. note::
    Enchantment level lore is rendered client side. Levels above 11 do not have a lang entry in the locale file. Use a resource pack to overcome this.

blocks
~~~~~~
* barrel
    * six-rows
        - **default**: false
        - **description**: Barrels should have 6 rows of inventory space

* ender_chest
    * six-rows
        - **default**: false
        - **description**: Ender chests should have 6 rows of inventory space
    * use-permissions-for-rows
        - **default**: false
        - **description**: Use permission nodes to determine the number of rows. `six-rows` MUST be enabled for this to work.

.. note::
    Enderchest row permissions:
        purpur.enderchest.rows.six
        purpur.enderchest.rows.five
        purpur.enderchest.rows.four
        purpur.enderchest.rows.three
        purpur.enderchest.rows.two
        purpur.enderchest.rows.one

* slime
    * not-movable-by-piston
        - **default**: false
        - **description**: Slime blocks should not be movable by pistons

timings
~~~~~~~
* url
    - **default**: "https://timings.pl3x.net"
    - **description**: The server where timing reports are posted to. To use Aikar's timings server use "http://timings.aikar.co"

World Settings
==============

World settings are on a per-world basis. The child-node `default` is used for all worlds that do not have their own specific settings

blocks
~~~~~~
* bamboo
    * max-height:
        - **default**: 16
        - **description**: Maximum height bamboo may grow to

    * small-height:
        - **default**: 10
        - **description**: Maximum height bamboo may be small thickness

* bed
    * explode
        - **default**: true
        - **description**: Whether beds explode. Setting this to false just makes the bed blip out of existence

    * explosion-power
        - **default**: 5.0
        - **description**: The blast radius of the explosion. (For comparison, TNT is 4.0 and charged creepers are 6.0)

    * explosion-fire
        - **default**: true
        - **description**: Whether the explosion can cause fire or not

    * explosion-effect
        - **default**: DESTROY
        - **description**: What to do with the blocks that are effected by the explosion. `DESTROY` will destroy the blocks (no item drops). `BREAK` will naturally break the blocks (items will drop). `NONE` will not break any blocks

* campfire
    * burn-out-in-rain
        - **default**: false
        - **description**: Campfires burn out in the rain

    * fall-with-gravity
        - **default**: false
        - **description**: When true, campfires will fall to the ground (like anvils do) instead of floating in the air

    * regen
        * requires-potion-to-activate
            - **default**: true
            - **description**: Regen on campfires only works once it has been splashed with a potion of regen

        * interval
            - **default**: 0
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
            - **default**: 0
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

* dispenser
    * apply-cursed-to-armor-slots
        - **default**: true
        - **description**: Should dispensers apply armor to armor slots if enchanted with curse of binding

* farmland
    * get-moist-from-below
        - **default**: false
        - **description**: Allow soil to moisten from water directly below it

* lava
    * infinite-source
        - **default**: false
        - **description**: Allow lava to take on infinite supply properties similar to water (two source blocks flowing together creates a new source block)

    * speed
        * nether
            - **default**: 10
            - **description**: Delay in ticks between physics/flowing (lower is faster)

        * not-nether
            - **default**: 30
            - **description**: Delay in ticks between physics/flowing (lower is faster)

* sign
    * allow-colors
        - **default**: false
        - **description**: Allow players to use color codes on signs

    * right-click-edit
        - **default**: false
        - **description**: Ability to edit signs by right clicking them with another sign in hand

* turtle_egg
    * break-from-exp-orbs
        - **default**: true
        - **description**: Allow exp orbs to damage/break turtle eggs

    * break-from-items
        - **default**: true
        - **description**: Allow dropped items to damage/break turtle eggs

    * break-from-minecarts
        - **default**: true
        - **description**: Allow minecarts to damage/break turtle eggs

* hay_block
    * fall-damage
        - **default**: 0.2
        - **description**: Damage factor for when falling onto hay blocks. 0 will apply no fall damage, 1.0 will apply 100% fall damage. Default of 0.2 will apply 20% of fall damage (80% reduction)

* grindstone
    * blacklist
        * disallow-placement
            - **default**: true
            - **description**: Disallow placing blacklisted items into the grindstone UI slots

        * returns-zero-exp
            - **default**: true
            - **description**: Return 0 exp for blacklisted items in the grindstone

        * blacklisted-items
            - **default**: []
            - **description**: List of blacklisted items for grindstone

.. note::
    Example of blacklisted-items:
      * blacklisted-items:
         - minecraft:tripwire_hook
         - minecraft:stone
         - minecraft:grass_block

gameplay-mechanics
~~~~~~~~~~~~~~~~~~
* disable-drops-on-cramming-death
    - **default**: false
    - **description**: Stops entities from dropping loot on death, if killed by cramming gamerule

* fix-climbing-bypassing-cramming-rule
    - **default**: false
    - **description**: Stops entities from bypassing the cramming gamerule by climbing

* milk-cures-bad-omen
    - **default**: true
    - **description**: Allow players to drink milk to cure bad omen status effect

* use-better-mending
    - **default**: false
    - **description**: Set to true for mending enchantment to always repair the most damaged equipment first

* save-projectiles-to-disk
    - **default**: true
    - **description**: Save projectile entities to the world/chunk so they can be reloaded later

* armorstand
    * step-height
        - **default**: 0.0
        - **description**: Set the default step height of armorstands. Useful for plugins that utilize armorstands as vehicles to be able to drive over blocks without jumping, etc

* boat
    * eject-players-on-land
        - **default**: false
        - **description**: Boats should eject players when on land

* trident-loyalty-void-return-height
    - **default**: 0.0
    - **description**: The void height at which a trident with loyalty will return to it's thrower. A value of 0.0 or higher disables this feature.

* void-damage-height
    - **default**: -64.0
    - **description**: Lower limit where void damage starts to happen

* controllable-minecarts
    * enabled
        - **default**: true
        - **description**: Whether minecarts can be controlled with WASD when not on rails

    * place-anywhere
        - **default**: false
        - **description**: Whether minecarts can be placed anywhere, not just on rails

    * step-height
        - **default**: 1.0
        - **description**: The step height in which a minecarts can go up to the next block without jumping

    * hop-boost
        - **default**: 0.5
        - **description**: Jump power when pressing spacebar on a controllable minecart

    * base-speed
        - **default**: 0.1
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

* item
    * float-in-lava
        - **default**: false
        - **description**: Can items float in lava

    * immune
        * explosions
            - **default**: {}
            - **description**: List of items that are immune to explosions

        * fire
            - **default**: {}
            - **description**: List of items that are immune to fire

        * lava
            - **default**: {}
            - **description**: List of items that are immune to lava

.. note::
    Example of item immune list:
      * explosions:
         - minecraft:diamond
         - minecraft:diamond_block
         - minecraft:diamond_sword

.. warning::
    These item immune lists can cause client desync issues, such as invisible items on the ground!
    There is nothing I can do about that from the server side, but I have patched this in my
    client mod, (PurpurClient) <https://ci.pl3x.net/job/PurpurClient/>, starting with build #12.


* player
    * exp-dropped-on-death
        * equation
            - **default**: expLevel * 7
            - **description**: How much exp to drop on death. Available NMS variables are `expLevel`, `expTotal`, and `exp`

        * maximum
            - **default**: 100
            - **description**: Maximum amount of exp value to drop on death

    * sleep
        * only-with-condition
            - **default**: false
            - **description**: Make players only sleep when the following time condition is true

        * condition
            - **default**: "time >= 12541 && time <= 23458"
            - **description**: The time condition for player to be able to sleep

    * idle-timeout
        * kick-if-idle
            - **default**: true
            - **description**: Kick players if they become idle (see server.properties for player-idle-timeout time)

        * tick-nearby-entities
            - **default**: true
            - **description**: Should entities tick normally when nearby players are afk. False will require at least 1 non-afk player in order to tick

        * count-as-sleeping
            - **default**: false
            - **description**: Should AFK players count as sleeping? (allows active players to skip night by sleeping, even if AFK players are not in bed)

        * update-tab-list
            - **default**: false
            - **description**: Should AFK players have their name updated in the tab list (puts `[AFK]` in front of their name)

        * broadcast
            * away
                - **default**: "&e&o{player} is now AFK"
                - **description**: The message to broadcast server-wide when a player goes afk. Set to empty string ("") to disable
            * back
                - **default**: "&e&o{player} is no longer AFK"
                - **description**: The message to broadcast server-wide when a player comes back from being afk. Set to empty string ("") to disable

* elytra
    * damage-per-second
        - **default**: 1
        - **description**: How much damage an elytra takes during flight each second

    * damage-multiplied-by-speed
        - **default**: 0.0
        - **description**: Damage is multiplied by speed if flight is faster than set speed. Value of 0 disables this multiplier

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

mobs
~~~~
* bat
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* bee
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* blaze
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* cat
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * spawn-delay
        - **default**: 1200
        - **description**: Number of ticks between attempting to naturally spawn a cat
    * scan-range-for-other-cats
        * swamp-hut
            - **default**: 16
            - **description**: Do not spawn a cat if another cat is found within this range. Set to 0 to disable
        * village
            - **default**: 48
            - **description**: Do not spawn a cat if another cat is found within this range. Set to 0 to disable

* cave_spider
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* chicken
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * dont-lay-eggs-when-ridden
        - **default**: false
        - **description**: Can chickens lay eggs while being ridden
    * eggs-hatch-when-despawned
        * max
            - **default**: 0
            - **description**: Maximum number of chickens in an area allowed to spawn a chicken when an egg despawns. Set to 0 to disable feature
        * range
            - **default**: 10
            - **description**: The range in which to check for maximum number of allowed chickens

* cod
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* cow
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * feed-mushrooms-for-mooshroom
        - **default**: 0
        - **description**: Number of mushrooms to feed a cow to make it transform into a mooshroom. Value of 0 disables feature

* creeper
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * naturally-charged-chance
        - **default**: 0.0
        - **description**: Chance creepers are charged (powered) when spawning (0.0 - 1.0)

* dolphin
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* donkey
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)

* drowned
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * jockey
        * chance
            - **default**: 0.05
            - **description**: Chance of riding a chicken when spawned
        * only-babies
            - **default**: true
            - **description**: Only babies can ride chickens
        * try-existing-chickens
            - **default**: true
            - **description**: Scan for existing chickens to spawn on

* elder_guardian
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* ender_dragon
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden
    * always-drop-egg-block
        - **default**: false
        - **description**: When true all valid ender dragon deaths will place an ender egg block on top of the portal
    * always-drop-full-exp
        - **default**: false
        - **description**: When true all valid ender dragon deaths will drop the full amount of experience orbs as if it were the first dragon death

* enderman
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* endermite
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* evoker
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* fox
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * tulips-change-type
        - **default**: false
        - **description**: Feeding a white/orange tulip changes type snow/regular

* ghast
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* giant
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * step-height
        - **default**: 2.0
        - **description**: How many blocks giants can walk up without having to jump
    * jump-height
        - **default**: 1.0
        - **description**: Jump height modifier. Default value of 1.0 makes giants jump about as high as their waist
    * max-health
        - **default**: 100.0
        - **description**: Max health attribute
    * movement-speed
        - **default**: 0.5
        - **description**: Movement speed attribute
    * attack-damage
        - **default**: 50.0
        - **description**: Attack damage (in half hearts)
    * naturally-spawn
        - **default**: false
        - **description**: Control if giant zombies naturally spawn in the game
    * have-ai
        - **default**: false
        - **description**: Control if giant zombies have AI instead of just standing there
    * have-hostile-ai
        - **default**: false
        - **description**: Control if giant zombies have hostile AI also

* guardian
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* husk
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * jockey
        * chance
            - **default**: 0.05
            - **description**: Chance of riding a chicken when spawned
        * only-babies
            - **default**: true
            - **description**: Only babies can ride chickens
        * try-existing-chickens
            - **default**: true
            - **description**: Scan for existing chickens to spawn on

* horse
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)

* illusioner
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * naturally-spawn
        - **default**: false
        - **description**: Control if illusioners naturally spawn in the game
    * max-health
        - **default**: 32.0
        - **description**: Max health attribute
    * movement-speed
        - **default**: 0.5
        - **description**: Movement speed attribute
    * follow-range
        - **default**: 18.0
        - **description**: Follow range attribute

* iron_golem
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * can-spawn-in-air
        - **default**: false
        - **description**: Set whether iron golems can spawn in the air, like in 1.12 and below
    * can-swim
        - **default**: false
        - **description**: Set whether iron golems can swim or not

* llama
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable. Llama's must be tamed and saddled (with carpet) to be WASD controllable.
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)

* trader_llama
    * ridable
        - **default**: false
        - **description**: Makes this mob mountable and WASD controllable. Trader llama's must be tamed to be WASD controllable. Being saddled (carpet) is not a requirement since it technically always has a carpet.
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)

* magma_cube
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* mooshroom
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* mule
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)

* ocelot
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* panda
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* parrot
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* phantom
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden
    * do-not-spawn-on-creative-players
        - **default**: false
        - **description**: Creative players will not cause phantoms to spawn
    * only-attack-insomniacs
        - **default**: false
        - **description**: Make phantoms only attack insomniac players. Players that have slept recently will be ignored
    * crystals-attack-range
        - **default**: 0.0
        - **description**: Radius crystals scan for phantoms to attack. Value of 0 disables feature
    * crystals-attack-damage
        - **default**: 1.0
        - **description**: Amount of damage per second crystals deal to phantoms. Value of 1.0 is half a heart
    * orbit-crystal-radius
        - **default**: 0.0
        - **description**: Radius which phantoms scan for crystals to orbit. Value of 0 disables feature

* pig
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* pillager
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * limit-outpost-spawns
        * **default**: 0
        * **description**: Limit the number of pillagers allowed to spawn at an outpost at any given time. 0 disables the limit

* polar_bear
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * breedable-item
        - **default**: ""
        - **description**: Item to tempt/feed polar bears and make them breed

* pufferfish
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* rabbit
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * spawn-killer-rabbit-chance
        - **default**: 0.0
        - **description**: Percent chance (0.0-1.0) the killer rabbit naturally spawns
    * spawn-toast-chance
        - **default**: 0.0
        - **description**: Percent chance (0.0-1.0) to naturally spawn a rabbit named Toast

* ravager
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* salmon
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* sheep
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* shulker
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* silverfish
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* skeleton
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* skeleton_horse
    * can-swim
        - **default**: false
        - **description**: Can skeleton horses swim in water. False makes them sink to the bottom (vanilla default)
    * ridable-in-water
        - **default**: true
        - **description**: Makes this mob ridable in water (it wont eject you)

* slime
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* snow_golem
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * leave-trail-when-ridden
        - **default**: false
        - **description**: Leaves a trail where a snowman walks when being ridden
    * drops-pumpkin-when-sheared
        - **default**: false
        - **description**: Control if shearing a snowman makes the pumpkin drop to the ground
    * pumpkin-can-be-added-back
        - **default**: false
        - **description**: Control if pumpkins can be placed back onto snowmen

* spider
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* squid
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* stray
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* tropical_fish
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* turtle
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* vex
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* villager
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * use-brain-ticks-only-when-lagging
        - **default**: true
        - **description**: Only use the brain ticks setting when the server is lagging (see lagging-threshold above). If set to false, the brain ticks setting is always used
    * brain-ticks
        - **default**: 1
        - **description**: How often (in ticks) should villager's tick their brain logic. Vanilla value is to tick every tick (1). Higher amounts makes them tick less often to reduce lag, but setting it too high could result is unresponsive villagers
    * can-be-leashed
        - **default**: false
        - **description**: Allow players to use leads on villagers (trader not included)
    * follow-emerald-blocks
        - **default**: false
        - **description**: Villagers will be tempted by emerald blocks and follow players holding them
    * spawn-iron-golem
        * radius
            * **default**: 0
            * **description**: Radius villagers search for existing iron golems before spawning more. Value of 0 disables features
        * limit
            * **default**: 0
            * **description**: Maximum amount of iron golems villagers can spawn in configured radius

* vindicator
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* wandering_trader
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * can-be-leashed
        - **default**: false
        - **description**: Allow players to use leads on villagers (trader not included)
    * follow-emerald-blocks
        - **default**: false
        - **description**: Villagers will be tempted by emerald blocks and follow players holding them

* witch
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* wither
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * ridable-max-y
        - **default**: 256
        - **description**: Maximum height this mob can fly to while being ridden

* wither_skeleton
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * takes-wither-damage
        * **default**: false
        * **description**: Allows wither skeletons to receive the wither effect (from wither roses, etc)

* wolf
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount

* zombie
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * target-turtle-eggs
        - **default**: true
        - **description**: Should zombies target/stomp turtle eggs
    * transform-to-villager-chance
        - **default**: -0.1
        - **description**: Chance that a villager can turn into a zombie upon death. Overrides world difficulty. Set to a negative number to use vanilla's default behavior
    * jockey
        * chance
            - **default**: 0.05
            - **description**: Chance of riding a chicken when spawned
        * only-babies
            - **default**: true
            - **description**: Only babies can ride chickens
        * try-existing-chickens
            - **default**: true
            - **description**: Scan for existing chickens to spawn on

* zombie_horse
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * spawn-chance
        - **default**: 0.0
        - **description**: Percent chance (0.0 - 1.0) a zombie horse will spawn instead of a skeleton horse (natural spawns during thunderstorms)

* zombie_pigman
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * dont-target-unless-hit
        - **default**: false
        - **description**: Prevent pigmen from targetting players unless they are hit. (fixes MC-56653)
    * jockey
        * chance
            - **default**: 0.05
            - **description**: Chance of riding a chicken when spawned
        * only-babies
            - **default**: true
            - **description**: Only babies can ride chickens
        * try-existing-chickens
            - **default**: true
            - **description**: Scan for existing chickens to spawn on

* zombie-villager
    * ridable
        - **default**: false
        - **description**: Makes this mob WASD controllable
    * ridable-in-water
        - **default**: false
        - **description**: Makes this mob ridable in water (it wont eject you)
    * require-shift-to-mount
        - **default**: true
        - **description**: Required to crouch (hold shift) and right click to mount
    * jockey
        * chance
            - **default**: 0.05
            - **description**: Chance of riding a chicken when spawned
        * only-babies
            - **default**: true
            - **description**: Only babies can ride chickens
        * try-existing-chickens
            - **default**: true
            - **description**: Scan for existing chickens to spawn on
