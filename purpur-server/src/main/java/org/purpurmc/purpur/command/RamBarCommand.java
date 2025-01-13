package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.purpurmc.purpur.PurpurConfig;
import org.purpurmc.purpur.task.RamBarTask;

import java.util.Collection;
import java.util.Collections;

public class RamBarCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("rambar")
                .requires(listener -> listener.hasPermission(2, "bukkit.command.rambar"))
                .executes(context -> execute(context.getSource(), Collections.singleton(context.getSource().getPlayerOrException())))
                .then(Commands.argument("targets", EntityArgument.players())
                        .requires(listener -> listener.hasPermission(2, "bukkit.command.rambar.other"))
                        .executes((context) -> execute(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                )
        );
    }

    private static int execute(CommandSourceStack sender, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            boolean result = RamBarTask.instance().togglePlayer(player.getBukkitEntity());
            player.ramBar(result);

            Component output = MiniMessage.miniMessage().deserialize(PurpurConfig.rambarCommandOutput,
                    Placeholder.component("onoff", Component.translatable(result ? "options.on" : "options.off")
                            .color(result ? NamedTextColor.GREEN : NamedTextColor.RED)),
                    Placeholder.parsed("target", player.getGameProfile().getName()));

            sender.sendSuccess(output, false);
        }
        return targets.size();
    }
}
