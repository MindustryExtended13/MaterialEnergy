package me13.me.integration.mixin;

import arc.struct.Seq;
import me13.me.integration.mixin.base.IMeNetBaseMixin;
import me13.me.integration.mixin.base.IMeBuildingBaseMixin;
import mindustry.gen.Building;

public interface IMeBuildingMixin extends IMeBuildingBaseMixin {
    @Override
    default boolean canUseMixin(Building self, IMeNetBaseMixin mixin) {
        return mixin != null && mixin.haveMixinIn(self) && self.enabled();
    }

    @Override
    default Seq<IMeNetBaseMixin> getMixinsSelf(Building self) {
        return new Seq<>();
    }

    @Override
    default Seq<Building> getChildren(Building self) {
        return self.proximity;
    }

    @Override
    default int getChannelsAdd(Building self) {
        return 0;
    }

    @Override
    default void updateState(Building self) {
    }

    @Override
    default int getChannels(Building self) {
        return 1;
    }

    @Override
    default Building getMixinBuild(Building self) {
        return self;
    }
}