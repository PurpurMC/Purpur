=============
Configuration
=============

This page details the various configuration settings exposed by Purpur in the purpur.yml file.

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

enable-tick-overload
~~~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Enable/disable the vanilla tick overload detection ("Can't keep up! Is the server overloaded?")

enable-tps-catchup
~~~~~~~~~~~~~~~~~~
* **default**: false
* **description**: Enable/disable Spigot's TPS catchup (makes everything tick faster than 20 tps after lag spikes, which can cause more lag - also skews /tps reports by ruining the average with above 20 tps entries)

packed-barrels
~~~~~~~~~~~~~~
* **default**: true
* **description:** Use large size barrels (6 rows, aka 54 slots)

ender-dragon-death-always-places-egg-block
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* **default**: true
* **description:** When true all valid ender dragon deaths will place an ender egg block on top of the portal

logger
~~~~~~
* show-duplicate-entity-uuid-errors
    - **default**: true
    - **description**:: Controls if errors about duplicate entity uuids are shown in console/logs

* show-unknown-attribute-warnings
    - **default**: true
    - **description**:: Controls if warnings about unknown attributes are shown in console/logs

mobs
~~~~
* cow
    * feed-mushrooms-for-mooshroom
        - **default**: 5
        - **description**: Number of mushrooms to feed a cow to make it transform into a mooshroom. Set to 0 to disable.

* giant
    * naturally-spawn
        - **default**: true
        - **description**: Control if giant zombies naturally spawn in the game

    * have-ai
        - **default**: true
        - **description**: Control if giant zombies have AI instead of just standing there

* snow_golem
    * drops-pumpkin-when-sheared
        - **default**: true
        - **description**: Control if shearing a snowman makes the pumpkin drop to the ground

    * pumpkin-can-be-added-back
        - **default**: true
        - **description**: Control if pumpkins can be placed back onto snowmen

World Settings
==============

World settings are on a per-world basis. The child-node `default` is used for all worlds that do not have their own specific settings.

editable-signs
~~~~~~~~~~~~~~
* **default**: true
* **description**: Ability to edit signs by right clicking them with another sign in hand

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
