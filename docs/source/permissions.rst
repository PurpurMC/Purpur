==========
Permissions
==========

.. contents::
   :depth: 2
   :local:

Permissions
=======

Purpur adds a few new permission nodes for some of it's added features.

By default **all** of these permissions are for OP users only. Any other users will
need to be granted the permissions you want them to have using a permissions plugin.

* **bukkit.command.purpur**
    - **description**: This permission gives the ability to use the /purpur command

* **allow.ride.<mob_id>**
    - **description**: This permission gives the ability to ride a certain mob by shift
    right clicking it. Once mounted you can use WASD to move around, and spacebar to jump
    or fly. Just replace "<mob_id>" with the mob's Entity ID.

    - **examples**:
        - `allow.ride.cow`
        - `allow.ride.zombie_pigman`
        - `allow.ride.snow_golem`

* **allow.special.<mob_id>**
    - **description**: This permission gives the ability to activate a ridable mob's
    special ability. Not all mobs have a special ability. Just replace "<mob_id>" with
    the mob's Entity ID.

* **allow.powered.creeper**
    - **description**: This permission gives the ability to toggle a creeper's powered state.
    Hold spacebar while not moving to charge the toggle. Instead of blowing up the powered
    state will toggle on or off.

* **purpur.debug.f3n**
    - **description**: Allows the use of the F3+N debug hotkey to swap gamemodes.
    Player must have this perm _and_ the gamemode perm for it to work.

* **purpur.drop.spawners**
    - **description**: Players with this permission can use a diamond pickaxe with silk
    touch enchantment to mine up any spawner cage instead of disappearing.

* **purpur.place.spawners**
    - **description**: Players with this permission can place down a spawner cage and
    have the mob type restored to what it was when it was mined using silk touch.

* **purpur.sign.edit**
    - **description**: Allows players to open the sign editor when right clicking a sign
    while holding a sign.

* **purpur.sign.color**
    - **description**: Allows players to use color codes on signs

* **purpur.sign.style**
    - **description**: Allows players to use style codes on signs (except the magic/obfuscated code)

* **purpur.sign.magic**
    - **description**: Allows players to use the magic/obfuscated style code on signs

* **purpur.enderchest.rows.six**
    - **description**: Allows players to have 6 rows in their enderchest

* **purpur.enderchest.rows.five**
    - **description**: Allows players to have 5 rows in their enderchest

* **purpur.enderchest.rows.four**
    - **description**: Allows players to have 4 rows in their enderchest

* **purpur.enderchest.rows.three**
    - **description**: Allows players to have 3 rows in their enderchest

* **purpur.enderchest.rows.two**
    - **description**: Allows players to have 2 rows in their enderchest

* **purpur.enderchest.rows.one**
    - **description**: Allows players to have 1 row in their enderchest

.. note::
    Enderchest row permissions require `settings.blocks.ender_chest.six-rows` to be enabled!