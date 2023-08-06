package me13.me.integration;

import arc.struct.Seq;
import me13.me.integration.mixin.base.IMeBoxBaseMixin;

public class BoxMixins {
    private static final Seq<IMeBoxBaseMixin> mixins = new Seq<>();

    public static<T extends IMeBoxBaseMixin> void register(T mixin) {
        if(mixin != null) mixins.add(mixin);
    }

    public static Seq<IMeBoxBaseMixin> getMixins() {
        return mixins;
    }
}
