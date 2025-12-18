package org.purpurmc.purpur.configuration.transformation;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.purpurmc.purpur.PurpurConfig;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public class FarEndTerrainGenerationMigration implements TransformAction {

    public static boolean HAS_BEEN_REGISTERED = false;

    public static final String MISC_KEY = "misc";
    public static final String FIX_FAR_END_TERRAIN_GENERATION_KEY = "fix-far-end-terrain-generation";

    @Override
    public Object @Nullable [] visitPath(final NodePath path, final ConfigurationNode value) throws ConfigurateException {
        String purpurGenerateEndVoidRingsPath = "settings.generate-end-void-rings";
        ConfigurationNode fixFarEndTerrainGenerationNode = value.node(MISC_KEY, FIX_FAR_END_TERRAIN_GENERATION_KEY);
        if (PurpurConfig.config.contains(purpurGenerateEndVoidRingsPath)) {
            boolean purpurGenerateEndVoidRings = PurpurConfig.config.getBoolean(purpurGenerateEndVoidRingsPath);
            if (purpurGenerateEndVoidRings) {
                fixFarEndTerrainGenerationNode.set(false);
            }
            PurpurConfig.config.set(purpurGenerateEndVoidRingsPath, null);
        }

        return null;
    }

    public static void apply(final ConfigurationTransformation.Builder builder) {
        if (PurpurConfig.version < 46) {
            HAS_BEEN_REGISTERED = true;
            builder.addAction(path(), new FarEndTerrainGenerationMigration());
        }
    }
}
