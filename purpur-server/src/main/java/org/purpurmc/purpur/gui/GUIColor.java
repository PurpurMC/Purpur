package org.purpurmc.purpur.gui;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum GUIColor {
    BLACK(ChatColor.BLACK, new Color(0x000000)),
    DARK_BLUE(ChatColor.DARK_BLUE, new Color(0x0000AA)),
    DARK_GREEN(ChatColor.DARK_GREEN, new Color(0x00AA00)),
    DARK_AQUA(ChatColor.DARK_AQUA, new Color(0x009999)),
    DARK_RED(ChatColor.DARK_RED, new Color(0xAA0000)),
    DARK_PURPLE(ChatColor.DARK_PURPLE, new Color(0xAA00AA)),
    GOLD(ChatColor.GOLD, new Color(0xBB8800)),
    GRAY(ChatColor.GRAY, new Color(0x888888)),
    DARK_GRAY(ChatColor.DARK_GRAY, new Color(0x444444)),
    BLUE(ChatColor.BLUE, new Color(0x5555FF)),
    GREEN(ChatColor.GREEN, new Color(0x55FF55)),
    AQUA(ChatColor.AQUA, new Color(0x55DDDD)),
    RED(ChatColor.RED, new Color(0xFF5555)),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, new Color(0xFF55FF)),
    YELLOW(ChatColor.YELLOW, new Color(0xFFBB00)),
    WHITE(ChatColor.WHITE, new Color(0xBBBBBB));

    private final ChatColor chat;
    private final Color color;

    private static final Map<ChatColor, GUIColor> BY_CHAT = new HashMap<>();

    GUIColor(ChatColor chat, Color color) {
        this.chat = chat;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public ChatColor getChatColor() {
        return chat;
    }

    public String getCode() {
        return chat.toString();
    }

    public static GUIColor getColor(ChatColor chat) {
        return BY_CHAT.get(chat);
    }

    static {
        for (GUIColor color : values()) {
            BY_CHAT.put(color.chat, color);
        }
    }
}
