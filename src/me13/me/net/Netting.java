package me13.me.net;

import arc.struct.Seq;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.world.blocks.Cable;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import net.tmmc.util.Geom;
import net.tmmc.util.XBlocks;
import net.tmmc.util.XWorld;

import java.util.HashMap;
import java.util.Map;

public class Netting {
    public static boolean inNet(Building building) {
        return building != null && (building instanceof IMaterialEnergyBuilding ||
                building.block instanceof Cable);
    }

    public static Seq<Building> getConnections(Building building) {
        Seq<Building> result = new Seq<>();
        Geom.each4dAngle(point2 -> {
            var build = XBlocks.of(XWorld.at(point2));
            if(!(building instanceof IMaterialEnergyBuilding building1) || (building1.canConnectTo(build) &&
                            (!(build instanceof IMaterialEnergyBuilding b) || b.canConnectTo(building))))
            {
                if(inNet(build)) {
                    result.add(build);
                }
            }
        }, Geom.toPoint(building));
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

    public static int maxCapacity(Building building) {
        int i = 0;
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        for(var build : result) {
            if(build instanceof IMaterialEnergyBuilding building1) {
                i += building1.maxCapacity();
            }
        }
        return i;
    }

    public static float maxLiquidCapacity(Building building) {
        float f = 0;
        var result = new Seq<Building>();
        Netting.getConnections(building, result);
        for(var build : result) {
            if(build instanceof IMaterialEnergyBuilding building1) {
                f += building1.maxLiquidCapacity();
            }
        }
        return f;
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