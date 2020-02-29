=============
Biomes.yml
=============

This page details the biome specific options found in biomes.yml

.. |br| raw:: html

.. contents::
   :depth: 1
   :local:

Biomes
===============

spawn-data
~~~~~~~~~~
* **description**: Spawn data is a map of key->value pairs for each entity and it's natural spawn settings per biome. The
    value consists of 3 integers representing the weight, minimum group, and maximum group.

    Weight represents the chance the entity has to spawn. It's not really a percent chance, it's the number of chances this
    entity gets in the rng roll. Take the `ocean` biome, for example. If you add up all the weights for all the entities
    you get a total of 540. Enderman has a weight of 10 here, so it gets 10/540 chance of winning the rng roll.

    There are still other rules to be met before a mob is spawned. Each entity has their own set of rules, which can be
    learned by researching the official minecraft wiki or google. But, for example, lets say a water location was chosen
    to spawn a mob at random and enderman won the rng roll. The enderman will _not_ spawn because it's in water. This tick
    the spawn chance gets skipped and nothing spawns. The rng roll happens every tick until the mob cap has been reached, so
    keep all this in mind when creating your weights.

    The minimum and maximum group values are exactly as they sound. It is the number of mobs of this type that will spawn
    from this attempt.

.. note::
    The default biomes.yml file matches perfectly with the vanilla gameplay. If you find something not right, please let me
    know on discord.

More biome specific customizations are planned for the future.
