package me13.me.integration.mixin;

import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import me13.me.integration.mixin.base.IMeBoxBaseMixin;
import mindustry.gen.Building;

public interface IMeBoxMixin extends IMeBoxBaseMixin {
    String name();
    TextureRegion icon();

    @Override
    default void buildTerminal(Table table, Building building) {
        table.table(t -> {
            t.add(new Image(icon()).setScaling(Scaling.fit)).size(40);
            t.table(info -> {
                info.add(name()).left().row();
                info.add(format(getCount(building))).left();
            }).pad(6).grow();
        }).left().height(50);
    }
}