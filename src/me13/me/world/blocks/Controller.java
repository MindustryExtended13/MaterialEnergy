package me13.me.world.blocks;

import arc.Events;
import arc.func.Prov;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Log;
import me13.core.block.instance.AdvancedBlock;
import me13.core.block.instance.Layer;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.configuration.Channels;
import me13.me.net.Netting;
import mindustry.entities.units.BuildPlan;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;

public class Controller extends AdvancedBlock implements IMaterialEnergyBlock {
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
        update = true;
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

    public class ControllerBuild extends AdvancedBuild implements IMaterialEnergyBuilding {
        public Seq<Building> cableProximity = new Seq<>();
        public boolean netEnabled = false;
        public Layer instance = null;
        public int timer = 0;

        @Override
        public int getChannelsAdd() {
            return enabled() ? Controller.this.getChannels() : 0;
        }

        @Override
        public void updateTile() {
            updateState();
            if(instance == null) {
                instance = top.get();
                instance.load(block);
            }

            if(!enabled()) {
                instance.color = Color.white;
            } else if(netEnabled) {
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
        public int getChannels() {
            return 0;
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

        @Override
        public boolean canConnectTo(Building building) {
            return true;
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table((t) -> {
                t.setBackground(Tex.button);
            }).update(table2 -> {
                table2.clearChildren();
                table2.add(Netting.getChannels(this) + "/" +
                        Netting.getMaximumChannels(this) + " Channels used");
            });
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            cableProximity = proximity.copy().filter(b -> b != null &&
                    (b.block instanceof Cable || b instanceof ControllerBuild));
        }

        @Override
        public Seq<Building> getChildren() {
            return cableProximity;
        }

        @Override
        public void updateState() {
            netEnabled = Netting.isNetEnabled(this);
        }
    }
}