package org.purpurmc.purpur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.logging.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Tilt;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import java.util.Map;
import org.purpurmc.purpur.tool.Flattenable;
import org.purpurmc.purpur.tool.Strippable;
import org.purpurmc.purpur.tool.Tillable;
import org.purpurmc.purpur.tool.Waxable;
import org.purpurmc.purpur.tool.Weatherable;

import static org.purpurmc.purpur.PurpurConfig.log;

@SuppressWarnings("unused")
public class PurpurWorldConfig {

    private final String worldName;
    private final World.Environment environment;

    public PurpurWorldConfig(String worldName, World.Environment environment) {
        this.worldName = worldName;
        this.environment = environment;
        init();
    }

    public void init() {
        log("-------- World Settings For [" + worldName + "] --------");
        PurpurConfig.readConfig(PurpurWorldConfig.class, this);
    }

    private void set(String path, Object val) {
        PurpurConfig.config.addDefault("world-settings.default." + path, val);
        PurpurConfig.config.set("world-settings.default." + path, val);
        if (PurpurConfig.config.get("world-settings." + worldName + "." + path) != null) {
            PurpurConfig.config.addDefault("world-settings." + worldName + "." + path, val);
            PurpurConfig.config.set("world-settings." + worldName + "." + path, val);
        }
    }

    private ConfigurationSection getConfigurationSection(String path) {
        ConfigurationSection section = PurpurConfig.config.getConfigurationSection("world-settings." + worldName + "." + path);
        return section != null ? section : PurpurConfig.config.getConfigurationSection("world-settings.default." + path);
    }

    private String getString(String path, String def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getString("world-settings." + worldName + "." + path, PurpurConfig.config.getString("world-settings.default." + path));
    }

    private boolean getBoolean(String path, boolean def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getBoolean("world-settings." + worldName + "." + path, PurpurConfig.config.getBoolean("world-settings.default." + path));
    }

    private boolean getBoolean(String path, Predicate<Boolean> predicate) {
        String val = getString(path, "default").toLowerCase();
        Boolean bool = BooleanUtils.toBooleanObject(val, "true", "false", "default");
        return predicate.test(bool);
    }

