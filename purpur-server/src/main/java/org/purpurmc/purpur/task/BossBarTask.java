package org.purpurmc.purpur.task;

import net.kyori.adventure.bossbar.BossBar;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.purpurmc.purpur.util.MinecraftInternalPlugin;

public abstract class BossBarTask extends BukkitRunnable {
    private final Map<UUID, ServerBossEvent> bossbars = new HashMap<>();
    private boolean started;

    abstract ServerBossEvent createBossBar(UUID id);

    abstract void updateBossBar(ServerBossEvent bossbar, Player player);

    @Override
    public void run() {
        Iterator<Map.Entry<UUID, ServerBossEvent>> iter = bossbars.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<UUID, ServerBossEvent> entry = iter.next();
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) {
                iter.remove();
                continue;
            }
            updateBossBar(entry.getValue(), player);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        new HashSet<>(this.bossbars.keySet()).forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                removePlayer(player);
            }
        });
        this.bossbars.clear();
    }

    public boolean removePlayer(Player player) {
        ServerBossEvent bossbar = this.bossbars.remove(player.getUniqueId());
        if (bossbar != null) {
            bossbar.removePlayer(((CraftPlayer) player).getHandle());
            return true;
        }
        return false;
    }

    public void addPlayer(Player player) {
        removePlayer(player);
        ServerBossEvent bossbar = createBossBar(createId(player));
        this.bossbars.put(player.getUniqueId(), bossbar);
        this.updateBossBar(bossbar, player);
        bossbar.addPlayer(((CraftPlayer) player).getHandle());
    }

    public boolean hasPlayer(UUID uuid) {
        return this.bossbars.containsKey(uuid);
    }

    public boolean togglePlayer(Player player) {
        if (removePlayer(player)) {
            return false;
        }
        addPlayer(player);
        return true;
    }

    public void start() {
        stop();
        this.runTaskTimerAsynchronously(new MinecraftInternalPlugin(), 1, 1);
        started = true;
    }

    public void stop() {
        if (started) {
            cancel();
        }
    }

    public static void startAll() {
        RamBarTask.instance().start();
        TPSBarTask.instance().start();
        CompassTask.instance().start();
    }

    public static void stopAll() {
        RamBarTask.instance().stop();
        TPSBarTask.instance().stop();
        CompassTask.instance().stop();
    }

    public static void addToAll(ServerPlayer player) {
        Player bukkit = player.getBukkitEntity();
        if (player.ramBar()) {
            RamBarTask.instance().addPlayer(bukkit);
        }
        if (player.tpsBar()) {
            TPSBarTask.instance().addPlayer(bukkit);
        }
        if (player.compassBar()) {
            CompassTask.instance().addPlayer(bukkit);
        }
    }

    public static void removeFromAll(Player player) {
        RamBarTask.instance().removePlayer(player);
        TPSBarTask.instance().removePlayer(player);
        CompassTask.instance().removePlayer(player);
    }

    protected static BossEvent.BossBarColor toVanillaColor(BossBar.Color color) {
        return switch (color) {
            case PINK -> BossEvent.BossBarColor.PINK;
            case BLUE -> BossEvent.BossBarColor.BLUE;
            case RED -> BossEvent.BossBarColor.RED;
            case GREEN -> BossEvent.BossBarColor.GREEN;
            case YELLOW -> BossEvent.BossBarColor.YELLOW;
            case PURPLE -> BossEvent.BossBarColor.PURPLE;
            case WHITE -> BossEvent.BossBarColor.WHITE;
        };
    }

    protected static BossEvent.BossBarOverlay toVanillaOverlay(BossBar.Overlay overlay) {
        return switch (overlay) {
            case PROGRESS -> BossEvent.BossBarOverlay.PROGRESS;
            case NOTCHED_6 -> BossEvent.BossBarOverlay.NOTCHED_6;
            case NOTCHED_10 -> BossEvent.BossBarOverlay.NOTCHED_10;
            case NOTCHED_12 -> BossEvent.BossBarOverlay.NOTCHED_12;
            case NOTCHED_20 -> BossEvent.BossBarOverlay.NOTCHED_20;
        };
    }

    private UUID createId(Player player) {
        return UUID.nameUUIDFromBytes(("purpur:" + getClass().getSimpleName() + ":" + player.getUniqueId()).getBytes(StandardCharsets.UTF_8));
    }
}
