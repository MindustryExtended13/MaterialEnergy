package me13.me.world.blocks.bus;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Point2;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.items.IllegalItemSelection;
import me13.me.net.Netting;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

public class EIBus extends Bus {
    public TextureRegion item, liquid;
    public boolean isImportMode;

    public EIBus(String name, boolean isImport) {
        super(name);
        isImportMode = isImport;
        outputsLiquid = !isImport;
        acceptsItems = isImport;
        hasItems = hasLiquids = true;
        itemCapacity = (int) (liquidCapacity = 1);
        configurable = true;

        config(Point2.class, (EIBusBuild b, Point2 p) -> {
            b.item = Vars.content.item(p.x);
            b.liquid = Vars.content.liquid(p.y);
        });

        configClear((EIBusBuild b) -> {
            b.item = null;
            b.liquid = null;
        });
    }

    @Override
    public boolean outputsItems() {
        return !isImportMode && super.outputsItems();
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

    public class EIBusBuild extends BusBuild {
        public Liquid liquid;
        public Item item;

        @Override
        public void updateTile() {
            super.updateTile();

            if(!isImportMode) {
                dump(item);
                if(liquid != null && liquids.get(liquid) >= 1) {
                    for(Building building : proximity) {
                        if(building != null && building.acceptLiquid(this, liquid)) {
                            if(building.liquids.get(liquid) < building.block.liquidCapacity) {
                                building.liquids.set(liquid, Math.min(building.liquids.get(liquid)+1,
                                        building.block.liquidCapacity));
                                liquids.remove(liquid, 1);
                            }
                        }
                    }
                }
            }

            if(isNetEnabled) {
                if(isImportMode) {
                    items.each((i, c) -> {
                        if(c > 0 && i != null) {
                            var stack = Netting.includeToNet(this, new ItemStack(i, c));
                            acceptStack(stack.item, stack.amount, this);
                            removeStack(stack.item, c - stack.amount);
                        }
                    });
                    liquids.each((i, c) -> {
                        if(c > 0 && i != null) {
                            var stack = Netting.includeToNet(this, new LiquidStack(i, c));
                            acceptStack(stack.liquid, stack.amount, this);
                            liquids.remove(stack.liquid, c - stack.amount);
                        }
                    });
                } else {
                    if(item != null && items.get(item) < itemCapacity) {
                        var excluded = Netting.excludeFromNet(this, new ItemStack(item, 1));
                        items.add(excluded.item, acceptStack(excluded.item, excluded.amount, this));
                    }
                    if(liquid != null && liquids.get(liquid) < liquidCapacity) {
                        var excluded = Netting.excludeFromNet(this, new LiquidStack(liquid, 1));
                        liquids.add(excluded.liquid, acceptStack(excluded.liquid, excluded.amount, this));
                    }
                }
            }
        }

        @Override
        public Object config() {
            return new Point2(item == null ? -1 : item.id, liquid == null ? -1 : liquid.id);
        }

        @Override
        public void draw() {
            Draw.color(liquid == null ? Color.white : liquid.color);
            Draw.rect(EIBus.this.liquid, x, y);
            Draw.color(item == null ? Color.white : item.color);
            Draw.rect(EIBus.this.item, x, y);
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
        public boolean acceptItem(Building source, Item item) {
            return isImportMode ? (items.get(item) < getMaximumAccepted(item)) &&
                    (item == this.item || this.item == null) : source == this;
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return isImportMode ? (liquids.get(liquid) < liquidCapacity) &&
                    (liquid == this.liquid || this.liquid == null) : source == this;
        }
    }
}

