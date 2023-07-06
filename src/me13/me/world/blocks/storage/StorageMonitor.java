package me13.me.world.blocks.storage;

import arc.graphics.Color;
import arc.math.geom.Point2;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.graphics.TextDraw;
import me13.core.items.IllegalItemSelection;
import me13.me.MaterialEnergy;
import me13.me.integration.mixin.ItemNetMixin;
import me13.me.integration.mixin.LiquidNetMixin;
import me13.me.net.Netting;
import me13.me.world.blocks.Prox;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.type.Item;
import mindustry.type.Liquid;

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
                if(!isNetEnabled) {
                    text("OFF");
                } else if(config instanceof Item item) {
                    int storage = (int) Netting.getCapFor(this, ItemNetMixin.class, item);
                    text(item.localizedName + "\n" + MaterialEnergy.formatItem(storage));
                } else if(config instanceof Liquid liquid) {
                    float storage = Netting.getCapFor(this, LiquidNetMixin.class, liquid);
                    text(liquid.localizedName + "\n" + MaterialEnergy.formatLiquid(storage));
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
            return new Point2(config instanceof Item ? 1 : 0, config == null ? -1 : config.id);
        }
    }
}