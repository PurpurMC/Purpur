package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.purpurmc.purpur.task.CompassTask;

public class CompassCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("compass")
                .requires(listener -> listener.hasPermission(2, "bukkit.command.compass"))
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompassTask task = CompassTask.instance();
                    if (player.compassBar()) {
                        task.removePlayer(player.getBukkitEntity());
                        player.compassBar(false);
                    } else {
                        task.addPlayer(player.getBukkitEntity());
                        player.compassBar(true);
                    }
                    return 1;
                })
        );
    }
}
