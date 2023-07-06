package me13.me.integration.mixin;

import me13.me.integration.mixin.base.IMeNetBaseMixin;
import mindustry.gen.Building;

public interface IMeNetMixin<T> extends IMeNetBaseMixin {
    void set(Building building, T type, float count);

    float getStored(Building building, T type);
    float getMaximumAccepted(Building building, T type);

    default void add(Building building, T type, float amount) {
        set(building, type, Math.min(getMaximumAccepted(building, type), getStored(building, type) + amount));
    }

    default void remove(Building building, T type, float amount) {
        set(building, type, Math.max(0, getStored(building, type) - amount));
    }
}