package me13.me.world.blocks.storage;

import me13.core.block.instance.AdvancedBlock;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;

public class Adapter extends AdvancedBlock implements IMaterialEnergyBlock {
    public Adapter(String name) {
        super(name);
        rotate = true;
        quickRotate = true;
        drawBase = false;
    }

    @SuppressWarnings("unused")
    public class AdapterBuild extends AdvancedBuild implements IMaterialEnergyBuilding {
        public boolean hasHost() {
            Building nearby = nearby();
            return nearby != null && nearby.team() == team();
        }

        public boolean isItem() {
            return hasHost() && (nearby().block.acceptsItems || nearby().block.hasItems);
        }

        public boolean isLiquid() {
            return hasHost() && nearby().block.hasLiquids;
        }

        @Override
        public ItemStack acceptItem(ItemStack itemStack) {
            if(isItem()) {
                var module = storage();
                int accepted = nearby().acceptStack(itemStack.item, itemStack.amount, this);
                if(accepted > 0) {
                    module.add(itemStack.item, accepted);
                }
                return new ItemStack(itemStack.item, itemStack.amount-accepted);
            } else return itemStack;
        }

        @Override
        public ItemStack removeItem(ItemStack itemStack) {
            if(isItem()) {
                int removed = nearby().removeStack(itemStack.item, itemStack.amount);
                return new ItemStack(itemStack.item, removed);
            } else return itemStack;
        }

        @Override
        public LiquidStack acceptLiquid(LiquidStack liquidStack) {
            if(isLiquid()) {
                var liquid = liquidStack.liquid;
                if(nearby().acceptLiquid(this, liquid)) {
                    var module = storageLiquid();
                    float capacity = nearby().block.liquidCapacity;
                    float accepted = Math.min(capacity-module.get(liquid), liquidStack.amount);
                    if(accepted > 0) {
                        module.add(liquid, accepted);
                    }
                    return new LiquidStack(liquid, liquidStack.amount-accepted);
                }
            }
            return liquidStack;
        }

        @Override
        public LiquidStack removeLiquid(LiquidStack liquidStack) {
            if(isLiquid()) {
                var liquid = liquidStack.liquid;
                if(nearby().acceptLiquid(this, liquid)) {
                    var module = storageLiquid();
                    float removed = Math.min(liquidStack.amount, module.get(liquid));
                    if(removed > 0) {
                        module.remove(liquid, removed);
                    }
                    return new LiquidStack(liquid, removed);
                }
            }
            return liquidStack;
        }

        @Override
        public int getChannels() {
            return (isItem() || isLiquid()) ? 1 : 0;
        }

        @Override
        public int maxCapacity() {
            return isItem() ? nearby().block.itemCapacity : 0;
        }

        @Override
        public float maxLiquidCapacity() {
            return isLiquid() ? nearby().block.liquidCapacity : 0;
        }

        @Override
        public ItemModule storage() {
            return isItem() ? nearby().items : null;
        }

        @Override
        public LiquidModule storageLiquid() {
            return isLiquid() ? nearby().liquids : null;
        }

        @Override
        public boolean canConnectTo(Building building) {
            return building != nearby();
        }
    }
}
