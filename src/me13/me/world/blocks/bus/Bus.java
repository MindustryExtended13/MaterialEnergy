package me13.me.world.blocks.bus;

import me13.me.world.blocks.Prox;
import mindustry.gen.Building;
import mindustry.type.Liquid;

public class Bus extends Prox {
    public Bus(String name) {
        super(name);
    }

    public class BusBuild extends ProxBuild {
        public float acceptStack(Liquid liquid, float amount, Building source) {
            return acceptLiquid(source, liquid) && liquids.get(liquid) < liquidCapacity ?
                    Math.min(liquidCapacity - liquids.get(liquid), amount) : 0;
        }
    }
}