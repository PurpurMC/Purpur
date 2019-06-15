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

* **purpur.drop.spawners**
    - **description**: Players with this permission can use a diamond pickaxe with silk
    touch enchantment to mine up any spawner cage instead of disappearing.

* **purpur.place.spawners**
    - **description**: Players with this permission can place down a spawner cage and
    have the mob type restored to what it was when it was mined using silk touch.
