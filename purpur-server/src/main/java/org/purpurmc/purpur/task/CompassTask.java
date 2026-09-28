package org.purpurmc.purpur.task;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.purpurmc.purpur.PurpurConfig;

public class CompassTask extends BossBarTask {
    private static CompassTask instance;

    private int tick = 0;

    public static CompassTask instance() {
        if (instance == null) {
            instance = new CompassTask();
        }
        return instance;
    }

    @Override
    public void run() {
        if (++tick < PurpurConfig.commandCompassBarTickInterval) {
            return;
        }
        tick = 0;

        MinecraftServer.getServer().getAllLevels().forEach((level) -> {
            if (level.purpurConfig.compassItemShowsBossBar) {
                level.players().forEach(player -> {
                    if (!player.compassBar()) {
                        if (player.getMainHandItem().getItem() != Items.COMPASS && player.getOffhandItem().getItem() != Items.COMPASS) {
                            removePlayer(player.getBukkitEntity());
                        } else if (!hasPlayer(player.getUUID())) {
                            addPlayer(player.getBukkitEntity());
                        }
                    }
                });
            }
        });

        super.run();
    }

    @Override
    BossBar createBossBar() {
        return BossBar.bossBar(Component.text(""), PurpurConfig.commandCompassBarProgressPercent, PurpurConfig.commandCompassBarProgressColor, PurpurConfig.commandCompassBarProgressOverlay);
    }

    @Override
    void updateBossBar(BossBar bossbar, Player player) {
        float yaw = player.getLocation().getYaw();
        int length = PurpurConfig.commandCompassBarTitle.length();
        int pos = (int) ((normalize(yaw) * (length / 720F)) + (length / 2F));
        bossbar.name(Component.text(PurpurConfig.commandCompassBarTitle.substring(pos - 25, pos + 25)));
    }

    private float normalize(float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }
        while (yaw > 180.0F) {
            yaw -= 360.0F;
        }
        return yaw;
    }
}
