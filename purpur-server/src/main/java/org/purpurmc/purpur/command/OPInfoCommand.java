package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;

import java.util.Collection;

public class OPInfoCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("opinfo")
            .requires(listener -> listener.hasPermission(Permissions.COMMANDS_OWNER, "bukkit.command.opinfo"))
            .executes(context -> executeList(context.getSource()))
            .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                .executes(context -> executePlayer(
                    context.getSource(),
                    GameProfileArgument.getGameProfiles(context, "targets")
                ))
            )
        );
    }

    private static int executeList(CommandSourceStack source) {
        ServerOpList opList = source.getServer().getPlayerList().getOps();
        Collection<ServerOpListEntry> entries = opList.getEntries();

        if (entries.isEmpty()) {
            source.sendFailure(Component.literal("There are no operators on this server.").withStyle(ChatFormatting.RED));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("--- Operators ---").withStyle(ChatFormatting.BLUE), false);
        for (ServerOpListEntry entry : entries) {
            formatAndSendEntry(source, entry);
        }

        return entries.size();
    }

    private static int executePlayer(CommandSourceStack source, Collection<NameAndId> targets) {
        ServerOpList opList = source.getServer().getPlayerList().getOps();
        int count = 0;

        for (NameAndId nameAndId : targets) {
            ServerOpListEntry entry = opList.get(nameAndId);

            if (entry == null) {
                source.sendFailure(Component.literal(nameAndId.name() + " is not an operator.").withStyle(ChatFormatting.RED));
            } else {
                formatAndSendEntry(source, entry);
                count++;
            }
        }

        return count;
    }

    private static void formatAndSendEntry(CommandSourceStack source, ServerOpListEntry entry) {
        NameAndId user = entry.getUser();
        String name = (user != null) ? user.name() : "Unknown";

        PermissionLevel level = entry.permissions().level();
        boolean bypassesLimit = entry.getBypassesPlayerLimit();

        Component info = Component.empty()
            .append(Component.literal("- ").withStyle(ChatFormatting.GRAY))
            .append(Component.literal(name).withStyle(ChatFormatting.AQUA))
            .append(Component.literal(" | ").withStyle(ChatFormatting.GRAY))
            .append(Component.literal("Level: ").withStyle(ChatFormatting.WHITE))
            .append(Component.literal(String.valueOf(level)).withStyle(ChatFormatting.AQUA))
            .append(Component.literal(" | ").withStyle(ChatFormatting.GRAY))
            .append(Component.literal("Bypasses Player Limit: ").withStyle(ChatFormatting.WHITE))
            .append(Component.literal(String.valueOf(bypassesLimit)).withStyle(bypassesLimit ? ChatFormatting.GREEN : ChatFormatting.RED));

        source.sendSuccess(() -> info, false);
    }
}
