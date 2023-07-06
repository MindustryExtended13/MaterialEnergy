package me13.me.world.blocks.storage;

import me13.core.block.instance.AdvancedBlock;

public class MeStorageBlock extends AdvancedBlock {
    public int channels = 1;

    public MeStorageBlock(String name) {
        super(name);
    }

    public void item(int cap) {
        hasItems = true;
        itemCapacity = cap;
    }

    public void liquid(float cap) {
        hasLiquids = true;
        liquidCapacity = cap;
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("liquid");
    }

    public class MeStorageBuild extends AdvancedBuild {
    }
}