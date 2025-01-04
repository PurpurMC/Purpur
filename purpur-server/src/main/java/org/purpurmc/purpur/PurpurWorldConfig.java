package org.purpurmc.purpur;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;
import java.util.Map;
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
    private void allaySettings() {
        allayRidable = getBoolean("mobs.allay.ridable", allayRidable);
        allayRidableInWater = getBoolean("mobs.allay.ridable-in-water", allayRidableInWater);
        allayControllable = getBoolean("mobs.allay.controllable", allayControllable);
    }

    public boolean armadilloRidable = false;
    public boolean armadilloRidableInWater = true;
    public boolean armadilloControllable = true;
    private void armadilloSettings() {
        armadilloRidable = getBoolean("mobs.armadillo.ridable", armadilloRidable);
        armadilloRidableInWater = getBoolean("mobs.armadillo.ridable-in-water", armadilloRidableInWater);
        armadilloControllable = getBoolean("mobs.armadillo.controllable", armadilloControllable);
    }

    public boolean axolotlRidable = false;
    public boolean axolotlControllable = true;
    private void axolotlSettings() {
        axolotlRidable = getBoolean("mobs.axolotl.ridable", axolotlRidable);
        axolotlControllable = getBoolean("mobs.axolotl.controllable", axolotlControllable);
    }

    public boolean batRidable = false;
    public boolean batRidableInWater = true;
    public boolean batControllable = true;
    public double batMaxY = 320D;
    private void batSettings() {
        batRidable = getBoolean("mobs.bat.ridable", batRidable);
        batRidableInWater = getBoolean("mobs.bat.ridable-in-water", batRidableInWater);
        batControllable = getBoolean("mobs.bat.controllable", batControllable);
        batMaxY = getDouble("mobs.bat.ridable-max-y", batMaxY);
    }

    public boolean beeRidable = false;
    public boolean beeRidableInWater = true;
    public boolean beeControllable = true;
    public double beeMaxY = 320D;
    private void beeSettings() {
        beeRidable = getBoolean("mobs.bee.ridable", beeRidable);
        beeRidableInWater = getBoolean("mobs.bee.ridable-in-water", beeRidableInWater);
        beeControllable = getBoolean("mobs.bee.controllable", beeControllable);
        beeMaxY = getDouble("mobs.bee.ridable-max-y", beeMaxY);
    }

    public boolean blazeRidable = false;
    public boolean blazeRidableInWater = true;
    public boolean blazeControllable = true;
    public double blazeMaxY = 320D;
    private void blazeSettings() {
        blazeRidable = getBoolean("mobs.blaze.ridable", blazeRidable);
        blazeRidableInWater = getBoolean("mobs.blaze.ridable-in-water", blazeRidableInWater);
        blazeControllable = getBoolean("mobs.blaze.controllable", blazeControllable);
        blazeMaxY = getDouble("mobs.blaze.ridable-max-y", blazeMaxY);
    }

    public boolean boggedRidable = false;
    public boolean boggedRidableInWater = true;
    public boolean boggedControllable = true;
    private void boggedSettings() {
        boggedRidable = getBoolean("mobs.bogged.ridable", boggedRidable);
        boggedRidableInWater = getBoolean("mobs.bogged.ridable-in-water", boggedRidableInWater);
        boggedControllable = getBoolean("mobs.bogged.controllable", boggedControllable);
    }

    public boolean camelRidableInWater = false;
    private void camelSettings() {
        camelRidableInWater = getBoolean("mobs.camel.ridable-in-water", camelRidableInWater);
    }

    public boolean catRidable = false;
    public boolean catRidableInWater = true;
    public boolean catControllable = true;
    private void catSettings() {
        catRidable = getBoolean("mobs.cat.ridable", catRidable);
        catRidableInWater = getBoolean("mobs.cat.ridable-in-water", catRidableInWater);
        catControllable = getBoolean("mobs.cat.controllable", catControllable);
    }

    public boolean caveSpiderRidable = false;
    public boolean caveSpiderRidableInWater = true;
    public boolean caveSpiderControllable = true;
    private void caveSpiderSettings() {
        caveSpiderRidable = getBoolean("mobs.cave_spider.ridable", caveSpiderRidable);
        caveSpiderRidableInWater = getBoolean("mobs.cave_spider.ridable-in-water", caveSpiderRidableInWater);
        caveSpiderControllable = getBoolean("mobs.cave_spider.controllable", caveSpiderControllable);
    }

    public boolean chickenRidable = false;
    public boolean chickenRidableInWater = false;
    public boolean chickenControllable = true;
    private void chickenSettings() {
        chickenRidable = getBoolean("mobs.chicken.ridable", chickenRidable);
        chickenRidableInWater = getBoolean("mobs.chicken.ridable-in-water", chickenRidableInWater);
        chickenControllable = getBoolean("mobs.chicken.controllable", chickenControllable);
    }

    public boolean codRidable = false;
    public boolean codControllable = true;
    private void codSettings() {
        codRidable = getBoolean("mobs.cod.ridable", codRidable);
        codControllable = getBoolean("mobs.cod.controllable", codControllable);
    }

    public boolean cowRidable = false;
    public boolean cowRidableInWater = true;
    public boolean cowControllable = true;
    private void cowSettings() {
        cowRidable = getBoolean("mobs.cow.ridable", cowRidable);
        cowRidableInWater = getBoolean("mobs.cow.ridable-in-water", cowRidableInWater);
        cowControllable = getBoolean("mobs.cow.controllable", cowControllable);
    }

    public boolean creakingRidable = false;
    public boolean creakingRidableInWater = true;
    public boolean creakingControllable = true;
    private void creakingSettings() {
        creakingRidable = getBoolean("mobs.creaking.ridable", creakingRidable);
        creakingRidableInWater = getBoolean("mobs.creaking.ridable-in-water", creakingRidableInWater);
        creakingControllable = getBoolean("mobs.creaking.controllable", creakingControllable);
    }

    public boolean creeperRidable = false;
    public boolean creeperRidableInWater = true;
    public boolean creeperControllable = true;
    private void creeperSettings() {
        creeperRidable = getBoolean("mobs.creeper.ridable", creeperRidable);
        creeperRidableInWater = getBoolean("mobs.creeper.ridable-in-water", creeperRidableInWater);
        creeperControllable = getBoolean("mobs.creeper.controllable", creeperControllable);
    }

    public boolean dolphinRidable = false;
    public boolean dolphinControllable = true;
    public int dolphinSpitCooldown = 20;
    public float dolphinSpitSpeed = 1.0F;
    public float dolphinSpitDamage = 2.0F;
    private void dolphinSettings() {
        dolphinRidable = getBoolean("mobs.dolphin.ridable", dolphinRidable);
        dolphinControllable = getBoolean("mobs.dolphin.controllable", dolphinControllable);
        dolphinSpitCooldown = getInt("mobs.dolphin.spit.cooldown", dolphinSpitCooldown);
        dolphinSpitSpeed = (float) getDouble("mobs.dolphin.spit.speed", dolphinSpitSpeed);
        dolphinSpitDamage = (float) getDouble("mobs.dolphin.spit.damage", dolphinSpitDamage);
    }

    public boolean donkeyRidableInWater = false;
    private void donkeySettings() {
        donkeyRidableInWater = getBoolean("mobs.donkey.ridable-in-water", donkeyRidableInWater);
    }

    public boolean drownedRidable = false;
    public boolean drownedRidableInWater = true;
    public boolean drownedControllable = true;
    private void drownedSettings() {
        drownedRidable = getBoolean("mobs.drowned.ridable", drownedRidable);
        drownedRidableInWater = getBoolean("mobs.drowned.ridable-in-water", drownedRidableInWater);
        drownedControllable = getBoolean("mobs.drowned.controllable", drownedControllable);
    }

    public boolean elderGuardianRidable = false;
    public boolean elderGuardianControllable = true;
    private void elderGuardianSettings() {
        elderGuardianRidable = getBoolean("mobs.elder_guardian.ridable", elderGuardianRidable);
        elderGuardianControllable = getBoolean("mobs.elder_guardian.controllable", elderGuardianControllable);
    }

    public boolean enderDragonRidable = false;
    public boolean enderDragonRidableInWater = true;
    public boolean enderDragonControllable = true;
    public double enderDragonMaxY = 320D;
    private void enderDragonSettings() {
        enderDragonRidable = getBoolean("mobs.ender_dragon.ridable", enderDragonRidable);
        enderDragonRidableInWater = getBoolean("mobs.ender_dragon.ridable-in-water", enderDragonRidableInWater);
        enderDragonControllable = getBoolean("mobs.ender_dragon.controllable", enderDragonControllable);
        enderDragonMaxY = getDouble("mobs.ender_dragon.ridable-max-y", enderDragonMaxY);
    }

    public boolean endermanRidable = false;
    public boolean endermanRidableInWater = true;
    public boolean endermanControllable = true;
    private void endermanSettings() {
        endermanRidable = getBoolean("mobs.enderman.ridable", endermanRidable);
        endermanRidableInWater = getBoolean("mobs.enderman.ridable-in-water", endermanRidableInWater);
        endermanControllable = getBoolean("mobs.enderman.controllable", endermanControllable);
    }

    public boolean endermiteRidable = false;
    public boolean endermiteRidableInWater = true;
    public boolean endermiteControllable = true;
    private void endermiteSettings() {
        endermiteRidable = getBoolean("mobs.endermite.ridable", endermiteRidable);
        endermiteRidableInWater = getBoolean("mobs.endermite.ridable-in-water", endermiteRidableInWater);
        endermiteControllable = getBoolean("mobs.endermite.controllable", endermiteControllable);
    }

    public boolean evokerRidable = false;
    public boolean evokerRidableInWater = true;
    public boolean evokerControllable = true;
    private void evokerSettings() {
        evokerRidable = getBoolean("mobs.evoker.ridable", evokerRidable);
        evokerRidableInWater = getBoolean("mobs.evoker.ridable-in-water", evokerRidableInWater);
        evokerControllable = getBoolean("mobs.evoker.controllable", evokerControllable);
    }

    public boolean foxRidable = false;
    public boolean foxRidableInWater = true;
    public boolean foxControllable = true;
    private void foxSettings() {
        foxRidable = getBoolean("mobs.fox.ridable", foxRidable);
        foxRidableInWater = getBoolean("mobs.fox.ridable-in-water", foxRidableInWater);
        foxControllable = getBoolean("mobs.fox.controllable", foxControllable);
    }

    public boolean frogRidable = false;
    public boolean frogRidableInWater = true;
    public boolean frogControllable = true;
    public float frogRidableJumpHeight = 0.65F;
    private void frogSettings() {
        frogRidable = getBoolean("mobs.frog.ridable", frogRidable);
        frogRidableInWater = getBoolean("mobs.frog.ridable-in-water", frogRidableInWater);
        frogControllable = getBoolean("mobs.frog.controllable", frogControllable);
        frogRidableJumpHeight = (float) getDouble("mobs.frog.ridable-jump-height", frogRidableJumpHeight);
    }

    public boolean ghastRidable = false;
    public boolean ghastRidableInWater = true;
    public boolean ghastControllable = true;
    public double ghastMaxY = 320D;
    private void ghastSettings() {
        ghastRidable = getBoolean("mobs.ghast.ridable", ghastRidable);
        ghastRidableInWater = getBoolean("mobs.ghast.ridable-in-water", ghastRidableInWater);
        ghastControllable = getBoolean("mobs.ghast.controllable", ghastControllable);
        ghastMaxY = getDouble("mobs.ghast.ridable-max-y", ghastMaxY);
    }

    public boolean giantRidable = false;
    public boolean giantRidableInWater = true;
    public boolean giantControllable = true;
    private void giantSettings() {
        giantRidable = getBoolean("mobs.giant.ridable", giantRidable);
        giantRidableInWater = getBoolean("mobs.giant.ridable-in-water", giantRidableInWater);
        giantControllable = getBoolean("mobs.giant.controllable", giantControllable);
    }

    public boolean glowSquidRidable = false;
    public boolean glowSquidControllable = true;
    private void glowSquidSettings() {
        glowSquidRidable = getBoolean("mobs.glow_squid.ridable", glowSquidRidable);
        glowSquidControllable = getBoolean("mobs.glow_squid.controllable", glowSquidControllable);
    }

    public boolean goatRidable = false;
    public boolean goatRidableInWater = true;
    public boolean goatControllable = true;
    private void goatSettings() {
        goatRidable = getBoolean("mobs.goat.ridable", goatRidable);
        goatRidableInWater = getBoolean("mobs.goat.ridable-in-water", goatRidableInWater);
        goatControllable = getBoolean("mobs.goat.controllable", goatControllable);
    }

    public boolean guardianRidable = false;
    public boolean guardianControllable = true;
    private void guardianSettings() {
        guardianRidable = getBoolean("mobs.guardian.ridable", guardianRidable);
        guardianControllable = getBoolean("mobs.guardian.controllable", guardianControllable);
    }

    public boolean hoglinRidable = false;
    public boolean hoglinRidableInWater = true;
    public boolean hoglinControllable = true;
    private void hoglinSettings() {
        hoglinRidable = getBoolean("mobs.hoglin.ridable", hoglinRidable);
        hoglinRidableInWater = getBoolean("mobs.hoglin.ridable-in-water", hoglinRidableInWater);
        hoglinControllable = getBoolean("mobs.hoglin.controllable", hoglinControllable);
    }

    public boolean horseRidableInWater = false;
    private void horseSettings() {
        horseRidableInWater = getBoolean("mobs.horse.ridable-in-water", horseRidableInWater);
    }

    public boolean huskRidable = false;
    public boolean huskRidableInWater = true;
    public boolean huskControllable = true;
    private void huskSettings() {
        huskRidable = getBoolean("mobs.husk.ridable", huskRidable);
        huskRidableInWater = getBoolean("mobs.husk.ridable-in-water", huskRidableInWater);
        huskControllable = getBoolean("mobs.husk.controllable", huskControllable);
    }

    public boolean illusionerRidable = false;
    public boolean illusionerRidableInWater = true;
    public boolean illusionerControllable = true;
    private void illusionerSettings() {
        illusionerRidable = getBoolean("mobs.illusioner.ridable", illusionerRidable);
        illusionerRidableInWater = getBoolean("mobs.illusioner.ridable-in-water", illusionerRidableInWater);
        illusionerControllable = getBoolean("mobs.illusioner.controllable", illusionerControllable);
    }

    public boolean ironGolemRidable = false;
    public boolean ironGolemRidableInWater = true;
    public boolean ironGolemControllable = true;
    public boolean ironGolemCanSwim = false;
    private void ironGolemSettings() {
        ironGolemRidable = getBoolean("mobs.iron_golem.ridable", ironGolemRidable);
        ironGolemRidableInWater = getBoolean("mobs.iron_golem.ridable-in-water", ironGolemRidableInWater);
        ironGolemControllable = getBoolean("mobs.iron_golem.controllable", ironGolemControllable);
        ironGolemCanSwim = getBoolean("mobs.iron_golem.can-swim", ironGolemCanSwim);
    }

    public boolean llamaRidable = false;
    public boolean llamaRidableInWater = false;
    public boolean llamaControllable = true;
    private void llamaSettings() {
        llamaRidable = getBoolean("mobs.llama.ridable", llamaRidable);
        llamaRidableInWater = getBoolean("mobs.llama.ridable-in-water", llamaRidableInWater);
        llamaControllable = getBoolean("mobs.llama.controllable", llamaControllable);
    }

    public boolean magmaCubeRidable = false;
    public boolean magmaCubeRidableInWater = true;
    public boolean magmaCubeControllable = true;
    private void magmaCubeSettings() {
        magmaCubeRidable = getBoolean("mobs.magma_cube.ridable", magmaCubeRidable);
        magmaCubeRidableInWater = getBoolean("mobs.magma_cube.ridable-in-water", magmaCubeRidableInWater);
        magmaCubeControllable = getBoolean("mobs.magma_cube.controllable", magmaCubeControllable);
    }

    public boolean mooshroomRidable = false;
    public boolean mooshroomRidableInWater = true;
    public boolean mooshroomControllable = true;
    private void mooshroomSettings() {
        mooshroomRidable = getBoolean("mobs.mooshroom.ridable", mooshroomRidable);
        mooshroomRidableInWater = getBoolean("mobs.mooshroom.ridable-in-water", mooshroomRidableInWater);
        mooshroomControllable = getBoolean("mobs.mooshroom.controllable", mooshroomControllable);
    }

    public boolean muleRidableInWater = false;
    private void muleSettings() {
        muleRidableInWater = getBoolean("mobs.mule.ridable-in-water", muleRidableInWater);
    }

    public boolean ocelotRidable = false;
    public boolean ocelotRidableInWater = true;
    public boolean ocelotControllable = true;
    private void ocelotSettings() {
        ocelotRidable = getBoolean("mobs.ocelot.ridable", ocelotRidable);
        ocelotRidableInWater = getBoolean("mobs.ocelot.ridable-in-water", ocelotRidableInWater);
        ocelotControllable = getBoolean("mobs.ocelot.controllable", ocelotControllable);
    }

    public boolean pandaRidable = false;
    public boolean pandaRidableInWater = true;
    public boolean pandaControllable = true;
    private void pandaSettings() {
        pandaRidable = getBoolean("mobs.panda.ridable", pandaRidable);
        pandaRidableInWater = getBoolean("mobs.panda.ridable-in-water", pandaRidableInWater);
        pandaControllable = getBoolean("mobs.panda.controllable", pandaControllable);
    }

    public boolean parrotRidable = false;
    public boolean parrotRidableInWater = true;
    public boolean parrotControllable = true;
    public double parrotMaxY = 320D;
    private void parrotSettings() {
        parrotRidable = getBoolean("mobs.parrot.ridable", parrotRidable);
        parrotRidableInWater = getBoolean("mobs.parrot.ridable-in-water", parrotRidableInWater);
        parrotControllable = getBoolean("mobs.parrot.controllable", parrotControllable);
        parrotMaxY = getDouble("mobs.parrot.ridable-max-y", parrotMaxY);
    }

    public boolean phantomRidable = false;
    public boolean phantomRidableInWater = true;
    public boolean phantomControllable = true;
    public double phantomMaxY = 320D;
    public float phantomFlameDamage = 1.0F;
    public int phantomFlameFireTime = 8;
    public boolean phantomAllowGriefing = false;
    private void phantomSettings() {
        phantomRidable = getBoolean("mobs.phantom.ridable", phantomRidable);
        phantomRidableInWater = getBoolean("mobs.phantom.ridable-in-water", phantomRidableInWater);
        phantomControllable = getBoolean("mobs.phantom.controllable", phantomControllable);
        phantomMaxY = getDouble("mobs.phantom.ridable-max-y", phantomMaxY);
        phantomFlameDamage = (float) getDouble("mobs.phantom.flames.damage", phantomFlameDamage);
        phantomFlameFireTime = getInt("mobs.phantom.flames.fire-time", phantomFlameFireTime);
        phantomAllowGriefing = getBoolean("mobs.phantom.allow-griefing", phantomAllowGriefing);
    }

    public boolean pigRidable = false;
    public boolean pigRidableInWater = false;
    public boolean pigControllable = true;
    private void pigSettings() {
        pigRidable = getBoolean("mobs.pig.ridable", pigRidable);
        pigRidableInWater = getBoolean("mobs.pig.ridable-in-water", pigRidableInWater);
        pigControllable = getBoolean("mobs.pig.controllable", pigControllable);
    }

    public boolean piglinRidable = false;
    public boolean piglinRidableInWater = true;
    public boolean piglinControllable = true;
    private void piglinSettings() {
        piglinRidable = getBoolean("mobs.piglin.ridable", piglinRidable);
        piglinRidableInWater = getBoolean("mobs.piglin.ridable-in-water", piglinRidableInWater);
        piglinControllable = getBoolean("mobs.piglin.controllable", piglinControllable);
    }

    public boolean piglinBruteRidable = false;
    public boolean piglinBruteRidableInWater = true;
    public boolean piglinBruteControllable = true;
    private void piglinBruteSettings() {
        piglinBruteRidable = getBoolean("mobs.piglin_brute.ridable", piglinBruteRidable);
        piglinBruteRidableInWater = getBoolean("mobs.piglin_brute.ridable-in-water", piglinBruteRidableInWater);
        piglinBruteControllable = getBoolean("mobs.piglin_brute.controllable", piglinBruteControllable);
    }

    public boolean pillagerRidable = false;
    public boolean pillagerRidableInWater = true;
    public boolean pillagerControllable = true;
    private void pillagerSettings() {
        pillagerRidable = getBoolean("mobs.pillager.ridable", pillagerRidable);
        pillagerRidableInWater = getBoolean("mobs.pillager.ridable-in-water", pillagerRidableInWater);
        pillagerControllable = getBoolean("mobs.pillager.controllable", pillagerControllable);
    }

    public boolean polarBearRidable = false;
    public boolean polarBearRidableInWater = true;
    public boolean polarBearControllable = true;
    private void polarBearSettings() {
        polarBearRidable = getBoolean("mobs.polar_bear.ridable", polarBearRidable);
        polarBearRidableInWater = getBoolean("mobs.polar_bear.ridable-in-water", polarBearRidableInWater);
        polarBearControllable = getBoolean("mobs.polar_bear.controllable", polarBearControllable);
    }

    public boolean pufferfishRidable = false;
    public boolean pufferfishControllable = true;
    private void pufferfishSettings() {
        pufferfishRidable = getBoolean("mobs.pufferfish.ridable", pufferfishRidable);
        pufferfishControllable = getBoolean("mobs.pufferfish.controllable", pufferfishControllable);
    }

    public boolean rabbitRidable = false;
    public boolean rabbitRidableInWater = true;
    public boolean rabbitControllable = true;
    private void rabbitSettings() {
        rabbitRidable = getBoolean("mobs.rabbit.ridable", rabbitRidable);
        rabbitRidableInWater = getBoolean("mobs.rabbit.ridable-in-water", rabbitRidableInWater);
        rabbitControllable = getBoolean("mobs.rabbit.controllable", rabbitControllable);
    }

    public boolean ravagerRidable = false;
    public boolean ravagerRidableInWater = false;
    public boolean ravagerControllable = true;
    private void ravagerSettings() {
        ravagerRidable = getBoolean("mobs.ravager.ridable", ravagerRidable);
        ravagerRidableInWater = getBoolean("mobs.ravager.ridable-in-water", ravagerRidableInWater);
        ravagerControllable = getBoolean("mobs.ravager.controllable", ravagerControllable);
    }

    public boolean salmonRidable = false;
    public boolean salmonControllable = true;
    private void salmonSettings() {
        salmonRidable = getBoolean("mobs.salmon.ridable", salmonRidable);
        salmonControllable = getBoolean("mobs.salmon.controllable", salmonControllable);
    }

    public boolean sheepRidable = false;
    public boolean sheepRidableInWater = true;
    public boolean sheepControllable = true;
    private void sheepSettings() {
        sheepRidable = getBoolean("mobs.sheep.ridable", sheepRidable);
        sheepRidableInWater = getBoolean("mobs.sheep.ridable-in-water", sheepRidableInWater);
        sheepControllable = getBoolean("mobs.sheep.controllable", sheepControllable);
    }

    public boolean shulkerRidable = false;
    public boolean shulkerRidableInWater = true;
    public boolean shulkerControllable = true;
    private void shulkerSettings() {
        shulkerRidable = getBoolean("mobs.shulker.ridable", shulkerRidable);
        shulkerRidableInWater = getBoolean("mobs.shulker.ridable-in-water", shulkerRidableInWater);
        shulkerControllable = getBoolean("mobs.shulker.controllable", shulkerControllable);
    }

    public boolean silverfishRidable = false;
    public boolean silverfishRidableInWater = true;
    public boolean silverfishControllable = true;
    private void silverfishSettings() {
        silverfishRidable = getBoolean("mobs.silverfish.ridable", silverfishRidable);
        silverfishRidableInWater = getBoolean("mobs.silverfish.ridable-in-water", silverfishRidableInWater);
        silverfishControllable = getBoolean("mobs.silverfish.controllable", silverfishControllable);
    }

    public boolean skeletonRidable = false;
    public boolean skeletonRidableInWater = true;
    public boolean skeletonControllable = true;
    private void skeletonSettings() {
        skeletonRidable = getBoolean("mobs.skeleton.ridable", skeletonRidable);
        skeletonRidableInWater = getBoolean("mobs.skeleton.ridable-in-water", skeletonRidableInWater);
        skeletonControllable = getBoolean("mobs.skeleton.controllable", skeletonControllable);
    }

    public boolean skeletonHorseRidable = false;
    public boolean skeletonHorseRidableInWater = true;
    public boolean skeletonHorseCanSwim = false;
    private void skeletonHorseSettings() {
        skeletonHorseRidable = getBoolean("mobs.skeleton_horse.ridable", skeletonHorseRidable);
        skeletonHorseRidableInWater = getBoolean("mobs.skeleton_horse.ridable-in-water", skeletonHorseRidableInWater);
        skeletonHorseCanSwim = getBoolean("mobs.skeleton_horse.can-swim", skeletonHorseCanSwim);
    }

    public boolean slimeRidable = false;
    public boolean slimeRidableInWater = true;
    public boolean slimeControllable = true;
    private void slimeSettings() {
        slimeRidable = getBoolean("mobs.slime.ridable", slimeRidable);
        slimeRidableInWater = getBoolean("mobs.slime.ridable-in-water", slimeRidableInWater);
        slimeControllable = getBoolean("mobs.slime.controllable", slimeControllable);
    }

    public boolean snowGolemRidable = false;
    public boolean snowGolemRidableInWater = true;
    public boolean snowGolemControllable = true;
    public boolean snowGolemLeaveTrailWhenRidden = false;
    private void snowGolemSettings() {
        snowGolemRidable = getBoolean("mobs.snow_golem.ridable", snowGolemRidable);
        snowGolemRidableInWater = getBoolean("mobs.snow_golem.ridable-in-water", snowGolemRidableInWater);
        snowGolemControllable = getBoolean("mobs.snow_golem.controllable", snowGolemControllable);
        snowGolemLeaveTrailWhenRidden = getBoolean("mobs.snow_golem.leave-trail-when-ridden", snowGolemLeaveTrailWhenRidden);
    }

    public boolean snifferRidable = false;
    public boolean snifferRidableInWater = true;
    public boolean snifferControllable = true;
    private void snifferSettings() {
        snifferRidable = getBoolean("mobs.sniffer.ridable", snifferRidable);
        snifferRidableInWater = getBoolean("mobs.sniffer.ridable-in-water", snifferRidableInWater);
        snifferControllable = getBoolean("mobs.sniffer.controllable", snifferControllable);
    }

    public boolean squidRidable = false;
    public boolean squidControllable = true;
    private void squidSettings() {
        squidRidable = getBoolean("mobs.squid.ridable", squidRidable);
        squidControllable = getBoolean("mobs.squid.controllable", squidControllable);
    }

    public boolean spiderRidable = false;
    public boolean spiderRidableInWater = false;
    public boolean spiderControllable = true;
    private void spiderSettings() {
        spiderRidable = getBoolean("mobs.spider.ridable", spiderRidable);
        spiderRidableInWater = getBoolean("mobs.spider.ridable-in-water", spiderRidableInWater);
        spiderControllable = getBoolean("mobs.spider.controllable", spiderControllable);
    }

    public boolean strayRidable = false;
    public boolean strayRidableInWater = true;
    public boolean strayControllable = true;
    private void straySettings() {
        strayRidable = getBoolean("mobs.stray.ridable", strayRidable);
        strayRidableInWater = getBoolean("mobs.stray.ridable-in-water", strayRidableInWater);
        strayControllable = getBoolean("mobs.stray.controllable", strayControllable);
    }

    public boolean striderRidable = false;
    public boolean striderRidableInWater = false;
    public boolean striderControllable = true;
    private void striderSettings() {
        striderRidable = getBoolean("mobs.strider.ridable", striderRidable);
        striderRidableInWater = getBoolean("mobs.strider.ridable-in-water", striderRidableInWater);
        striderControllable = getBoolean("mobs.strider.controllable", striderControllable);
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
    private void traderLlamaSettings() {
        traderLlamaRidable = getBoolean("mobs.trader_llama.ridable", traderLlamaRidable);
        traderLlamaRidableInWater = getBoolean("mobs.trader_llama.ridable-in-water", traderLlamaRidableInWater);
        traderLlamaControllable = getBoolean("mobs.trader_llama.controllable", traderLlamaControllable);
    }

    public boolean tropicalFishRidable = false;
    public boolean tropicalFishControllable = true;
    private void tropicalFishSettings() {
        tropicalFishRidable = getBoolean("mobs.tropical_fish.ridable", tropicalFishRidable);
        tropicalFishControllable = getBoolean("mobs.tropical_fish.controllable", tropicalFishControllable);
    }

    public boolean turtleRidable = false;
    public boolean turtleRidableInWater = true;
    public boolean turtleControllable = true;
    private void turtleSettings() {
        turtleRidable = getBoolean("mobs.turtle.ridable", turtleRidable);
        turtleRidableInWater = getBoolean("mobs.turtle.ridable-in-water", turtleRidableInWater);
        turtleControllable = getBoolean("mobs.turtle.controllable", turtleControllable);
    }

    public boolean vexRidable = false;
    public boolean vexRidableInWater = true;
    public boolean vexControllable = true;
    public double vexMaxY = 320D;
    private void vexSettings() {
        vexRidable = getBoolean("mobs.vex.ridable", vexRidable);
        vexRidableInWater = getBoolean("mobs.vex.ridable-in-water", vexRidableInWater);
        vexControllable = getBoolean("mobs.vex.controllable", vexControllable);
        vexMaxY = getDouble("mobs.vex.ridable-max-y", vexMaxY);
    }

    public boolean villagerRidable = false;
    public boolean villagerRidableInWater = true;
    public boolean villagerControllable = true;
    private void villagerSettings() {
        villagerRidable = getBoolean("mobs.villager.ridable", villagerRidable);
        villagerRidableInWater = getBoolean("mobs.villager.ridable-in-water", villagerRidableInWater);
        villagerControllable = getBoolean("mobs.villager.controllable", villagerControllable);
    }

    public boolean vindicatorRidable = false;
    public boolean vindicatorRidableInWater = true;
    public boolean vindicatorControllable = true;
    private void vindicatorSettings() {
        vindicatorRidable = getBoolean("mobs.vindicator.ridable", vindicatorRidable);
        vindicatorRidableInWater = getBoolean("mobs.vindicator.ridable-in-water", vindicatorRidableInWater);
        vindicatorControllable = getBoolean("mobs.vindicator.controllable", vindicatorControllable);
    }

    public boolean wanderingTraderRidable = false;
    public boolean wanderingTraderRidableInWater = true;
    public boolean wanderingTraderControllable = true;
    private void wanderingTraderSettings() {
        wanderingTraderRidable = getBoolean("mobs.wandering_trader.ridable", wanderingTraderRidable);
        wanderingTraderRidableInWater = getBoolean("mobs.wandering_trader.ridable-in-water", wanderingTraderRidableInWater);
        wanderingTraderControllable = getBoolean("mobs.wandering_trader.controllable", wanderingTraderControllable);
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
    private void witchSettings() {
        witchRidable = getBoolean("mobs.witch.ridable", witchRidable);
        witchRidableInWater = getBoolean("mobs.witch.ridable-in-water", witchRidableInWater);
        witchControllable = getBoolean("mobs.witch.controllable", witchControllable);
    }

    public boolean witherRidable = false;
    public boolean witherRidableInWater = true;
    public boolean witherControllable = true;
    public double witherMaxY = 320D;
    private void witherSettings() {
        witherRidable = getBoolean("mobs.wither.ridable", witherRidable);
        witherRidableInWater = getBoolean("mobs.wither.ridable-in-water", witherRidableInWater);
        witherControllable = getBoolean("mobs.wither.controllable", witherControllable);
        witherMaxY = getDouble("mobs.wither.ridable-max-y", witherMaxY);
    }

    public boolean witherSkeletonRidable = false;
    public boolean witherSkeletonRidableInWater = true;
    public boolean witherSkeletonControllable = true;
    private void witherSkeletonSettings() {
        witherSkeletonRidable = getBoolean("mobs.wither_skeleton.ridable", witherSkeletonRidable);
        witherSkeletonRidableInWater = getBoolean("mobs.wither_skeleton.ridable-in-water", witherSkeletonRidableInWater);
        witherSkeletonControllable = getBoolean("mobs.wither_skeleton.controllable", witherSkeletonControllable);
    }

    public boolean wolfRidable = false;
    public boolean wolfRidableInWater = true;
    public boolean wolfControllable = true;
    private void wolfSettings() {
        wolfRidable = getBoolean("mobs.wolf.ridable", wolfRidable);
        wolfRidableInWater = getBoolean("mobs.wolf.ridable-in-water", wolfRidableInWater);
        wolfControllable = getBoolean("mobs.wolf.controllable", wolfControllable);
    }

    public boolean zoglinRidable = false;
    public boolean zoglinRidableInWater = true;
    public boolean zoglinControllable = true;
    private void zoglinSettings() {
        zoglinRidable = getBoolean("mobs.zoglin.ridable", zoglinRidable);
        zoglinRidableInWater = getBoolean("mobs.zoglin.ridable-in-water", zoglinRidableInWater);
        zoglinControllable = getBoolean("mobs.zoglin.controllable", zoglinControllable);
    }

    public boolean zombieRidable = false;
    public boolean zombieRidableInWater = true;
    public boolean zombieControllable = true;
    private void zombieSettings() {
        zombieRidable = getBoolean("mobs.zombie.ridable", zombieRidable);
        zombieRidableInWater = getBoolean("mobs.zombie.ridable-in-water", zombieRidableInWater);
        zombieControllable = getBoolean("mobs.zombie.controllable", zombieControllable);
    }

    public boolean zombieHorseRidable = false;
    public boolean zombieHorseRidableInWater = false;
    public boolean zombieHorseCanSwim = false;
    private void zombieHorseSettings() {
        zombieHorseRidable = getBoolean("mobs.zombie_horse.ridable", zombieHorseRidable);
        zombieHorseRidableInWater = getBoolean("mobs.zombie_horse.ridable-in-water", zombieHorseRidableInWater);
        zombieHorseCanSwim = getBoolean("mobs.zombie_horse.can-swim", zombieHorseCanSwim);
    }

    public boolean zombieVillagerRidable = false;
    public boolean zombieVillagerRidableInWater = true;
    public boolean zombieVillagerControllable = true;
    private void zombieVillagerSettings() {
        zombieVillagerRidable = getBoolean("mobs.zombie_villager.ridable", zombieVillagerRidable);
        zombieVillagerRidableInWater = getBoolean("mobs.zombie_villager.ridable-in-water", zombieVillagerRidableInWater);
        zombieVillagerControllable = getBoolean("mobs.zombie_villager.controllable", zombieVillagerControllable);
    }

    public boolean zombifiedPiglinRidable = false;
    public boolean zombifiedPiglinRidableInWater = true;
    public boolean zombifiedPiglinControllable = true;
    private void zombifiedPiglinSettings() {
        zombifiedPiglinRidable = getBoolean("mobs.zombified_piglin.ridable", zombifiedPiglinRidable);
        zombifiedPiglinRidableInWater = getBoolean("mobs.zombified_piglin.ridable-in-water", zombifiedPiglinRidableInWater);
        zombifiedPiglinControllable = getBoolean("mobs.zombified_piglin.controllable", zombifiedPiglinControllable);
    }
}
