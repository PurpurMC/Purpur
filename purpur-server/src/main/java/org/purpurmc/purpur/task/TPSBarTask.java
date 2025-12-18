package org.purpurmc.purpur.task;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.purpurmc.purpur.PurpurConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TPSBarTask extends BossBarTask {
    private static TPSBarTask instance;
    private double tps = 20.0D;
    private double mspt = 0.0D;
    private int tick = 0;

    public static TPSBarTask instance() {
        if (instance == null) {
            instance = new TPSBarTask();
        }
        return instance;
    }

    @Override
    BossBar createBossBar() {
        return BossBar.bossBar(Component.text(""), 0.0F, instance().getBossBarColor(), PurpurConfig.commandTPSBarProgressOverlay);
    }

    @Override
    void updateBossBar(BossBar bossbar, Player player) {
        bossbar.progress(getBossBarProgress());
        bossbar.color(getBossBarColor());
        bossbar.name(MiniMessage.miniMessage().deserialize(PurpurConfig.commandTPSBarTitle,
                Placeholder.component("tps", getTPSColor()),
                Placeholder.component("mspt", getMSPTColor()),
                Placeholder.component("ping", getPingColor(player.getPing()))
        ));
    }

    @Override
    public void run() {
        if (++tick < PurpurConfig.commandTPSBarTickInterval) {
            return;
        }
        tick = 0;

        this.tps = Math.max(Math.min(Bukkit.getTPS()[0], 20.0D), 0.0D);
        this.mspt = Bukkit.getAverageTickTime();

        super.run();
    }

    private float getBossBarProgress() {
        if (PurpurConfig.commandTPSBarProgressFillMode == FillMode.MSPT) {
            return Math.max(Math.min((float) mspt / 50.0F, 1.0F), 0.0F);
        } else {
            return Math.max(Math.min((float) tps / 20.0F, 1.0F), 0.0F);
        }
    }

    private BossBar.Color getBossBarColor() {
        if (isGood(PurpurConfig.commandTPSBarProgressFillMode)) {
            return PurpurConfig.commandTPSBarProgressColorGood;
        } else if (isMedium(PurpurConfig.commandTPSBarProgressFillMode)) {
            return PurpurConfig.commandTPSBarProgressColorMedium;
        } else {
            return PurpurConfig.commandTPSBarProgressColorLow;
        }
    }

    private boolean isGood(FillMode mode) {
        return isGood(mode, 0);
    }

    private boolean isGood(FillMode mode, int ping) {
        if (mode == FillMode.MSPT) {
            return mspt < 40;
        } else if (mode == FillMode.TPS) {
            return tps >= 19;
        } else if (mode == FillMode.PING) {
            return ping < 100;
        } else {
            return false;
        }
    }

    private boolean isMedium(FillMode mode) {
        return isMedium(mode, 0);
    }

    private boolean isMedium(FillMode mode, int ping) {
        if (mode == FillMode.MSPT) {
            return mspt < 50;
        } else if (mode == FillMode.TPS) {
            return tps >= 15;
        } else if (mode == FillMode.PING) {
            return ping < 200;
        } else {
            return false;
        }
    }

    private Component getTPSColor() {
        String color;
        if (isGood(FillMode.TPS)) {
            color = PurpurConfig.commandTPSBarTextColorGood;
        } else if (isMedium(FillMode.TPS)) {
            color = PurpurConfig.commandTPSBarTextColorMedium;
        } else {
            color = PurpurConfig.commandTPSBarTextColorLow;
        }
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%.2f", tps)));
    }

    private Component getMSPTColor() {
        String color;
        if (isGood(FillMode.MSPT)) {
            color = PurpurConfig.commandTPSBarTextColorGood;
        } else if (isMedium(FillMode.MSPT)) {
            color = PurpurConfig.commandTPSBarTextColorMedium;
        } else {
            color = PurpurConfig.commandTPSBarTextColorLow;
        }
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%.2f", mspt)));
    }

    private Component getPingColor(int ping) {
        String color;
        if (isGood(FillMode.PING, ping)) {
            color = PurpurConfig.commandTPSBarTextColorGood;
        } else if (isMedium(FillMode.PING, ping)) {
            color = PurpurConfig.commandTPSBarTextColorMedium;
        } else {
            color = PurpurConfig.commandTPSBarTextColorLow;
        }
        return MiniMessage.miniMessage().deserialize(color, Placeholder.parsed("text", String.format("%s", ping)));
    }

    public enum FillMode {
        TPS, MSPT, PING
    }
}
