package me13.me.world.blocks;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.me.net.Netting;
import me13.me.ui.TerminalDialog;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import mindustry.net.Net;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.modules.ItemModule;
import mindustry.world.modules.LiquidModule;
import net.tmmc.util.GraphBlock;

import java.util.HashMap;
import java.util.Map;

public class Terminal extends GraphBlock implements IMaterialEnergyBlock {
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

    public class TerminalBuild extends GraphBlockBuild implements IMaterialEnergyBuilding {
        @Override
        public void buildConfiguration(Table table) {
            new TerminalDialog(this).show();
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
    }
}
