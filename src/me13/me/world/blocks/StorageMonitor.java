package me13.me.world.blocks;

import arc.func.Boolf;
import arc.graphics.Color;
import arc.math.geom.Point2;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.graphics.TextDraw;
import me13.me.MaterialEnergy;
import me13.me.net.Netting;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;
import net.tmmc.util.BoolInt;
import net.tmmc.util.IllegalItemSelection;

public class StorageMonitor extends Prox {
    public float textWidth;

    public StorageMonitor(String name) {
        super(name);
        configurable = true;

        config(Point2.class, (StorageMonitorBuild build, Point2 content) -> {
            boolean isItem = content.x == 1;
            if(isItem) {
                build.config = Vars.content.item(content.y);
            } else {
                build.config = Vars.content.liquid(content.y);
            }
        });

        configClear((StorageMonitorBuild build) -> {
            build.config = null;
        });
    }

    public class StorageMonitorBuild extends ProxBuild {
        public UnlockableContent config;

        public float getWidth() {
            return textWidth;
        }

        public void text(String str) {
            TextDraw.text(x, y, getWidth(), Color.cyan, str);
        }

        @Override
        public void draw() {
            super.draw();
            if(config != null) {
                if(config instanceof Item item) {
                    int c = 0;
                    for(var s : Netting.getStorage(this)) {
                        if(s.item == item) {
                            c = s.amount;
                        }
                    }
                    text(item.localizedName + "\n" + MaterialEnergy.formatItem(c));
                } else if(config instanceof Liquid liquid) {
                    float c = 0;
                    for(var s : Netting.getStorageLiquid(this)) {
                        if(s.liquid == liquid) {
                            c = s.amount;
                        }
                    }
                    text(liquid.localizedName + "\n" + MaterialEnergy.formatLiquid(c));
                } else {
                    text("ERROR");
                }
            } else {
                text("NULL");
            }
        }

        @Override
        public void write(Writes write) {
            write.bool(config instanceof Item);
            write.str(config.name);
        }

        @Override
        public void read(Reads read, byte revision) {
            boolean isItem = read.bool();
            String name = read.str();
            if(isItem) {
                config = Vars.content.item(name);
            } else {
                config = Vars.content.liquid(name);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            IllegalItemSelection.buildTable(table, MaterialEnergy.liqItem(), () -> config, (cof) -> {
                config = cof;
            });
        }

        @Override
        public Object config() {
            return new Point2(BoolInt.toInt(config instanceof Item), config.id);
        }

        @Override
        public ItemStack acceptItem(ItemStack itemStack) {
            return null;
        }

        @Override
        public ItemStack removeItem(ItemStack itemStack) {
            return null;
        }

        @Override
        public LiquidStack acceptLiquid(LiquidStack liquidStack) {
            return null;
        }

        @Override
        public LiquidStack removeLiquid(LiquidStack liquidStack) {
            return null;
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
            return null;
        }

        @Override
        public LiquidModule storageLiquid() {
            return null;
        }
    }
}