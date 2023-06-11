package me13.me.world.blocks.storage;

import me13.core.block.instance.AdvancedBlock;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;

public class MeStorageBlock extends AdvancedBlock implements IMaterialEnergyBlock {
    public int channels = 1;

    public MeStorageBlock(String name) {
        super(name);
    }

    public void item(int cap) {
        hasItems = true;
        itemCapacity = cap;
        acceptsItems = true;
    }

    public void liquid(float cap) {
        hasLiquids = true;
        outputsLiquid = false;
        liquidCapacity = cap;
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("liquid");
    }

    @Override
    public boolean outputsItems() {
        return false;
    }

    public class MeStorageBuild extends AdvancedBuild implements IMaterialEnergyBuilding {
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
        public int getChannels() {
            return channels;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return false;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return false;
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