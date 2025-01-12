package org.purpurmc.purpur;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.purpurmc.purpur.command.PurpurCommand;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.purpurmc.purpur.task.TPSBarTask;

@SuppressWarnings("unused")
public class PurpurConfig {
    private static final String HEADER = "This is the main configuration file for Purpur.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Purpur,\n"
            + "join us in our Discord guild.\n"
            + "\n"
            + "Website: https://purpurmc.org \n"
            + "Docs: https://purpurmc.org/docs \n";
    private static File CONFIG_FILE;
    public static YamlConfiguration config;

    private static Map<String, Command> commands;

    public static int version;
    static boolean verbose;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load purpur.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);
        verbose = getBoolean("verbose", false);

        commands = new HashMap<>();
        commands.put("purpur", new PurpurCommand("purpur"));

        version = getInt("config-version", 38);
        set("config-version", 38);

        readConfig(PurpurConfig.class, null);

        Block.BLOCK_STATE_REGISTRY.forEach(BlockBehaviour.BlockStateBase::initCache);
    }

    protected static void log(String s) {
        if (verbose) {
            log(Level.INFO, s);
        }
    }

    protected static void log(Level level, String s) {
        Bukkit.getLogger().log(level, s);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Purpur", entry.getValue());
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.addDefault(path, val);
        config.set(path, val);
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List<?> getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    static Map<String, Object> getMap(String path, Map<String, Object> def) {
        if (def != null && config.getConfigurationSection(path) == null) {
            config.addDefault(path, def);
            return def;
        }
        return toMap(config.getConfigurationSection(path));
    }

    private static Map<String, Object> toMap(ConfigurationSection section) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Object obj = section.get(key);
                if (obj != null) {
                    builder.put(key, obj instanceof ConfigurationSection val ? toMap(val) : obj);
                }
            }
        }
        return builder.build();
    }

    public static String cannotRideMob = "<red>You cannot mount that mob";
    public static String afkBroadcastAway = "<yellow><italic>%s is now AFK";
    public static String afkBroadcastBack = "<yellow><italic>%s is no longer AFK";
    public static boolean afkBroadcastUseDisplayName = false;
    public static String afkTabListPrefix = "[AFK] ";
    public static String afkTabListSuffix = "";
    public static String creditsCommandOutput = "<green>%s has been shown the end credits";
    public static String demoCommandOutput = "<green>%s has been shown the demo screen";
    public static String pingCommandOutput = "<green>%s's ping is %sms";
    public static String tpsbarCommandOutput = "<green>Tpsbar toggled <onoff> for <target>";
    public static String dontRunWithScissors = "<red><italic>Don't run with scissors!";
    private static void messages() {
        cannotRideMob = getString("settings.messages.cannot-ride-mob", cannotRideMob);
        afkBroadcastAway = getString("settings.messages.afk-broadcast-away", afkBroadcastAway);
        afkBroadcastBack = getString("settings.messages.afk-broadcast-back", afkBroadcastBack);
        afkBroadcastUseDisplayName = getBoolean("settings.messages.afk-broadcast-use-display-name", afkBroadcastUseDisplayName);
        afkTabListPrefix = MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize(getString("settings.messages.afk-tab-list-prefix", afkTabListPrefix)));
        afkTabListSuffix = MiniMessage.miniMessage().serialize(MiniMessage.miniMessage().deserialize(getString("settings.messages.afk-tab-list-suffix", afkTabListSuffix)));
        creditsCommandOutput = getString("settings.messages.credits-command-output", creditsCommandOutput);
        demoCommandOutput = getString("settings.messages.demo-command-output", demoCommandOutput);
        pingCommandOutput = getString("settings.messages.ping-command-output", pingCommandOutput);
        tpsbarCommandOutput = getString("settings.messages.tpsbar-command-output", tpsbarCommandOutput);
        dontRunWithScissors = getString("settings.messages.dont-run-with-scissors", dontRunWithScissors);
    }

    public static String deathMsgRunWithScissors = "<player> slipped and fell on their shears";
    private static void deathMessages() {
        deathMsgRunWithScissors = getString("settings.messages.death-message.run-with-scissors", deathMsgRunWithScissors);
    }

    public static String serverModName = io.papermc.paper.ServerBuildInfo.buildInfo().brandName();
    private static void serverModName() {
        serverModName = getString("settings.server-mod-name", serverModName);
    }

    public static double laggingThreshold = 19.0D;
    private static void tickLoopSettings() {
        laggingThreshold = getDouble("settings.lagging-threshold", laggingThreshold);
    }

    public static boolean useAlternateKeepAlive = false;
    private static void useAlternateKeepAlive() {
        useAlternateKeepAlive = getBoolean("settings.use-alternate-keepalive", useAlternateKeepAlive);
    }

    public static boolean disableGiveCommandDrops = false;
    private static void disableGiveCommandDrops() {
        disableGiveCommandDrops = getBoolean("settings.disable-give-dropping", disableGiveCommandDrops);
    }

    public static String commandTPSBarTitle = "<gray>TPS<yellow>:</yellow> <tps> MSPT<yellow>:</yellow> <mspt> Ping<yellow>:</yellow> <ping>ms";
    public static BossBar.Overlay commandTPSBarProgressOverlay = BossBar.Overlay.NOTCHED_20;
    public static TPSBarTask.FillMode commandTPSBarProgressFillMode = TPSBarTask.FillMode.MSPT;
    public static BossBar.Color commandTPSBarProgressColorGood = BossBar.Color.GREEN;
    public static BossBar.Color commandTPSBarProgressColorMedium = BossBar.Color.YELLOW;
    public static BossBar.Color commandTPSBarProgressColorLow = BossBar.Color.RED;
    public static String commandTPSBarTextColorGood = "<gradient:#55ff55:#00aa00><text></gradient>";
    public static String commandTPSBarTextColorMedium = "<gradient:#ffff55:#ffaa00><text></gradient>";
    public static String commandTPSBarTextColorLow = "<gradient:#ff5555:#aa0000><text></gradient>";
    public static int commandTPSBarTickInterval = 20;
    public static boolean commandGamemodeRequiresPermission = false;
    private static void commandSettings() {
        commandTPSBarTitle = getString("settings.command.tpsbar.title", commandTPSBarTitle);
        commandTPSBarProgressOverlay = BossBar.Overlay.valueOf(getString("settings.command.tpsbar.overlay", commandTPSBarProgressOverlay.name()));
        commandTPSBarProgressFillMode = TPSBarTask.FillMode.valueOf(getString("settings.command.tpsbar.fill-mode", commandTPSBarProgressFillMode.name()));
        commandTPSBarProgressColorGood = BossBar.Color.valueOf(getString("settings.command.tpsbar.progress-color.good", commandTPSBarProgressColorGood.name()));
        commandTPSBarProgressColorMedium = BossBar.Color.valueOf(getString("settings.command.tpsbar.progress-color.medium", commandTPSBarProgressColorMedium.name()));
        commandTPSBarProgressColorLow = BossBar.Color.valueOf(getString("settings.command.tpsbar.progress-color.low", commandTPSBarProgressColorLow.name()));
        commandTPSBarTextColorGood = getString("settings.command.tpsbar.text-color.good", commandTPSBarTextColorGood);
        commandTPSBarTextColorMedium = getString("settings.command.tpsbar.text-color.medium", commandTPSBarTextColorMedium);
        commandTPSBarTextColorLow = getString("settings.command.tpsbar.text-color.low", commandTPSBarTextColorLow);
        commandTPSBarTickInterval = getInt("settings.command.tpsbar.tick-interval", commandTPSBarTickInterval);
        commandGamemodeRequiresPermission = getBoolean("settings.command.gamemode.requires-specific-permission", commandGamemodeRequiresPermission);
    }

    public static int barrelRows = 3;
    public static boolean enderChestSixRows = false;
    public static boolean enderChestPermissionRows = false;
    public static boolean cryingObsidianValidForPortalFrame = false;
    public static int beeInsideBeeHive = 3;
    private static void blockSettings() {
        if (version < 3) {
            boolean oldValue = getBoolean("settings.barrel.packed-barrels", true);
            set("settings.blocks.barrel.six-rows", oldValue);
            set("settings.packed-barrels", null);
            oldValue = getBoolean("settings.large-ender-chests", true);
            set("settings.blocks.ender_chest.six-rows", oldValue);
            set("settings.large-ender-chests", null);
        }
        if (version < 20) {
            boolean oldValue = getBoolean("settings.blocks.barrel.six-rows", false);
            set("settings.blocks.barrel.rows", oldValue ? 6 : 3);
            set("settings.blocks.barrel.six-rows", null);
        }
        barrelRows = getInt("settings.blocks.barrel.rows", barrelRows);
        if (barrelRows < 1 || barrelRows > 6) {
            Bukkit.getLogger().severe("settings.blocks.barrel.rows must be 1-6, resetting to default");
            barrelRows = 3;
        }
        org.bukkit.event.inventory.InventoryType.BARREL.setDefaultSize(switch (barrelRows) {
            case 6 -> 54;
            case 5 -> 45;
            case 4 -> 36;
            case 2 -> 18;
            case 1 -> 9;
            default -> 27;
        });
        enderChestSixRows = getBoolean("settings.blocks.ender_chest.six-rows", enderChestSixRows);
        org.bukkit.event.inventory.InventoryType.ENDER_CHEST.setDefaultSize(enderChestSixRows ? 54 : 27);
        enderChestPermissionRows = getBoolean("settings.blocks.ender_chest.use-permissions-for-rows", enderChestPermissionRows);
        cryingObsidianValidForPortalFrame = getBoolean("settings.blocks.crying_obsidian.valid-for-portal-frame", cryingObsidianValidForPortalFrame);
        beeInsideBeeHive = getInt("settings.blocks.beehive.max-bees-inside", beeInsideBeeHive);
    }

    public static boolean allowInapplicableEnchants = false;
    public static boolean allowIncompatibleEnchants = false;
    public static boolean allowHigherEnchantsLevels = false;
    public static boolean allowUnsafeEnchantCommand = false;
    public static boolean replaceIncompatibleEnchants = false;
    private static void enchantmentSettings() {
        if (version < 30) {
            boolean oldValue = getBoolean("settings.enchantment.allow-unsafe-enchants", false);
            set("settings.enchantment.anvil.allow-unsafe-enchants", oldValue);
            set("settings.enchantment.anvil.allow-inapplicable-enchants", true);
            set("settings.enchantment.anvil.allow-incompatible-enchants", true);
            set("settings.enchantment.anvil.allow-higher-enchants-levels", true);
            set("settings.enchantment.allow-unsafe-enchants", null);
        }
        if (version < 37) {
            boolean allowUnsafeEnchants = getBoolean("settings.enchantment.anvil.allow-unsafe-enchants", false);
            if (!allowUnsafeEnchants) {
                set("settings.enchantment.anvil.allow-inapplicable-enchants", false);
                set("settings.enchantment.anvil.allow-incompatible-enchants", false);
                set("settings.enchantment.anvil.allow-higher-enchants-levels", false);
            }
            set("settings.enchantment.anvil.allow-unsafe-enchants", null);
        }
        allowInapplicableEnchants = getBoolean("settings.enchantment.anvil.allow-inapplicable-enchants", allowInapplicableEnchants);
        allowIncompatibleEnchants = getBoolean("settings.enchantment.anvil.allow-incompatible-enchants", allowIncompatibleEnchants);
        allowHigherEnchantsLevels = getBoolean("settings.enchantment.anvil.allow-higher-enchants-levels", allowHigherEnchantsLevels);
        allowUnsafeEnchantCommand = getBoolean("settings.enchantment.allow-unsafe-enchant-command", allowUnsafeEnchantCommand);
        replaceIncompatibleEnchants = getBoolean("settings.enchantment.anvil.replace-incompatible-enchants", replaceIncompatibleEnchants);
    }

    public static boolean endermanShortHeight = false;
    private static void entitySettings() {
        endermanShortHeight = getBoolean("settings.entity.enderman.short-height", endermanShortHeight);
        if (endermanShortHeight) EntityType.ENDERMAN.dimensions = EntityDimensions.scalable(0.6F, 1.9F);
    }

    public static boolean allowWaterPlacementInTheEnd = true;
    private static void allowWaterPlacementInEnd() {
        allowWaterPlacementInTheEnd = getBoolean("settings.allow-water-placement-in-the-end", allowWaterPlacementInTheEnd);
    }

    public static boolean loggerSuppressInitLegacyMaterialError = false;
    public static boolean loggerSuppressIgnoredAdvancementWarnings = false;
    public static boolean loggerSuppressUnrecognizedRecipeErrors = false;
    public static boolean loggerSuppressSetBlockFarChunk = false;
    private static void loggerSettings() {
        loggerSuppressInitLegacyMaterialError = getBoolean("settings.logger.suppress-init-legacy-material-errors", loggerSuppressInitLegacyMaterialError);
        loggerSuppressIgnoredAdvancementWarnings = getBoolean("settings.logger.suppress-ignored-advancement-warnings", loggerSuppressIgnoredAdvancementWarnings);
        loggerSuppressUnrecognizedRecipeErrors = getBoolean("settings.logger.suppress-unrecognized-recipe-errors", loggerSuppressUnrecognizedRecipeErrors);
        loggerSuppressSetBlockFarChunk = getBoolean("settings.logger.suppress-setblock-in-far-chunk-errors", loggerSuppressSetBlockFarChunk);
    }

    public static boolean tpsCatchup = true;
    private static void tpsCatchup() {
        tpsCatchup = getBoolean("settings.tps-catchup", tpsCatchup);
    }
}
