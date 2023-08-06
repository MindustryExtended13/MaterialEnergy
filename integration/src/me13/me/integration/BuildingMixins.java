package me13.me.integration;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import me13.me.integration.mixin.IMeBuildingMixin;
import me13.me.integration.mixin.base.IMeBuildingBaseMixin;
import mindustry.gen.Building;
import mindustry.world.Block;

public class BuildingMixins {
    public static final ObjectMap<Class<?>, IMeBuildingBaseMixin> mixins = new ObjectMap<>();
    public static final Seq<Block> meBlocks = new Seq<>();

    public static<T extends IMeBuildingBaseMixin, G extends Building> void register(Class<G> building, T mixin) {
        if(building != null && mixin != null) mixins.put(building, mixin);
    }

    public static<T extends IMeBuildingBaseMixin, G extends Building> void register(T mixin, Class<G> building) {
        register(building, mixin);
    }

    public static boolean isMeBuilding(Building building) {
        return getMixin(building) != null;
    }

    public static IMeBuildingBaseMixin getMixin(Building building) {
        if(building == null) return null;
        Class<?> cl = building.getClass();
        var mixin = mixins.get(cl);
        if(mixin == null) {
            return mixins.get(cl.getSuperclass());
        } else {
            return mixin;
        }
    }

    public static IMeBuildingMixin getDefaultMixin(boolean canConnect) {
        return new IMeBuildingMixin() {
            @Override
            public boolean canConnectTo(Building self, Building other) {
                return canConnect;
            }
        };
    }
}