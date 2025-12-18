package org.purpurmc.purpur.tool;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class Waxable extends Actionable {
    public Waxable(Block into, Map<Item, Double> drops) {
        super(into, drops);
    }
}
