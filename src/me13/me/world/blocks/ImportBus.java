package me13.me.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Point2;
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

public class ImportBus extends Prox {
    public TextureRegion item, liquid;

    public ImportBus(String name) {
        super(name);
        outputsLiquid = false;
        acceptsItems = true;
        hasItems = hasLiquids = true;
        itemCapacity = (int) (liquidCapacity = 1);
        configurable = true;
        update = true;

        config(Point2.class, (ImportBusBuild b, Point2 p) -> {
            b.item = Vars.content.item(p.x);
            b.liquid = Vars.content.liquid(p.y);
        });

        configClear((ImportBusBuild b) -> {
            b.item = null;
            b.liquid = null;
        });
    }

    @Override
    public boolean outputsItems() {
        return false;
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

    public class ImportBusBuild extends ProxBuild {
        public Liquid liquid;
        public Item item;

        @Override
        public Object config() {
            return new Point2(item == null ? -1 : item.id, liquid == null ? -1 : liquid.id);
        }

        @Override
        public void updateTile() {
            Seq<Building> seq = new Seq<>();
            Netting.getConnections(this, seq);
            if (item != null && items.get(item) > 0) {
                for (var build : seq) {
                    if (build instanceof IMaterialEnergyBuilding building && build != this) {
                        var s = building.storage();
                        if (s != null && s.get(item) < building.maxCapacity()) {
                            building.acceptItem(removeItem(new ItemStack(item, 1)));
                            break;
                        }
                    }
                }
            } else if(item == null) {
                items.each((i, c) -> {
                    if(c > 0 && i != null) {
                        for (var build : seq) {
                            if (build instanceof IMaterialEnergyBuilding building && build != this) {
                                var s = building.storage();
                                if (s != null && s.get(i) < building.maxCapacity()) {
                                    building.acceptItem(removeItem(new ItemStack(i, 1)));
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            if(liquid != null && liquids.get(liquid) > 0) {
                for(var build : seq) {
                    if(build instanceof IMaterialEnergyBuilding building && build != this) {
                        var s = building.storageLiquid();
                        if(s != null && s.get(liquid) < building.maxLiquidCapacity()) {
                            building.acceptLiquid(removeLiquid(new LiquidStack(liquid, 1)));
                            break;
                        }
                    }
                }
            } else if(liquid == null) {
                liquids.each((l, c) -> {
                    if(c > 0 && l != null) {
                        for(var build : seq) {
                            if(build instanceof IMaterialEnergyBuilding building && build != this) {
                                var s = building.storageLiquid();
                                if(s != null && s.get(l) < building.maxLiquidCapacity()) {
                                    building.acceptLiquid(removeLiquid(new LiquidStack(l, 1)));
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void draw() {
            Draw.color(liquid == null ? Color.white : liquid.color);
            Draw.rect(ImportBus.this.liquid, x, y);
            Draw.color(item == null ? Color.white : item.color);
            Draw.rect(ImportBus.this.item, x, y);
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
            return items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return liquids.get(liquid) < liquidCapacity;
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
