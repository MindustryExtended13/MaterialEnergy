package me13.me.world.blocks;

import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;
import me13.core.layers.blocks.LayerBlock;
import me13.core.layers.layout.DrawAtlas;
import net.tmmc.util.XBlocks;

import java.util.List;

public class Cable extends LayerBlock {
    public Cable(String name) {
        super(name);
        drawDefault = false;
        layers = List.of(new DrawAtlas() {{
            prefix = "";
            boolf = (tile, self) -> XBlocks.isTheSameBlock(tile.build, Cable.this)
                    || (tile.build instanceof IMaterialEnergyBuilding b && b.canConnectTo(self));
            boolfHeme = (tile, self, other) -> other != null && (self.block == other.block
                    || other.block instanceof IMaterialEnergyBlock);
        }});
    }
}