    private double getDouble(String path, double def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getDouble("world-settings." + worldName + "." + path, PurpurConfig.config.getDouble("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getInt("world-settings." + worldName + "." + path, PurpurConfig.config.getInt("world-settings.default." + path));
    }

    private <T> List<?> getList(String path, T def) {
        PurpurConfig.config.addDefault("world-settings.default." + path, def);
        return PurpurConfig.config.getList("world-settings." + worldName + "." + path, PurpurConfig.config.getList("world-settings.default." + path));
    }

    private Map<String, Object> getMap(String path, Map<String, Object> def) {
        final Map<String, Object> fallback = PurpurConfig.getMap("world-settings.default." + path, def);
        final Map<String, Object> value = PurpurConfig.getMap("world-settings." + worldName + "." + path, null);
        return value.isEmpty() ? fallback : value;
    }

    public float armorstandStepHeight = 0.0F;
    public boolean armorstandSetNameVisible = false;
    public boolean armorstandFixNametags = false;
    public boolean armorstandMovement = true;
    public boolean armorstandWaterMovement = true;
    public boolean armorstandWaterFence = true;
    public boolean armorstandPlaceWithArms = false;
    private void armorstandSettings() {
        armorstandStepHeight = (float) getDouble("gameplay-mechanics.armorstand.step-height", armorstandStepHeight);
        armorstandSetNameVisible = getBoolean("gameplay-mechanics.armorstand.set-name-visible-when-placing-with-custom-name", armorstandSetNameVisible);
        armorstandFixNametags = getBoolean("gameplay-mechanics.armorstand.fix-nametags", armorstandFixNametags);
        armorstandMovement = getBoolean("gameplay-mechanics.armorstand.can-movement-tick", armorstandMovement);
        armorstandWaterMovement = getBoolean("gameplay-mechanics.armorstand.can-move-in-water", armorstandWaterMovement);
        armorstandWaterFence = getBoolean("gameplay-mechanics.armorstand.can-move-in-water-over-fence", armorstandWaterFence);
        armorstandPlaceWithArms = getBoolean("gameplay-mechanics.armorstand.place-with-arms-visible", armorstandPlaceWithArms);
    }

    public boolean arrowMovementResetsDespawnCounter = true;
    private void arrowSettings() {
        arrowMovementResetsDespawnCounter = getBoolean("gameplay-mechanics.arrow.movement-resets-despawn-counter", arrowMovementResetsDespawnCounter);
    }

    public boolean useBetterMending = false;
    public boolean alwaysTameInCreative = false;
    public boolean boatEjectPlayersOnLand = false;
    public boolean boatsDoFallDamage = false;
    public boolean disableDropsOnCrammingDeath = false;
    public boolean milkCuresBadOmen = true;
    public double tridentLoyaltyVoidReturnHeight = 0.0D;
    public boolean entitiesCanUsePortals = true;
    public int raidCooldownSeconds = 0;
    public int animalBreedingCooldownSeconds = 0;
    public boolean persistentDroppableEntityDisplayNames = true;
    public boolean entitiesPickUpLootBypassMobGriefing = false;
    public boolean fireballsBypassMobGriefing = false;
    public boolean projectilesBypassMobGriefing = false;
    public boolean noteBlockIgnoreAbove = false;
    public boolean imposeTeleportRestrictionsOnGateways = false;
    public boolean imposeTeleportRestrictionsOnNetherPortals = false;
    public boolean imposeTeleportRestrictionsOnEndPortals = false;
    public boolean tickFluids = true;
    public double mobsBlindnessMultiplier = 1;
    public boolean mobsIgnoreRails = false;
    public boolean rainStopsAfterSleep = true;
    public boolean thunderStopsAfterSleep = true;
    private void miscGameplayMechanicsSettings() {
        useBetterMending = getBoolean("gameplay-mechanics.use-better-mending", useBetterMending);
        alwaysTameInCreative = getBoolean("gameplay-mechanics.always-tame-in-creative", alwaysTameInCreative);
        boatEjectPlayersOnLand = getBoolean("gameplay-mechanics.boat.eject-players-on-land", boatEjectPlayersOnLand);
        boatsDoFallDamage = getBoolean("gameplay-mechanics.boat.do-fall-damage", boatsDoFallDamage);
        disableDropsOnCrammingDeath = getBoolean("gameplay-mechanics.disable-drops-on-cramming-death", disableDropsOnCrammingDeath);
        milkCuresBadOmen = getBoolean("gameplay-mechanics.milk-cures-bad-omen", milkCuresBadOmen);
        tridentLoyaltyVoidReturnHeight = getDouble("gameplay-mechanics.trident-loyalty-void-return-height", tridentLoyaltyVoidReturnHeight);
        entitiesCanUsePortals = getBoolean("gameplay-mechanics.entities-can-use-portals", entitiesCanUsePortals);
        raidCooldownSeconds = getInt("gameplay-mechanics.raid-cooldown-seconds", raidCooldownSeconds);
        animalBreedingCooldownSeconds = getInt("gameplay-mechanics.animal-breeding-cooldown-seconds", animalBreedingCooldownSeconds);
        persistentDroppableEntityDisplayNames = getBoolean("gameplay-mechanics.persistent-droppable-entity-display-names", persistentDroppableEntityDisplayNames);
        entitiesPickUpLootBypassMobGriefing = getBoolean("gameplay-mechanics.entities-pick-up-loot-bypass-mob-griefing", entitiesPickUpLootBypassMobGriefing);
        fireballsBypassMobGriefing = getBoolean("gameplay-mechanics.fireballs-bypass-mob-griefing", fireballsBypassMobGriefing);
        projectilesBypassMobGriefing = getBoolean("gameplay-mechanics.projectiles-bypass-mob-griefing", projectilesBypassMobGriefing);
        noteBlockIgnoreAbove = getBoolean("gameplay-mechanics.note-block-ignore-above", noteBlockIgnoreAbove);
        imposeTeleportRestrictionsOnGateways = getBoolean("gameplay-mechanics.impose-teleport-restrictions-on-gateways", imposeTeleportRestrictionsOnGateways);
        imposeTeleportRestrictionsOnNetherPortals = getBoolean("gameplay-mechanics.impose-teleport-restrictions-on-nether-portals", imposeTeleportRestrictionsOnNetherPortals);
        imposeTeleportRestrictionsOnEndPortals = getBoolean("gameplay-mechanics.impose-teleport-restrictions-on-end-portals", imposeTeleportRestrictionsOnEndPortals);
        tickFluids = getBoolean("gameplay-mechanics.tick-fluids", tickFluids);
        mobsBlindnessMultiplier = getDouble("gameplay-mechanics.entity-blindness-multiplier", mobsBlindnessMultiplier);
        mobsIgnoreRails = getBoolean("gameplay-mechanics.mobs-ignore-rails", mobsIgnoreRails);
        rainStopsAfterSleep = getBoolean("gameplay-mechanics.rain-stops-after-sleep", rainStopsAfterSleep);
        thunderStopsAfterSleep = getBoolean("gameplay-mechanics.thunder-stops-after-sleep", thunderStopsAfterSleep);
    }

    public int daytimeTicks = 12000;
    public int nighttimeTicks = 12000;
    private void daytimeCycleSettings() {
        daytimeTicks = getInt("gameplay-mechanics.daylight-cycle-ticks.daytime", daytimeTicks);
        nighttimeTicks = getInt("gameplay-mechanics.daylight-cycle-ticks.nighttime", nighttimeTicks);
    }

    public int drowningAirTicks = 300;
    public int drowningDamageInterval = 20;
    public double damageFromDrowning = 2.0F;
    private void drowningSettings() {
        drowningAirTicks = getInt("gameplay-mechanics.drowning.air-ticks", drowningAirTicks);
        drowningDamageInterval = getInt("gameplay-mechanics.drowning.ticks-per-damage", drowningDamageInterval);
        damageFromDrowning = getDouble("gameplay-mechanics.drowning.damage-from-drowning", damageFromDrowning);
    }

    public int elytraDamagePerSecond = 1;
    public double elytraDamageMultiplyBySpeed = 0;
    public int elytraDamagePerFireworkBoost = 0;
    public int elytraDamagePerTridentBoost = 0;
    public boolean elytraKineticDamage = true;
    private void elytraSettings() {
        elytraDamagePerSecond = getInt("gameplay-mechanics.elytra.damage-per-second", elytraDamagePerSecond);
        elytraDamageMultiplyBySpeed = getDouble("gameplay-mechanics.elytra.damage-multiplied-by-speed", elytraDamageMultiplyBySpeed);
        elytraDamagePerFireworkBoost = getInt("gameplay-mechanics.elytra.damage-per-boost.firework", elytraDamagePerFireworkBoost);
        elytraDamagePerTridentBoost = getInt("gameplay-mechanics.elytra.damage-per-boost.trident", elytraDamagePerTridentBoost);
        elytraKineticDamage = getBoolean("gameplay-mechanics.elytra.kinetic-damage", elytraKineticDamage);
    }

    public int entityLifeSpan = 0;
    public float entityLeftHandedChance = 0.05f;
    private void entitySettings() {
        entityLifeSpan = getInt("gameplay-mechanics.entity-lifespan", entityLifeSpan);
        entityLeftHandedChance = (float) getDouble("gameplay-mechanics.entity-left-handed-chance", entityLeftHandedChance);
    }

    public boolean infinityWorksWithoutArrows = false;
    private void infinityArrowsSettings() {
        infinityWorksWithoutArrows = getBoolean("gameplay-mechanics.infinity-bow.works-without-arrows", infinityWorksWithoutArrows);
    }

    public List<Item> itemImmuneToCactus = new ArrayList<>();
    public List<Item> itemImmuneToExplosion = new ArrayList<>();
    public List<Item> itemImmuneToFire = new ArrayList<>();
    public List<Item> itemImmuneToLightning = new ArrayList<>();
    public boolean dontRunWithScissors = false;
    public ResourceLocation dontRunWithScissorsItemModelReference = ResourceLocation.parse("purpurmc:scissors");
    public boolean ignoreScissorsInWater = false;
    public boolean ignoreScissorsInLava = false;
    public double scissorsRunningDamage = 1D;
    public float enderPearlDamage = 5.0F;
    public int enderPearlCooldown = 20;
    public int enderPearlCooldownCreative = 20;
    public float enderPearlEndermiteChance = 0.05F;
    public int glowBerriesEatGlowDuration = 0;
    public boolean shulkerBoxItemDropContentsWhenDestroyed = true;
    public boolean compassItemShowsBossBar = false;
    private void itemSettings() {
        itemImmuneToCactus.clear();
        getList("gameplay-mechanics.item.immune.cactus", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                BuiltInRegistries.ITEM.stream().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToCactus.add(item));
                return;
            }
            Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(key.toString()));
            if (item != Items.AIR) itemImmuneToCactus.add(item);
        });
        itemImmuneToExplosion.clear();
        getList("gameplay-mechanics.item.immune.explosion", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                BuiltInRegistries.ITEM.stream().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToExplosion.add(item));
                return;
            }
            Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(key.toString()));
            if (item != Items.AIR) itemImmuneToExplosion.add(item);
        });
        itemImmuneToFire.clear();
        getList("gameplay-mechanics.item.immune.fire", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                BuiltInRegistries.ITEM.stream().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToFire.add(item));
                return;
            }
            Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(key.toString()));
            if (item != Items.AIR) itemImmuneToFire.add(item);
        });
        itemImmuneToLightning.clear();
        getList("gameplay-mechanics.item.immune.lightning", new ArrayList<>()).forEach(key -> {
            if (key.toString().equals("*")) {
                BuiltInRegistries.ITEM.stream().filter(item -> item != Items.AIR).forEach((item) -> itemImmuneToLightning.add(item));
                return;
            }
            Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(key.toString()));
            if (item != Items.AIR) itemImmuneToLightning.add(item);
        });
        dontRunWithScissors = getBoolean("gameplay-mechanics.item.shears.damage-if-sprinting", dontRunWithScissors);
        dontRunWithScissorsItemModelReference = ResourceLocation.parse(getString("gameplay-mechanics.item.shears.damage-if-sprinting-item-model", "purpurmc:scissors"));
        ignoreScissorsInWater = getBoolean("gameplay-mechanics.item.shears.ignore-in-water", ignoreScissorsInWater);
        ignoreScissorsInLava = getBoolean("gameplay-mechanics.item.shears.ignore-in-lava", ignoreScissorsInLava);
        scissorsRunningDamage = getDouble("gameplay-mechanics.item.shears.sprinting-damage", scissorsRunningDamage);
        enderPearlDamage = (float) getDouble("gameplay-mechanics.item.ender-pearl.damage", enderPearlDamage);
        enderPearlCooldown = getInt("gameplay-mechanics.item.ender-pearl.cooldown", enderPearlCooldown);
        enderPearlCooldownCreative = getInt("gameplay-mechanics.item.ender-pearl.creative-cooldown", enderPearlCooldownCreative);
        enderPearlEndermiteChance = (float) getDouble("gameplay-mechanics.item.ender-pearl.endermite-spawn-chance", enderPearlEndermiteChance);
        glowBerriesEatGlowDuration = getInt("gameplay-mechanics.item.glow_berries.eat-glow-duration", glowBerriesEatGlowDuration);
        shulkerBoxItemDropContentsWhenDestroyed = getBoolean("gameplay-mechanics.item.shulker_box.drop-contents-when-destroyed", shulkerBoxItemDropContentsWhenDestroyed);
        compassItemShowsBossBar = getBoolean("gameplay-mechanics.item.compass.holding-shows-bossbar", compassItemShowsBossBar);
    }

    public double minecartMaxSpeed = 0.4D;
    public boolean minecartPlaceAnywhere = false;
    public boolean minecartControllable = false;
    public float minecartControllableStepHeight = 1.0F;
    public double minecartControllableHopBoost = 0.5D;
    public boolean minecartControllableFallDamage = true;
    public double minecartControllableBaseSpeed = 0.1D;
    public Map<Block, Double> minecartControllableBlockSpeeds = new HashMap<>();
    public double poweredRailBoostModifier = 0.06;
    private void minecartSettings() {
        if (PurpurConfig.version < 12) {
            boolean oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.place-anywhere", minecartPlaceAnywhere);
            set("gameplay-mechanics.controllable-minecarts.place-anywhere", null);
            set("gameplay-mechanics.minecart.place-anywhere", oldBool);
            oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.enabled", minecartControllable);
            set("gameplay-mechanics.controllable-minecarts.enabled", null);
            set("gameplay-mechanics.minecart.controllable.enabled", oldBool);
            double oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.step-height", minecartControllableStepHeight);
            set("gameplay-mechanics.controllable-minecarts.step-height", null);
            set("gameplay-mechanics.minecart.controllable.step-height", oldDouble);
            oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.hop-boost", minecartControllableHopBoost);
            set("gameplay-mechanics.controllable-minecarts.hop-boost", null);
            set("gameplay-mechanics.minecart.controllable.hop-boost", oldDouble);
            oldBool = getBoolean("gameplay-mechanics.controllable-minecarts.fall-damage", minecartControllableFallDamage);
            set("gameplay-mechanics.controllable-minecarts.fall-damage", null);
            set("gameplay-mechanics.minecart.controllable.fall-damage", oldBool);
            oldDouble = getDouble("gameplay-mechanics.controllable-minecarts.base-speed", minecartControllableBaseSpeed);
            set("gameplay-mechanics.controllable-minecarts.base-speed", null);
            set("gameplay-mechanics.minecart.controllable.base-speed", oldDouble);
            ConfigurationSection section = getConfigurationSection("gameplay-mechanics.controllable-minecarts.block-speed");
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    if ("grass-block".equals(key)) key = "grass_block"; // oopsie
                    oldDouble = section.getDouble(key, minecartControllableBaseSpeed);
                    set("gameplay-mechanics.controllable-minecarts.block-speed." + key, null);
                    set("gameplay-mechanics.minecart.controllable.block-speed." + key, oldDouble);
                }
                set("gameplay-mechanics.controllable-minecarts.block-speed", null);
            }
            set("gameplay-mechanics.controllable-minecarts", null);
        }

        minecartMaxSpeed = getDouble("gameplay-mechanics.minecart.max-speed", minecartMaxSpeed);
        minecartPlaceAnywhere = getBoolean("gameplay-mechanics.minecart.place-anywhere", minecartPlaceAnywhere);
        minecartControllable = getBoolean("gameplay-mechanics.minecart.controllable.enabled", minecartControllable);
        minecartControllableStepHeight = (float) getDouble("gameplay-mechanics.minecart.controllable.step-height", minecartControllableStepHeight);
        minecartControllableHopBoost = getDouble("gameplay-mechanics.minecart.controllable.hop-boost", minecartControllableHopBoost);
        minecartControllableFallDamage = getBoolean("gameplay-mechanics.minecart.controllable.fall-damage", minecartControllableFallDamage);
        minecartControllableBaseSpeed = getDouble("gameplay-mechanics.minecart.controllable.base-speed", minecartControllableBaseSpeed);
        ConfigurationSection section = getConfigurationSection("gameplay-mechanics.minecart.controllable.block-speed");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(key));
                if (block != Blocks.AIR) {
                    minecartControllableBlockSpeeds.put(block, section.getDouble(key, minecartControllableBaseSpeed));
                }
            }
        } else {
            set("gameplay-mechanics.minecart.controllable.block-speed.grass_block", 0.3D);
            set("gameplay-mechanics.minecart.controllable.block-speed.stone", 0.5D);
        }
        poweredRailBoostModifier = getDouble("gameplay-mechanics.minecart.powered-rail.boost-modifier", poweredRailBoostModifier);
    }

    public float entityHealthRegenAmount = 1.0F;
    public float entityMinimalHealthPoison = 1.0F;
    public float entityPoisonDegenerationAmount = 1.0F;
    public float entityWitherDegenerationAmount = 1.0F;
    public float humanHungerExhaustionAmount = 0.005F;
    public float humanSaturationRegenAmount = 1.0F;
    private void mobEffectSettings() {
        entityHealthRegenAmount = (float) getDouble("gameplay-mechanics.mob-effects.health-regen-amount", entityHealthRegenAmount);
        entityMinimalHealthPoison = (float) getDouble("gameplay-mechanics.mob-effects.minimal-health-poison-amount", entityMinimalHealthPoison);
        entityPoisonDegenerationAmount = (float) getDouble("gameplay-mechanics.mob-effects.poison-degeneration-amount", entityPoisonDegenerationAmount);
        entityWitherDegenerationAmount = (float) getDouble("gameplay-mechanics.mob-effects.wither-degeneration-amount", entityWitherDegenerationAmount);
        humanHungerExhaustionAmount = (float) getDouble("gameplay-mechanics.mob-effects.hunger-exhaustion-amount", humanHungerExhaustionAmount);
        humanSaturationRegenAmount = (float) getDouble("gameplay-mechanics.mob-effects.saturation-regen-amount", humanSaturationRegenAmount);
    }

    public boolean catSpawning;
    public boolean patrolSpawning;
    public boolean phantomSpawning;
    public boolean villagerTraderSpawning;
    public boolean villageSiegeSpawning;
    private void mobSpawnerSettings() {
        // values of "default" or null will default to true only if the world environment is normal (aka overworld)
        Predicate<Boolean> predicate = (bool) -> (bool != null && bool) || (bool == null && environment == World.Environment.NORMAL);
        catSpawning = getBoolean("gameplay-mechanics.mob-spawning.village-cats", predicate);
        patrolSpawning = getBoolean("gameplay-mechanics.mob-spawning.raid-patrols", predicate);
        phantomSpawning = getBoolean("gameplay-mechanics.mob-spawning.phantoms", predicate);
        villagerTraderSpawning = getBoolean("gameplay-mechanics.mob-spawning.wandering-traders", predicate);
        villageSiegeSpawning = getBoolean("gameplay-mechanics.mob-spawning.village-sieges", predicate);
    }

    public boolean disableObserverClocks = false;
    private void observerSettings() {
        disableObserverClocks = getBoolean("blocks.observer.disable-clock", disableObserverClocks);
    }

    public int playerNetheriteFireResistanceDuration = 0;
    public int playerNetheriteFireResistanceAmplifier = 0;
    public boolean playerNetheriteFireResistanceAmbient = false;
    public boolean playerNetheriteFireResistanceShowParticles = false;
    public boolean playerNetheriteFireResistanceShowIcon = true;
    private void playerNetheriteFireResistance() {
        playerNetheriteFireResistanceDuration = getInt("gameplay-mechanics.player.netherite-fire-resistance.duration", playerNetheriteFireResistanceDuration);
        playerNetheriteFireResistanceAmplifier = getInt("gameplay-mechanics.player.netherite-fire-resistance.amplifier", playerNetheriteFireResistanceAmplifier);
        playerNetheriteFireResistanceAmbient = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.ambient", playerNetheriteFireResistanceAmbient);
        playerNetheriteFireResistanceShowParticles = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.show-particles", playerNetheriteFireResistanceShowParticles);
        playerNetheriteFireResistanceShowIcon = getBoolean("gameplay-mechanics.player.netherite-fire-resistance.show-icon", playerNetheriteFireResistanceShowIcon);
    }

    public boolean idleTimeoutKick = true;
    public boolean idleTimeoutTickNearbyEntities = true;
    public boolean idleTimeoutCountAsSleeping = false;
    public boolean idleTimeoutUpdateTabList = false;
    public boolean idleTimeoutTargetPlayer = true;
    public String playerDeathExpDropEquation = "expLevel * 7";
    public int playerDeathExpDropMax = 100;
    public boolean teleportIfOutsideBorder = false;
    public boolean totemOfUndyingWorksInInventory = false;
    public boolean playerFixStuckPortal = false;
    public boolean creativeOnePunch = false;
    public boolean playerSleepNearMonsters = false;
    public boolean playersSkipNight = true;
    public double playerCriticalDamageMultiplier = 1.5D;
    public int playerBurpDelay = 10;
    public boolean playerBurpWhenFull = false;
    public boolean playerRidableInWater = false;
    public boolean playerRemoveBindingWithWeakness = false;
    public int shiftRightClickRepairsMendingPoints = 0;
    private void playerSettings() {
        if (PurpurConfig.version < 19) {
            boolean oldVal = getBoolean("gameplay-mechanics.player.idle-timeout.mods-target", idleTimeoutTargetPlayer);
            set("gameplay-mechanics.player.idle-timeout.mods-target", null);
            set("gameplay-mechanics.player.idle-timeout.mobs-target", oldVal);
        }
        idleTimeoutKick = System.getenv("PURPUR_FORCE_IDLE_KICK") == null ? getBoolean("gameplay-mechanics.player.idle-timeout.kick-if-idle", idleTimeoutKick) : Boolean.parseBoolean(System.getenv("PURPUR_FORCE_IDLE_KICK"));
        idleTimeoutTickNearbyEntities = getBoolean("gameplay-mechanics.player.idle-timeout.tick-nearby-entities", idleTimeoutTickNearbyEntities);
        idleTimeoutCountAsSleeping = getBoolean("gameplay-mechanics.player.idle-timeout.count-as-sleeping", idleTimeoutCountAsSleeping);
        idleTimeoutUpdateTabList = getBoolean("gameplay-mechanics.player.idle-timeout.update-tab-list", idleTimeoutUpdateTabList);
        idleTimeoutTargetPlayer = getBoolean("gameplay-mechanics.player.idle-timeout.mobs-target", idleTimeoutTargetPlayer);
        playerDeathExpDropEquation = getString("gameplay-mechanics.player.exp-dropped-on-death.equation", playerDeathExpDropEquation);
        playerDeathExpDropMax = getInt("gameplay-mechanics.player.exp-dropped-on-death.maximum", playerDeathExpDropMax);
        teleportIfOutsideBorder = getBoolean("gameplay-mechanics.player.teleport-if-outside-border", teleportIfOutsideBorder);
        totemOfUndyingWorksInInventory = getBoolean("gameplay-mechanics.player.totem-of-undying-works-in-inventory", totemOfUndyingWorksInInventory);
        playerFixStuckPortal = getBoolean("gameplay-mechanics.player.fix-stuck-in-portal", playerFixStuckPortal);
        creativeOnePunch = getBoolean("gameplay-mechanics.player.one-punch-in-creative", creativeOnePunch);
        playerSleepNearMonsters = getBoolean("gameplay-mechanics.player.sleep-ignore-nearby-mobs", playerSleepNearMonsters);
        playersSkipNight = getBoolean("gameplay-mechanics.player.can-skip-night", playersSkipNight);
        playerCriticalDamageMultiplier = getDouble("gameplay-mechanics.player.critical-damage-multiplier", playerCriticalDamageMultiplier);
        playerBurpDelay = getInt("gameplay-mechanics.player.burp-delay", playerBurpDelay);
        playerBurpWhenFull = getBoolean("gameplay-mechanics.player.burp-when-full", playerBurpWhenFull);
        playerRidableInWater = getBoolean("gameplay-mechanics.player.ridable-in-water", playerRidableInWater);
        playerRemoveBindingWithWeakness = getBoolean("gameplay-mechanics.player.curse-of-binding.remove-with-weakness", playerRemoveBindingWithWeakness);
        shiftRightClickRepairsMendingPoints = getInt("gameplay-mechanics.player.shift-right-click-repairs-mending-points", shiftRightClickRepairsMendingPoints);
    }

    public boolean silkTouchEnabled = false;
    public String silkTouchSpawnerName = "<reset><white>Monster Spawner";
    public List<String> silkTouchSpawnerLore = new ArrayList<>();
    public List<Item> silkTouchTools = new ArrayList<>();
    public int minimumSilkTouchSpawnerRequire = 1;
    private void silkTouchSettings() {
        if (PurpurConfig.version < 21) {
            String oldName = getString("gameplay-mechanics.silk-touch.spawner-name", silkTouchSpawnerName);
            set("gameplay-mechanics.silk-touch.spawner-name", "<reset>" + ChatColor.toMM(oldName.replace("{mob}", "<mob>")));
            List<String> list = new ArrayList<>();
            getList("gameplay-mechanics.silk-touch.spawner-lore", List.of("Spawns a <mob>"))
                    .forEach(line -> list.add("<reset>" + ChatColor.toMM(line.toString().replace("{mob}", "<mob>"))));
            set("gameplay-mechanics.silk-touch.spawner-lore", list);
        }
        silkTouchEnabled = getBoolean("gameplay-mechanics.silk-touch.enabled", silkTouchEnabled);
        silkTouchSpawnerName = getString("gameplay-mechanics.silk-touch.spawner-name", silkTouchSpawnerName);
        minimumSilkTouchSpawnerRequire = getInt("gameplay-mechanics.silk-touch.minimal-level", minimumSilkTouchSpawnerRequire);
        silkTouchSpawnerLore.clear();
        getList("gameplay-mechanics.silk-touch.spawner-lore", List.of("Spawns a <mob>"))
                .forEach(line -> silkTouchSpawnerLore.add(line.toString()));
        silkTouchTools.clear();
        getList("gameplay-mechanics.silk-touch.tools", List.of(
                "minecraft:iron_pickaxe",
                "minecraft:golden_pickaxe",
                "minecraft:diamond_pickaxe",
                "minecraft:netherite_pickaxe"
        )).forEach(key -> {
            Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(key.toString()));
            if (item != Items.AIR) silkTouchTools.add(item);
        });
    }

    public double bowProjectileOffset = 1.0D;
    public double crossbowProjectileOffset = 1.0D;
    public double eggProjectileOffset = 1.0D;
    public double enderPearlProjectileOffset = 1.0D;
    public double throwablePotionProjectileOffset = 1.0D;
    public double tridentProjectileOffset = 1.0D;
    public double snowballProjectileOffset = 1.0D;
    private void projectileOffsetSettings() {
        bowProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.bow", bowProjectileOffset);
        crossbowProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.crossbow", crossbowProjectileOffset);
        eggProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.egg", eggProjectileOffset);
        enderPearlProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.ender-pearl", enderPearlProjectileOffset);
        throwablePotionProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.throwable-potion", throwablePotionProjectileOffset);
        tridentProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.trident", tridentProjectileOffset);
        snowballProjectileOffset = getDouble("gameplay-mechanics.projectile-offset.snowball", snowballProjectileOffset);
    }

    public int snowballDamage = -1;
    private void snowballSettings() {
        snowballDamage = getInt("gameplay-mechanics.projectile-damage.snowball", snowballDamage);
    }

    public Map<Block, Strippable> axeStrippables = new HashMap<>();
    public Map<Block, Waxable> axeWaxables = new HashMap<>();
    public Map<Block, Weatherable> axeWeatherables = new HashMap<>();
    public Map<Block, Tillable> hoeTillables = new HashMap<>();
    public Map<Block, Flattenable> shovelFlattenables = new HashMap<>();
    private void toolSettings() {
        axeStrippables.clear();
        axeWaxables.clear();
        axeWeatherables.clear();
        hoeTillables.clear();
        shovelFlattenables.clear();
        if (PurpurConfig.version < 18) {
            ConfigurationSection section = PurpurConfig.config.getConfigurationSection("world-settings." + worldName + ".tools.hoe.tilling");
            if (section != null) {
                PurpurConfig.config.set("world-settings." + worldName + ".tools.hoe.tillables", section);
                PurpurConfig.config.set("world-settings." + worldName + ".tools.hoe.tilling", null);
            }
            section = PurpurConfig.config.getConfigurationSection("world-settings.default.tools.hoe.tilling");
            if (section != null) {
                PurpurConfig.config.set("world-settings.default.tools.hoe.tillables", section);
                PurpurConfig.config.set("world-settings.default.tools.hoe.tilling", null);
            }
        }
        if (PurpurConfig.version < 29) {
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:mangrove_log", Map.of("into", "minecraft:stripped_mangrove_log", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:mangrove_wood", Map.of("into", "minecraft:stripped_mangrove_wood", "drops", new HashMap<String, Double>()));
        }
        if (PurpurConfig.version < 32) {
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:cherry_log", Map.of("into", "minecraft:stripped_cherry_log", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:cherry_wood", Map.of("into", "minecraft:stripped_cherry_wood", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:bamboo_block", Map.of("into", "minecraft:stripped_bamboo_block", "drops", new HashMap<String, Double>()));
        }
        if (PurpurConfig.version < 33) {
            getList("gameplay-mechanics.shovel-turns-block-to-grass-path", new ArrayList<String>(){{
                add("minecraft:coarse_dirt");
                add("minecraft:dirt");
                add("minecraft:grass_block");
                add("minecraft:mycelium");
                add("minecraft:podzol");
                add("minecraft:rooted_dirt");
            }}).forEach(key -> {
                PurpurConfig.config.set("world-settings.default.tools.shovel.flattenables." + key.toString(), Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>()));
            });
            set("gameplay-mechanics.shovel-turns-block-to-grass-path", null);
        }
        if (PurpurConfig.version < 34) {
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_chiseled_copper", Map.of("into", "minecraft:chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_exposed_chiseled_copper", Map.of("into", "minecraft:exposed_chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_weathered_chiseled_copper", Map.of("into", "minecraft:weathered_chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_oxidized_chiseled_copper", Map.of("into", "minecraft:oxidized_chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_copper_door", Map.of("into", "minecraft:copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_exposed_copper_door", Map.of("into", "minecraft:exposed_copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_weathered_copper_door", Map.of("into", "minecraft:weathered_copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_oxidized_copper_door", Map.of("into", "minecraft:oxidized_copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_copper_trapdoor", Map.of("into", "minecraft:copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_exposed_copper_trapdoor", Map.of("into", "minecraft:exposed_copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_weathered_copper_trapdoor", Map.of("into", "minecraft:weathered_copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_oxidized_copper_trapdoor", Map.of("into", "minecraft:oxidized_copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_copper_grate", Map.of("into", "minecraft:copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_exposed_copper_grate", Map.of("into", "minecraft:exposed_copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_weathered_copper_grate", Map.of("into", "minecraft:weathered_copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_oxidized_copper_grate", Map.of("into", "minecraft:oxidized_copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_copper_bulb", Map.of("into", "minecraft:copper_bulb", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_exposed_copper_bulb", Map.of("into", "minecraft:exposed_copper_bulb", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_weathered_copper_bulb", Map.of("into", "minecraft:weathered_copper_bulb", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.waxables.minecraft:waxed_oxidized_copper_bulb", Map.of("into", "minecraft:oxidized_copper_bulb", "drops", new HashMap<String, Double>()));

            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:exposed_chiseled_copper", Map.of("into", "minecraft:chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:weathered_chiseled_copper", Map.of("into", "minecraft:exposed_chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_chiseled_copper", Map.of("into", "minecraft:weathered_chiseled_copper", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_cut_copper_stairs", Map.of("into", "minecraft:weathered_cut_copper_stairs", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:exposed_copper_door", Map.of("into", "minecraft:copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:weathered_copper_door", Map.of("into", "minecraft:exposed_copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_copper_door", Map.of("into", "minecraft:weathered_copper_door", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:exposed_copper_trapdoor", Map.of("into", "minecraft:copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:weathered_copper_trapdoor", Map.of("into", "minecraft:exposed_copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_copper_trapdoor", Map.of("into", "minecraft:weathered_copper_trapdoor", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:exposed_copper_grate", Map.of("into", "minecraft:copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:weathered_copper_grate", Map.of("into", "minecraft:exposed_copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_copper_grate", Map.of("into", "minecraft:weathered_copper_grate", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:exposed_copper_bulb", Map.of("into", "minecraft:copper_bulb", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:weathered_copper_bulb", Map.of("into", "minecraft:exposed_copper_bulb", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.weatherables.minecraft:oxidized_copper_bulb", Map.of("into", "minecraft:weathered_copper_bulb", "drops", new HashMap<String, Double>()));
        }
        if (PurpurConfig.version < 38) {
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:pale_oak_wood", Map.of("into", "minecraft:stripped_pale_oak_wood", "drops", new HashMap<String, Double>()));
            PurpurConfig.config.set("world-settings.default.tools.axe.strippables.minecraft:pale_oak_log", Map.of("into", "minecraft:stripped_pale_oak_log", "drops", new HashMap<String, Double>()));
        }
        getMap("tools.axe.strippables", Map.ofEntries(
                Map.entry("minecraft:oak_wood", Map.of("into", "minecraft:stripped_oak_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oak_log", Map.of("into", "minecraft:stripped_oak_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:dark_oak_wood", Map.of("into", "minecraft:stripped_dark_oak_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:dark_oak_log", Map.of("into", "minecraft:stripped_dark_oak_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:pale_oak_wood", Map.of("into", "minecraft:stripped_pale_oak_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:pale_oak_log", Map.of("into", "minecraft:stripped_pale_oak_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:acacia_wood", Map.of("into", "minecraft:stripped_acacia_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:acacia_log", Map.of("into", "minecraft:stripped_acacia_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:cherry_wood", Map.of("into", "minecraft:stripped_cherry_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:cherry_log", Map.of("into", "minecraft:stripped_cherry_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:birch_wood", Map.of("into", "minecraft:stripped_birch_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:birch_log", Map.of("into", "minecraft:stripped_birch_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:jungle_wood", Map.of("into", "minecraft:stripped_jungle_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:jungle_log", Map.of("into", "minecraft:stripped_jungle_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:spruce_wood", Map.of("into", "minecraft:stripped_spruce_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:spruce_log", Map.of("into", "minecraft:stripped_spruce_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:warped_stem", Map.of("into", "minecraft:stripped_warped_stem", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:warped_hyphae", Map.of("into", "minecraft:stripped_warped_hyphae", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:crimson_stem", Map.of("into", "minecraft:stripped_crimson_stem", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:crimson_hyphae", Map.of("into", "minecraft:stripped_crimson_hyphae", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:mangrove_wood", Map.of("into", "minecraft:stripped_mangrove_wood", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:mangrove_log", Map.of("into", "minecraft:stripped_mangrove_log", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:bamboo_block", Map.of("into", "minecraft:stripped_bamboo_block", "drops", new HashMap<String, Double>()))
            )
        ).forEach((blockId, obj) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockId));
            if (block == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.strippables`: " + blockId); return; }
            if (!(obj instanceof Map<?, ?> map)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.strippables." + blockId + "`"); return; }
            String intoId = (String) map.get("into");
            Block into = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(intoId));
            if (into == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.strippables." + blockId + ".into`: " + intoId); return; }
            Object dropsObj = map.get("drops");
            if (!(dropsObj instanceof Map<?, ?> dropsMap)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.strippables." + blockId + ".drops`"); return; }
            Map<Item, Double> drops = new HashMap<>();
            dropsMap.forEach((itemId, chance) -> {
                Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemId.toString()));
                if (item == Items.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid item for `tools.axe.strippables." + blockId + ".drops`: " + itemId); return; }
                drops.put(item, (double) chance);
            });
            axeStrippables.put(block, new Strippable(into, drops));
        });
        getMap("tools.axe.waxables", Map.ofEntries(
                Map.entry("minecraft:waxed_copper_block", Map.of("into", "minecraft:copper_block", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_copper", Map.of("into", "minecraft:exposed_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_copper", Map.of("into", "minecraft:weathered_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_copper", Map.of("into", "minecraft:oxidized_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_cut_copper", Map.of("into", "minecraft:cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_cut_copper", Map.of("into", "minecraft:exposed_cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_cut_copper", Map.of("into", "minecraft:weathered_cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_cut_copper", Map.of("into", "minecraft:oxidized_cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_cut_copper_slab", Map.of("into", "minecraft:cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_cut_copper_slab", Map.of("into", "minecraft:exposed_cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_cut_copper_slab", Map.of("into", "minecraft:weathered_cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_cut_copper_slab", Map.of("into", "minecraft:oxidized_cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_cut_copper_stairs", Map.of("into", "minecraft:cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_cut_copper_stairs", Map.of("into", "minecraft:exposed_cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_cut_copper_stairs", Map.of("into", "minecraft:weathered_cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_cut_copper_stairs", Map.of("into", "minecraft:oxidized_cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_chiseled_copper", Map.of("into", "minecraft:chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_chiseled_copper", Map.of("into", "minecraft:exposed_chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_chiseled_copper", Map.of("into", "minecraft:weathered_chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_chiseled_copper", Map.of("into", "minecraft:oxidized_chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_copper_door", Map.of("into", "minecraft:copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_copper_door", Map.of("into", "minecraft:exposed_copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_copper_door", Map.of("into", "minecraft:weathered_copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_copper_door", Map.of("into", "minecraft:oxidized_copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_copper_trapdoor", Map.of("into", "minecraft:copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_copper_trapdoor", Map.of("into", "minecraft:exposed_copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_copper_trapdoor", Map.of("into", "minecraft:weathered_copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_copper_trapdoor", Map.of("into", "minecraft:oxidized_copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_copper_grate", Map.of("into", "minecraft:copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_copper_grate", Map.of("into", "minecraft:exposed_copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_copper_grate", Map.of("into", "minecraft:weathered_copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_copper_grate", Map.of("into", "minecraft:oxidized_copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_copper_bulb", Map.of("into", "minecraft:copper_bulb", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_exposed_copper_bulb", Map.of("into", "minecraft:exposed_copper_bulb", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_weathered_copper_bulb", Map.of("into", "minecraft:weathered_copper_bulb", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:waxed_oxidized_copper_bulb", Map.of("into", "minecraft:oxidized_copper_bulb", "drops", new HashMap<String, Double>())))
        ).forEach((blockId, obj) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockId));
            if (block == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.waxables`: " + blockId); return; }
            if (!(obj instanceof Map<?, ?> map)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.waxables." + blockId + "`"); return; }
            String intoId = (String) map.get("into");
            Block into = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(intoId));
            if (into == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.waxables." + blockId + ".into`: " + intoId); return; }
            Object dropsObj = map.get("drops");
            if (!(dropsObj instanceof Map<?, ?> dropsMap)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.waxables." + blockId + ".drops`"); return; }
            Map<Item, Double> drops = new HashMap<>();
            dropsMap.forEach((itemId, chance) -> {
                Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemId.toString()));
                if (item == Items.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid item for `tools.axe.waxables." + blockId + ".drops`: " + itemId); return; }
                drops.put(item, (double) chance);
            });
            axeWaxables.put(block, new Waxable(into, drops));
        });
        getMap("tools.axe.weatherables", Map.ofEntries(
                Map.entry("minecraft:exposed_copper", Map.of("into", "minecraft:copper_block", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_copper", Map.of("into", "minecraft:exposed_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_copper", Map.of("into", "minecraft:weathered_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_cut_copper", Map.of("into", "minecraft:cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_cut_copper", Map.of("into", "minecraft:exposed_cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_cut_copper", Map.of("into", "minecraft:weathered_cut_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_chiseled_copper", Map.of("into", "minecraft:chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_chiseled_copper", Map.of("into", "minecraft:exposed_chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_chiseled_copper", Map.of("into", "minecraft:weathered_chiseled_copper", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_cut_copper_slab", Map.of("into", "minecraft:cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_cut_copper_slab", Map.of("into", "minecraft:exposed_cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_cut_copper_slab", Map.of("into", "minecraft:weathered_cut_copper_slab", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_cut_copper_stairs", Map.of("into", "minecraft:cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_cut_copper_stairs", Map.of("into", "minecraft:exposed_cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_cut_copper_stairs", Map.of("into", "minecraft:weathered_cut_copper_stairs", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_copper_door", Map.of("into", "minecraft:copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_copper_door", Map.of("into", "minecraft:exposed_copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_copper_door", Map.of("into", "minecraft:weathered_copper_door", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_copper_trapdoor", Map.of("into", "minecraft:copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_copper_trapdoor", Map.of("into", "minecraft:exposed_copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_copper_trapdoor", Map.of("into", "minecraft:weathered_copper_trapdoor", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_copper_grate", Map.of("into", "minecraft:copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_copper_grate", Map.of("into", "minecraft:exposed_copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_copper_grate", Map.of("into", "minecraft:weathered_copper_grate", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:exposed_copper_bulb", Map.of("into", "minecraft:copper_bulb", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:weathered_copper_bulb", Map.of("into", "minecraft:exposed_copper_bulb", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:oxidized_copper_bulb", Map.of("into", "minecraft:weathered_copper_bulb", "drops", new HashMap<String, Double>())))
        ).forEach((blockId, obj) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockId));
            if (block == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.weatherables`: " + blockId); return; }
            if (!(obj instanceof Map<?, ?> map)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.weatherables." + blockId + "`"); return; }
            String intoId = (String) map.get("into");
            Block into = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(intoId));
            if (into == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.axe.weatherables." + blockId + ".into`: " + intoId); return; }
            Object dropsObj = map.get("drops");
            if (!(dropsObj instanceof Map<?, ?> dropsMap)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.axe.weatherables." + blockId + ".drops`"); return; }
            Map<Item, Double> drops = new HashMap<>();
            dropsMap.forEach((itemId, chance) -> {
                Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemId.toString()));
                if (item == Items.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid item for `tools.axe.weatherables." + blockId + ".drops`: " + itemId); return; }
                drops.put(item, (double) chance);
            });
            axeWeatherables.put(block, new Weatherable(into, drops));
        });
        getMap("tools.hoe.tillables", Map.ofEntries(
                Map.entry("minecraft:grass_block", Map.of("condition", "air_above", "into", "minecraft:farmland", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:dirt_path", Map.of("condition", "air_above", "into", "minecraft:farmland", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:dirt", Map.of("condition", "air_above", "into", "minecraft:farmland", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:coarse_dirt", Map.of("condition", "air_above", "into", "minecraft:dirt", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:rooted_dirt", Map.of("condition", "always", "into", "minecraft:dirt", "drops", Map.of("minecraft:hanging_roots", 1.0D))))
        ).forEach((blockId, obj) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockId));
            if (block == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.hoe.tillables`: " + blockId); return; }
            if (!(obj instanceof Map<?, ?> map)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.hoe.tillables." + blockId + "`"); return; }
            String conditionId = (String) map.get("condition");
            Tillable.Condition condition = Tillable.Condition.get(conditionId);
            if (condition == null) { PurpurConfig.log(Level.SEVERE, "Invalid condition for `tools.hoe.tillables." + blockId + ".condition`: " + conditionId); return; }
            String intoId = (String) map.get("into");
            Block into = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(intoId));
            if (into == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.hoe.tillables." + blockId + ".into`: " + intoId); return; }
            Object dropsObj = map.get("drops");
            if (!(dropsObj instanceof Map<?, ?> dropsMap)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.hoe.tillables." + blockId + ".drops`"); return; }
            Map<Item, Double> drops = new HashMap<>();
            dropsMap.forEach((itemId, chance) -> {
                Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemId.toString()));
                if (item == Items.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid item for `tools.hoe.tillables." + blockId + ".drops`: " + itemId); return; }
                drops.put(item, (double) chance);
            });
            hoeTillables.put(block, new Tillable(condition, into, drops));
        });
        getMap("tools.shovel.flattenables", Map.ofEntries(
                Map.entry("minecraft:grass_block", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:dirt", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:podzol", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:coarse_dirt", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:mycelium", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())),
                Map.entry("minecraft:rooted_dirt", Map.of("into", "minecraft:dirt_path", "drops", new HashMap<String, Double>())))
        ).forEach((blockId, obj) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(blockId));
            if (block == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.shovel.flattenables`: " + blockId); return; }
            if (!(obj instanceof Map<?, ?> map)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.shovel.flattenables." + blockId + "`"); return; }
            String intoId = (String) map.get("into");
            Block into = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(intoId));
            if (into == Blocks.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid block for `tools.shovel.flattenables." + blockId + ".into`: " + intoId); return; }
            Object dropsObj = map.get("drops");
            if (!(dropsObj instanceof Map<?, ?> dropsMap)) { PurpurConfig.log(Level.SEVERE, "Invalid yaml for `tools.shovel.flattenables." + blockId + ".drops`"); return; }
            Map<Item, Double> drops = new HashMap<>();
            dropsMap.forEach((itemId, chance) -> {
                Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemId.toString()));
                if (item == Items.AIR) { PurpurConfig.log(Level.SEVERE, "Invalid item for `tools.shovel.flattenables." + blockId + ".drops`: " + itemId); return; }
                drops.put(item, (double) chance);
            });
            shovelFlattenables.put(block, new Flattenable(into, drops));
        });
    }

    public boolean anvilAllowColors = false;
    public boolean anvilColorsUseMiniMessage;
    private void anvilSettings() {
        anvilAllowColors = getBoolean("blocks.anvil.allow-colors", anvilAllowColors);
        anvilColorsUseMiniMessage = getBoolean("blocks.anvil.use-mini-message", anvilColorsUseMiniMessage);
    }

    public double azaleaGrowthChance = 0.0D;
    private void azaleaSettings() {
        azaleaGrowthChance = getDouble("blocks.azalea.growth-chance", azaleaGrowthChance);
    }

    public int beaconLevelOne = 20;
    public int beaconLevelTwo = 30;
    public int beaconLevelThree = 40;
    public int beaconLevelFour = 50;
    private void beaconSettings() {
        beaconLevelOne = getInt("blocks.beacon.effect-range.level-1", beaconLevelOne);
        beaconLevelTwo = getInt("blocks.beacon.effect-range.level-2", beaconLevelTwo);
        beaconLevelThree = getInt("blocks.beacon.effect-range.level-3", beaconLevelThree);
        beaconLevelFour = getInt("blocks.beacon.effect-range.level-4", beaconLevelFour);
    }

    public boolean bedExplode = true;
    public boolean bedExplodeOnVillagerSleep = false;
    public double bedExplosionPower = 5.0D;
    public boolean bedExplosionFire = true;
    public net.minecraft.world.level.Level.ExplosionInteraction bedExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
    private void bedSettings() {
        if (PurpurConfig.version < 31) {
            if ("DESTROY".equals(getString("blocks.bed.explosion-effect", bedExplosionEffect.name()))) {
                set("blocks.bed.explosion-effect", "BLOCK");
            }
        }
        bedExplode = getBoolean("blocks.bed.explode", bedExplode);
        bedExplodeOnVillagerSleep = getBoolean("blocks.bed.explode-on-villager-sleep", bedExplodeOnVillagerSleep);
        bedExplosionPower = getDouble("blocks.bed.explosion-power", bedExplosionPower);
        bedExplosionFire = getBoolean("blocks.bed.explosion-fire", bedExplosionFire);
        try {
            bedExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.valueOf(getString("blocks.bed.explosion-effect", bedExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.bed.explosion-effect`! Using default of `BLOCK`");
            bedExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
        }
    }

    public Map<Tilt, Integer> bigDripleafTiltDelay = new HashMap<>();
    private void bigDripleafSettings() {
        bigDripleafTiltDelay.clear();
        getMap("blocks.big_dripleaf.tilt-delay", Map.ofEntries(
                Map.entry("UNSTABLE", 10),
                Map.entry("PARTIAL", 10),
                Map.entry("FULL", 100))
        ).forEach((tilt, delay) -> {
            try {
                bigDripleafTiltDelay.put(Tilt.valueOf(tilt), (int) delay);
            } catch (IllegalArgumentException e) {
                PurpurConfig.log(Level.SEVERE, "Invalid big_dripleaf tilt key: " + tilt);
            }
        });
    }

    public boolean cactusBreaksFromSolidNeighbors = true;
    private void cactusSettings() {
        cactusBreaksFromSolidNeighbors = getBoolean("blocks.cactus.breaks-from-solid-neighbors", cactusBreaksFromSolidNeighbors);
    }

    public boolean chestOpenWithBlockOnTop = false;
    private void chestSettings() {
        chestOpenWithBlockOnTop = getBoolean("blocks.chest.open-with-solid-block-on-top", chestOpenWithBlockOnTop);
    }

    public boolean composterBulkProcess = false;
    private void composterSettings() {
        composterBulkProcess = getBoolean("blocks.composter.sneak-to-bulk-process", composterBulkProcess);
    }

    public boolean dispenserApplyCursedArmor = true;
    public boolean dispenserPlaceAnvils = false;
    private void dispenserSettings() {
        dispenserApplyCursedArmor = getBoolean("blocks.dispenser.apply-cursed-to-armor-slots", dispenserApplyCursedArmor);
        dispenserPlaceAnvils = getBoolean("blocks.dispenser.place-anvils", dispenserPlaceAnvils);
    }

    public List<Block> doorRequiresRedstone = new ArrayList<>();
    private void doorSettings() {
        getList("blocks.door.requires-redstone", new ArrayList<String>()).forEach(key -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(key.toString()));
            if (!block.defaultBlockState().isAir()) {
                doorRequiresRedstone.add(block);
            }
        });
    }

    public boolean dragonEggTeleport = true;
    private void dragonEggSettings() {
        dragonEggTeleport = getBoolean("blocks.dragon_egg.teleport", dragonEggTeleport);
    }

    public boolean baselessEndCrystalExplode = true;
    public double baselessEndCrystalExplosionPower = 6.0D;
    public boolean baselessEndCrystalExplosionFire = false;
    public net.minecraft.world.level.Level.ExplosionInteraction baselessEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
    public boolean basedEndCrystalExplode = true;
    public double basedEndCrystalExplosionPower = 6.0D;
    public boolean basedEndCrystalExplosionFire = false;
    public net.minecraft.world.level.Level.ExplosionInteraction basedEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
    private void endCrystalSettings() {
        if (PurpurConfig.version < 31) {
            if ("DESTROY".equals(getString("blocks.end-crystal.baseless.explosion-effect", baselessEndCrystalExplosionEffect.name()))) {
                set("blocks.end-crystal.baseless.explosion-effect", "BLOCK");
            }
            if ("DESTROY".equals(getString("blocks.end-crystal.base.explosion-effect", basedEndCrystalExplosionEffect.name()))) {
                set("blocks.end-crystal.base.explosion-effect", "BLOCK");
            }
        }
        baselessEndCrystalExplode = getBoolean("blocks.end-crystal.baseless.explode", baselessEndCrystalExplode);
        baselessEndCrystalExplosionPower = getDouble("blocks.end-crystal.baseless.explosion-power", baselessEndCrystalExplosionPower);
        baselessEndCrystalExplosionFire = getBoolean("blocks.end-crystal.baseless.explosion-fire", baselessEndCrystalExplosionFire);
        try {
            baselessEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.valueOf(getString("blocks.end-crystal.baseless.explosion-effect", baselessEndCrystalExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.end-crystal.baseless.explosion-effect`! Using default of `BLOCK`");
            baselessEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
        }
        basedEndCrystalExplode = getBoolean("blocks.end-crystal.base.explode", basedEndCrystalExplode);
        basedEndCrystalExplosionPower = getDouble("blocks.end-crystal.base.explosion-power", basedEndCrystalExplosionPower);
        basedEndCrystalExplosionFire = getBoolean("blocks.end-crystal.base.explosion-fire", basedEndCrystalExplosionFire);
        try {
            basedEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.valueOf(getString("blocks.end-crystal.base.explosion-effect", basedEndCrystalExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.end-crystal.base.explosion-effect`! Using default of `BLOCK`");
            basedEndCrystalExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
        }
    }

    public boolean farmlandBypassMobGriefing = false;
    public boolean farmlandGetsMoistFromBelow = false;
    public boolean farmlandAlpha = false;
    public boolean farmlandTramplingDisabled = false;
    public boolean farmlandTramplingOnlyPlayers = false;
    public boolean farmlandTramplingFeatherFalling = false;
    private void farmlandSettings() {
        farmlandBypassMobGriefing = getBoolean("blocks.farmland.bypass-mob-griefing", farmlandBypassMobGriefing);
        farmlandGetsMoistFromBelow = getBoolean("blocks.farmland.gets-moist-from-below", farmlandGetsMoistFromBelow);
        farmlandAlpha = getBoolean("blocks.farmland.use-alpha-farmland", farmlandAlpha);
        farmlandTramplingDisabled = getBoolean("blocks.farmland.disable-trampling", farmlandTramplingDisabled);
        farmlandTramplingOnlyPlayers = getBoolean("blocks.farmland.only-players-trample", farmlandTramplingOnlyPlayers);
        farmlandTramplingFeatherFalling = getBoolean("blocks.farmland.feather-fall-distance-affects-trampling", farmlandTramplingFeatherFalling);
    }

    public double floweringAzaleaGrowthChance = 0.0D;
    private void floweringAzaleaSettings() {
        floweringAzaleaGrowthChance = getDouble("blocks.flowering_azalea.growth-chance", floweringAzaleaGrowthChance);
    }

    public boolean furnaceUseLavaFromUnderneath = false;
    private void furnaceSettings() {
        if (PurpurConfig.version < 17) {
            furnaceUseLavaFromUnderneath = getBoolean("blocks.furnace.infinite-fuel", furnaceUseLavaFromUnderneath);
            boolean oldValue = getBoolean("blocks.furnace.infinite-fuel", furnaceUseLavaFromUnderneath);
            set("blocks.furnace.infinite-fuel", null);
            set("blocks.furnace.use-lava-from-underneath", oldValue);
        }
        furnaceUseLavaFromUnderneath = getBoolean("blocks.furnace.use-lava-from-underneath", furnaceUseLavaFromUnderneath);
    }

    public boolean mobsSpawnOnPackedIce = true;
    public boolean mobsSpawnOnBlueIce = true;
    private void iceSettings() {
        mobsSpawnOnPackedIce = getBoolean("blocks.packed_ice.allow-mob-spawns", mobsSpawnOnPackedIce);
        mobsSpawnOnBlueIce = getBoolean("blocks.blue_ice.allow-mob-spawns", mobsSpawnOnBlueIce);
    }

    public int lavaInfiniteRequiredSources = 2;
    public int lavaSpeedNether = 10;
    public int lavaSpeedNotNether = 30;
    private void lavaSettings() {
        lavaInfiniteRequiredSources = getInt("blocks.lava.infinite-required-sources", lavaInfiniteRequiredSources);
        lavaSpeedNether = getInt("blocks.lava.speed.nether", lavaSpeedNether);
        lavaSpeedNotNether = getInt("blocks.lava.speed.not-nether", lavaSpeedNotNether);
    }

    public int pistonBlockPushLimit = 12;
    private void pistonSettings() {
        pistonBlockPushLimit = getInt("blocks.piston.block-push-limit", pistonBlockPushLimit);
    }

    public boolean powderSnowBypassMobGriefing = false;
    private void powderSnowSettings() {
        powderSnowBypassMobGriefing = getBoolean("blocks.powder_snow.bypass-mob-griefing", powderSnowBypassMobGriefing);
    }

    public int railActivationRange = 8;
    private void railSettings() {
        railActivationRange = getInt("blocks.powered-rail.activation-range", railActivationRange);
    }

    public boolean respawnAnchorExplode = true;
    public double respawnAnchorExplosionPower = 5.0D;
    public boolean respawnAnchorExplosionFire = true;
    public net.minecraft.world.level.Level.ExplosionInteraction respawnAnchorExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
    private void respawnAnchorSettings() {
        if (PurpurConfig.version < 31) {
            if ("DESTROY".equals(getString("blocks.respawn_anchor.explosion-effect", respawnAnchorExplosionEffect.name()))) {
                set("blocks.respawn_anchor.explosion-effect", "BLOCK");
            }
        }
        respawnAnchorExplode = getBoolean("blocks.respawn_anchor.explode", respawnAnchorExplode);
        respawnAnchorExplosionPower = getDouble("blocks.respawn_anchor.explosion-power", respawnAnchorExplosionPower);
        respawnAnchorExplosionFire = getBoolean("blocks.respawn_anchor.explosion-fire", respawnAnchorExplosionFire);
        try {
            respawnAnchorExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.valueOf(getString("blocks.respawn_anchor.explosion-effect", respawnAnchorExplosionEffect.name()));
        } catch (IllegalArgumentException e) {
            log(Level.SEVERE, "Unknown value for `blocks.respawn_anchor.explosion-effect`! Using default of `BLOCK`");
            respawnAnchorExplosionEffect = net.minecraft.world.level.Level.ExplosionInteraction.BLOCK;
        }
    }

    public boolean slabHalfBreak = false;
    private void slabSettings() {
        slabHalfBreak = getBoolean("blocks.slab.break-individual-slabs-when-sneaking", slabHalfBreak);
    }

    public boolean spawnerDeactivateByRedstone = false;
    private void spawnerSettings() {
        spawnerDeactivateByRedstone = getBoolean("blocks.spawner.deactivate-by-redstone", spawnerDeactivateByRedstone);
    }

    public int spongeAbsorptionArea = 65;
    public int spongeAbsorptionRadius = 6;
    public boolean spongeAbsorbsLava = false;
    public boolean spongeAbsorbsWaterFromMud = false;
    private void spongeSettings() {
        spongeAbsorptionArea = getInt("blocks.sponge.absorption.area", spongeAbsorptionArea);
        spongeAbsorptionRadius = getInt("blocks.sponge.absorption.radius", spongeAbsorptionRadius);
        spongeAbsorbsLava = getBoolean("blocks.sponge.absorbs-lava", spongeAbsorbsLava);
        spongeAbsorbsWaterFromMud = getBoolean("blocks.sponge.absorbs-water-from-mud", spongeAbsorbsWaterFromMud);
    }

    public boolean turtleEggsBreakFromExpOrbs = false;
    public boolean turtleEggsBreakFromItems = false;
    public boolean turtleEggsBreakFromMinecarts = false;
    public boolean turtleEggsBypassMobGriefing = false;
    private void turtleEggSettings() {
        turtleEggsBreakFromExpOrbs = getBoolean("blocks.turtle_egg.break-from-exp-orbs", turtleEggsBreakFromExpOrbs);
        turtleEggsBreakFromItems = getBoolean("blocks.turtle_egg.break-from-items", turtleEggsBreakFromItems);
        turtleEggsBreakFromMinecarts = getBoolean("blocks.turtle_egg.break-from-minecarts", turtleEggsBreakFromMinecarts);
        turtleEggsBypassMobGriefing = getBoolean("blocks.turtle_egg.bypass-mob-griefing", turtleEggsBypassMobGriefing);
    }

    public int waterInfiniteRequiredSources = 2;
    private void waterSources() {
        waterInfiniteRequiredSources = getInt("blocks.water.infinite-required-sources", waterInfiniteRequiredSources);
    }

    public boolean babiesAreRidable = true;
    public boolean untamedTamablesAreRidable = true;
    public boolean useNightVisionWhenRiding = false;
    public boolean useDismountsUnderwaterTag = true;
    private void ridableSettings() {
        babiesAreRidable = getBoolean("ridable-settings.babies-are-ridable", babiesAreRidable);
        untamedTamablesAreRidable = getBoolean("ridable-settings.untamed-tamables-are-ridable", untamedTamablesAreRidable);
        useNightVisionWhenRiding = getBoolean("ridable-settings.use-night-vision", useNightVisionWhenRiding);
        useDismountsUnderwaterTag = getBoolean("ridable-settings.use-dismounts-underwater-tag", useDismountsUnderwaterTag);
    }

    public boolean allayRidable = false;
    public boolean allayRidableInWater = true;
    public boolean allayControllable = true;
    public double allayMaxHealth = 20.0D;
    public double allayScale = 1.0D;
    private void allaySettings() {
        allayRidable = getBoolean("mobs.allay.ridable", allayRidable);
        allayRidableInWater = getBoolean("mobs.allay.ridable-in-water", allayRidableInWater);
        allayControllable = getBoolean("mobs.allay.controllable", allayControllable);
        allayMaxHealth = getDouble("mobs.allay.attributes.max_health", allayMaxHealth);
        allayScale = Mth.clamp(getDouble("mobs.allay.attributes.scale", allayScale), 0.0625D, 16.0D);
    }

    public boolean armadilloRidable = false;
    public boolean armadilloRidableInWater = true;
    public boolean armadilloControllable = true;
    public double armadilloMaxHealth = 12.0D;
    public double armadilloScale = 1.0D;
    public int armadilloBreedingTicks = 6000;
    private void armadilloSettings() {
        armadilloRidable = getBoolean("mobs.armadillo.ridable", armadilloRidable);
        armadilloRidableInWater = getBoolean("mobs.armadillo.ridable-in-water", armadilloRidableInWater);
        armadilloControllable = getBoolean("mobs.armadillo.controllable", armadilloControllable);
        armadilloMaxHealth = getDouble("mobs.armadillo.attributes.max_health", armadilloMaxHealth);
        armadilloScale = Mth.clamp(getDouble("mobs.armadillo.attributes.scale", armadilloScale), 0.0625D, 16.0D);
        armadilloBreedingTicks = getInt("mobs.armadillo.breeding-delay-ticks", armadilloBreedingTicks);
    }

    public boolean axolotlRidable = false;
    public boolean axolotlControllable = true;
    public double axolotlMaxHealth = 14.0D;
    public double axolotlScale = 1.0D;
    public int axolotlBreedingTicks = 6000;
    public boolean axolotlTakeDamageFromWater = false;
    private void axolotlSettings() {
        axolotlRidable = getBoolean("mobs.axolotl.ridable", axolotlRidable);
        axolotlControllable = getBoolean("mobs.axolotl.controllable", axolotlControllable);
        axolotlMaxHealth = getDouble("mobs.axolotl.attributes.max_health", axolotlMaxHealth);
        axolotlScale = Mth.clamp(getDouble("mobs.axolotl.attributes.scale", axolotlScale), 0.0625D, 16.0D);
        axolotlBreedingTicks = getInt("mobs.axolotl.breeding-delay-ticks", axolotlBreedingTicks);
        axolotlTakeDamageFromWater = getBoolean("mobs.axolotl.takes-damage-from-water", axolotlTakeDamageFromWater);
    }

    public boolean batRidable = false;
    public boolean batRidableInWater = true;
    public boolean batControllable = true;
    public double batMaxY = 320D;
    public double batMaxHealth = 6.0D;
    public double batScale = 1.0D;
    public double batFollowRange = 16.0D;
    public double batKnockbackResistance = 0.0D;
    public double batMovementSpeed = 0.6D;
    public double batFlyingSpeed = 0.6D;
    public double batArmor = 0.0D;
    public double batArmorToughness = 0.0D;
    public double batAttackKnockback = 0.0D;
    public boolean batTakeDamageFromWater = false;
    private void batSettings() {
        batRidable = getBoolean("mobs.bat.ridable", batRidable);
        batRidableInWater = getBoolean("mobs.bat.ridable-in-water", batRidableInWater);
        batControllable = getBoolean("mobs.bat.controllable", batControllable);
        batMaxY = getDouble("mobs.bat.ridable-max-y", batMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.bat.attributes.max-health", batMaxHealth);
            set("mobs.bat.attributes.max-health", null);
            set("mobs.bat.attributes.max_health", oldValue);
        }
        batMaxHealth = getDouble("mobs.bat.attributes.max_health", batMaxHealth);
        batScale = Mth.clamp(getDouble("mobs.bat.attributes.scale", batScale), 0.0625D, 16.0D);
        batFollowRange = getDouble("mobs.bat.attributes.follow_range", batFollowRange);
        batKnockbackResistance = getDouble("mobs.bat.attributes.knockback_resistance", batKnockbackResistance);
        batMovementSpeed = getDouble("mobs.bat.attributes.movement_speed", batMovementSpeed);
        batFlyingSpeed = getDouble("mobs.bat.attributes.flying_speed", batFlyingSpeed);
        batArmor = getDouble("mobs.bat.attributes.armor", batArmor);
        batArmorToughness = getDouble("mobs.bat.attributes.armor_toughness", batArmorToughness);
        batAttackKnockback = getDouble("mobs.bat.attributes.attack_knockback", batAttackKnockback);
        batTakeDamageFromWater = getBoolean("mobs.bat.takes-damage-from-water", batTakeDamageFromWater);
    }

    public boolean beeRidable = false;
    public boolean beeRidableInWater = true;
    public boolean beeControllable = true;
    public double beeMaxY = 320D;
    public double beeMaxHealth = 10.0D;
    public double beeScale = 1.0D;
    public int beeBreedingTicks = 6000;
    public boolean beeTakeDamageFromWater = true;
    public boolean beeCanWorkAtNight = false;
    public boolean beeCanWorkInRain = false;
    private void beeSettings() {
        beeRidable = getBoolean("mobs.bee.ridable", beeRidable);
        beeRidableInWater = getBoolean("mobs.bee.ridable-in-water", beeRidableInWater);
        beeControllable = getBoolean("mobs.bee.controllable", beeControllable);
        beeMaxY = getDouble("mobs.bee.ridable-max-y", beeMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.bee.attributes.max-health", beeMaxHealth);
            set("mobs.bee.attributes.max-health", null);
            set("mobs.bee.attributes.max_health", oldValue);
        }
        beeMaxHealth = getDouble("mobs.bee.attributes.max_health", beeMaxHealth);
        beeScale = Mth.clamp(getDouble("mobs.bee.attributes.scale", beeScale), 0.0625D, 16.0D);
        beeBreedingTicks = getInt("mobs.bee.breeding-delay-ticks", beeBreedingTicks);
        beeTakeDamageFromWater = getBoolean("mobs.bee.takes-damage-from-water", beeTakeDamageFromWater);
        beeCanWorkAtNight = getBoolean("mobs.bee.can-work-at-night", beeCanWorkAtNight);
        beeCanWorkInRain = getBoolean("mobs.bee.can-work-in-rain", beeCanWorkInRain);
    }

    public boolean blazeRidable = false;
    public boolean blazeRidableInWater = true;
    public boolean blazeControllable = true;
    public double blazeMaxY = 320D;
    public double blazeMaxHealth = 20.0D;
    public double blazeScale = 1.0D;
    public boolean blazeTakeDamageFromWater = true;
    private void blazeSettings() {
        blazeRidable = getBoolean("mobs.blaze.ridable", blazeRidable);
        blazeRidableInWater = getBoolean("mobs.blaze.ridable-in-water", blazeRidableInWater);
        blazeControllable = getBoolean("mobs.blaze.controllable", blazeControllable);
        blazeMaxY = getDouble("mobs.blaze.ridable-max-y", blazeMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.blaze.attributes.max-health", blazeMaxHealth);
            set("mobs.blaze.attributes.max-health", null);
            set("mobs.blaze.attributes.max_health", oldValue);
        }
        blazeMaxHealth = getDouble("mobs.blaze.attributes.max_health", blazeMaxHealth);
        blazeScale = Mth.clamp(getDouble("mobs.blaze.attributes.scale", blazeScale), 0.0625D, 16.0D);
        blazeTakeDamageFromWater = getBoolean("mobs.blaze.takes-damage-from-water", blazeTakeDamageFromWater);
    }

    public boolean boggedRidable = false;
    public boolean boggedRidableInWater = true;
    public boolean boggedControllable = true;
    public double boggedMaxHealth = 16.0D;
    public double boggedScale = 1.0D;
    private void boggedSettings() {
        boggedRidable = getBoolean("mobs.bogged.ridable", boggedRidable);
        boggedRidableInWater = getBoolean("mobs.bogged.ridable-in-water", boggedRidableInWater);
        boggedControllable = getBoolean("mobs.bogged.controllable", boggedControllable);
        boggedMaxHealth = getDouble("mobs.bogged.attributes.max_health", boggedMaxHealth);
        boggedScale = Mth.clamp(getDouble("mobs.bogged.attributes.scale", boggedScale), 0.0625D, 16.0D);
    }

    public boolean camelRidableInWater = false;
    public double camelMaxHealthMin = 32.0D;
    public double camelMaxHealthMax = 32.0D;
    public double camelJumpStrengthMin = 0.42D;
    public double camelJumpStrengthMax = 0.42D;
    public double camelMovementSpeedMin = 0.09D;
    public double camelMovementSpeedMax = 0.09D;
    public int camelBreedingTicks = 6000;
    private void camelSettings() {
        camelRidableInWater = getBoolean("mobs.camel.ridable-in-water", camelRidableInWater);
        camelMaxHealthMin = getDouble("mobs.camel.attributes.max_health.min", camelMaxHealthMin);
        camelMaxHealthMax = getDouble("mobs.camel.attributes.max_health.max", camelMaxHealthMax);
        camelJumpStrengthMin = getDouble("mobs.camel.attributes.jump_strength.min", camelJumpStrengthMin);
        camelJumpStrengthMax = getDouble("mobs.camel.attributes.jump_strength.max", camelJumpStrengthMax);
        camelMovementSpeedMin = getDouble("mobs.camel.attributes.movement_speed.min", camelMovementSpeedMin);
        camelMovementSpeedMax = getDouble("mobs.camel.attributes.movement_speed.max", camelMovementSpeedMax);
        camelBreedingTicks = getInt("mobs.camel.breeding-delay-ticks", camelBreedingTicks);
    }

    public boolean catRidable = false;
    public boolean catRidableInWater = true;
    public boolean catControllable = true;
    public double catMaxHealth = 10.0D;
    public double catScale = 1.0D;
    public int catSpawnDelay = 1200;
    public int catSpawnSwampHutScanRange = 16;
    public int catSpawnVillageScanRange = 48;
    public int catBreedingTicks = 6000;
    public DyeColor catDefaultCollarColor = DyeColor.RED;
    public boolean catTakeDamageFromWater = false;
    private void catSettings() {
        catRidable = getBoolean("mobs.cat.ridable", catRidable);
        catRidableInWater = getBoolean("mobs.cat.ridable-in-water", catRidableInWater);
        catControllable = getBoolean("mobs.cat.controllable", catControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cat.attributes.max-health", catMaxHealth);
            set("mobs.cat.attributes.max-health", null);
            set("mobs.cat.attributes.max_health", oldValue);
        }
        catMaxHealth = getDouble("mobs.cat.attributes.max_health", catMaxHealth);
        catScale = Mth.clamp(getDouble("mobs.cat.attributes.scale", catScale), 0.0625D, 16.0D);
        catSpawnDelay = getInt("mobs.cat.spawn-delay", catSpawnDelay);
        catSpawnSwampHutScanRange = getInt("mobs.cat.scan-range-for-other-cats.swamp-hut", catSpawnSwampHutScanRange);
        catSpawnVillageScanRange = getInt("mobs.cat.scan-range-for-other-cats.village", catSpawnVillageScanRange);
        catBreedingTicks = getInt("mobs.cat.breeding-delay-ticks", catBreedingTicks);
        try {
            catDefaultCollarColor = DyeColor.valueOf(getString("mobs.cat.default-collar-color", catDefaultCollarColor.name()));
        } catch (IllegalArgumentException ignore) {
            catDefaultCollarColor = DyeColor.RED;
        }
        catTakeDamageFromWater = getBoolean("mobs.cat.takes-damage-from-water", catTakeDamageFromWater);
    }

    public boolean caveSpiderRidable = false;
    public boolean caveSpiderRidableInWater = true;
    public boolean caveSpiderControllable = true;
    public double caveSpiderMaxHealth = 12.0D;
    public double caveSpiderScale = 1.0D;
    public boolean caveSpiderTakeDamageFromWater = false;
    private void caveSpiderSettings() {
        caveSpiderRidable = getBoolean("mobs.cave_spider.ridable", caveSpiderRidable);
        caveSpiderRidableInWater = getBoolean("mobs.cave_spider.ridable-in-water", caveSpiderRidableInWater);
        caveSpiderControllable = getBoolean("mobs.cave_spider.controllable", caveSpiderControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cave_spider.attributes.max-health", caveSpiderMaxHealth);
            set("mobs.cave_spider.attributes.max-health", null);
            set("mobs.cave_spider.attributes.max_health", oldValue);
        }
        caveSpiderMaxHealth = getDouble("mobs.cave_spider.attributes.max_health", caveSpiderMaxHealth);
        caveSpiderScale = Mth.clamp(getDouble("mobs.cave_spider.attributes.scale", caveSpiderScale), 0.0625D, 16.0D);
        caveSpiderTakeDamageFromWater = getBoolean("mobs.cave_spider.takes-damage-from-water", caveSpiderTakeDamageFromWater);
    }

    public boolean chickenRidable = false;
    public boolean chickenRidableInWater = false;
    public boolean chickenControllable = true;
    public double chickenMaxHealth = 4.0D;
    public double chickenScale = 1.0D;
    public boolean chickenRetaliate = false;
    public int chickenBreedingTicks = 6000;
    public boolean chickenTakeDamageFromWater = false;
    private void chickenSettings() {
        chickenRidable = getBoolean("mobs.chicken.ridable", chickenRidable);
        chickenRidableInWater = getBoolean("mobs.chicken.ridable-in-water", chickenRidableInWater);
        chickenControllable = getBoolean("mobs.chicken.controllable", chickenControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.chicken.attributes.max-health", chickenMaxHealth);
            set("mobs.chicken.attributes.max-health", null);
            set("mobs.chicken.attributes.max_health", oldValue);
        }
        chickenMaxHealth = getDouble("mobs.chicken.attributes.max_health", chickenMaxHealth);
        chickenScale = Mth.clamp(getDouble("mobs.chicken.attributes.scale", chickenScale), 0.0625D, 16.0D);
        chickenRetaliate = getBoolean("mobs.chicken.retaliate", chickenRetaliate);
        chickenBreedingTicks = getInt("mobs.chicken.breeding-delay-ticks", chickenBreedingTicks);
        chickenTakeDamageFromWater = getBoolean("mobs.chicken.takes-damage-from-water", chickenTakeDamageFromWater);
    }

    public boolean codRidable = false;
    public boolean codControllable = true;
    public double codMaxHealth = 3.0D;
    public double codScale = 1.0D;
    public boolean codTakeDamageFromWater = false;
    private void codSettings() {
        codRidable = getBoolean("mobs.cod.ridable", codRidable);
        codControllable = getBoolean("mobs.cod.controllable", codControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cod.attributes.max-health", codMaxHealth);
            set("mobs.cod.attributes.max-health", null);
            set("mobs.cod.attributes.max_health", oldValue);
        }
        codMaxHealth = getDouble("mobs.cod.attributes.max_health", codMaxHealth);
        codScale = Mth.clamp(getDouble("mobs.cod.attributes.scale", codScale), 0.0625D, 16.0D);
        codTakeDamageFromWater = getBoolean("mobs.cod.takes-damage-from-water", codTakeDamageFromWater);
    }

    public boolean cowRidable = false;
    public boolean cowRidableInWater = true;
    public boolean cowControllable = true;
    public double cowMaxHealth = 10.0D;
    public double cowScale = 1.0D;
    public int cowFeedMushrooms = 0;
    public int cowBreedingTicks = 6000;
    public boolean cowTakeDamageFromWater = false;
    public double cowNaturallyAggressiveToPlayersChance = 0.0D;
    public double cowNaturallyAggressiveToPlayersDamage = 2.0D;
    private void cowSettings() {
        if (PurpurConfig.version < 22) {
            double oldValue = getDouble("mobs.cow.naturally-aggressive-to-players-chance", cowNaturallyAggressiveToPlayersChance);
            set("mobs.cow.naturally-aggressive-to-players-chance", null);
            set("mobs.cow.naturally-aggressive-to-players.chance", oldValue);
        }
        cowRidable = getBoolean("mobs.cow.ridable", cowRidable);
        cowRidableInWater = getBoolean("mobs.cow.ridable-in-water", cowRidableInWater);
        cowControllable = getBoolean("mobs.cow.controllable", cowControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.cow.attributes.max-health", cowMaxHealth);
            set("mobs.cow.attributes.max-health", null);
            set("mobs.cow.attributes.max_health", oldValue);
        }
        cowMaxHealth = getDouble("mobs.cow.attributes.max_health", cowMaxHealth);
        cowScale = Mth.clamp(getDouble("mobs.cow.attributes.scale", cowScale), 0.0625D, 16.0D);
        cowFeedMushrooms = getInt("mobs.cow.feed-mushrooms-for-mooshroom", cowFeedMushrooms);
        cowBreedingTicks = getInt("mobs.cow.breeding-delay-ticks", cowBreedingTicks);
        cowTakeDamageFromWater = getBoolean("mobs.cow.takes-damage-from-water", cowTakeDamageFromWater);
        cowNaturallyAggressiveToPlayersChance = getDouble("mobs.cow.naturally-aggressive-to-players.chance", cowNaturallyAggressiveToPlayersChance);
        cowNaturallyAggressiveToPlayersDamage = getDouble("mobs.cow.naturally-aggressive-to-players.damage", cowNaturallyAggressiveToPlayersDamage);
    }

    public boolean creakingRidable = false;
    public boolean creakingRidableInWater = true;
    public boolean creakingControllable = true;
    public double creakingMaxHealth = 1.0D;
    public double creakingScale = 1.0D;
    private void creakingSettings() {
        creakingRidable = getBoolean("mobs.creaking.ridable", creakingRidable);
        creakingRidableInWater = getBoolean("mobs.creaking.ridable-in-water", creakingRidableInWater);
        creakingControllable = getBoolean("mobs.creaking.controllable", creakingControllable);
        creakingMaxHealth = getDouble("mobs.creaking.attributes.max_health", creakingMaxHealth);
        creakingScale = Mth.clamp(getDouble("mobs.creaking.attributes.scale", creakingScale), 0.0625D, 16.0D);
    }

    public boolean creeperRidable = false;
    public boolean creeperRidableInWater = true;
    public boolean creeperControllable = true;
    public double creeperMaxHealth = 20.0D;
    public double creeperScale = 1.0D;
    public double creeperChargedChance = 0.0D;
    public boolean creeperAllowGriefing = true;
    public boolean creeperBypassMobGriefing = false;
    public boolean creeperTakeDamageFromWater = false;
    public boolean creeperExplodeWhenKilled = false;
    public boolean creeperHealthRadius = false;
    private void creeperSettings() {
        creeperRidable = getBoolean("mobs.creeper.ridable", creeperRidable);
        creeperRidableInWater = getBoolean("mobs.creeper.ridable-in-water", creeperRidableInWater);
        creeperControllable = getBoolean("mobs.creeper.controllable", creeperControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.creeper.attributes.max-health", creeperMaxHealth);
            set("mobs.creeper.attributes.max-health", null);
            set("mobs.creeper.attributes.max_health", oldValue);
        }
        creeperMaxHealth = getDouble("mobs.creeper.attributes.max_health", creeperMaxHealth);
        creeperScale = Mth.clamp(getDouble("mobs.creeper.attributes.scale", creeperScale), 0.0625D, 16.0D);
        creeperChargedChance = getDouble("mobs.creeper.naturally-charged-chance", creeperChargedChance);
        creeperAllowGriefing = getBoolean("mobs.creeper.allow-griefing", creeperAllowGriefing);
        creeperBypassMobGriefing = getBoolean("mobs.creeper.bypass-mob-griefing", creeperBypassMobGriefing);
        creeperTakeDamageFromWater = getBoolean("mobs.creeper.takes-damage-from-water", creeperTakeDamageFromWater);
        creeperExplodeWhenKilled = getBoolean("mobs.creeper.explode-when-killed", creeperExplodeWhenKilled);
        creeperHealthRadius = getBoolean("mobs.creeper.health-impacts-explosion", creeperHealthRadius);
    }

    public boolean dolphinRidable = false;
    public boolean dolphinControllable = true;
    public int dolphinSpitCooldown = 20;
    public float dolphinSpitSpeed = 1.0F;
    public float dolphinSpitDamage = 2.0F;
    public double dolphinMaxHealth = 10.0D;
    public double dolphinScale = 1.0D;
    public boolean dolphinDisableTreasureSearching = false;
    public boolean dolphinTakeDamageFromWater = false;
    public double dolphinNaturallyAggressiveToPlayersChance = 0.0D;
    private void dolphinSettings() {
        dolphinRidable = getBoolean("mobs.dolphin.ridable", dolphinRidable);
        dolphinControllable = getBoolean("mobs.dolphin.controllable", dolphinControllable);
        dolphinSpitCooldown = getInt("mobs.dolphin.spit.cooldown", dolphinSpitCooldown);
        dolphinSpitSpeed = (float) getDouble("mobs.dolphin.spit.speed", dolphinSpitSpeed);
        dolphinSpitDamage = (float) getDouble("mobs.dolphin.spit.damage", dolphinSpitDamage);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.dolphin.attributes.max-health", dolphinMaxHealth);
            set("mobs.dolphin.attributes.max-health", null);
            set("mobs.dolphin.attributes.max_health", oldValue);
        }
        dolphinMaxHealth = getDouble("mobs.dolphin.attributes.max_health", dolphinMaxHealth);
        dolphinScale = Mth.clamp(getDouble("mobs.dolphin.attributes.scale", dolphinScale), 0.0625D, 16.0D);
        dolphinDisableTreasureSearching = getBoolean("mobs.dolphin.disable-treasure-searching", dolphinDisableTreasureSearching);
        dolphinTakeDamageFromWater = getBoolean("mobs.dolphin.takes-damage-from-water", dolphinTakeDamageFromWater);
        dolphinNaturallyAggressiveToPlayersChance = getDouble("mobs.dolphin.naturally-aggressive-to-players-chance", dolphinNaturallyAggressiveToPlayersChance);
    }

    public boolean donkeyRidableInWater = false;
    public double donkeyMaxHealthMin = 15.0D;
    public double donkeyMaxHealthMax = 30.0D;
    public double donkeyJumpStrengthMin = 0.5D;
    public double donkeyJumpStrengthMax = 0.5D;
    public double donkeyMovementSpeedMin = 0.175D;
    public double donkeyMovementSpeedMax = 0.175D;
    public int donkeyBreedingTicks = 6000;
    public boolean donkeyTakeDamageFromWater = false;
    private void donkeySettings() {
        donkeyRidableInWater = getBoolean("mobs.donkey.ridable-in-water", donkeyRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.donkey.attributes.max-health.min", donkeyMaxHealthMin);
            double oldMax = getDouble("mobs.donkey.attributes.max-health.max", donkeyMaxHealthMax);
            set("mobs.donkey.attributes.max-health", null);
            set("mobs.donkey.attributes.max_health.min", oldMin);
            set("mobs.donkey.attributes.max_health.max", oldMax);
        }
        donkeyMaxHealthMin = getDouble("mobs.donkey.attributes.max_health.min", donkeyMaxHealthMin);
        donkeyMaxHealthMax = getDouble("mobs.donkey.attributes.max_health.max", donkeyMaxHealthMax);
        donkeyJumpStrengthMin = getDouble("mobs.donkey.attributes.jump_strength.min", donkeyJumpStrengthMin);
        donkeyJumpStrengthMax = getDouble("mobs.donkey.attributes.jump_strength.max", donkeyJumpStrengthMax);
        donkeyMovementSpeedMin = getDouble("mobs.donkey.attributes.movement_speed.min", donkeyMovementSpeedMin);
        donkeyMovementSpeedMax = getDouble("mobs.donkey.attributes.movement_speed.max", donkeyMovementSpeedMax);
        donkeyBreedingTicks = getInt("mobs.donkey.breeding-delay-ticks", donkeyBreedingTicks);
        donkeyTakeDamageFromWater = getBoolean("mobs.donkey.takes-damage-from-water", donkeyTakeDamageFromWater);
    }

    public boolean drownedRidable = false;
    public boolean drownedRidableInWater = true;
    public boolean drownedControllable = true;
    public double drownedMaxHealth = 20.0D;
    public double drownedScale = 1.0D;
    public double drownedSpawnReinforcements = 0.1D;
    public boolean drownedJockeyOnlyBaby = true;
    public double drownedJockeyChance = 0.05D;
    public boolean drownedJockeyTryExistingChickens = true;
    public boolean drownedTakeDamageFromWater = false;
    public boolean drownedBreakDoors = false;
    private void drownedSettings() {
        drownedRidable = getBoolean("mobs.drowned.ridable", drownedRidable);
        drownedRidableInWater = getBoolean("mobs.drowned.ridable-in-water", drownedRidableInWater);
        drownedControllable = getBoolean("mobs.drowned.controllable", drownedControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.drowned.attributes.max-health", drownedMaxHealth);
            set("mobs.drowned.attributes.max-health", null);
            set("mobs.drowned.attributes.max_health", oldValue);
        }
        drownedMaxHealth = getDouble("mobs.drowned.attributes.max_health", drownedMaxHealth);
        drownedScale = Mth.clamp(getDouble("mobs.drowned.attributes.scale", drownedScale), 0.0625D, 16.0D);
        drownedSpawnReinforcements = getDouble("mobs.drowned.attributes.spawn_reinforcements", drownedSpawnReinforcements);
        drownedJockeyOnlyBaby = getBoolean("mobs.drowned.jockey.only-babies", drownedJockeyOnlyBaby);
        drownedJockeyChance = getDouble("mobs.drowned.jockey.chance", drownedJockeyChance);
        drownedJockeyTryExistingChickens = getBoolean("mobs.drowned.jockey.try-existing-chickens", drownedJockeyTryExistingChickens);
        drownedTakeDamageFromWater = getBoolean("mobs.drowned.takes-damage-from-water", drownedTakeDamageFromWater);
        drownedBreakDoors = getBoolean("mobs.drowned.can-break-doors", drownedBreakDoors);
    }

    public boolean elderGuardianRidable = false;
    public boolean elderGuardianControllable = true;
    public double elderGuardianMaxHealth = 80.0D;
    public double elderGuardianScale = 1.0D;
    public boolean elderGuardianTakeDamageFromWater = false;
    private void elderGuardianSettings() {
        elderGuardianRidable = getBoolean("mobs.elder_guardian.ridable", elderGuardianRidable);
        elderGuardianControllable = getBoolean("mobs.elder_guardian.controllable", elderGuardianControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.elder_guardian.attributes.max-health", elderGuardianMaxHealth);
            set("mobs.elder_guardian.attributes.max-health", null);
            set("mobs.elder_guardian.attributes.max_health", oldValue);
        }
        elderGuardianMaxHealth = getDouble("mobs.elder_guardian.attributes.max_health", elderGuardianMaxHealth);
        elderGuardianScale = Mth.clamp(getDouble("mobs.elder_guardian.attributes.scale", elderGuardianScale), 0.0625D, 16.0D);
        elderGuardianTakeDamageFromWater = getBoolean("mobs.elder_guardian.takes-damage-from-water", elderGuardianTakeDamageFromWater);
    }

    public boolean enderDragonRidable = false;
    public boolean enderDragonRidableInWater = true;
    public boolean enderDragonControllable = true;
    public double enderDragonMaxY = 320D;
    public double enderDragonMaxHealth = 200.0D;
    public boolean enderDragonAlwaysDropsFullExp = false;
    public boolean enderDragonBypassMobGriefing = false;
    public boolean enderDragonTakeDamageFromWater = false;
    public boolean enderDragonCanRideVehicles = false;
    private void enderDragonSettings() {
        enderDragonRidable = getBoolean("mobs.ender_dragon.ridable", enderDragonRidable);
        enderDragonRidableInWater = getBoolean("mobs.ender_dragon.ridable-in-water", enderDragonRidableInWater);
        enderDragonControllable = getBoolean("mobs.ender_dragon.controllable", enderDragonControllable);
        enderDragonMaxY = getDouble("mobs.ender_dragon.ridable-max-y", enderDragonMaxY);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.ender_dragon.max-health", enderDragonMaxHealth);
            set("mobs.ender_dragon.max-health", null);
            set("mobs.ender_dragon.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ender_dragon.attributes.max-health", enderDragonMaxHealth);
            set("mobs.ender_dragon.attributes.max-health", null);
            set("mobs.ender_dragon.attributes.max_health", oldValue);
        }
        enderDragonMaxHealth = getDouble("mobs.ender_dragon.attributes.max_health", enderDragonMaxHealth);
        enderDragonAlwaysDropsFullExp = getBoolean("mobs.ender_dragon.always-drop-full-exp", enderDragonAlwaysDropsFullExp);
        enderDragonBypassMobGriefing = getBoolean("mobs.ender_dragon.bypass-mob-griefing", enderDragonBypassMobGriefing);
        enderDragonTakeDamageFromWater = getBoolean("mobs.ender_dragon.takes-damage-from-water", enderDragonTakeDamageFromWater);
        enderDragonCanRideVehicles = getBoolean("mobs.ender_dragon.can-ride-vehicles", enderDragonCanRideVehicles);
    }

    public boolean endermanRidable = false;
    public boolean endermanRidableInWater = true;
    public boolean endermanControllable = true;
    public double endermanMaxHealth = 40.0D;
    public double endermanScale = 1.0D;
    public boolean endermanAllowGriefing = true;
    public boolean endermanDespawnEvenWithBlock = false;
    public boolean endermanBypassMobGriefing = false;
    public boolean endermanTakeDamageFromWater = true;
    public boolean endermanAggroEndermites = true;
    public boolean endermanAggroEndermitesOnlyIfPlayerSpawned = false;
    public boolean endermanDisableStareAggro = false;
    public boolean endermanIgnoreProjectiles = false;
    private void endermanSettings() {
        endermanRidable = getBoolean("mobs.enderman.ridable", endermanRidable);
        endermanRidableInWater = getBoolean("mobs.enderman.ridable-in-water", endermanRidableInWater);
        endermanControllable = getBoolean("mobs.enderman.controllable", endermanControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.enderman.attributes.max-health", endermanMaxHealth);
            set("mobs.enderman.attributes.max-health", null);
            set("mobs.enderman.attributes.max_health", oldValue);
        }
        if (PurpurConfig.version < 15) {
            // remove old option
            set("mobs.enderman.aggressive-towards-spawned-endermites", null);
        }
        endermanMaxHealth = getDouble("mobs.enderman.attributes.max_health", endermanMaxHealth);
        endermanScale = Mth.clamp(getDouble("mobs.enderman.attributes.scale", endermanScale), 0.0625D, 16.0D);
        endermanAllowGriefing = getBoolean("mobs.enderman.allow-griefing", endermanAllowGriefing);
        endermanDespawnEvenWithBlock = getBoolean("mobs.enderman.can-despawn-with-held-block", endermanDespawnEvenWithBlock);
        endermanBypassMobGriefing = getBoolean("mobs.enderman.bypass-mob-griefing", endermanBypassMobGriefing);
        endermanTakeDamageFromWater = getBoolean("mobs.enderman.takes-damage-from-water", endermanTakeDamageFromWater);
        endermanAggroEndermites = getBoolean("mobs.enderman.aggressive-towards-endermites", endermanAggroEndermites);
        endermanAggroEndermitesOnlyIfPlayerSpawned = getBoolean("mobs.enderman.aggressive-towards-endermites-only-spawned-by-player-thrown-ender-pearls", endermanAggroEndermitesOnlyIfPlayerSpawned);
        endermanDisableStareAggro = getBoolean("mobs.enderman.disable-player-stare-aggression", endermanDisableStareAggro);
        endermanIgnoreProjectiles = getBoolean("mobs.enderman.ignore-projectiles", endermanIgnoreProjectiles);
    }

    public boolean endermiteRidable = false;
    public boolean endermiteRidableInWater = true;
    public boolean endermiteControllable = true;
    public double endermiteMaxHealth = 8.0D;
    public double endermiteScale = 1.0D;
    public boolean endermiteTakeDamageFromWater = false;
    private void endermiteSettings() {
        endermiteRidable = getBoolean("mobs.endermite.ridable", endermiteRidable);
        endermiteRidableInWater = getBoolean("mobs.endermite.ridable-in-water", endermiteRidableInWater);
        endermiteControllable = getBoolean("mobs.endermite.controllable", endermiteControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.endermite.attributes.max-health", endermiteMaxHealth);
            set("mobs.endermite.attributes.max-health", null);
            set("mobs.endermite.attributes.max_health", oldValue);
        }
        endermiteMaxHealth = getDouble("mobs.endermite.attributes.max_health", endermiteMaxHealth);
        endermiteScale = Mth.clamp(getDouble("mobs.endermite.attributes.scale", endermiteScale), 0.0625D, 16.0D);
        endermiteTakeDamageFromWater = getBoolean("mobs.endermite.takes-damage-from-water", endermiteTakeDamageFromWater);
    }

    public boolean evokerRidable = false;
    public boolean evokerRidableInWater = true;
    public boolean evokerControllable = true;
    public double evokerMaxHealth = 24.0D;
    public double evokerScale = 1.0D;
    public boolean evokerBypassMobGriefing = false;
    public boolean evokerTakeDamageFromWater = false;
    private void evokerSettings() {
        evokerRidable = getBoolean("mobs.evoker.ridable", evokerRidable);
        evokerRidableInWater = getBoolean("mobs.evoker.ridable-in-water", evokerRidableInWater);
        evokerControllable = getBoolean("mobs.evoker.controllable", evokerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.evoker.attributes.max-health", evokerMaxHealth);
            set("mobs.evoker.attributes.max-health", null);
            set("mobs.evoker.attributes.max_health", oldValue);
        }
        evokerMaxHealth = getDouble("mobs.evoker.attributes.max_health", evokerMaxHealth);
        evokerScale = Mth.clamp(getDouble("mobs.evoker.attributes.scale", evokerScale), 0.0625D, 16.0D);
        evokerBypassMobGriefing = getBoolean("mobs.evoker.bypass-mob-griefing", evokerBypassMobGriefing);
        evokerTakeDamageFromWater = getBoolean("mobs.evoker.takes-damage-from-water", evokerTakeDamageFromWater);
    }

    public boolean foxRidable = false;
    public boolean foxRidableInWater = true;
    public boolean foxControllable = true;
    public double foxMaxHealth = 10.0D;
    public double foxScale = 1.0D;
    public boolean foxTypeChangesWithTulips = false;
    public int foxBreedingTicks = 6000;
    public boolean foxBypassMobGriefing = false;
    public boolean foxTakeDamageFromWater = false;
    private void foxSettings() {
        foxRidable = getBoolean("mobs.fox.ridable", foxRidable);
        foxRidableInWater = getBoolean("mobs.fox.ridable-in-water", foxRidableInWater);
        foxControllable = getBoolean("mobs.fox.controllable", foxControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.fox.attributes.max-health", foxMaxHealth);
            set("mobs.fox.attributes.max-health", null);
            set("mobs.fox.attributes.max_health", oldValue);
        }
        foxMaxHealth = getDouble("mobs.fox.attributes.max_health", foxMaxHealth);
        foxScale = Mth.clamp(getDouble("mobs.fox.attributes.scale", foxScale), 0.0625D, 16.0D);
        foxTypeChangesWithTulips = getBoolean("mobs.fox.tulips-change-type", foxTypeChangesWithTulips);
        foxBreedingTicks = getInt("mobs.fox.breeding-delay-ticks", foxBreedingTicks);
        foxBypassMobGriefing = getBoolean("mobs.fox.bypass-mob-griefing", foxBypassMobGriefing);
        foxTakeDamageFromWater = getBoolean("mobs.fox.takes-damage-from-water", foxTakeDamageFromWater);
    }

    public boolean frogRidable = false;
    public boolean frogRidableInWater = true;
    public boolean frogControllable = true;
    public float frogRidableJumpHeight = 0.65F;
    public int frogBreedingTicks = 6000;
    private void frogSettings() {
        frogRidable = getBoolean("mobs.frog.ridable", frogRidable);
        frogRidableInWater = getBoolean("mobs.frog.ridable-in-water", frogRidableInWater);
        frogControllable = getBoolean("mobs.frog.controllable", frogControllable);
        frogRidableJumpHeight = (float) getDouble("mobs.frog.ridable-jump-height", frogRidableJumpHeight);
        frogBreedingTicks = getInt("mobs.frog.breeding-delay-ticks", frogBreedingTicks);
    }

    public boolean ghastRidable = false;
    public boolean ghastRidableInWater = true;
    public boolean ghastControllable = true;
    public double ghastMaxY = 320D;
    public double ghastMaxHealth = 10.0D;
    public double ghastScale = 1.0D;
    public boolean ghastTakeDamageFromWater = false;
    private void ghastSettings() {
        ghastRidable = getBoolean("mobs.ghast.ridable", ghastRidable);
        ghastRidableInWater = getBoolean("mobs.ghast.ridable-in-water", ghastRidableInWater);
        ghastControllable = getBoolean("mobs.ghast.controllable", ghastControllable);
        ghastMaxY = getDouble("mobs.ghast.ridable-max-y", ghastMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ghast.attributes.max-health", ghastMaxHealth);
            set("mobs.ghast.attributes.max-health", null);
            set("mobs.ghast.attributes.max_health", oldValue);
        }
        ghastMaxHealth = getDouble("mobs.ghast.attributes.max_health", ghastMaxHealth);
        ghastScale = Mth.clamp(getDouble("mobs.ghast.attributes.scale", ghastScale), 0.0625D, 16.0D);
        ghastTakeDamageFromWater = getBoolean("mobs.ghast.takes-damage-from-water", ghastTakeDamageFromWater);
    }

    public boolean giantRidable = false;
    public boolean giantRidableInWater = true;
    public boolean giantControllable = true;
    public double giantMovementSpeed = 0.5D;
    public double giantAttackDamage = 50.0D;
    public double giantMaxHealth = 100.0D;
    public double giantScale = 1.0D;
    public float giantStepHeight = 2.0F;
    public float giantJumpHeight = 1.0F;
    public boolean giantHaveAI = false;
    public boolean giantHaveHostileAI = false;
    public boolean giantTakeDamageFromWater = false;
    private void giantSettings() {
        giantRidable = getBoolean("mobs.giant.ridable", giantRidable);
        giantRidableInWater = getBoolean("mobs.giant.ridable-in-water", giantRidableInWater);
        giantControllable = getBoolean("mobs.giant.controllable", giantControllable);
        giantMovementSpeed = getDouble("mobs.giant.movement-speed", giantMovementSpeed);
        giantAttackDamage = getDouble("mobs.giant.attack-damage", giantAttackDamage);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.giant.max-health", giantMaxHealth);
            set("mobs.giant.max-health", null);
            set("mobs.giant.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.giant.attributes.max-health", giantMaxHealth);
            set("mobs.giant.attributes.max-health", null);
            set("mobs.giant.attributes.max_health", oldValue);
        }
        giantMaxHealth = getDouble("mobs.giant.attributes.max_health", giantMaxHealth);
        giantScale = Mth.clamp(getDouble("mobs.giant.attributes.scale", giantScale), 0.0625D, 16.0D);
        giantStepHeight = (float) getDouble("mobs.giant.step-height", giantStepHeight);
        giantJumpHeight = (float) getDouble("mobs.giant.jump-height", giantJumpHeight);
        giantHaveAI = getBoolean("mobs.giant.have-ai", giantHaveAI);
        giantHaveHostileAI = getBoolean("mobs.giant.have-hostile-ai", giantHaveHostileAI);
        giantTakeDamageFromWater = getBoolean("mobs.giant.takes-damage-from-water", giantTakeDamageFromWater);
    }

    public boolean glowSquidRidable = false;
    public boolean glowSquidControllable = true;
    public double glowSquidMaxHealth = 10.0D;
    public double glowSquidScale = 1.0D;
    public boolean glowSquidsCanFly = false;
    public boolean glowSquidTakeDamageFromWater = false;
    private void glowSquidSettings() {
        glowSquidRidable = getBoolean("mobs.glow_squid.ridable", glowSquidRidable);
        glowSquidControllable = getBoolean("mobs.glow_squid.controllable", glowSquidControllable);
        glowSquidMaxHealth = getDouble("mobs.glow_squid.attributes.max_health", glowSquidMaxHealth);
        glowSquidScale = Mth.clamp(getDouble("mobs.glow_squid.attributes.scale", glowSquidScale), 0.0625D, 16.0D);
        glowSquidsCanFly = getBoolean("mobs.glow_squid.can-fly", glowSquidsCanFly);
        glowSquidTakeDamageFromWater = getBoolean("mobs.glow_squid.takes-damage-from-water", glowSquidTakeDamageFromWater);
    }

    public boolean goatRidable = false;
    public boolean goatRidableInWater = true;
    public boolean goatControllable = true;
    public double goatMaxHealth = 10.0D;
    public double goatScale = 1.0D;
    public int goatBreedingTicks = 6000;
    public boolean goatTakeDamageFromWater = false;
    private void goatSettings() {
        goatRidable = getBoolean("mobs.goat.ridable", goatRidable);
        goatRidableInWater = getBoolean("mobs.goat.ridable-in-water", goatRidableInWater);
        goatControllable = getBoolean("mobs.goat.controllable", goatControllable);
        goatMaxHealth = getDouble("mobs.goat.attributes.max_health", goatMaxHealth);
        goatScale = Mth.clamp(getDouble("mobs.goat.attributes.scale", goatScale), 0.0625D, 16.0D);
        goatBreedingTicks = getInt("mobs.goat.breeding-delay-ticks", goatBreedingTicks);
        goatTakeDamageFromWater = getBoolean("mobs.goat.takes-damage-from-water", goatTakeDamageFromWater);
    }

    public boolean guardianRidable = false;
    public boolean guardianControllable = true;
    public double guardianMaxHealth = 30.0D;
    public double guardianScale = 1.0D;
    public boolean guardianTakeDamageFromWater = false;
    private void guardianSettings() {
        guardianRidable = getBoolean("mobs.guardian.ridable", guardianRidable);
        guardianControllable = getBoolean("mobs.guardian.controllable", guardianControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.guardian.attributes.max-health", guardianMaxHealth);
            set("mobs.guardian.attributes.max-health", null);
            set("mobs.guardian.attributes.max_health", oldValue);
        }
        guardianMaxHealth = getDouble("mobs.guardian.attributes.max_health", guardianMaxHealth);
        guardianScale = Mth.clamp(getDouble("mobs.guardian.attributes.scale", guardianScale), 0.0625D, 16.0D);
        guardianTakeDamageFromWater = getBoolean("mobs.guardian.takes-damage-from-water", guardianTakeDamageFromWater);
    }

    public boolean hoglinRidable = false;
    public boolean hoglinRidableInWater = true;
    public boolean hoglinControllable = true;
    public double hoglinMaxHealth = 40.0D;
    public double hoglinScale = 1.0D;
    public int hoglinBreedingTicks = 6000;
    public boolean hoglinTakeDamageFromWater = false;
    private void hoglinSettings() {
        hoglinRidable = getBoolean("mobs.hoglin.ridable", hoglinRidable);
        hoglinRidableInWater = getBoolean("mobs.hoglin.ridable-in-water", hoglinRidableInWater);
        hoglinControllable = getBoolean("mobs.hoglin.controllable", hoglinControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.hoglin.attributes.max-health", hoglinMaxHealth);
            set("mobs.hoglin.attributes.max-health", null);
            set("mobs.hoglin.attributes.max_health", oldValue);
        }
        hoglinMaxHealth = getDouble("mobs.hoglin.attributes.max_health", hoglinMaxHealth);
        hoglinScale = Mth.clamp(getDouble("mobs.hoglin.attributes.scale", hoglinScale), 0.0625D, 16.0D);
        hoglinBreedingTicks = getInt("mobs.hoglin.breeding-delay-ticks", hoglinBreedingTicks);
        hoglinTakeDamageFromWater = getBoolean("mobs.hoglin.takes-damage-from-water", hoglinTakeDamageFromWater);
    }

    public boolean horseRidableInWater = false;
    public double horseMaxHealthMin = 15.0D;
    public double horseMaxHealthMax = 30.0D;
    public double horseJumpStrengthMin = 0.4D;
    public double horseJumpStrengthMax = 1.0D;
    public double horseMovementSpeedMin = 0.1125D;
    public double horseMovementSpeedMax = 0.3375D;
    public int horseBreedingTicks = 6000;
    public boolean horseTakeDamageFromWater = false;
    private void horseSettings() {
        horseRidableInWater = getBoolean("mobs.horse.ridable-in-water", horseRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.horse.attributes.max-health.min", horseMaxHealthMin);
            double oldMax = getDouble("mobs.horse.attributes.max-health.max", horseMaxHealthMax);
            set("mobs.horse.attributes.max-health", null);
            set("mobs.horse.attributes.max_health.min", oldMin);
            set("mobs.horse.attributes.max_health.max", oldMax);
        }
        horseMaxHealthMin = getDouble("mobs.horse.attributes.max_health.min", horseMaxHealthMin);
        horseMaxHealthMax = getDouble("mobs.horse.attributes.max_health.max", horseMaxHealthMax);
        horseJumpStrengthMin = getDouble("mobs.horse.attributes.jump_strength.min", horseJumpStrengthMin);
        horseJumpStrengthMax = getDouble("mobs.horse.attributes.jump_strength.max", horseJumpStrengthMax);
        horseMovementSpeedMin = getDouble("mobs.horse.attributes.movement_speed.min", horseMovementSpeedMin);
        horseMovementSpeedMax = getDouble("mobs.horse.attributes.movement_speed.max", horseMovementSpeedMax);
        horseBreedingTicks = getInt("mobs.horse.breeding-delay-ticks", horseBreedingTicks);
        horseTakeDamageFromWater = getBoolean("mobs.horse.takes-damage-from-water", horseTakeDamageFromWater);
    }

    public boolean huskRidable = false;
    public boolean huskRidableInWater = true;
    public boolean huskControllable = true;
    public double huskMaxHealth = 20.0D;
    public double huskScale = 1.0D;
    public double huskSpawnReinforcements = 0.1D;
    public boolean huskJockeyOnlyBaby = true;
    public double huskJockeyChance = 0.05D;
    public boolean huskJockeyTryExistingChickens = true;
    public boolean huskTakeDamageFromWater = false;
    private void huskSettings() {
        huskRidable = getBoolean("mobs.husk.ridable", huskRidable);
        huskRidableInWater = getBoolean("mobs.husk.ridable-in-water", huskRidableInWater);
        huskControllable = getBoolean("mobs.husk.controllable", huskControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.husk.attributes.max-health", huskMaxHealth);
            set("mobs.husk.attributes.max-health", null);
            set("mobs.husk.attributes.max_health", oldValue);
        }
        huskMaxHealth = getDouble("mobs.husk.attributes.max_health", huskMaxHealth);
        huskScale = Mth.clamp(getDouble("mobs.husk.attributes.scale", huskScale), 0.0625D, 16.0D);
        huskSpawnReinforcements = getDouble("mobs.husk.attributes.spawn_reinforcements", huskSpawnReinforcements);
        huskJockeyOnlyBaby = getBoolean("mobs.husk.jockey.only-babies", huskJockeyOnlyBaby);
        huskJockeyChance = getDouble("mobs.husk.jockey.chance", huskJockeyChance);
        huskJockeyTryExistingChickens = getBoolean("mobs.husk.jockey.try-existing-chickens", huskJockeyTryExistingChickens);
        huskTakeDamageFromWater = getBoolean("mobs.husk.takes-damage-from-water", huskTakeDamageFromWater);
    }

    public boolean illusionerRidable = false;
    public boolean illusionerRidableInWater = true;
    public boolean illusionerControllable = true;
    public double illusionerMovementSpeed = 0.5D;
    public double illusionerFollowRange = 18.0D;
    public double illusionerMaxHealth = 32.0D;
    public double illusionerScale = 1.0D;
    public boolean illusionerTakeDamageFromWater = false;
    private void illusionerSettings() {
        illusionerRidable = getBoolean("mobs.illusioner.ridable", illusionerRidable);
        illusionerRidableInWater = getBoolean("mobs.illusioner.ridable-in-water", illusionerRidableInWater);
        illusionerControllable = getBoolean("mobs.illusioner.controllable", illusionerControllable);
        illusionerMovementSpeed = getDouble("mobs.illusioner.movement-speed", illusionerMovementSpeed);
        illusionerFollowRange = getDouble("mobs.illusioner.follow-range", illusionerFollowRange);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.illusioner.max-health", illusionerMaxHealth);
            set("mobs.illusioner.max-health", null);
            set("mobs.illusioner.attributes.max_health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.illusioner.attributes.max-health", illusionerMaxHealth);
            set("mobs.illusioner.attributes.max-health", null);
            set("mobs.illusioner.attributes.max_health", oldValue);
        }
        illusionerMaxHealth = getDouble("mobs.illusioner.attributes.max_health", illusionerMaxHealth);
        illusionerScale = Mth.clamp(getDouble("mobs.illusioner.attributes.scale", illusionerScale), 0.0625D, 16.0D);
        illusionerTakeDamageFromWater = getBoolean("mobs.illusioner.takes-damage-from-water", illusionerTakeDamageFromWater);
    }

    public boolean ironGolemRidable = false;
    public boolean ironGolemRidableInWater = true;
    public boolean ironGolemControllable = true;
    public boolean ironGolemCanSwim = false;
    public double ironGolemMaxHealth = 100.0D;
    public double ironGolemScale = 1.0D;
    public boolean ironGolemTakeDamageFromWater = false;
    public boolean ironGolemPoppyCalm = false;
    public boolean ironGolemHealCalm = false;
    private void ironGolemSettings() {
        ironGolemRidable = getBoolean("mobs.iron_golem.ridable", ironGolemRidable);
        ironGolemRidableInWater = getBoolean("mobs.iron_golem.ridable-in-water", ironGolemRidableInWater);
        ironGolemControllable = getBoolean("mobs.iron_golem.controllable", ironGolemControllable);
        ironGolemCanSwim = getBoolean("mobs.iron_golem.can-swim", ironGolemCanSwim);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.iron_golem.attributes.max-health", ironGolemMaxHealth);
            set("mobs.iron_golem.attributes.max-health", null);
            set("mobs.iron_golem.attributes.max_health", oldValue);
        }
        ironGolemMaxHealth = getDouble("mobs.iron_golem.attributes.max_health", ironGolemMaxHealth);
        ironGolemScale = Mth.clamp(getDouble("mobs.iron_golem.attributes.scale", ironGolemScale), 0.0625D, 16.0D);
        ironGolemTakeDamageFromWater = getBoolean("mobs.iron_golem.takes-damage-from-water", ironGolemTakeDamageFromWater);
        ironGolemPoppyCalm = getBoolean("mobs.iron_golem.poppy-calms-anger", ironGolemPoppyCalm);
        ironGolemHealCalm = getBoolean("mobs.iron_golem.healing-calms-anger", ironGolemHealCalm);
    }

    public boolean llamaRidable = false;
    public boolean llamaRidableInWater = false;
    public boolean llamaControllable = true;
    public double llamaMaxHealthMin = 15.0D;
    public double llamaMaxHealthMax = 30.0D;
    public double llamaJumpStrengthMin = 0.5D;
    public double llamaJumpStrengthMax = 0.5D;
    public double llamaMovementSpeedMin = 0.175D;
    public double llamaMovementSpeedMax = 0.175D;
    public int llamaBreedingTicks = 6000;
    public boolean llamaTakeDamageFromWater = false;
    public boolean llamaJoinCaravans = true;
    private void llamaSettings() {
        llamaRidable = getBoolean("mobs.llama.ridable", llamaRidable);
        llamaRidableInWater = getBoolean("mobs.llama.ridable-in-water", llamaRidableInWater);
        llamaControllable = getBoolean("mobs.llama.controllable", llamaControllable);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.llama.attributes.max-health.min", llamaMaxHealthMin);
            double oldMax = getDouble("mobs.llama.attributes.max-health.max", llamaMaxHealthMax);
            set("mobs.llama.attributes.max-health", null);
            set("mobs.llama.attributes.max_health.min", oldMin);
            set("mobs.llama.attributes.max_health.max", oldMax);
        }
        llamaMaxHealthMin = getDouble("mobs.llama.attributes.max_health.min", llamaMaxHealthMin);
        llamaMaxHealthMax = getDouble("mobs.llama.attributes.max_health.max", llamaMaxHealthMax);
        llamaJumpStrengthMin = getDouble("mobs.llama.attributes.jump_strength.min", llamaJumpStrengthMin);
        llamaJumpStrengthMax = getDouble("mobs.llama.attributes.jump_strength.max", llamaJumpStrengthMax);
        llamaMovementSpeedMin = getDouble("mobs.llama.attributes.movement_speed.min", llamaMovementSpeedMin);
        llamaMovementSpeedMax = getDouble("mobs.llama.attributes.movement_speed.max", llamaMovementSpeedMax);
        llamaBreedingTicks = getInt("mobs.llama.breeding-delay-ticks", llamaBreedingTicks);
        llamaTakeDamageFromWater = getBoolean("mobs.llama.takes-damage-from-water", llamaTakeDamageFromWater);
        llamaJoinCaravans = getBoolean("mobs.llama.join-caravans", llamaJoinCaravans);
    }

    public boolean magmaCubeRidable = false;
    public boolean magmaCubeRidableInWater = true;
    public boolean magmaCubeControllable = true;
    public String magmaCubeMaxHealth = "size * size";
    public String magmaCubeAttackDamage = "size";
    public Map<Integer, Double> magmaCubeMaxHealthCache = new HashMap<>();
    public Map<Integer, Double> magmaCubeAttackDamageCache = new HashMap<>();
    public boolean magmaCubeTakeDamageFromWater = false;
    private void magmaCubeSettings() {
        magmaCubeRidable = getBoolean("mobs.magma_cube.ridable", magmaCubeRidable);
        magmaCubeRidableInWater = getBoolean("mobs.magma_cube.ridable-in-water", magmaCubeRidableInWater);
        magmaCubeControllable = getBoolean("mobs.magma_cube.controllable", magmaCubeControllable);
        if (PurpurConfig.version < 10) {
            String oldValue = getString("mobs.magma_cube.attributes.max-health", magmaCubeMaxHealth);
            set("mobs.magma_cube.attributes.max-health", null);
            set("mobs.magma_cube.attributes.max_health", oldValue);
        }
        magmaCubeMaxHealth = getString("mobs.magma_cube.attributes.max_health", magmaCubeMaxHealth);
        magmaCubeAttackDamage = getString("mobs.magma_cube.attributes.attack_damage", magmaCubeAttackDamage);
        magmaCubeMaxHealthCache.clear();
        magmaCubeAttackDamageCache.clear();
        magmaCubeTakeDamageFromWater = getBoolean("mobs.magma_cube.takes-damage-from-water", magmaCubeTakeDamageFromWater);
    }

    public boolean mooshroomRidable = false;
    public boolean mooshroomRidableInWater = true;
    public boolean mooshroomControllable = true;
    public double mooshroomMaxHealth = 10.0D;
    public double mooshroomScale = 1.0D;
    public int mooshroomBreedingTicks = 6000;
    public boolean mooshroomTakeDamageFromWater = false;
    private void mooshroomSettings() {
        mooshroomRidable = getBoolean("mobs.mooshroom.ridable", mooshroomRidable);
        mooshroomRidableInWater = getBoolean("mobs.mooshroom.ridable-in-water", mooshroomRidableInWater);
        mooshroomControllable = getBoolean("mobs.mooshroom.controllable", mooshroomControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.mooshroom.attributes.max-health", mooshroomMaxHealth);
            set("mobs.mooshroom.attributes.max-health", null);
            set("mobs.mooshroom.attributes.max_health", oldValue);
        }
        mooshroomMaxHealth = getDouble("mobs.mooshroom.attributes.max_health", mooshroomMaxHealth);
        mooshroomScale = Mth.clamp(getDouble("mobs.mooshroom.attributes.scale", mooshroomScale), 0.0625D, 16.0D);
        mooshroomBreedingTicks = getInt("mobs.mooshroom.breeding-delay-ticks", mooshroomBreedingTicks);
        mooshroomTakeDamageFromWater = getBoolean("mobs.mooshroom.takes-damage-from-water", mooshroomTakeDamageFromWater);
    }

    public boolean muleRidableInWater = false;
    public double muleMaxHealthMin = 15.0D;
    public double muleMaxHealthMax = 30.0D;
    public double muleJumpStrengthMin = 0.5D;
    public double muleJumpStrengthMax = 0.5D;
    public double muleMovementSpeedMin = 0.175D;
    public double muleMovementSpeedMax = 0.175D;
    public int muleBreedingTicks = 6000;
    public boolean muleTakeDamageFromWater = false;
    private void muleSettings() {
        muleRidableInWater = getBoolean("mobs.mule.ridable-in-water", muleRidableInWater);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.mule.attributes.max-health.min", muleMaxHealthMin);
            double oldMax = getDouble("mobs.mule.attributes.max-health.max", muleMaxHealthMax);
            set("mobs.mule.attributes.max-health", null);
            set("mobs.mule.attributes.max_health.min", oldMin);
            set("mobs.mule.attributes.max_health.max", oldMax);
        }
        muleMaxHealthMin = getDouble("mobs.mule.attributes.max_health.min", muleMaxHealthMin);
        muleMaxHealthMax = getDouble("mobs.mule.attributes.max_health.max", muleMaxHealthMax);
        muleJumpStrengthMin = getDouble("mobs.mule.attributes.jump_strength.min", muleJumpStrengthMin);
        muleJumpStrengthMax = getDouble("mobs.mule.attributes.jump_strength.max", muleJumpStrengthMax);
        muleMovementSpeedMin = getDouble("mobs.mule.attributes.movement_speed.min", muleMovementSpeedMin);
        muleMovementSpeedMax = getDouble("mobs.mule.attributes.movement_speed.max", muleMovementSpeedMax);
        muleBreedingTicks = getInt("mobs.mule.breeding-delay-ticks", muleBreedingTicks);
        muleTakeDamageFromWater = getBoolean("mobs.mule.takes-damage-from-water", muleTakeDamageFromWater);
    }

    public boolean ocelotRidable = false;
    public boolean ocelotRidableInWater = true;
    public boolean ocelotControllable = true;
    public double ocelotMaxHealth = 10.0D;
    public double ocelotScale = 1.0D;
    public int ocelotBreedingTicks = 6000;
    public boolean ocelotTakeDamageFromWater = false;
    private void ocelotSettings() {
        ocelotRidable = getBoolean("mobs.ocelot.ridable", ocelotRidable);
        ocelotRidableInWater = getBoolean("mobs.ocelot.ridable-in-water", ocelotRidableInWater);
        ocelotControllable = getBoolean("mobs.ocelot.controllable", ocelotControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ocelot.attributes.max-health", ocelotMaxHealth);
            set("mobs.ocelot.attributes.max-health", null);
            set("mobs.ocelot.attributes.max_health", oldValue);
        }
        ocelotMaxHealth = getDouble("mobs.ocelot.attributes.max_health", ocelotMaxHealth);
        ocelotScale = Mth.clamp(getDouble("mobs.ocelot.attributes.scale", ocelotScale), 0.0625D, 16.0D);
        ocelotBreedingTicks = getInt("mobs.ocelot.breeding-delay-ticks", ocelotBreedingTicks);
        ocelotTakeDamageFromWater = getBoolean("mobs.ocelot.takes-damage-from-water", ocelotTakeDamageFromWater);
    }

    public boolean pandaRidable = false;
    public boolean pandaRidableInWater = true;
    public boolean pandaControllable = true;
    public double pandaMaxHealth = 20.0D;
    public double pandaScale = 1.0D;
    public int pandaBreedingTicks = 6000;
    public boolean pandaTakeDamageFromWater = false;
    private void pandaSettings() {
        pandaRidable = getBoolean("mobs.panda.ridable", pandaRidable);
        pandaRidableInWater = getBoolean("mobs.panda.ridable-in-water", pandaRidableInWater);
        pandaControllable = getBoolean("mobs.panda.controllable", pandaControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.panda.attributes.max-health", pandaMaxHealth);
            set("mobs.panda.attributes.max-health", null);
            set("mobs.panda.attributes.max_health", oldValue);
        }
        pandaMaxHealth = getDouble("mobs.panda.attributes.max_health", pandaMaxHealth);
        pandaScale = Mth.clamp(getDouble("mobs.panda.attributes.scale", pandaScale), 0.0625D, 16.0D);
        pandaBreedingTicks = getInt("mobs.panda.breeding-delay-ticks", pandaBreedingTicks);
        pandaTakeDamageFromWater = getBoolean("mobs.panda.takes-damage-from-water", pandaTakeDamageFromWater);
    }

    public boolean parrotRidable = false;
    public boolean parrotRidableInWater = true;
    public boolean parrotControllable = true;
    public double parrotMaxY = 320D;
    public double parrotMaxHealth = 6.0D;
    public double parrotScale = 1.0D;
    public boolean parrotTakeDamageFromWater = false;
    public boolean parrotBreedable = false;
    private void parrotSettings() {
        parrotRidable = getBoolean("mobs.parrot.ridable", parrotRidable);
        parrotRidableInWater = getBoolean("mobs.parrot.ridable-in-water", parrotRidableInWater);
        parrotControllable = getBoolean("mobs.parrot.controllable", parrotControllable);
        parrotMaxY = getDouble("mobs.parrot.ridable-max-y", parrotMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.parrot.attributes.max-health", parrotMaxHealth);
            set("mobs.parrot.attributes.max-health", null);
            set("mobs.parrot.attributes.max_health", oldValue);
        }
        parrotMaxHealth = getDouble("mobs.parrot.attributes.max_health", parrotMaxHealth);
        parrotScale = Mth.clamp(getDouble("mobs.parrot.attributes.scale", parrotScale), 0.0625D, 16.0D);
        parrotTakeDamageFromWater = getBoolean("mobs.parrot.takes-damage-from-water", parrotTakeDamageFromWater);
        parrotBreedable = getBoolean("mobs.parrot.can-breed", parrotBreedable);
    }

    public boolean phantomRidable = false;
    public boolean phantomRidableInWater = true;
    public boolean phantomControllable = true;
    public double phantomMaxY = 320D;
    public float phantomFlameDamage = 1.0F;
    public int phantomFlameFireTime = 8;
    public boolean phantomAllowGriefing = false;
    public String phantomMaxHealth = "20.0";
    public String phantomAttackDamage = "6 + size";
    public Map<Integer, Double> phantomMaxHealthCache = new HashMap<>();
    public Map<Integer, Double> phantomAttackDamageCache = new HashMap<>();
    public double phantomAttackedByCrystalRadius = 0.0D;
    public float phantomAttackedByCrystalDamage = 1.0F;
    public double phantomOrbitCrystalRadius = 0.0D;
    public int phantomSpawnMinSkyDarkness = 5;
    public boolean phantomSpawnOnlyAboveSeaLevel = true;
    public boolean phantomSpawnOnlyWithVisibleSky = true;
    public double phantomSpawnLocalDifficultyChance = 3.0D;
    public int phantomSpawnMinPerAttempt = 1;
    public int phantomSpawnMaxPerAttempt = -1;
    public int phantomBurnInLight = 0;
    public boolean phantomIgnorePlayersWithTorch = false;
    public boolean phantomBurnInDaylight = true;
    public boolean phantomFlamesOnSwoop = false;
    public boolean phantomTakeDamageFromWater = false;
    private void phantomSettings() {
        phantomRidable = getBoolean("mobs.phantom.ridable", phantomRidable);
        phantomRidableInWater = getBoolean("mobs.phantom.ridable-in-water", phantomRidableInWater);
        phantomControllable = getBoolean("mobs.phantom.controllable", phantomControllable);
        phantomMaxY = getDouble("mobs.phantom.ridable-max-y", phantomMaxY);
        phantomFlameDamage = (float) getDouble("mobs.phantom.flames.damage", phantomFlameDamage);
        phantomFlameFireTime = getInt("mobs.phantom.flames.fire-time", phantomFlameFireTime);
        phantomAllowGriefing = getBoolean("mobs.phantom.allow-griefing", phantomAllowGriefing);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.phantom.attributes.max-health", Double.parseDouble(phantomMaxHealth));
            set("mobs.phantom.attributes.max-health", null);
            set("mobs.phantom.attributes.max_health", String.valueOf(oldValue));
        }
        if (PurpurConfig.version < 25) {
            double oldValue = getDouble("mobs.phantom.attributes.max_health", Double.parseDouble(phantomMaxHealth));
            set("mobs.phantom.attributes.max_health", String.valueOf(oldValue));
        }
        phantomMaxHealth = getString("mobs.phantom.attributes.max_health", phantomMaxHealth);
        phantomAttackDamage = getString("mobs.phantom.attributes.attack_damage", phantomAttackDamage);
        phantomMaxHealthCache.clear();
        phantomAttackDamageCache.clear();
        phantomAttackedByCrystalRadius = getDouble("mobs.phantom.attacked-by-crystal-range", phantomAttackedByCrystalRadius);
        phantomAttackedByCrystalDamage = (float) getDouble("mobs.phantom.attacked-by-crystal-damage", phantomAttackedByCrystalDamage);
        phantomOrbitCrystalRadius = getDouble("mobs.phantom.orbit-crystal-radius", phantomOrbitCrystalRadius);
        phantomSpawnMinSkyDarkness = getInt("mobs.phantom.spawn.min-sky-darkness", phantomSpawnMinSkyDarkness);
        phantomSpawnOnlyAboveSeaLevel = getBoolean("mobs.phantom.spawn.only-above-sea-level", phantomSpawnOnlyAboveSeaLevel);
        phantomSpawnOnlyWithVisibleSky = getBoolean("mobs.phantom.spawn.only-with-visible-sky", phantomSpawnOnlyWithVisibleSky);
        phantomSpawnLocalDifficultyChance = getDouble("mobs.phantom.spawn.local-difficulty-chance", phantomSpawnLocalDifficultyChance);
        phantomSpawnMinPerAttempt = getInt("mobs.phantom.spawn.per-attempt.min", phantomSpawnMinPerAttempt);
        phantomSpawnMaxPerAttempt = getInt("mobs.phantom.spawn.per-attempt.max", phantomSpawnMaxPerAttempt);
        phantomBurnInLight = getInt("mobs.phantom.burn-in-light", phantomBurnInLight);
        phantomBurnInDaylight = getBoolean("mobs.phantom.burn-in-daylight", phantomBurnInDaylight);
        phantomIgnorePlayersWithTorch = getBoolean("mobs.phantom.ignore-players-with-torch", phantomIgnorePlayersWithTorch);
        phantomFlamesOnSwoop = getBoolean("mobs.phantom.flames-on-swoop", phantomFlamesOnSwoop);
        phantomTakeDamageFromWater = getBoolean("mobs.phantom.takes-damage-from-water", phantomTakeDamageFromWater);
    }

    public boolean pigRidable = false;
    public boolean pigRidableInWater = false;
    public boolean pigControllable = true;
    public double pigMaxHealth = 10.0D;
    public double pigScale = 1.0D;
    public boolean pigGiveSaddleBack = false;
    public int pigBreedingTicks = 6000;
    public boolean pigTakeDamageFromWater = false;
    private void pigSettings() {
        pigRidable = getBoolean("mobs.pig.ridable", pigRidable);
        pigRidableInWater = getBoolean("mobs.pig.ridable-in-water", pigRidableInWater);
        pigControllable = getBoolean("mobs.pig.controllable", pigControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pig.attributes.max-health", pigMaxHealth);
            set("mobs.pig.attributes.max-health", null);
            set("mobs.pig.attributes.max_health", oldValue);
        }
        pigMaxHealth = getDouble("mobs.pig.attributes.max_health", pigMaxHealth);
        pigScale = Mth.clamp(getDouble("mobs.pig.attributes.scale", pigScale), 0.0625D, 16.0D);
        pigGiveSaddleBack = getBoolean("mobs.pig.give-saddle-back", pigGiveSaddleBack);
        pigBreedingTicks = getInt("mobs.pig.breeding-delay-ticks", pigBreedingTicks);
        pigTakeDamageFromWater = getBoolean("mobs.pig.takes-damage-from-water", pigTakeDamageFromWater);
    }

    public boolean piglinRidable = false;
    public boolean piglinRidableInWater = true;
    public boolean piglinControllable = true;
    public double piglinMaxHealth = 16.0D;
    public double piglinScale = 1.0D;
    public boolean piglinBypassMobGriefing = false;
    public boolean piglinTakeDamageFromWater = false;
    public int piglinPortalSpawnModifier = 2000;
    private void piglinSettings() {
        piglinRidable = getBoolean("mobs.piglin.ridable", piglinRidable);
        piglinRidableInWater = getBoolean("mobs.piglin.ridable-in-water", piglinRidableInWater);
        piglinControllable = getBoolean("mobs.piglin.controllable", piglinControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.piglin.attributes.max-health", piglinMaxHealth);
            set("mobs.piglin.attributes.max-health", null);
            set("mobs.piglin.attributes.max_health", oldValue);
        }
        piglinMaxHealth = getDouble("mobs.piglin.attributes.max_health", piglinMaxHealth);
        piglinScale = Mth.clamp(getDouble("mobs.piglin.attributes.scale", piglinScale), 0.0625D, 16.0D);
        piglinBypassMobGriefing = getBoolean("mobs.piglin.bypass-mob-griefing", piglinBypassMobGriefing);
        piglinTakeDamageFromWater = getBoolean("mobs.piglin.takes-damage-from-water", piglinTakeDamageFromWater);
        piglinPortalSpawnModifier = getInt("mobs.piglin.portal-spawn-modifier", piglinPortalSpawnModifier);
    }

    public boolean piglinBruteRidable = false;
    public boolean piglinBruteRidableInWater = true;
    public boolean piglinBruteControllable = true;
    public double piglinBruteMaxHealth = 50.0D;
    public double piglinBruteScale = 1.0D;
    public boolean piglinBruteTakeDamageFromWater = false;
    private void piglinBruteSettings() {
        piglinBruteRidable = getBoolean("mobs.piglin_brute.ridable", piglinBruteRidable);
        piglinBruteRidableInWater = getBoolean("mobs.piglin_brute.ridable-in-water", piglinBruteRidableInWater);
        piglinBruteControllable = getBoolean("mobs.piglin_brute.controllable", piglinBruteControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.piglin_brute.attributes.max-health", piglinBruteMaxHealth);
            set("mobs.piglin_brute.attributes.max-health", null);
            set("mobs.piglin_brute.attributes.max_health", oldValue);
        }
        piglinBruteMaxHealth = getDouble("mobs.piglin_brute.attributes.max_health", piglinBruteMaxHealth);
        piglinBruteScale = Mth.clamp(getDouble("mobs.piglin_brute.attributes.scale", piglinBruteScale), 0.0625D, 16.0D);
        piglinBruteTakeDamageFromWater = getBoolean("mobs.piglin_brute.takes-damage-from-water", piglinBruteTakeDamageFromWater);
    }

    public boolean pillagerRidable = false;
    public boolean pillagerRidableInWater = true;
    public boolean pillagerControllable = true;
    public double pillagerMaxHealth = 24.0D;
    public double pillagerScale = 1.0D;
    public boolean pillagerBypassMobGriefing = false;
    public boolean pillagerTakeDamageFromWater = false;
    private void pillagerSettings() {
        pillagerRidable = getBoolean("mobs.pillager.ridable", pillagerRidable);
        pillagerRidableInWater = getBoolean("mobs.pillager.ridable-in-water", pillagerRidableInWater);
        pillagerControllable = getBoolean("mobs.pillager.controllable", pillagerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pillager.attributes.max-health", pillagerMaxHealth);
            set("mobs.pillager.attributes.max-health", null);
            set("mobs.pillager.attributes.max_health", oldValue);
        }
        pillagerMaxHealth = getDouble("mobs.pillager.attributes.max_health", pillagerMaxHealth);
        pillagerScale = Mth.clamp(getDouble("mobs.pillager.attributes.scale", pillagerScale), 0.0625D, 16.0D);
        pillagerBypassMobGriefing = getBoolean("mobs.pillager.bypass-mob-griefing", pillagerBypassMobGriefing);
        pillagerTakeDamageFromWater = getBoolean("mobs.pillager.takes-damage-from-water", pillagerTakeDamageFromWater);
    }

    public boolean polarBearRidable = false;
    public boolean polarBearRidableInWater = true;
    public boolean polarBearControllable = true;
    public double polarBearMaxHealth = 30.0D;
    public double polarBearScale = 1.0D;
    public String polarBearBreedableItemString = "";
    public Item polarBearBreedableItem = null;
    public int polarBearBreedingTicks = 6000;
    public boolean polarBearTakeDamageFromWater = false;
    private void polarBearSettings() {
        polarBearRidable = getBoolean("mobs.polar_bear.ridable", polarBearRidable);
        polarBearRidableInWater = getBoolean("mobs.polar_bear.ridable-in-water", polarBearRidableInWater);
        polarBearControllable = getBoolean("mobs.polar_bear.controllable", polarBearControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.polar_bear.attributes.max-health", polarBearMaxHealth);
            set("mobs.polar_bear.attributes.max-health", null);
            set("mobs.polar_bear.attributes.max_health", oldValue);
        }
        polarBearMaxHealth = getDouble("mobs.polar_bear.attributes.max_health", polarBearMaxHealth);
        polarBearScale = Mth.clamp(getDouble("mobs.polar_bear.attributes.scale", polarBearScale), 0.0625D, 16.0D);
        polarBearBreedableItemString = getString("mobs.polar_bear.breedable-item", polarBearBreedableItemString);
        Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(polarBearBreedableItemString));
        if (item != Items.AIR) polarBearBreedableItem = item;
        polarBearBreedingTicks = getInt("mobs.polar_bear.breeding-delay-ticks", polarBearBreedingTicks);
        polarBearTakeDamageFromWater = getBoolean("mobs.polar_bear.takes-damage-from-water", polarBearTakeDamageFromWater);
    }

    public boolean pufferfishRidable = false;
    public boolean pufferfishControllable = true;
    public double pufferfishMaxHealth = 3.0D;
    public double pufferfishScale = 1.0D;
    public boolean pufferfishTakeDamageFromWater = false;
    private void pufferfishSettings() {
        pufferfishRidable = getBoolean("mobs.pufferfish.ridable", pufferfishRidable);
        pufferfishControllable = getBoolean("mobs.pufferfish.controllable", pufferfishControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.pufferfish.attributes.max-health", pufferfishMaxHealth);
            set("mobs.pufferfish.attributes.max-health", null);
            set("mobs.pufferfish.attributes.max_health", oldValue);
        }
        pufferfishMaxHealth = getDouble("mobs.pufferfish.attributes.max_health", pufferfishMaxHealth);
        pufferfishScale = Mth.clamp(getDouble("mobs.pufferfish.attributes.scale", pufferfishScale), 0.0625D, 16.0D);
        pufferfishTakeDamageFromWater = getBoolean("mobs.pufferfish.takes-damage-from-water", pufferfishTakeDamageFromWater);
    }

    public boolean rabbitRidable = false;
    public boolean rabbitRidableInWater = true;
    public boolean rabbitControllable = true;
    public double rabbitMaxHealth = 3.0D;
    public double rabbitScale = 1.0D;
    public double rabbitNaturalToast = 0.0D;
    public double rabbitNaturalKiller = 0.0D;
    public int rabbitBreedingTicks = 6000;
    public boolean rabbitBypassMobGriefing = false;
    public boolean rabbitTakeDamageFromWater = false;
    private void rabbitSettings() {
        rabbitRidable = getBoolean("mobs.rabbit.ridable", rabbitRidable);
        rabbitRidableInWater = getBoolean("mobs.rabbit.ridable-in-water", rabbitRidableInWater);
        rabbitControllable = getBoolean("mobs.rabbit.controllable", rabbitControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.rabbit.attributes.max-health", rabbitMaxHealth);
            set("mobs.rabbit.attributes.max-health", null);
            set("mobs.rabbit.attributes.max_health", oldValue);
        }
        rabbitMaxHealth = getDouble("mobs.rabbit.attributes.max_health", rabbitMaxHealth);
        rabbitScale = Mth.clamp(getDouble("mobs.rabbit.attributes.scale", rabbitScale), 0.0625D, 16.0D);
        rabbitNaturalToast = getDouble("mobs.rabbit.spawn-toast-chance", rabbitNaturalToast);
        rabbitNaturalKiller = getDouble("mobs.rabbit.spawn-killer-rabbit-chance", rabbitNaturalKiller);
        rabbitBreedingTicks = getInt("mobs.rabbit.breeding-delay-ticks", rabbitBreedingTicks);
        rabbitBypassMobGriefing = getBoolean("mobs.rabbit.bypass-mob-griefing", rabbitBypassMobGriefing);
        rabbitTakeDamageFromWater = getBoolean("mobs.rabbit.takes-damage-from-water", rabbitTakeDamageFromWater);
    }

    public boolean ravagerRidable = false;
    public boolean ravagerRidableInWater = false;
    public boolean ravagerControllable = true;
    public double ravagerMaxHealth = 100.0D;
    public double ravagerScale = 1.0D;
    public boolean ravagerBypassMobGriefing = false;
    public boolean ravagerTakeDamageFromWater = false;
    public List<Block> ravagerGriefableBlocks = new ArrayList<>();
    private void ravagerSettings() {
        ravagerRidable = getBoolean("mobs.ravager.ridable", ravagerRidable);
        ravagerRidableInWater = getBoolean("mobs.ravager.ridable-in-water", ravagerRidableInWater);
        ravagerControllable = getBoolean("mobs.ravager.controllable", ravagerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.ravager.attributes.max-health", ravagerMaxHealth);
            set("mobs.ravager.attributes.max-health", null);
            set("mobs.ravager.attributes.max_health", oldValue);
        }
        ravagerMaxHealth = getDouble("mobs.ravager.attributes.max_health", ravagerMaxHealth);
        ravagerScale = Mth.clamp(getDouble("mobs.ravager.attributes.scale", ravagerScale), 0.0625D, 16.0D);
        ravagerBypassMobGriefing = getBoolean("mobs.ravager.bypass-mob-griefing", ravagerBypassMobGriefing);
        ravagerTakeDamageFromWater = getBoolean("mobs.ravager.takes-damage-from-water", ravagerTakeDamageFromWater);
        getList("mobs.ravager.griefable-blocks", new ArrayList<String>(){{
            add("minecraft:oak_leaves");
            add("minecraft:spruce_leaves");
            add("minecraft:birch_leaves");
            add("minecraft:jungle_leaves");
            add("minecraft:acacia_leaves");
            add("minecraft:dark_oak_leaves");
            add("minecraft:beetroots");
            add("minecraft:carrots");
            add("minecraft:potatoes");
            add("minecraft:wheat");
        }}).forEach(key -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(key.toString()));
            if (!block.defaultBlockState().isAir()) {
                ravagerGriefableBlocks.add(block);
            }
        });
    }

    public boolean salmonRidable = false;
    public boolean salmonControllable = true;
    public double salmonMaxHealth = 3.0D;
    public double salmonScale = 1.0D;
    public boolean salmonTakeDamageFromWater = false;
    private void salmonSettings() {
        salmonRidable = getBoolean("mobs.salmon.ridable", salmonRidable);
        salmonControllable = getBoolean("mobs.salmon.controllable", salmonControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.salmon.attributes.max-health", salmonMaxHealth);
            set("mobs.salmon.attributes.max-health", null);
            set("mobs.salmon.attributes.max_health", oldValue);
        }
        salmonMaxHealth = getDouble("mobs.salmon.attributes.max_health", salmonMaxHealth);
        salmonScale = Mth.clamp(getDouble("mobs.salmon.attributes.scale", salmonScale), 0.0625D, 16.0D);
        salmonTakeDamageFromWater = getBoolean("mobs.salmon.takes-damage-from-water", salmonTakeDamageFromWater);
    }

    public boolean sheepRidable = false;
    public boolean sheepRidableInWater = true;
    public boolean sheepControllable = true;
    public double sheepMaxHealth = 8.0D;
    public double sheepScale = 1.0D;
    public int sheepBreedingTicks = 6000;
    public boolean sheepBypassMobGriefing = false;
    public boolean sheepTakeDamageFromWater = false;
    private void sheepSettings() {
        sheepRidable = getBoolean("mobs.sheep.ridable", sheepRidable);
        sheepRidableInWater = getBoolean("mobs.sheep.ridable-in-water", sheepRidableInWater);
        sheepControllable = getBoolean("mobs.sheep.controllable", sheepControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.sheep.attributes.max-health", sheepMaxHealth);
            set("mobs.sheep.attributes.max-health", null);
            set("mobs.sheep.attributes.max_health", oldValue);
        }
        sheepMaxHealth = getDouble("mobs.sheep.attributes.max_health", sheepMaxHealth);
        sheepScale = Mth.clamp(getDouble("mobs.sheep.attributes.scale", sheepScale), 0.0625D, 16.0D);
        sheepBreedingTicks = getInt("mobs.sheep.breeding-delay-ticks", sheepBreedingTicks);
        sheepBypassMobGriefing = getBoolean("mobs.sheep.bypass-mob-griefing", sheepBypassMobGriefing);
        sheepTakeDamageFromWater = getBoolean("mobs.sheep.takes-damage-from-water", sheepTakeDamageFromWater);
    }

    public boolean shulkerRidable = false;
    public boolean shulkerRidableInWater = true;
    public boolean shulkerControllable = true;
    public double shulkerMaxHealth = 30.0D;
    public double shulkerScale = 1.0D;
    public boolean shulkerTakeDamageFromWater = false;
    public float shulkerSpawnFromBulletBaseChance = 1.0F;
    public boolean shulkerSpawnFromBulletRequireOpenLid = true;
    public double shulkerSpawnFromBulletNearbyRange = 8.0D;
    public String shulkerSpawnFromBulletNearbyEquation = "(nearby - 1) / 5.0";
    public boolean shulkerSpawnFromBulletRandomColor = false;
    public boolean shulkerChangeColorWithDye = false;
    private void shulkerSettings() {
        shulkerRidable = getBoolean("mobs.shulker.ridable", shulkerRidable);
        shulkerRidableInWater = getBoolean("mobs.shulker.ridable-in-water", shulkerRidableInWater);
        shulkerControllable = getBoolean("mobs.shulker.controllable", shulkerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.shulker.attributes.max-health", shulkerMaxHealth);
            set("mobs.shulker.attributes.max-health", null);
            set("mobs.shulker.attributes.max_health", oldValue);
        }
        shulkerMaxHealth = getDouble("mobs.shulker.attributes.max_health", shulkerMaxHealth);
        shulkerScale = Mth.clamp(getDouble("mobs.shulker.attributes.scale", shulkerScale), 0.0625D, Shulker.MAX_SCALE);
        shulkerTakeDamageFromWater = getBoolean("mobs.shulker.takes-damage-from-water", shulkerTakeDamageFromWater);
        shulkerSpawnFromBulletBaseChance = (float) getDouble("mobs.shulker.spawn-from-bullet.base-chance", shulkerSpawnFromBulletBaseChance);
        shulkerSpawnFromBulletRequireOpenLid = getBoolean("mobs.shulker.spawn-from-bullet.require-open-lid", shulkerSpawnFromBulletRequireOpenLid);
        shulkerSpawnFromBulletNearbyRange = getDouble("mobs.shulker.spawn-from-bullet.nearby-range", shulkerSpawnFromBulletNearbyRange);
        shulkerSpawnFromBulletNearbyEquation = getString("mobs.shulker.spawn-from-bullet.nearby-equation", shulkerSpawnFromBulletNearbyEquation);
        shulkerSpawnFromBulletRandomColor = getBoolean("mobs.shulker.spawn-from-bullet.random-color", shulkerSpawnFromBulletRandomColor);
        shulkerChangeColorWithDye = getBoolean("mobs.shulker.change-color-with-dye", shulkerChangeColorWithDye);
    }

    public boolean silverfishRidable = false;
    public boolean silverfishRidableInWater = true;
    public boolean silverfishControllable = true;
    public double silverfishMaxHealth = 8.0D;
    public double silverfishScale = 1.0D;
    public double silverfishMovementSpeed = 0.25D;
    public double silverfishAttackDamage = 1.0D;
    public boolean silverfishBypassMobGriefing = false;
    public boolean silverfishTakeDamageFromWater = false;
    private void silverfishSettings() {
        silverfishRidable = getBoolean("mobs.silverfish.ridable", silverfishRidable);
        silverfishRidableInWater = getBoolean("mobs.silverfish.ridable-in-water", silverfishRidableInWater);
        silverfishControllable = getBoolean("mobs.silverfish.controllable", silverfishControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.silverfish.attributes.max-health", silverfishMaxHealth);
            set("mobs.silverfish.attributes.max-health", null);
            set("mobs.silverfish.attributes.max_health", oldValue);
        }
        silverfishMaxHealth = getDouble("mobs.silverfish.attributes.max_health", silverfishMaxHealth);
        silverfishScale = Mth.clamp(getDouble("mobs.silverfish.attributes.scale", silverfishScale), 0.0625D, 16.0D);
        silverfishMovementSpeed = getDouble("mobs.silverfish.attributes.movement_speed", silverfishMovementSpeed);
        silverfishAttackDamage = getDouble("mobs.silverfish.attributes.attack_damage", silverfishAttackDamage);
        silverfishBypassMobGriefing = getBoolean("mobs.silverfish.bypass-mob-griefing", silverfishBypassMobGriefing);
        silverfishTakeDamageFromWater = getBoolean("mobs.silverfish.takes-damage-from-water", silverfishTakeDamageFromWater);
    }

    public boolean skeletonRidable = false;
    public boolean skeletonRidableInWater = true;
    public boolean skeletonControllable = true;
    public double skeletonMaxHealth = 20.0D;
    public double skeletonScale = 1.0D;
    public boolean skeletonTakeDamageFromWater = false;
    private void skeletonSettings() {
        skeletonRidable = getBoolean("mobs.skeleton.ridable", skeletonRidable);
        skeletonRidableInWater = getBoolean("mobs.skeleton.ridable-in-water", skeletonRidableInWater);
        skeletonControllable = getBoolean("mobs.skeleton.controllable", skeletonControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.skeleton.attributes.max-health", skeletonMaxHealth);
            set("mobs.skeleton.attributes.max-health", null);
            set("mobs.skeleton.attributes.max_health", oldValue);
        }
        skeletonMaxHealth = getDouble("mobs.skeleton.attributes.max_health", skeletonMaxHealth);
        skeletonScale = Mth.clamp(getDouble("mobs.skeleton.attributes.scale", skeletonScale), 0.0625D, 16.0D);
        skeletonTakeDamageFromWater = getBoolean("mobs.skeleton.takes-damage-from-water", skeletonTakeDamageFromWater);
    }

    public boolean skeletonHorseRidable = false;
    public boolean skeletonHorseRidableInWater = true;
    public boolean skeletonHorseCanSwim = false;
    public double skeletonHorseMaxHealthMin = 15.0D;
    public double skeletonHorseMaxHealthMax = 15.0D;
    public double skeletonHorseJumpStrengthMin = 0.4D;
    public double skeletonHorseJumpStrengthMax = 1.0D;
    public double skeletonHorseMovementSpeedMin = 0.2D;
    public double skeletonHorseMovementSpeedMax = 0.2D;
    public boolean skeletonHorseTakeDamageFromWater = false;
    private void skeletonHorseSettings() {
        skeletonHorseRidable = getBoolean("mobs.skeleton_horse.ridable", skeletonHorseRidable);
        skeletonHorseRidableInWater = getBoolean("mobs.skeleton_horse.ridable-in-water", skeletonHorseRidableInWater);
        skeletonHorseCanSwim = getBoolean("mobs.skeleton_horse.can-swim", skeletonHorseCanSwim);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.skeleton_horse.attributes.max-health", skeletonHorseMaxHealthMin);
            set("mobs.skeleton_horse.attributes.max-health", null);
            set("mobs.skeleton_horse.attributes.max_health.min", oldValue);
            set("mobs.skeleton_horse.attributes.max_health.max", oldValue);
        }
        skeletonHorseMaxHealthMin = getDouble("mobs.skeleton_horse.attributes.max_health.min", skeletonHorseMaxHealthMin);
        skeletonHorseMaxHealthMax = getDouble("mobs.skeleton_horse.attributes.max_health.max", skeletonHorseMaxHealthMax);
        skeletonHorseJumpStrengthMin = getDouble("mobs.skeleton_horse.attributes.jump_strength.min", skeletonHorseJumpStrengthMin);
        skeletonHorseJumpStrengthMax = getDouble("mobs.skeleton_horse.attributes.jump_strength.max", skeletonHorseJumpStrengthMax);
        skeletonHorseMovementSpeedMin = getDouble("mobs.skeleton_horse.attributes.movement_speed.min", skeletonHorseMovementSpeedMin);
        skeletonHorseMovementSpeedMax = getDouble("mobs.skeleton_horse.attributes.movement_speed.max", skeletonHorseMovementSpeedMax);
        skeletonHorseTakeDamageFromWater = getBoolean("mobs.skeleton_horse.takes-damage-from-water", skeletonHorseTakeDamageFromWater);
    }

    public boolean slimeRidable = false;
    public boolean slimeRidableInWater = true;
    public boolean slimeControllable = true;
    public String slimeMaxHealth = "size * size";
    public String slimeAttackDamage = "size";
    public Map<Integer, Double> slimeMaxHealthCache = new HashMap<>();
    public Map<Integer, Double> slimeAttackDamageCache = new HashMap<>();
    public boolean slimeTakeDamageFromWater = false;
    private void slimeSettings() {
        slimeRidable = getBoolean("mobs.slime.ridable", slimeRidable);
        slimeRidableInWater = getBoolean("mobs.slime.ridable-in-water", slimeRidableInWater);
        slimeControllable = getBoolean("mobs.slime.controllable", slimeControllable);
        if (PurpurConfig.version < 10) {
            String oldValue = getString("mobs.slime.attributes.max-health", slimeMaxHealth);
            set("mobs.slime.attributes.max-health", null);
            set("mobs.slime.attributes.max_health", oldValue);
        }
        slimeMaxHealth = getString("mobs.slime.attributes.max_health", slimeMaxHealth);
        slimeAttackDamage = getString("mobs.slime.attributes.attack_damage", slimeAttackDamage);
        slimeMaxHealthCache.clear();
        slimeAttackDamageCache.clear();
        slimeTakeDamageFromWater = getBoolean("mobs.slime.takes-damage-from-water", slimeTakeDamageFromWater);
    }

    public boolean snowGolemRidable = false;
    public boolean snowGolemRidableInWater = true;
    public boolean snowGolemControllable = true;
    public boolean snowGolemLeaveTrailWhenRidden = false;
    public double snowGolemMaxHealth = 4.0D;
    public double snowGolemScale = 1.0D;
    public boolean snowGolemPutPumpkinBack = false;
    public int snowGolemSnowBallMin = 20;
    public int snowGolemSnowBallMax = 20;
    public float snowGolemSnowBallModifier = 10.0F;
    public double snowGolemAttackDistance = 1.25D;
    public boolean snowGolemBypassMobGriefing = false;
    public boolean snowGolemTakeDamageFromWater = true;
    private void snowGolemSettings() {
        snowGolemRidable = getBoolean("mobs.snow_golem.ridable", snowGolemRidable);
        snowGolemRidableInWater = getBoolean("mobs.snow_golem.ridable-in-water", snowGolemRidableInWater);
        snowGolemControllable = getBoolean("mobs.snow_golem.controllable", snowGolemControllable);
        snowGolemLeaveTrailWhenRidden = getBoolean("mobs.snow_golem.leave-trail-when-ridden", snowGolemLeaveTrailWhenRidden);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.snow_golem.attributes.max-health", snowGolemMaxHealth);
            set("mobs.snow_golem.attributes.max-health", null);
            set("mobs.snow_golem.attributes.max_health", oldValue);
        }
        snowGolemMaxHealth = getDouble("mobs.snow_golem.attributes.max_health", snowGolemMaxHealth);
        snowGolemScale = Mth.clamp(getDouble("mobs.snow_golem.attributes.scale", snowGolemScale), 0.0625D, 16.0D);
        snowGolemPutPumpkinBack = getBoolean("mobs.snow_golem.pumpkin-can-be-added-back", snowGolemPutPumpkinBack);
        snowGolemSnowBallMin = getInt("mobs.snow_golem.min-shoot-interval-ticks", snowGolemSnowBallMin);
        snowGolemSnowBallMax = getInt("mobs.snow_golem.max-shoot-interval-ticks", snowGolemSnowBallMax);
        snowGolemSnowBallModifier = (float) getDouble("mobs.snow_golem.snow-ball-modifier", snowGolemSnowBallModifier);
        snowGolemAttackDistance = getDouble("mobs.snow_golem.attack-distance", snowGolemAttackDistance);
        snowGolemBypassMobGriefing = getBoolean("mobs.snow_golem.bypass-mob-griefing", snowGolemBypassMobGriefing);
        snowGolemTakeDamageFromWater = getBoolean("mobs.snow_golem.takes-damage-from-water", snowGolemTakeDamageFromWater);
    }

    public boolean snifferRidable = false;
    public boolean snifferRidableInWater = true;
    public boolean snifferControllable = true;
    public double snifferMaxHealth = 14.0D;
    public double snifferScale = 1.0D;
    public int snifferBreedingTicks = 6000;
    private void snifferSettings() {
        snifferRidable = getBoolean("mobs.sniffer.ridable", snifferRidable);
        snifferRidableInWater = getBoolean("mobs.sniffer.ridable-in-water", snifferRidableInWater);
        snifferControllable = getBoolean("mobs.sniffer.controllable", snifferControllable);
        snifferMaxHealth = getDouble("mobs.sniffer.attributes.max_health", snifferMaxHealth);
        snifferScale = Mth.clamp(getDouble("mobs.sniffer.attributes.scale", snifferScale), 0.0625D, 16.0D);
        snifferBreedingTicks = getInt("mobs.sniffer.breeding-delay-ticks", snifferBreedingTicks);
    }

    public boolean squidRidable = false;
    public boolean squidControllable = true;
    public double squidMaxHealth = 10.0D;
    public double squidScale = 1.0D;
    public boolean squidImmuneToEAR = true;
    public double squidOffsetWaterCheck = 0.0D;
    public boolean squidsCanFly = false;
    public boolean squidTakeDamageFromWater = false;
    private void squidSettings() {
        squidRidable = getBoolean("mobs.squid.ridable", squidRidable);
        squidControllable = getBoolean("mobs.squid.controllable", squidControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.squid.attributes.max-health", squidMaxHealth);
            set("mobs.squid.attributes.max-health", null);
            set("mobs.squid.attributes.max_health", oldValue);
        }
        squidMaxHealth = getDouble("mobs.squid.attributes.max_health", squidMaxHealth);
        squidScale = Mth.clamp(getDouble("mobs.squid.attributes.scale", squidScale), 0.0625D, 16.0D);
        squidImmuneToEAR = getBoolean("mobs.squid.immune-to-EAR", squidImmuneToEAR);
        squidOffsetWaterCheck = getDouble("mobs.squid.water-offset-check", squidOffsetWaterCheck);
        squidsCanFly = getBoolean("mobs.squid.can-fly", squidsCanFly);
        squidTakeDamageFromWater = getBoolean("mobs.squid.takes-damage-from-water", squidTakeDamageFromWater);
    }

    public boolean spiderRidable = false;
    public boolean spiderRidableInWater = false;
    public boolean spiderControllable = true;
    public double spiderMaxHealth = 16.0D;
    public double spiderScale = 1.0D;
    public boolean spiderTakeDamageFromWater = false;
    private void spiderSettings() {
        spiderRidable = getBoolean("mobs.spider.ridable", spiderRidable);
        spiderRidableInWater = getBoolean("mobs.spider.ridable-in-water", spiderRidableInWater);
        spiderControllable = getBoolean("mobs.spider.controllable", spiderControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.spider.attributes.max-health", spiderMaxHealth);
            set("mobs.spider.attributes.max-health", null);
            set("mobs.spider.attributes.max_health", oldValue);
        }
        spiderMaxHealth = getDouble("mobs.spider.attributes.max_health", spiderMaxHealth);
        spiderScale = Mth.clamp(getDouble("mobs.spider.attributes.scale", spiderScale), 0.0625D, 16.0D);
        spiderTakeDamageFromWater = getBoolean("mobs.spider.takes-damage-from-water", spiderTakeDamageFromWater);
    }

    public boolean strayRidable = false;
    public boolean strayRidableInWater = true;
    public boolean strayControllable = true;
    public double strayMaxHealth = 20.0D;
    public double strayScale = 1.0D;
    public boolean strayTakeDamageFromWater = false;
    private void straySettings() {
        strayRidable = getBoolean("mobs.stray.ridable", strayRidable);
        strayRidableInWater = getBoolean("mobs.stray.ridable-in-water", strayRidableInWater);
        strayControllable = getBoolean("mobs.stray.controllable", strayControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.stray.attributes.max-health", strayMaxHealth);
            set("mobs.stray.attributes.max-health", null);
            set("mobs.stray.attributes.max_health", oldValue);
        }
        strayMaxHealth = getDouble("mobs.stray.attributes.max_health", strayMaxHealth);
        strayScale = Mth.clamp(getDouble("mobs.stray.attributes.scale", strayScale), 0.0625D, 16.0D);
        strayTakeDamageFromWater = getBoolean("mobs.stray.takes-damage-from-water", strayTakeDamageFromWater);
    }

    public boolean striderRidable = false;
    public boolean striderRidableInWater = false;
    public boolean striderControllable = true;
    public double striderMaxHealth = 20.0D;
    public double striderScale = 1.0D;
    public int striderBreedingTicks = 6000;
    public boolean striderGiveSaddleBack = false;
    public boolean striderTakeDamageFromWater = true;
    private void striderSettings() {
        striderRidable = getBoolean("mobs.strider.ridable", striderRidable);
        striderRidableInWater = getBoolean("mobs.strider.ridable-in-water", striderRidableInWater);
        striderControllable = getBoolean("mobs.strider.controllable", striderControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.strider.attributes.max-health", striderMaxHealth);
            set("mobs.strider.attributes.max-health", null);
            set("mobs.strider.attributes.max_health", oldValue);
        }
        striderMaxHealth = getDouble("mobs.strider.attributes.max_health", striderMaxHealth);
        striderScale = Mth.clamp(getDouble("mobs.strider.attributes.scale", striderScale), 0.0625D, 16.0D);
        striderBreedingTicks = getInt("mobs.strider.breeding-delay-ticks", striderBreedingTicks);
        striderGiveSaddleBack = getBoolean("mobs.strider.give-saddle-back", striderGiveSaddleBack);
        striderTakeDamageFromWater = getBoolean("mobs.strider.takes-damage-from-water", striderTakeDamageFromWater);
    }

    public boolean tadpoleRidable = false;
    public boolean tadpoleRidableInWater = true;
    public boolean tadpoleControllable = true;
    private void tadpoleSettings() {
        tadpoleRidable = getBoolean("mobs.tadpole.ridable", tadpoleRidable);
        tadpoleRidableInWater = getBoolean("mobs.tadpole.ridable-in-water", tadpoleRidableInWater);
        tadpoleControllable = getBoolean("mobs.tadpole.controllable", tadpoleControllable);
    }

    public boolean traderLlamaRidable = false;
    public boolean traderLlamaRidableInWater = false;
    public boolean traderLlamaControllable = true;
    public double traderLlamaMaxHealthMin = 15.0D;
    public double traderLlamaMaxHealthMax = 30.0D;
    public double traderLlamaJumpStrengthMin = 0.5D;
    public double traderLlamaJumpStrengthMax = 0.5D;
    public double traderLlamaMovementSpeedMin = 0.175D;
    public double traderLlamaMovementSpeedMax = 0.175D;
    public int traderLlamaBreedingTicks = 6000;
    public boolean traderLlamaTakeDamageFromWater = false;
    private void traderLlamaSettings() {
        traderLlamaRidable = getBoolean("mobs.trader_llama.ridable", traderLlamaRidable);
        traderLlamaRidableInWater = getBoolean("mobs.trader_llama.ridable-in-water", traderLlamaRidableInWater);
        traderLlamaControllable = getBoolean("mobs.trader_llama.controllable", traderLlamaControllable);
        if (PurpurConfig.version < 10) {
            double oldMin = getDouble("mobs.trader_llama.attributes.max-health.min", traderLlamaMaxHealthMin);
            double oldMax = getDouble("mobs.trader_llama.attributes.max-health.max", traderLlamaMaxHealthMax);
            set("mobs.trader_llama.attributes.max-health", null);
            set("mobs.trader_llama.attributes.max_health.min", oldMin);
            set("mobs.trader_llama.attributes.max_health.max", oldMax);
        }
        traderLlamaMaxHealthMin = getDouble("mobs.trader_llama.attributes.max_health.min", traderLlamaMaxHealthMin);
        traderLlamaMaxHealthMax = getDouble("mobs.trader_llama.attributes.max_health.max", traderLlamaMaxHealthMax);
        traderLlamaJumpStrengthMin = getDouble("mobs.trader_llama.attributes.jump_strength.min", traderLlamaJumpStrengthMin);
        traderLlamaJumpStrengthMax = getDouble("mobs.trader_llama.attributes.jump_strength.max", traderLlamaJumpStrengthMax);
        traderLlamaMovementSpeedMin = getDouble("mobs.trader_llama.attributes.movement_speed.min", traderLlamaMovementSpeedMin);
        traderLlamaMovementSpeedMax = getDouble("mobs.trader_llama.attributes.movement_speed.max", traderLlamaMovementSpeedMax);
        traderLlamaBreedingTicks = getInt("mobs.trader_llama.breeding-delay-ticks", traderLlamaBreedingTicks);
        traderLlamaTakeDamageFromWater = getBoolean("mobs.trader_llama.takes-damage-from-water", traderLlamaTakeDamageFromWater);
    }

    public boolean tropicalFishRidable = false;
    public boolean tropicalFishControllable = true;
    public double tropicalFishMaxHealth = 3.0D;
    public double tropicalFishScale = 1.0D;
    public boolean tropicalFishTakeDamageFromWater = false;
    private void tropicalFishSettings() {
        tropicalFishRidable = getBoolean("mobs.tropical_fish.ridable", tropicalFishRidable);
        tropicalFishControllable = getBoolean("mobs.tropical_fish.controllable", tropicalFishControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.tropical_fish.attributes.max-health", tropicalFishMaxHealth);
            set("mobs.tropical_fish.attributes.max-health", null);
            set("mobs.tropical_fish.attributes.max_health", oldValue);
        }
        tropicalFishMaxHealth = getDouble("mobs.tropical_fish.attributes.max_health", tropicalFishMaxHealth);
        tropicalFishScale = Mth.clamp(getDouble("mobs.tropical_fish.attributes.scale", tropicalFishScale), 0.0625D, 16.0D);
        tropicalFishTakeDamageFromWater = getBoolean("mobs.tropical_fish.takes-damage-from-water", tropicalFishTakeDamageFromWater);
    }

    public boolean turtleRidable = false;
    public boolean turtleRidableInWater = true;
    public boolean turtleControllable = true;
    public double turtleMaxHealth = 30.0D;
    public double turtleScale = 1.0D;
    public int turtleBreedingTicks = 6000;
    public boolean turtleTakeDamageFromWater = false;
    private void turtleSettings() {
        turtleRidable = getBoolean("mobs.turtle.ridable", turtleRidable);
        turtleRidableInWater = getBoolean("mobs.turtle.ridable-in-water", turtleRidableInWater);
        turtleControllable = getBoolean("mobs.turtle.controllable", turtleControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.turtle.attributes.max-health", turtleMaxHealth);
            set("mobs.turtle.attributes.max-health", null);
            set("mobs.turtle.attributes.max_health", oldValue);
        }
        turtleMaxHealth = getDouble("mobs.turtle.attributes.max_health", turtleMaxHealth);
        turtleScale = Mth.clamp(getDouble("mobs.turtle.attributes.scale", turtleScale), 0.0625D, 16.0D);
        turtleBreedingTicks = getInt("mobs.turtle.breeding-delay-ticks", turtleBreedingTicks);
        turtleTakeDamageFromWater = getBoolean("mobs.turtle.takes-damage-from-water", turtleTakeDamageFromWater);
    }

    public boolean vexRidable = false;
    public boolean vexRidableInWater = true;
    public boolean vexControllable = true;
    public double vexMaxY = 320D;
    public double vexMaxHealth = 14.0D;
    public double vexScale = 1.0D;
    public boolean vexTakeDamageFromWater = false;
    private void vexSettings() {
        vexRidable = getBoolean("mobs.vex.ridable", vexRidable);
        vexRidableInWater = getBoolean("mobs.vex.ridable-in-water", vexRidableInWater);
        vexControllable = getBoolean("mobs.vex.controllable", vexControllable);
        vexMaxY = getDouble("mobs.vex.ridable-max-y", vexMaxY);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.vex.attributes.max-health", vexMaxHealth);
            set("mobs.vex.attributes.max-health", null);
            set("mobs.vex.attributes.max_health", oldValue);
        }
        vexMaxHealth = getDouble("mobs.vex.attributes.max_health", vexMaxHealth);
        vexScale = Mth.clamp(getDouble("mobs.vex.attributes.scale", vexScale), 0.0625D, 16.0D);
        vexTakeDamageFromWater = getBoolean("mobs.vex.takes-damage-from-water", vexTakeDamageFromWater);
    }

    public boolean villagerRidable = false;
    public boolean villagerRidableInWater = true;
    public boolean villagerControllable = true;
    public double villagerMaxHealth = 20.0D;
    public double villagerScale = 1.0D;
    public boolean villagerFollowEmeraldBlock = false;
    public double villagerTemptRange = 10.0D;
    public boolean villagerCanBeLeashed = false;
    public boolean villagerCanBreed = true;
    public int villagerBreedingTicks = 6000;
    public boolean villagerClericsFarmWarts = false;
    public boolean villagerClericFarmersThrowWarts = true;
    public boolean villagerBypassMobGriefing = false;
    public boolean villagerTakeDamageFromWater = false;
    public boolean villagerAllowTrading = true;
    private void villagerSettings() {
        villagerRidable = getBoolean("mobs.villager.ridable", villagerRidable);
        villagerRidableInWater = getBoolean("mobs.villager.ridable-in-water", villagerRidableInWater);
        villagerControllable = getBoolean("mobs.villager.controllable", villagerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.villager.attributes.max-health", villagerMaxHealth);
            set("mobs.villager.attributes.max-health", null);
            set("mobs.villager.attributes.max_health", oldValue);
        }
        villagerMaxHealth = getDouble("mobs.villager.attributes.max_health", villagerMaxHealth);
        villagerScale = Mth.clamp(getDouble("mobs.villager.attributes.scale", villagerScale), 0.0625D, 16.0D);
        villagerFollowEmeraldBlock = getBoolean("mobs.villager.follow-emerald-blocks", villagerFollowEmeraldBlock);
        villagerTemptRange = getDouble("mobs.villager.attributes.tempt_range", villagerTemptRange);
        villagerCanBeLeashed = getBoolean("mobs.villager.can-be-leashed", villagerCanBeLeashed);
        villagerCanBreed = getBoolean("mobs.villager.can-breed", villagerCanBreed);
        villagerBreedingTicks = getInt("mobs.villager.breeding-delay-ticks", villagerBreedingTicks);
        villagerClericsFarmWarts = getBoolean("mobs.villager.clerics-farm-warts", villagerClericsFarmWarts);
        villagerClericFarmersThrowWarts = getBoolean("mobs.villager.cleric-wart-farmers-throw-warts-at-villagers", villagerClericFarmersThrowWarts);
        villagerBypassMobGriefing = getBoolean("mobs.villager.bypass-mob-griefing", villagerBypassMobGriefing);
        villagerTakeDamageFromWater = getBoolean("mobs.villager.takes-damage-from-water", villagerTakeDamageFromWater);
        villagerAllowTrading = getBoolean("mobs.villager.allow-trading", villagerAllowTrading);
    }

    public boolean vindicatorRidable = false;
    public boolean vindicatorRidableInWater = true;
    public boolean vindicatorControllable = true;
    public double vindicatorMaxHealth = 24.0D;
    public double vindicatorScale = 1.0D;
    public double vindicatorJohnnySpawnChance = 0D;
    public boolean vindicatorTakeDamageFromWater = false;
    private void vindicatorSettings() {
        vindicatorRidable = getBoolean("mobs.vindicator.ridable", vindicatorRidable);
        vindicatorRidableInWater = getBoolean("mobs.vindicator.ridable-in-water", vindicatorRidableInWater);
        vindicatorControllable = getBoolean("mobs.vindicator.controllable", vindicatorControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.vindicator.attributes.max-health", vindicatorMaxHealth);
            set("mobs.vindicator.attributes.max-health", null);
            set("mobs.vindicator.attributes.max_health", oldValue);
        }
        vindicatorMaxHealth = getDouble("mobs.vindicator.attributes.max_health", vindicatorMaxHealth);
        vindicatorScale = Mth.clamp(getDouble("mobs.vindicator.attributes.scale", vindicatorScale), 0.0625D, 16.0D);
        vindicatorJohnnySpawnChance = getDouble("mobs.vindicator.johnny.spawn-chance", vindicatorJohnnySpawnChance);
        vindicatorTakeDamageFromWater = getBoolean("mobs.vindicator.takes-damage-from-water", vindicatorTakeDamageFromWater);
    }

    public boolean wanderingTraderRidable = false;
    public boolean wanderingTraderRidableInWater = true;
    public boolean wanderingTraderControllable = true;
    public double wanderingTraderMaxHealth = 20.0D;
    public double wanderingTraderScale = 1.0D;
    public boolean wanderingTraderFollowEmeraldBlock = false;
    public double wanderingTraderTemptRange = 10.0D;
    public boolean wanderingTraderCanBeLeashed = false;
    public boolean wanderingTraderTakeDamageFromWater = false;
    public boolean wanderingTraderAllowTrading = true;
    private void wanderingTraderSettings() {
        wanderingTraderRidable = getBoolean("mobs.wandering_trader.ridable", wanderingTraderRidable);
        wanderingTraderRidableInWater = getBoolean("mobs.wandering_trader.ridable-in-water", wanderingTraderRidableInWater);
        wanderingTraderControllable = getBoolean("mobs.wandering_trader.controllable", wanderingTraderControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wandering_trader.attributes.max-health", wanderingTraderMaxHealth);
            set("mobs.wandering_trader.attributes.max-health", null);
            set("mobs.wandering_trader.attributes.max_health", oldValue);
        }
        wanderingTraderMaxHealth = getDouble("mobs.wandering_trader.attributes.max_health", wanderingTraderMaxHealth);
        wanderingTraderScale = Mth.clamp(getDouble("mobs.wandering_trader.attributes.scale", wanderingTraderScale), 0.0625D, 16.0D);
        wanderingTraderFollowEmeraldBlock = getBoolean("mobs.wandering_trader.follow-emerald-blocks", wanderingTraderFollowEmeraldBlock);
        wanderingTraderTemptRange = getDouble("mobs.wandering_trader.attributes.tempt_range", wanderingTraderTemptRange);
        wanderingTraderCanBeLeashed = getBoolean("mobs.wandering_trader.can-be-leashed", wanderingTraderCanBeLeashed);
        wanderingTraderTakeDamageFromWater = getBoolean("mobs.wandering_trader.takes-damage-from-water", wanderingTraderTakeDamageFromWater);
        wanderingTraderAllowTrading = getBoolean("mobs.wandering_trader.allow-trading", wanderingTraderAllowTrading);
    }

    public boolean wardenRidable = false;
    public boolean wardenRidableInWater = true;
    public boolean wardenControllable = true;
    private void wardenSettings() {
        wardenRidable = getBoolean("mobs.warden.ridable", wardenRidable);
        wardenRidableInWater = getBoolean("mobs.warden.ridable-in-water", wardenRidableInWater);
        wardenControllable = getBoolean("mobs.warden.controllable", wardenControllable);
    }

    public boolean witchRidable = false;
    public boolean witchRidableInWater = true;
    public boolean witchControllable = true;
    public double witchMaxHealth = 26.0D;
    public double witchScale = 1.0D;
    public boolean witchTakeDamageFromWater = false;
    private void witchSettings() {
        witchRidable = getBoolean("mobs.witch.ridable", witchRidable);
        witchRidableInWater = getBoolean("mobs.witch.ridable-in-water", witchRidableInWater);
        witchControllable = getBoolean("mobs.witch.controllable", witchControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.witch.attributes.max-health", witchMaxHealth);
            set("mobs.witch.attributes.max-health", null);
            set("mobs.witch.attributes.max_health", oldValue);
        }
        witchMaxHealth = getDouble("mobs.witch.attributes.max_health", witchMaxHealth);
        witchScale = Mth.clamp(getDouble("mobs.witch.attributes.scale", witchScale), 0.0625D, 16.0D);
        witchTakeDamageFromWater = getBoolean("mobs.witch.takes-damage-from-water", witchTakeDamageFromWater);
    }

    public boolean witherRidable = false;
    public boolean witherRidableInWater = true;
    public boolean witherControllable = true;
    public double witherMaxY = 320D;
    public double witherMaxHealth = 300.0D;
    public double witherScale = 1.0D;
    public float witherHealthRegenAmount = 1.0f;
    public int witherHealthRegenDelay = 20;
    public boolean witherBypassMobGriefing = false;
    public boolean witherTakeDamageFromWater = false;
    public boolean witherCanRideVehicles = false;
    public float witherExplosionRadius = 1.0F;
    public boolean witherPlaySpawnSound = true;
    private void witherSettings() {
        witherRidable = getBoolean("mobs.wither.ridable", witherRidable);
        witherRidableInWater = getBoolean("mobs.wither.ridable-in-water", witherRidableInWater);
        witherControllable = getBoolean("mobs.wither.controllable", witherControllable);
        witherMaxY = getDouble("mobs.wither.ridable-max-y", witherMaxY);
        if (PurpurConfig.version < 8) {
            double oldValue = getDouble("mobs.wither.max-health", witherMaxHealth);
            set("mobs.wither.max_health", null);
            set("mobs.wither.attributes.max-health", oldValue);
        } else if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wither.attributes.max-health", witherMaxHealth);
            set("mobs.wither.attributes.max-health", null);
            set("mobs.wither.attributes.max_health", oldValue);
        }
        witherMaxHealth = getDouble("mobs.wither.attributes.max_health", witherMaxHealth);
        witherScale = Mth.clamp(getDouble("mobs.wither.attributes.scale", witherScale), 0.0625D, 16.0D);
        witherHealthRegenAmount = (float) getDouble("mobs.wither.health-regen-amount", witherHealthRegenAmount);
        witherHealthRegenDelay = getInt("mobs.wither.health-regen-delay", witherHealthRegenDelay);
        witherBypassMobGriefing = getBoolean("mobs.wither.bypass-mob-griefing", witherBypassMobGriefing);
        witherTakeDamageFromWater = getBoolean("mobs.wither.takes-damage-from-water", witherTakeDamageFromWater);
        witherCanRideVehicles = getBoolean("mobs.wither.can-ride-vehicles", witherCanRideVehicles);
        witherExplosionRadius = (float) getDouble("mobs.wither.explosion-radius", witherExplosionRadius);
        witherPlaySpawnSound = getBoolean("mobs.wither.play-spawn-sound", witherPlaySpawnSound);
    }

    public boolean witherSkeletonRidable = false;
    public boolean witherSkeletonRidableInWater = true;
    public boolean witherSkeletonControllable = true;
    public double witherSkeletonMaxHealth = 20.0D;
    public double witherSkeletonScale = 1.0D;
    public boolean witherSkeletonTakeDamageFromWater = false;
    private void witherSkeletonSettings() {
        witherSkeletonRidable = getBoolean("mobs.wither_skeleton.ridable", witherSkeletonRidable);
        witherSkeletonRidableInWater = getBoolean("mobs.wither_skeleton.ridable-in-water", witherSkeletonRidableInWater);
        witherSkeletonControllable = getBoolean("mobs.wither_skeleton.controllable", witherSkeletonControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wither_skeleton.attributes.max-health", witherSkeletonMaxHealth);
            set("mobs.wither_skeleton.attributes.max-health", null);
            set("mobs.wither_skeleton.attributes.max_health", oldValue);
        }
        witherSkeletonMaxHealth = getDouble("mobs.wither_skeleton.attributes.max_health", witherSkeletonMaxHealth);
        witherSkeletonScale = Mth.clamp(getDouble("mobs.wither_skeleton.attributes.scale", witherSkeletonScale), 0.0625D, 16.0D);
        witherSkeletonTakeDamageFromWater = getBoolean("mobs.wither_skeleton.takes-damage-from-water", witherSkeletonTakeDamageFromWater);
    }

    public boolean wolfRidable = false;
    public boolean wolfRidableInWater = true;
    public boolean wolfControllable = true;
    public double wolfMaxHealth = 8.0D;
    public double wolfScale = 1.0D;
    public DyeColor wolfDefaultCollarColor = DyeColor.RED;
    public boolean wolfMilkCuresRabies = true;
    public double wolfNaturalRabid = 0.0D;
    public int wolfBreedingTicks = 6000;
    public boolean wolfTakeDamageFromWater = false;
    private void wolfSettings() {
        wolfRidable = getBoolean("mobs.wolf.ridable", wolfRidable);
        wolfRidableInWater = getBoolean("mobs.wolf.ridable-in-water", wolfRidableInWater);
        wolfControllable = getBoolean("mobs.wolf.controllable", wolfControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.wolf.attributes.max-health", wolfMaxHealth);
            set("mobs.wolf.attributes.max-health", null);
            set("mobs.wolf.attributes.max_health", oldValue);
        }
        wolfMaxHealth = getDouble("mobs.wolf.attributes.max_health", wolfMaxHealth);
        wolfScale = Mth.clamp(getDouble("mobs.wolf.attributes.scale", wolfScale), 0.0625D, 16.0D);
        try {
            wolfDefaultCollarColor = DyeColor.valueOf(getString("mobs.wolf.default-collar-color", wolfDefaultCollarColor.name()));
        } catch (IllegalArgumentException ignore) {
            wolfDefaultCollarColor = DyeColor.RED;
        }
        wolfMilkCuresRabies = getBoolean("mobs.wolf.milk-cures-rabid-wolves", wolfMilkCuresRabies);
        wolfNaturalRabid = getDouble("mobs.wolf.spawn-rabid-chance", wolfNaturalRabid);
        wolfBreedingTicks = getInt("mobs.wolf.breeding-delay-ticks", wolfBreedingTicks);
        wolfTakeDamageFromWater = getBoolean("mobs.wolf.takes-damage-from-water", wolfTakeDamageFromWater);
    }

    public boolean zoglinRidable = false;
    public boolean zoglinRidableInWater = true;
    public boolean zoglinControllable = true;
    public double zoglinMaxHealth = 40.0D;
    public double zoglinScale = 1.0D;
    public boolean zoglinTakeDamageFromWater = false;
    private void zoglinSettings() {
        zoglinRidable = getBoolean("mobs.zoglin.ridable", zoglinRidable);
        zoglinRidableInWater = getBoolean("mobs.zoglin.ridable-in-water", zoglinRidableInWater);
        zoglinControllable = getBoolean("mobs.zoglin.controllable", zoglinControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zoglin.attributes.max-health", zoglinMaxHealth);
            set("mobs.zoglin.attributes.max-health", null);
            set("mobs.zoglin.attributes.max_health", oldValue);
        }
        zoglinMaxHealth = getDouble("mobs.zoglin.attributes.max_health", zoglinMaxHealth);
        zoglinScale = Mth.clamp(getDouble("mobs.zoglin.attributes.scale", zoglinScale), 0.0625D, 16.0D);
        zoglinTakeDamageFromWater = getBoolean("mobs.zoglin.takes-damage-from-water", zoglinTakeDamageFromWater);
    }

    public boolean zombieRidable = false;
    public boolean zombieRidableInWater = true;
    public boolean zombieControllable = true;
    public double zombieMaxHealth = 20.0D;
    public double zombieScale = 1.0D;
    public double zombieSpawnReinforcements = 0.1D;
    public boolean zombieJockeyOnlyBaby = true;
    public double zombieJockeyChance = 0.05D;
    public boolean zombieJockeyTryExistingChickens = true;
    public boolean zombieAggressiveTowardsVillagerWhenLagging = true;
    public boolean zombieBypassMobGriefing = false;
    public boolean zombieTakeDamageFromWater = false;
    private void zombieSettings() {
        zombieRidable = getBoolean("mobs.zombie.ridable", zombieRidable);
        zombieRidableInWater = getBoolean("mobs.zombie.ridable-in-water", zombieRidableInWater);
        zombieControllable = getBoolean("mobs.zombie.controllable", zombieControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie.attributes.max-health", zombieMaxHealth);
            set("mobs.zombie.attributes.max-health", null);
            set("mobs.zombie.attributes.max_health", oldValue);
        }
        zombieMaxHealth = getDouble("mobs.zombie.attributes.max_health", zombieMaxHealth);
        zombieScale = Mth.clamp(getDouble("mobs.zombie.attributes.scale", zombieScale), 0.0625D, 16.0D);
        zombieSpawnReinforcements = getDouble("mobs.zombie.attributes.spawn_reinforcements", zombieSpawnReinforcements);
        zombieJockeyOnlyBaby = getBoolean("mobs.zombie.jockey.only-babies", zombieJockeyOnlyBaby);
        zombieJockeyChance = getDouble("mobs.zombie.jockey.chance", zombieJockeyChance);
        zombieJockeyTryExistingChickens = getBoolean("mobs.zombie.jockey.try-existing-chickens", zombieJockeyTryExistingChickens);
        zombieAggressiveTowardsVillagerWhenLagging = getBoolean("mobs.zombie.aggressive-towards-villager-when-lagging", zombieAggressiveTowardsVillagerWhenLagging);
        zombieBypassMobGriefing = getBoolean("mobs.zombie.bypass-mob-griefing", zombieBypassMobGriefing);
        zombieTakeDamageFromWater = getBoolean("mobs.zombie.takes-damage-from-water", zombieTakeDamageFromWater);
    }

    public boolean zombieHorseRidable = false;
    public boolean zombieHorseRidableInWater = false;
    public boolean zombieHorseCanSwim = false;
    public double zombieHorseMaxHealthMin = 15.0D;
    public double zombieHorseMaxHealthMax = 15.0D;
    public double zombieHorseJumpStrengthMin = 0.4D;
    public double zombieHorseJumpStrengthMax = 1.0D;
    public double zombieHorseMovementSpeedMin = 0.2D;
    public double zombieHorseMovementSpeedMax = 0.2D;
    public double zombieHorseSpawnChance = 0.0D;
    public boolean zombieHorseTakeDamageFromWater = false;
    private void zombieHorseSettings() {
        zombieHorseRidable = getBoolean("mobs.zombie_horse.ridable", zombieHorseRidable);
        zombieHorseRidableInWater = getBoolean("mobs.zombie_horse.ridable-in-water", zombieHorseRidableInWater);
        zombieHorseCanSwim = getBoolean("mobs.zombie_horse.can-swim", zombieHorseCanSwim);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie_horse.attributes.max-health", zombieHorseMaxHealthMin);
            set("mobs.zombie_horse.attributes.max-health", null);
            set("mobs.zombie_horse.attributes.max_health.min", oldValue);
            set("mobs.zombie_horse.attributes.max_health.max", oldValue);
        }
        zombieHorseMaxHealthMin = getDouble("mobs.zombie_horse.attributes.max_health.min", zombieHorseMaxHealthMin);
        zombieHorseMaxHealthMax = getDouble("mobs.zombie_horse.attributes.max_health.max", zombieHorseMaxHealthMax);
        zombieHorseJumpStrengthMin = getDouble("mobs.zombie_horse.attributes.jump_strength.min", zombieHorseJumpStrengthMin);
        zombieHorseJumpStrengthMax = getDouble("mobs.zombie_horse.attributes.jump_strength.max", zombieHorseJumpStrengthMax);
        zombieHorseMovementSpeedMin = getDouble("mobs.zombie_horse.attributes.movement_speed.min", zombieHorseMovementSpeedMin);
        zombieHorseMovementSpeedMax = getDouble("mobs.zombie_horse.attributes.movement_speed.max", zombieHorseMovementSpeedMax);
        zombieHorseSpawnChance = getDouble("mobs.zombie_horse.spawn-chance", zombieHorseSpawnChance);
        zombieHorseTakeDamageFromWater = getBoolean("mobs.zombie_horse.takes-damage-from-water", zombieHorseTakeDamageFromWater);
    }

    public boolean zombieVillagerRidable = false;
    public boolean zombieVillagerRidableInWater = true;
    public boolean zombieVillagerControllable = true;
    public double zombieVillagerMaxHealth = 20.0D;
    public double zombieVillagerScale = 1.0D;
    public double zombieVillagerSpawnReinforcements = 0.1D;
    public boolean zombieVillagerJockeyOnlyBaby = true;
    public double zombieVillagerJockeyChance = 0.05D;
    public boolean zombieVillagerJockeyTryExistingChickens = true;
    public boolean zombieVillagerTakeDamageFromWater = false;
    public int zombieVillagerCuringTimeMin = 3600;
    public int zombieVillagerCuringTimeMax = 6000;
    private void zombieVillagerSettings() {
        zombieVillagerRidable = getBoolean("mobs.zombie_villager.ridable", zombieVillagerRidable);
        zombieVillagerRidableInWater = getBoolean("mobs.zombie_villager.ridable-in-water", zombieVillagerRidableInWater);
        zombieVillagerControllable = getBoolean("mobs.zombie_villager.controllable", zombieVillagerControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombie_villager.attributes.max-health", zombieVillagerMaxHealth);
            set("mobs.zombie_villager.attributes.max-health", null);
            set("mobs.zombie_villager.attributes.max_health", oldValue);
        }
        zombieVillagerMaxHealth = getDouble("mobs.zombie_villager.attributes.max_health", zombieVillagerMaxHealth);
        zombieVillagerScale = Mth.clamp(getDouble("mobs.zombie_villager.attributes.scale", zombieVillagerScale), 0.0625D, 16.0D);
        zombieVillagerSpawnReinforcements = getDouble("mobs.zombie_villager.attributes.spawn_reinforcements", zombieVillagerSpawnReinforcements);
        zombieVillagerJockeyOnlyBaby = getBoolean("mobs.zombie_villager.jockey.only-babies", zombieVillagerJockeyOnlyBaby);
        zombieVillagerJockeyChance = getDouble("mobs.zombie_villager.jockey.chance", zombieVillagerJockeyChance);
        zombieVillagerJockeyTryExistingChickens = getBoolean("mobs.zombie_villager.jockey.try-existing-chickens", zombieVillagerJockeyTryExistingChickens);
        zombieVillagerTakeDamageFromWater = getBoolean("mobs.zombie_villager.takes-damage-from-water", zombieVillagerTakeDamageFromWater);
        zombieVillagerCuringTimeMin = getInt("mobs.zombie_villager.curing_time.min", zombieVillagerCuringTimeMin);
        zombieVillagerCuringTimeMax = getInt("mobs.zombie_villager.curing_time.max", zombieVillagerCuringTimeMax);
    }

    public boolean zombifiedPiglinRidable = false;
    public boolean zombifiedPiglinRidableInWater = true;
    public boolean zombifiedPiglinControllable = true;
    public double zombifiedPiglinMaxHealth = 20.0D;
    public double zombifiedPiglinScale = 1.0D;
    public double zombifiedPiglinSpawnReinforcements = 0.0D;
    public boolean zombifiedPiglinJockeyOnlyBaby = true;
    public double zombifiedPiglinJockeyChance = 0.05D;
    public boolean zombifiedPiglinJockeyTryExistingChickens = true;
    public boolean zombifiedPiglinCountAsPlayerKillWhenAngry = true;
    public boolean zombifiedPiglinTakeDamageFromWater = false;
    private void zombifiedPiglinSettings() {
        zombifiedPiglinRidable = getBoolean("mobs.zombified_piglin.ridable", zombifiedPiglinRidable);
        zombifiedPiglinRidableInWater = getBoolean("mobs.zombified_piglin.ridable-in-water", zombifiedPiglinRidableInWater);
        zombifiedPiglinControllable = getBoolean("mobs.zombified_piglin.controllable", zombifiedPiglinControllable);
        if (PurpurConfig.version < 10) {
            double oldValue = getDouble("mobs.zombified_piglin.attributes.max-health", zombifiedPiglinMaxHealth);
            set("mobs.zombified_piglin.attributes.max-health", null);
            set("mobs.zombified_piglin.attributes.max_health", oldValue);
        }
        zombifiedPiglinMaxHealth = getDouble("mobs.zombified_piglin.attributes.max_health", zombifiedPiglinMaxHealth);
        zombifiedPiglinScale = Mth.clamp(getDouble("mobs.zombified_piglin.attributes.scale", zombifiedPiglinScale), 0.0625D, 16.0D);
        zombifiedPiglinSpawnReinforcements = getDouble("mobs.zombified_piglin.attributes.spawn_reinforcements", zombifiedPiglinSpawnReinforcements);
        zombifiedPiglinJockeyOnlyBaby = getBoolean("mobs.zombified_piglin.jockey.only-babies", zombifiedPiglinJockeyOnlyBaby);
        zombifiedPiglinJockeyChance = getDouble("mobs.zombified_piglin.jockey.chance", zombifiedPiglinJockeyChance);
        zombifiedPiglinJockeyTryExistingChickens = getBoolean("mobs.zombified_piglin.jockey.try-existing-chickens", zombifiedPiglinJockeyTryExistingChickens);
        zombifiedPiglinCountAsPlayerKillWhenAngry = getBoolean("mobs.zombified_piglin.count-as-player-kill-when-angry", zombifiedPiglinCountAsPlayerKillWhenAngry);
        zombifiedPiglinTakeDamageFromWater = getBoolean("mobs.zombified_piglin.takes-damage-from-water", zombifiedPiglinTakeDamageFromWater);
    }

    public float hungerStarvationDamage = 1.0F;
    private void hungerSettings() {
        hungerStarvationDamage = (float) getDouble("hunger.starvation-damage", hungerStarvationDamage);
    }

    public int conduitDistance = 16;
    public double conduitDamageDistance = 8;
    public float conduitDamageAmount = 4;
    public Block[] conduitBlocks;
    private void conduitSettings() {
        conduitDistance = getInt("blocks.conduit.effect-distance", conduitDistance);
        conduitDamageDistance = getDouble("blocks.conduit.mob-damage.distance", conduitDamageDistance);
        conduitDamageAmount = (float) getDouble("blocks.conduit.mob-damage.damage-amount", conduitDamageAmount);
        List<Block> conduitBlockList = new ArrayList<>();
        getList("blocks.conduit.valid-ring-blocks", new ArrayList<String>(){{
            add("minecraft:prismarine");
            add("minecraft:prismarine_bricks");
            add("minecraft:sea_lantern");
            add("minecraft:dark_prismarine");
        }}).forEach(key -> {
            Block block = BuiltInRegistries.BLOCK.getValue(ResourceLocation.parse(key.toString()));
            if (!block.defaultBlockState().isAir()) {
                conduitBlockList.add(block);
            }
        });
        conduitBlocks = conduitBlockList.toArray(Block[]::new);
    }

    public float cauldronRainChance = 0.05F;
    public float cauldronPowderSnowChance = 0.1F;
    public float cauldronDripstoneWaterFillChance = 0.17578125F;
    public float cauldronDripstoneLavaFillChance = 0.05859375F;
    private void cauldronSettings() {
        cauldronRainChance = (float) getDouble("blocks.cauldron.fill-chances.rain", cauldronRainChance);
        cauldronPowderSnowChance = (float) getDouble("blocks.cauldron.fill-chances.powder-snow", cauldronPowderSnowChance);
        cauldronDripstoneWaterFillChance = (float) getDouble("blocks.cauldron.fill-chances.dripstone-water", cauldronDripstoneWaterFillChance);
        cauldronDripstoneLavaFillChance = (float) getDouble("blocks.cauldron.fill-chances.dripstone-lava", cauldronDripstoneLavaFillChance);
    }
}
