package me13.me.world.blocks.storage;

import me13.core.block.instance.AdvancedBlock;

public class Adapter extends AdvancedBlock {
    public Adapter(String name) {
        super(name);
        rotate = true;
        quickRotate = true;
        drawBase = false;
    }

    public class AdapterBuild extends AdvancedBuild {
    }
}
