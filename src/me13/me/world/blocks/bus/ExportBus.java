package me13.me.world.blocks.bus;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Point2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.core.items.IllegalItemSelection;
import me13.me.net.Netting;
import me13.me.world.blocks.Prox;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;

public class ExportBus extends Prox {
    public TextureRegion item, liquid;

    public ExportBus(String name) {
        super(name);
        outputsLiquid = true;
        acceptsItems = false;
        hasItems = hasLiquids = true;
        itemCapacity = (int) (liquidCapacity = 1);
        configurable = true;

        config(Point2.class, (ExportBusBuild b, Point2 p) -> {
            b.item = Vars.content.item(p.x);
            b.liquid = Vars.content.liquid(p.y);
        });

        configClear((ExportBusBuild b) -> {
            b.item = null;
            b.liquid = null;
        });
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

    public class ExportBusBuild extends ProxBuild {
        public Liquid liquid;
        public Item item;

        @Override
        public void updateTile() {
            super.updateTile();
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

            if(isNetEnabled) {
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
        }

        @Override
        public Object config() {
            return new Point2(item == null ? -1 : item.id, liquid == null ? -1 : liquid.id);
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
    }
}