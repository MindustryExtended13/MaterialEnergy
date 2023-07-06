package me13.me.integration.mixin.base;

import arc.struct.Seq;
import mindustry.gen.Building;

public interface IMeBuildingBaseMixin {
    boolean canUseMixin(Building self, IMeNetBaseMixin mixin);
    boolean canConnectTo(Building self, Building other);
    Seq<IMeNetBaseMixin> getMixinsSelf(Building self);
    Seq<Building> getChildren(Building self);
    int getChannelsAdd(Building self);
    void updateState(Building self);
    int getChannels(Building self);
    Building getMixinBuild(Building self);
}