package me13.me.integration;

import arc.struct.Seq;
import mindustry.gen.Building;

public class AdapterTypes {
    private static final Seq<Class<?>> allowed = new Seq<>();

    public static<T extends Building> void register(Class<T> buildingClass) {
        if(buildingClass != null) allowed.add(buildingClass);
    }

    public static boolean isAdaptive(Building building) {
        if(building == null) return false;
        var get = building.getClass();

        for(var cl : allowed) {
            if(cl == get || cl == get.getSuperclass()) {
                return true;
            }
        }

        return false;
    }
}