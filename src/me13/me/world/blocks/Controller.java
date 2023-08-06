package me13.me.world.blocks;

import arc.Events;
import arc.func.Prov;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import me13.core.block.instance.Layer;
import me13.me.configuration.Channels;
import me13.me.net.Netting;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.ui.Styles;

public class Controller extends Prox {
    public static Color color;
    private static int time;
    public Prov<Layer> top;
    public Layer scheme;

    public static void randColor() {
        color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    static {
        randColor();
        Events.run(EventType.Trigger.update, () -> {
            if(time++ % 30 == 0) {
                randColor();
            }
        });
    }

    public int getChannels() {
        return Channels.conf.getChannelsAddition(name);
    }

    public Controller(String name) {
        super(name);
        configurable = true;
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        super.drawPlanRegion(plan, list);
        scheme.drawPlan(plan, list);
    }

    @Override
    public void load() {
        super.load();
        scheme = top.get();
        scheme.load(this);
    }

    public class ControllerBuild extends ProxBuild {
        public Layer instance = null;

        public int getChannelsAdd() {
            return enabled() ? Controller.this.getChannels() : 0;
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if(instance == null) {
                instance = top.get();
                instance.load(block);
            }

            if(!enabled()) {
                instance.color = Color.white;
            } else if(isNetEnabled) {
                instance.color = color;
            } else {
                instance.color = Color.red;
            }

            instance.draw(this);
        }

        @Override
        public void draw() {
            super.draw();
            if(instance != null) {
                instance.draw(this);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            table.setBackground(Styles.black5);
            table.add(Netting.getChannels(this) + "/" + Netting.getMaximumChannels(this)
                    + " Channels used").pad(6, 6, 6, 6);
        }
    }
}