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

        purpur.recalculatePermissibles();
        return purpur;
    }
}
