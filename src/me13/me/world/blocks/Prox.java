package me13.me.world.blocks;

import me13.me.net.Netting;
import me13.core.block.instance.AdvancedBlock;

public class Prox extends AdvancedBlock {
    public Prox(String name) {
        super(name);
        update = true;
    }

    public class ProxBuild extends AdvancedBuild {
        public boolean isNetEnabled = true;
        public int timer = 0;

        @Override
        public void updateTile() {
            if(timer++ % 30 == 0) {
                isNetEnabled = Netting.isNetEnabled(this);
            }
        }
    }
}
