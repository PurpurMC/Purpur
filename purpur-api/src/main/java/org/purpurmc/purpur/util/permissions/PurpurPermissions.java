package org.purpurmc.purpur.util.permissions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class PurpurPermissions {
    private static final String ROOT = "purpur";
    private static final String PREFIX = ROOT + ".";
    private static final Set<String> mobs = new HashSet<>();

    static {
        for (EntityType mob : EntityType.values()) {
            Class<? extends Entity> clazz = mob.getEntityClass();
            if (clazz != null && Mob.class.isAssignableFrom(clazz)) {
                mobs.add(mob.getName());
            }
        }
    }

    @NotNull
    public static Permission registerPermissions() {
        Permission purpur = DefaultPermissions.registerPermission(ROOT, "Gives the user the ability to use all Purpur utilities and commands", PermissionDefault.FALSE);

        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.six", "Gives the user six rows of enderchest space", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.five", "Gives the user five rows of enderchest space", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.four", "Gives the user four rows of enderchest space", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.three", "Gives the user three rows of enderchest space", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.two", "Gives the user two rows of enderchest space", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "enderchest.rows.one", "Gives the user one row of enderchest space", PermissionDefault.FALSE, purpur);

        DefaultPermissions.registerPermission(PREFIX + "debug.f3n", "Allows the user to use F3+N keybind to swap gamemodes", PermissionDefault.FALSE, purpur);

        DefaultPermissions.registerPermission(PREFIX + "joinfullserver", "Allows the user to join a full server", PermissionDefault.OP, purpur);

        DefaultPermissions.registerPermission(PREFIX + "bypassIdleKick", "Allows the user to bypass being kicked while idle", PermissionDefault.FALSE, purpur);

        DefaultPermissions.registerPermission(PREFIX + "inventory_totem", "Allows the user to use totem of undying anywhere in their inventory", PermissionDefault.FALSE, purpur);

        Permission anvil = DefaultPermissions.registerPermission(PREFIX + "anvil", "Allows the user to use all anvil color and format abilities", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "anvil.color", "Allows the user to use color codes in an anvil", PermissionDefault.FALSE, anvil);
        DefaultPermissions.registerPermission(PREFIX + "anvil.minimessage", "Allows the user to use minimessage tags in an anvil", PermissionDefault.FALSE, anvil);
        DefaultPermissions.registerPermission(PREFIX + "anvil.remove_italics", "Allows the user to remove italics in an anvil", PermissionDefault.FALSE, anvil);
        DefaultPermissions.registerPermission(PREFIX + "anvil.format", "Allows the user to use format codes in an anvil", PermissionDefault.FALSE, anvil);
        anvil.recalculatePermissibles();

        Permission book = DefaultPermissions.registerPermission(PREFIX + "book", "Allows the user to use color codes on books", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "book.color.edit", "Allows the user to use color codes on books when editing", PermissionDefault.FALSE, book);
        DefaultPermissions.registerPermission(PREFIX + "book.color.sign", "Allows the user to use color codes on books when signing", PermissionDefault.FALSE, book);
        book.recalculatePermissibles();

        Permission sign = DefaultPermissions.registerPermission(PREFIX + "sign", "Allows the user to use all sign abilities", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission(PREFIX + "sign.edit", "Allows the user to click signs to open sign editor", PermissionDefault.TRUE, sign);
        DefaultPermissions.registerPermission(PREFIX + "sign.color", "Allows the user to use color codes on signs", PermissionDefault.FALSE, sign);
        DefaultPermissions.registerPermission(PREFIX + "sign.style", "Allows the user to use style codes on signs", PermissionDefault.FALSE, sign);
        DefaultPermissions.registerPermission(PREFIX + "sign.magic", "Allows the user to use magic/obfuscate code on signs", PermissionDefault.FALSE, sign);
        sign.recalculatePermissibles();

        Permission ride = DefaultPermissions.registerPermission("allow.ride", "Allows the user to ride all mobs", PermissionDefault.FALSE, purpur);
        for (String mob : mobs) {
            DefaultPermissions.registerPermission("allow.ride." + mob, "Allows the user to ride " + mob, PermissionDefault.FALSE, ride);
        }
        ride.recalculatePermissibles();

        Permission special = DefaultPermissions.registerPermission("allow.special", "Allows the user to use all mobs special abilities", PermissionDefault.FALSE, purpur);
        for (String mob : mobs) {
            DefaultPermissions.registerPermission("allow.special." + mob, "Allows the user to use " + mob + " special ability", PermissionDefault.FALSE, special);
        }
        special.recalculatePermissibles();

        Permission powered = DefaultPermissions.registerPermission("allow.powered", "Allows the user to toggle all mobs powered state", PermissionDefault.FALSE, purpur);
        DefaultPermissions.registerPermission("allow.powered.creeper", "Allows the user to toggle creeper powered state", PermissionDefault.FALSE, powered);
        powered.recalculatePermissibles();

        DefaultPermissions.registerPermission(PREFIX + "portal.instant", "Allows the user to bypass portal wait time", PermissionDefault.FALSE, purpur);

        purpur.recalculatePermissibles();
        return purpur;
    }
}
