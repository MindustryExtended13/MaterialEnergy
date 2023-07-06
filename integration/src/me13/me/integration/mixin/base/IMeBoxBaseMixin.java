package me13.me.integration.mixin.base;

import arc.scene.ui.layout.Table;
import mindustry.gen.Building;

public interface IMeBoxBaseMixin {
    void buildTerminal(Table table, Building building);
    float getCount(Building building);
    String format(float value);
}