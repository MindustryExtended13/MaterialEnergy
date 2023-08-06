package me13.me;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import me13.me.configuration.MeConfiguration;
import me13.me.content.MeBlocks;
import me13.me.content.MeTech;
import me13.me.integration.*;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class MaterialEnergy extends Mod {
    public static String formatItem(int count) {
        return 'x' + format(count);
    }

    public static String formatLiquid(float count) {
        return format(Math.round(count)) + "mb";
    }

    private static String _func_14103591(int _integer_1521, int _integer_8291) {
        float _float_6720 = (float) _integer_1521 / _integer_8291;
        String _string_1230 = _float_6720 + "";
        return _string_1230.substring(0, Math.min(_string_1230.length()-1, 5));
    }

    public static String format(int value) {
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return value + "";
        if (value < 1000000) return _func_14103591(value, 1000) + "K";
        if (value < 1000000000) return _func_14103591(value, 1000000) + "M";
        return _func_14103591(value, 1000000000) + "B";
    }

    public static Seq<UnlockableContent> liqItem() {
        Seq<UnlockableContent> result = new Seq<>();
        result.addAll(Vars.content.items());
        result.addAll(Vars.content.liquids());
        return result;
    }

    @Override
    public void loadContent() {
        MeBlocks.load();
        MeTech.load();

        Vars.content.blocks().each(block -> {
            if(block.name.startsWith("me-erekir-")) {
                String n = block.name.replace("erekir-", "");
                block.localizedName = Core.bundle.get("block." + n + ".name");
                block.description = Core.bundle.get("block." + n + ".description");
            }
        });
    }

    @Override
    public void init() {
        MeConfiguration.load();
    }

    public MaterialEnergy() {
        Events.on(EventType.ClientLoadEvent.class, ignored -> {
            MaterialEnergyInvoker.load(); //loading self-invoker
            Events.fire(new ModInvokeEvent());
        });
    }

    /*
    public static void invoke(String name, Runnable runnable) {
        var meta = Vars.mods.getMod(name);
        if(meta != null && meta.enabled()) {
            runnable.run();
        }
    }
     */
}
