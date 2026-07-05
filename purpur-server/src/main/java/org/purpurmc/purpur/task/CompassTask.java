package org.purpurmc.purpur.task;

import net.kyori.adventure.bossbar.BossBar;
import io.papermc.paper.adventure.PaperAdventure;
import net.minecraft.server.level.ServerBossEvent;

import java.util.UUID;
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
    ServerBossEvent createBossBar(UUID id) {
        ServerBossEvent bossbar = new ServerBossEvent(id, net.minecraft.network.chat.Component.empty(), toVanillaColor(PurpurConfig.commandCompassBarProgressColor), toVanillaOverlay(PurpurConfig.commandCompassBarProgressOverlay));
        bossbar.setProgress(PurpurConfig.commandCompassBarProgressPercent);
        return bossbar;
    }

    @Override
    void updateBossBar(ServerBossEvent bossbar, Player player) {
        float yaw = player.getLocation().getYaw();
        int length = PurpurConfig.commandCompassBarTitle.length();
        int pos = (int) ((normalize(yaw) * (length / 720F)) + (length / 2F));
        bossbar.setName(PaperAdventure.asVanilla(net.kyori.adventure.text.Component.text(PurpurConfig.commandCompassBarTitle.substring(pos - 25, pos + 25))));
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
