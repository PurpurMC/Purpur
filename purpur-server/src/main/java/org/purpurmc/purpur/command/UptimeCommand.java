package org.purpurmc.purpur.command;

import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.purpurmc.purpur.PurpurConfig;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class UptimeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("uptime")
                .requires((listener) -> listener.hasPermission(2, "bukkit.command.uptime"))
                .executes((context) -> execute(context.getSource()))
        );
    }

    private static int execute(CommandSourceStack sender) {
        Data data = new Data();

        data.format = PurpurConfig.uptimeFormat;
        data.hide = true;
        data.millis = System.currentTimeMillis() - MinecraftServer.startTimeMillis;

        process(data, "<days>", PurpurConfig.uptimeDay, PurpurConfig.uptimeDays, TimeUnit.DAYS, TimeUnit.MILLISECONDS::toDays);
        process(data, "<hours>", PurpurConfig.uptimeHour, PurpurConfig.uptimeHours, TimeUnit.HOURS, TimeUnit.MILLISECONDS::toHours);
        process(data, "<minutes>", PurpurConfig.uptimeMinute, PurpurConfig.uptimeMinutes, TimeUnit.MINUTES, TimeUnit.MILLISECONDS::toMinutes);
        data.hide = false; // never hide seconds
        process(data, "<seconds>", PurpurConfig.uptimeSecond, PurpurConfig.uptimeSeconds, TimeUnit.SECONDS, TimeUnit.MILLISECONDS::toSeconds);

        Component output = MiniMessage.miniMessage().deserialize(PurpurConfig.uptimeCommandOutput, Placeholder.unparsed("uptime", data.format));
        sender.sendSuccess(output, false);
        return 1;
    }

    private static void process(Data data, String replace, String singular, String plural, TimeUnit unit, Function<Long, Long> func) {
        if (data.format.contains(replace)) {
            long val = func.apply(data.millis);
            if (data.hide) data.hide = val == 0;
            if (!data.hide) data.millis -= unit.toMillis(val);
            data.format = data.format.replace(replace, data.hide ? "" : String.format(val == 1 ? singular : plural, val));
        }
    }

    private static class Data {
        String format;
        boolean hide;
        long millis;
    }
}
