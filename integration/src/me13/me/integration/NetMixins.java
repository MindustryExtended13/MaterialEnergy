package me13.me.integration;

import arc.struct.Seq;
import java.util.Objects;
import mindustry.world.Block;
import me13.me.integration.mixin.base.IMeNetBaseMixin;

public class NetMixins {
    private static final Seq<IMeNetBaseMixin> mixins = new Seq<>();
    private static final Seq<Block> cables = new Seq<>();

    public static<T extends IMeNetBaseMixin> void register(T mixin) {
        mixins.add(Objects.requireNonNull(mixin));
    }

    public static void registerCable(Block block) {
        cables.add(Objects.requireNonNull(block));
    }

    public static Seq<IMeNetBaseMixin> getMixins() {
        return mixins;
    }

    public static Seq<Block> getCables() {
        return cables;
    }
}