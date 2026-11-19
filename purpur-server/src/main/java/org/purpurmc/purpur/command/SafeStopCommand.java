package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.purpurmc.purpur.PurpurConfig;
import org.purpurmc.purpur.util.MinecraftInternalPlugin;

import java.util.ArrayList;
import java.util.List;

public class SafeStopCommand {
    private static BukkitTask countdownTask = null;
    private static int remainingSeconds = 0;
    private static String shutdownReason = "";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("safestop")
                .requires(listener -> listener.hasPermission(Permissions.COMMANDS_OWNER, "bukkit.command.safestop"))
                .executes(context -> execute(context.getSource(), PurpurConfig.safestopDefaultCountdown, PurpurConfig.safestopDefaultReason))
                .then(Commands.literal("cancel")
                        .executes(context -> cancel(context.getSource()))
                )
                .then(Commands.argument("seconds", IntegerArgumentType.integer(0, 86400))
                        .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "seconds"), PurpurConfig.safestopDefaultReason))
                        .then(Commands.argument("reason", StringArgumentType.greedyString())
                                .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "seconds"), StringArgumentType.getString(context, "reason")))
                        )
                )
        );
    }

    private static int execute(CommandSourceStack sender, int seconds, String reason) {
        if (countdownTask != null) {
            sender.sendFailure(PaperAdventure.asVanilla(Component.text("A safe shutdown is already in progress (" + remainingSeconds + "s remaining). Use '/safestop cancel' first.")));
            return 0;
        }

        if (seconds <= 0) {
            sender.sendSuccess(Component.text("Initiating immediate graceful shutdown..."), true);
            performGracefulShutdown(reason);
            return 1;
        }

        remainingSeconds = seconds;
        shutdownReason = reason;

        broadcastCountdown(remainingSeconds, shutdownReason);

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                remainingSeconds--;
                if (remainingSeconds <= 0) {
                    cancel();
                    countdownTask = null;
                    performGracefulShutdown(shutdownReason);
                } else if (shouldBroadcast(remainingSeconds)) {
                    broadcastCountdown(remainingSeconds, shutdownReason);
                }
            }
        }.runTaskTimer(new MinecraftInternalPlugin(), 20L, 20L);

        return 1;
    }

    private static int cancel(CommandSourceStack sender) {
        if (countdownTask == null) {
            sender.sendFailure(PaperAdventure.asVanilla(Component.text("No safe shutdown is currently in progress.")));
            return 0;
        }

        countdownTask.cancel();
        countdownTask = null;
        remainingSeconds = 0;

        Component msg = MiniMessage.miniMessage().deserialize(PurpurConfig.safestopCancelled);
        Bukkit.broadcast(msg);
        sender.sendSuccess(Component.text("Safe shutdown cancelled successfully."), true);
        return 1;
    }

    private static boolean shouldBroadcast(int seconds) {
        return seconds == 300 || seconds == 180 || seconds == 120 || seconds == 60 || seconds == 30 || seconds == 15 || seconds == 10 || seconds <= 5;
    }

    private static void broadcastCountdown(int seconds, String reason) {
        Component msg = MiniMessage.miniMessage().deserialize(
                PurpurConfig.safestopCountdown,
                Placeholder.unparsed("seconds", String.valueOf(seconds)),
                Placeholder.unparsed("reason", reason)
        );
        Bukkit.broadcast(msg);
    }

    private static void performGracefulShutdown(String reason) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return;
        }

        List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        for (ServerPlayer player : players) {
            try {
                player.getBukkitEntity().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            } catch (Exception ignored) {}
        }

        Component kickMsg = MiniMessage.miniMessage().deserialize(
                PurpurConfig.safestopKick,
                Placeholder.unparsed("reason", reason)
        );

        for (ServerPlayer player : players) {
            try {
                player.getBukkitEntity().kick(kickMsg, PlayerKickEvent.Cause.RESTART_COMMAND);
            } catch (Exception ignored) {}
        }

        server.halt(false);
    }
}
