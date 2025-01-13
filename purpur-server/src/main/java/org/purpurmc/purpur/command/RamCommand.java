package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.purpurmc.purpur.PurpurConfig;
import org.purpurmc.purpur.task.RamBarTask;

public class RamCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ram")
                .requires(listener -> listener.hasPermission(2, "bukkit.command.ram"))
                .executes(context -> {
                    CommandSourceStack sender = context.getSource();
                    RamBarTask ramBar = RamBarTask.instance();
                    sender.sendSuccess(() -> PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(PurpurConfig.ramCommandOutput,
                            Placeholder.component("allocated", ramBar.format(ramBar.getAllocated())),
                            Placeholder.component("used", ramBar.format(ramBar.getUsed())),
                            Placeholder.component("xmx", ramBar.format(ramBar.getXmx())),
                            Placeholder.component("xms", ramBar.format(ramBar.getXms())),
                            Placeholder.unparsed("percent", ((int) (ramBar.getPercent() * 100)) + "%")
                    )), false);
                    return 1;
                })
        );
    }
}
