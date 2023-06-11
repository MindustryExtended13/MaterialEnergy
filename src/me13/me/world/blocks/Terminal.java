package me13.me.world.blocks;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import me13.core.block.instance.AdvancedBlock;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.net.Netting;
import me13.me.ui.TerminalDialog;
import mindustry.gen.Building;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;

public class Terminal extends AdvancedBlock implements IMaterialEnergyBlock {
    public TextureRegion teamRegion;

    public Terminal(String name) {
        super(name);
        configurable = true;
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[] {region, teamRegion};
    }

    @Override
    public void load() {
        super.load();
        teamRegion = Core.atlas.find(name + "-team");
    }

    @SuppressWarnings("unused")
    public class TerminalBuild extends AdvancedBuild implements IMaterialEnergyBuilding {
        @Override
        public void buildConfiguration(Table table) {
            if(Netting.isNetEnabled(this)) {
                new TerminalDialog(this).show();
            } else {
                deselect();
            }
        }

        @Override
        public void draw() {
            super.draw();
            Draw.color(team.color);
            Draw.rect(teamRegion, x, y, drawrot());
            Draw.reset();
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
            return 1;
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
        public Seq<Building> getChildren() {
            return proximity.copy().filter(b -> b != null && b.block instanceof Cable);
        }
    }
}
