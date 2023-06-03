package me13.me.ui;

import arc.graphics.Color;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import me13.me.MaterialEnergy;
import me13.me.net.Netting;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.type.ItemStack;
import mindustry.ui.dialogs.BaseDialog;
import net.tmmc.graphics.FDraw;

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
        var c = cont.table(main -> {
            main.background(Tex.button);
            main.table(info -> {
                info.image(MaterialEnergy.atlas3.get("its")).left().size(96).update(img -> {
                    //rotate
                });
                info.table(other -> {}).update(t -> {
                    t.clearChildren();
                    t.table(item -> {
                        int cap = Netting.maxCapacity(building);
                        item.add("x"+Seq.with(Netting.getStorage(building)).sum(s -> s.amount))
                                .fontScale(1.5f).color(Color.orange);
                        item.add("/x" + cap + " [" + cap * Vars.content.items().size + ']').color(Color.gray);
                    }).left().row();
                    t.table(liquid -> {
                        liquid.add(Math.round(Seq.with(Netting.getStorageLiquid(building)).sumf(s -> s.amount))
                                        + "mb").fontScale(1.5f).color(Color.orange);
                        liquid.add("/" + Math.round(Netting.maxLiquidCapacity(building)) + "mb").color(Color.gray);
                    }).left();
                }).grow();
            }).growX().left().top().row();
            main.pane(pane2 -> {
                pane[0] = pane2;
            }).update(ignored -> {
                int cols = (int) (FDraw.width(0.8f)/200);
                int i = 0;
                pane[0].clearChildren();
                for(var stack : Netting.getStorage(building)) {
                    pane[0].table(t -> {
                        t.add(new Image(stack.item.uiIcon).setScaling(Scaling.fit)).size(40);
                        t.table(info -> {
                            info.add(stack.item.localizedName).left().row();
                            info.add("x" + stack.amount).left();
                        }).pad(6).grow();
                    }).left().height(50);
                    if(++i % cols == 0) {
                        pane[0].row();
                    }
                }
                for(var stack : Netting.getStorageLiquid(building)) {
                    pane[0].table(t -> {
                        t.add(new Image(stack.liquid.uiIcon).setScaling(Scaling.fit)).size(40);
                        t.table(info -> {
                            info.add(stack.liquid.localizedName).left().row();
                            info.add(Math.round(stack.amount) + "mb").left();
                        }).pad(6).grow();
                    }).left().height(50);
                    if(++i % cols == 0) {
                        pane[0].row();
                    }
                }
            }).grow();
        }).center();
    }
}