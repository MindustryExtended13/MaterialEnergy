package me13.me.world.blocks;

import me13.core.block.instance.AdvancedBlock;
import me13.core.block.instance.EnumTextureMapping;
import me13.core.block.instance.Layer;
import me13.me.integration.BuildingMixins;
import me13.me.integration.NetMixins;

public class Cable extends AdvancedBlock {
    public Cable(String name) {
        super(name);
        drawBase = false;
        layers.add(new Layer(this, "-", EnumTextureMapping.TF_TYPE) {{
            rotate = false;
            hand = (self, other, tile) -> other != null && (NetMixins.getCables().contains(other.block)
                    || (BuildingMixins.isMeBuilding(other) && BuildingMixins.getMixin(other).canConnectTo(
                            other, self)))
                    && tile.build != null && tile.build.team == self.team;
            hand2 = (self, other, tile) -> other != null && (NetMixins.getCables().contains(other.block)
                    || BuildingMixins.meBlocks.contains(other.block));
        }});
    }
}