package me13.me.integration.mixin;

import mindustry.gen.Building;
import mindustry.type.Liquid;
import mindustry.world.modules.LiquidModule;

public class LiquidNetMixin implements IMeNetMixin<Liquid> {
    @Override
    public void set(Building building, Liquid type, float count) {
        building.liquids.set(type, count);
    }

    @Override
    public void clear(Building building) {
        building.liquids = new LiquidModule();
    }

    @Override
    public boolean haveMixinIn(Building building) {
        return building != null && building.liquids != null;
    }

    @Override
    public float getStored(Building building, Liquid type) {
        return building.liquids.get(type);
    }

    @Override
    public float getMaximumAccepted(Building building, Liquid type) {
        return building.block.liquidCapacity;
    }
}