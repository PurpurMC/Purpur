package org.purpurmc.purpur.tool;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Tillable extends Actionable {
    private final Condition condition;

    public Tillable(Condition condition, Block into, Map<Item, Double> drops) {
        super(into, drops);
        this.condition = condition;
    }

    public Condition condition() {
        return condition;
    }

    public enum Condition {
        AIR_ABOVE(HoeItem::onlyIfAirAbove),
        ALWAYS((useOnContext) -> true);

        private final Predicate<UseOnContext> predicate;

        Condition(Predicate<UseOnContext> predicate) {
            this.predicate = predicate;
        }

        public Predicate<UseOnContext> predicate() {
            return predicate;
        }

        private static final Map<String, Condition> BY_NAME = new HashMap<>();

        static {
            for (Condition condition : values()) {
                BY_NAME.put(condition.name(), condition);
            }
        }

        public static Condition get(String name) {
            return BY_NAME.get(name.toUpperCase(java.util.Locale.ROOT));
        }
    }
}
