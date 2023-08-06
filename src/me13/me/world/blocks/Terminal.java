package me13.me.world.blocks;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import me13.core.block.instance.AdvancedBlock;
import me13.me.net.Netting;
import me13.me.ui.TerminalDialog;

public class Terminal extends AdvancedBlock {
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
    public class TerminalBuild extends AdvancedBuild {
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
    }
}
