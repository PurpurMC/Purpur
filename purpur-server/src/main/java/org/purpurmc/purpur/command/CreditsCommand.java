package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import org.purpurmc.purpur.PurpurConfig;

import java.util.Collection;
import java.util.Collections;

public class CreditsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("credits")
                .requires((listener) -> listener.hasPermission(Permissions.COMMANDS_GAMEMASTER, "bukkit.command.credits"))
                .executes((context) -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
                .then(Commands.argument("targets", EntityArgument.players())
                        .requires(listener -> listener.hasPermission(Permissions.COMMANDS_GAMEMASTER, "bukkit.command.credits.other"))
                        .executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                )
        );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            ClientboundGameEventPacket packet = new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 1F);
            player.connection.send(packet);
            String output = String.format(PurpurConfig.creditsCommandOutput, player.getGameProfile().name());
            sender.sendSuccess(output, false);
        }
        return targets.size();
    }
}
