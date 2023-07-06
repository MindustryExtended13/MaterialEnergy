package me13.me.integration.mixin;

import me13.me.integration.mixin.base.IMeNetBaseMixin;
import mindustry.gen.Building;

public interface IMeSingleNetMixin extends IMeNetBaseMixin {
    void set(Building building, float amount);
    float getMaximumStored(Building building);
    float getStored(Building building);

    default void add(Building building, float amount) {
        set(building, Math.min(getMaximumStored(building), getStored(building) + amount));
    }

    default void remove(Building building, float amount) {
        set(building, Math.max(0, getStored(building) - amount));
    }

    default void clear(Building building) {
        set(building, 0);
    }
}