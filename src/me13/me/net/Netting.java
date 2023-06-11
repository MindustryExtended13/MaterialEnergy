package me13.me.net;

import arc.struct.Seq;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.configuration.Channels;
import me13.me.world.blocks.Cable;
import me13.me.world.blocks.Controller;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

import java.util.HashMap;
import java.util.Map;

public class Netting {
    public static boolean inNet(Building building, Building host) {
        return building != null && building.team == host.team && ((building instanceof IMaterialEnergyBuilding
                b && b.canConnectTo(host)) || building.block instanceof Cable);
    }

    public static Seq<Building> getConnections(Building building) {
        Seq<Building> result = new Seq<>();
        if(building instanceof IMaterialEnergyBuilding building1) {
            building1.getChildren().forEach(build -> {
                if(inNet(build, building)) {
                    result.add(build);
                }
            });
        } else if(building.enabled()) {
            building.proximity.forEach(build -> {
                if(inNet(build, building)) {
                    result.add(build);
                }
            });
        }
        return result;
    }

    public static void getConnections(Building source, Seq<Building> buildings) {
        getConnections(source).forEach(b -> {
            if(!buildings.contains(b)) {
                buildings.add(b);
                getConnections(b, buildings);
            }
        });
    }

    public static boolean isNetEnabled(Building building) {
        return getChannels(building) <= getMaximumChannels(building);
    }

    public static int getMaximumChannels(Building building) {
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        int channels = 0;
        for(var build : result) {
            if(build instanceof Controller.ControllerBuild controller) {
                channels += controller.getChannelsAdd();
            }
        }
        return channels + Channels.conf.getDefaultChannels();
    }

    public static int getChannels(Building building) {
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        int channels = 0;
        for(var build : result) {
            if(build instanceof IMaterialEnergyBuilding building1) {
                channels += building1.getChannels();
            }
        }
        return channels;
    }

    public static LiquidStack[] getStorageLiquid(Building building2) {
        Map<Liquid, Float> map = new HashMap<>();
        var result = new Seq<Building>();
        Netting.getConnections(building2, result);
        for(var build : result) {
            if(build instanceof IMaterialEnergyBuilding building) {
                var module = building.storageLiquid();
                if(module != null) {
                    module.each((l, c) -> {
                        if(map.containsKey(l)) {
                            map.replace(l, map.get(l)+c);
                        } else {
                            map.put(l, c);
                        }
                    });
                }
            }
        }
        Liquid[] id = new Liquid[map.keySet().size()];
        int[] i = {0, 0};
        map.keySet().forEach(liquid -> {
            id[i[0]] = liquid;
            i[0]++;
        });
        LiquidStack[] stacks = new LiquidStack[id.length];
        for(; i[1] < stacks.length; i[1]++) {
            var key = id[i[1]];
            stacks[i[1]] = new LiquidStack(key, map.get(key));
        }
        return stacks;
    }

    public static ItemStack[] getStorage(Building building2) {
        Map<Item, Integer> map = new HashMap<>();
        var result = new Seq<Building>();
        Netting.getConnections(building2, result);
        for(var build : result) {
            if(build instanceof IMaterialEnergyBuilding building) {
                var module = building.storage();
                if(module != null) {
                    module.each((i, c) -> {
                        if(map.containsKey(i)) {
                            map.replace(i, map.get(i)+c);
                        } else {
                            map.put(i, c);
                        }
                    });
                }
            }
        }
        Item[] id = new Item[map.keySet().size()];
        int[] i = {0, 0};
        map.keySet().forEach(item -> {
            id[i[0]] = item;
            i[0]++;
        });
        ItemStack[] stacks = new ItemStack[id.length];
        for(; i[1] < stacks.length; i[1]++) {
            var key = id[i[1]];
            stacks[i[1]] = new ItemStack(key, map.get(key));
        }
        return stacks;
    }
}