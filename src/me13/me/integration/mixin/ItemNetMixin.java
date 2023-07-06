package me13.me.integration.mixin;

import arc.math.Mathf;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.modules.ItemModule;

public class ItemNetMixin implements IMeNetMixin<Item> {
    @Override
    public void set(Building building, Item type, float count) {
        building.items.set(type, Mathf.round(count));
    }

    @Override
    public void clear(Building building) {
        building.items = new ItemModule();
    }

    @Override
    public boolean haveMixinIn(Building building) {
        return building != null && building.items != null;
    }

    @Override
    public float getStored(Building building, Item type) {
        return building.items.get(type);
    }

    @Override
    public float getMaximumAccepted(Building building, Item type) {
        return building.getMaximumAccepted(type);
    }
}