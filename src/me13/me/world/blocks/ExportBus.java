package me13.me.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.net.Netting;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;
import net.tmmc.util.GraphBlock;
import net.tmmc.util.IllegalItemSelection;

public class ExportBus extends GraphBlock implements IMaterialEnergyBlock {
    public TextureRegion item, liquid;

    public ExportBus(String name) {
        super(name);
        outputsLiquid = true;
        acceptsItems = false;
        hasItems = hasLiquids = true;
        itemCapacity = (int) (liquidCapacity = 10);
        configurable = true;
        update = true;
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[] {item, liquid, region};
    }

    @Override
    public void load() {
        super.load();
        item = Core.atlas.find(name+"-item");
        liquid = Core.atlas.find(name+"-liquid");
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("liquid");
        removeBar("items");
    }

    public class ExportBusBuild extends GraphBlockBuild implements IMaterialEnergyBuilding {
        public Liquid liquid;
        public Item item;

        @Override
        public void updateTile() {
            dump(item);
            if(liquid != null) {
                dumpLiquid(liquid);
            }

            Seq<Building> seq = new Seq<>();
            Netting.getConnections(this, seq);
            if(item != null && items.get(item) < itemCapacity) {
                for(var build : seq) {
                    if(build instanceof IMaterialEnergyBuilding building && build != this) {
                        var s = building.storage();
                        if(s != null && s.has(item)) {
                            acceptItem(building.removeItem(new ItemStack(item, 1)));
                            break;
                        }
                    }
                }
            }
            if(liquid != null && liquids.get(liquid) < liquidCapacity) {
                for(var build : seq) {
                    if(build instanceof IMaterialEnergyBuilding building && build != this) {
                        var s = building.storageLiquid();
                        if(s != null && s.get(liquid) > 0) {
                            acceptLiquid(building.removeLiquid(new LiquidStack(liquid, 1)));
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void draw() {
            Draw.color(liquid == null ? Color.white : liquid.color);
            Draw.rect(ExportBus.this.liquid, x, y);
            Draw.color(item == null ? Color.white : item.color);
            Draw.rect(ExportBus.this.item, x, y);
            Draw.reset();
            super.draw();
        }

        @Override
        public void write(Writes write) {
            write.str(item == null ? "null" : item.name);
            write.str(liquid == null ? "null" : liquid.name);
        }

        @Override
        public void read(Reads read, byte revision) {
            item = Vars.content.item(read.str());
            liquid = Vars.content.liquid(read.str());
        }

        @Override
        public void buildConfiguration(Table table) {
            IllegalItemSelection.buildTable(table, Vars.content.items(), () -> item, t -> {
                item = t;
            });
            table.row();
            IllegalItemSelection.buildTable(table, Vars.content.liquids(), () -> liquid, t -> {
                liquid = t;
            });
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
        public boolean acceptItem(Building source, Item item) {
            return false;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return false;
        }

        @Override
        public int maxCapacity() {
            return 0;
        }

        @Override
        public float maxLiquidCapacity() {
            return 0;
        }

        @Override
        public ItemModule storage() {
            return new ItemModule();
        }

        @Override
        public LiquidModule storageLiquid() {
            return new LiquidModule();
        }

        @Override
        public boolean canConnectTo(Building building) {
            return true;
        }
    }
}