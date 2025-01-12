package org.purpurmc.purpur.tool;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class Flattenable extends Actionable {
    public Flattenable(Block into, Map<Item, Double> drops) {
        super(into, drops);
    }
}
