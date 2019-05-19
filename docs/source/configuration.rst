=============
Configuration
=============

This page details the various configuration settings exposed by Purpur.

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
    or think something may be wrong, :doc:`../about/contact`

Global Settings
===============

Global settings affect all worlds on the server as well as the core server
functionality itself.

verbose
~~~~~~~
* **default**: false
* **description**: Sets whether the server should dump all configuration values
  to the server log on startup.

logger
~~~~~~
* show-duplicate-entity-uuid-errors
    - **default**: true
    - **description**:: Controls if errors about duplicate entity uuids are shown in console/logs

* show-unknown-attribute-warnings
    - **default**: true
    - **description**:: Controls if warnings about unkown attributes are shown in console/logs

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
