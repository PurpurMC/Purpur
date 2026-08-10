package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;

import java.util.Collection;

public class OPLevelCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("oplevel")
            .requires(listener -> listener.hasPermission(Permissions.COMMANDS_OWNER, "bukkit.command.oplevel"))
            .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                .then(Commands.argument("level", IntegerArgumentType.integer(0, 4))
                    .executes(context -> execute(
                        context.getSource(),
                        GameProfileArgument.getGameProfiles(context, "targets"),
                        IntegerArgumentType.getInteger(context, "level")
                    ))
                )
            )
        );
    }

    private static int execute(CommandSourceStack source, Collection<NameAndId> targets, int newLevel) {
        MinecraftServer server = source.getServer();
        ServerOpList opList = server.getPlayerList().getOps();
        int count = 0;

        for (NameAndId nameAndId : targets) {
            ServerOpListEntry existingEntry = opList.get(nameAndId);

            if (existingEntry == null) {
                source.sendFailure(Component.literal(nameAndId.name() + " is not an operator.").withStyle(ChatFormatting.RED));
                continue;
            }

            ServerOpListEntry newEntry = new ServerOpListEntry(
                nameAndId,
                LevelBasedPermissionSet.forLevel(PermissionLevel.byId(newLevel)),
                existingEntry.getBypassesPlayerLimit()
            );

            opList.add(newEntry);

            ServerPlayer serverPlayer = server.getPlayerList().getPlayer(nameAndId.id());
            if (serverPlayer != null) {
                server.getPlayerList().sendPlayerPermissionLevel(serverPlayer);
            }

            Component message = Component.empty()
                .append(Component.literal("Set operator level for ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(nameAndId.name()).withStyle(ChatFormatting.AQUA))
                .append(Component.literal(" to level ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(String.valueOf(newLevel)).withStyle(ChatFormatting.AQUA))
                .append(Component.literal(".").withStyle(ChatFormatting.WHITE));

            source.sendSuccess(() -> message, true);
            count++;
        }

        return count;
    }
}
