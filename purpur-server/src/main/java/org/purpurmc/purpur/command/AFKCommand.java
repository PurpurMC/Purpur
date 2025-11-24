package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;

public class AFKCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("afk")
            .requires((listener) -> listener.hasPermission(2, "bukkit.command.afk"))
            .executes((context) -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
            .then(Commands.argument("targets", EntityArgument.players())
                .requires(listener -> listener.hasPermission(2, "bukkit.command.afk.other"))
                .executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
            )
        );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            boolean afk = player.isAfk();

            if (afk) player.setAfk(true);
        }

        return targets.size();
    }
}
