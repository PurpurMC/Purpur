package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;

import java.util.Collection;

public class BypassPlayerLimitCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("bypassplayerlimit")
            .requires(listener -> listener.hasPermission(Permissions.COMMANDS_OWNER, "bukkit.command.bypassplayerlimit"))
            .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                .then(Commands.argument("bypass", BoolArgumentType.bool())
                    .executes(context -> execute(
                        context.getSource(),
                        GameProfileArgument.getGameProfiles(context, "targets"),
                        BoolArgumentType.getBool(context, "bypass")
                    ))
                )
            )
        );
    }

    private static int execute(CommandSourceStack source, Collection<NameAndId> targets, boolean bypass) {
        ServerOpList opList = source.getServer().getPlayerList().getOps();
        int count = 0;

        for (NameAndId nameAndId : targets) {
            ServerOpListEntry existingEntry = opList.get(nameAndId);

            if (existingEntry == null) {
                source.sendFailure(Component.literal(nameAndId.name() + " is not an operator.").withStyle(ChatFormatting.RED));
                continue;
            }

            ServerOpListEntry newEntry = new ServerOpListEntry(
                nameAndId,
                existingEntry.permissions(),
                bypass
            );

            opList.add(newEntry);

            Component message = Component.empty()
                .append(Component.literal("Set bypass player limit privilege for ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(nameAndId.name()).withStyle(ChatFormatting.AQUA))
                .append(Component.literal(" to ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(String.valueOf(bypass)).withStyle(bypass ? ChatFormatting.GREEN : ChatFormatting.RED))
                .append(Component.literal(".").withStyle(ChatFormatting.WHITE));

            source.sendSuccess(() -> message, true);
            count++;
        }

        return count;
    }
}
