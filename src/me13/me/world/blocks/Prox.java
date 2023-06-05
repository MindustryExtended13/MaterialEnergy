package me13.me.world.blocks;

import arc.struct.Seq;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;
import net.tmmc.util.GraphBlock;

public class Prox extends GraphBlock implements IMaterialEnergyBlock {
    public Prox(String name) {
        super(name);
    }

    public class ProxBuild extends GraphBlockBuild implements IMaterialEnergyBuilding {
        public Seq<Building> cableProximity = new Seq<>();

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            cableProximity = proximity.copy().filter(b -> b != null && b.block instanceof Cable);
        }

        @Override
        public Seq<Building> getChildren() {
            return cableProximity;
        }

        @Override
        public int maxCapacity() {
            return itemCapacity;
        }

        @Override
        public float maxLiquidCapacity() {
            return liquidCapacity;
        }

        @Override
        public ItemStack acceptItem(ItemStack itemStack) {
            var item = itemStack.item;
            int accepted = Math.min(itemCapacity-items.get(item), itemStack.amount);
            items.add(item, accepted);
            return new ItemStack(item, itemStack.amount-accepted);
        }

        @Override
        public ItemStack removeItem(ItemStack itemStack) {
            var item = itemStack.item;
            int removed = Math.min(items.get(item), itemStack.amount);
            items.remove(item, removed);
            return new ItemStack(item, removed);
        }

        @Override
        public LiquidStack acceptLiquid(LiquidStack liquidStack) {
            var liquid = liquidStack.liquid;
            float accepted = Math.min(liquidCapacity-liquids.get(liquid), liquidStack.amount);
            liquids.add(liquid, accepted);
            return new LiquidStack(liquid, liquidStack.amount-accepted);
        }

        @Override
        public LiquidStack removeLiquid(LiquidStack liquidStack) {
            var liquid = liquidStack.liquid;
            float removed = Math.min(liquids.get(liquid), liquidStack.amount);
            liquids.remove(liquid, removed);
            return new LiquidStack(liquid, removed);
        }

        @Override
        public ItemModule storage() {
            return items;
        }

        @Override
        public LiquidModule storageLiquid() {
            return liquids;
        }

        @Override
        public boolean canConnectTo(Building building) {
            return true;
        }
    }
}
