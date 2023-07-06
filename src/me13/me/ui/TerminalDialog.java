package me13.me.ui;

import arc.Core;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import me13.me.integration.BoxMixins;
import me13.me.integration.mixin.ItemNetMixin;
import me13.me.integration.mixin.LiquidNetMixin;
import me13.me.net.Netting;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;

public class TerminalDialog extends BaseDialog {
    public Building building;

    public TerminalDialog(Building building) {
        super(building.block.localizedName);
        this.building = building;

        buttons.defaults().size(200, 50);
        buttons.button("@exit", Icon.left, () -> {
            building.deselect();
            hide();
        });

        final Table[] pane = new Table[1];
        cont.table(main -> {
            main.background(Tex.button);
            main.table(info -> {
                info.image(Core.atlas.find("me-its")).left().size(96).update(img -> {
                    //rotate
                });
            }).growX().left().top().row();
            main.pane(pane2 -> {
                pane[0] = pane2;
            }).update(ignored -> {
                int cols = (int) ((Core.scene.getWidth()*0.8)/200);
                final int[] i = {0};
                pane[0].clearChildren();
                BoxMixins.getMixins().forEach(mixin -> {
                    mixin.buildTerminal(pane[0], building);
                    if(++i[0] % cols == 0) {
                        pane[0].row();
                    }
                });
            }).grow();
        }).center();
    }
}