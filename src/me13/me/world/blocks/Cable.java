package me13.me.world.blocks;

import me13.core.block.instance.AdvancedBlock;
import me13.core.block.instance.EnumTextureMapping;
import me13.core.block.instance.Layer;
import me13.core.intergration.IMaterialEnergyBlock;
import me13.core.intergration.IMaterialEnergyBuilding;

public class Cable extends AdvancedBlock {
    public Cable(String name) {
        super(name);
        drawBase = false;
        layers.add(new Layer(this, "-", EnumTextureMapping.TF_TYPE) {{
            rotate = false;
            hand = (self, other, tile) -> ((other != null && other.block instanceof Cable)
                    || (tile.build instanceof IMaterialEnergyBuilding b && b.canConnectTo(self)))
                    && tile.build != null && tile.build.team == self.team;
            hand2 = (self, other, tile) -> other != null && (other.block instanceof Cable
                    || other.block instanceof IMaterialEnergyBlock);
        }});
    }
}