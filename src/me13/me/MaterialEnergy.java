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

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class MaterialEnergy extends Mod {
    private static final NavigableMap<Integer, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1000, "K");
        suffixes.put(1000000, "M");
        suffixes.put(1000000000, "B");
    }

    public static String formatItem(int count) {
        return 'x' + format(count);
    }

    public static String formatLiquid(float count) {
        return format(Math.round(count)) + "mb";
    }

    public static String format(int value) {
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value);

        Map.Entry<Integer, String> e = suffixes.floorEntry(value);
        Integer divideBy = e.getKey();
        String suffix = e.getValue();

        int truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != ((double) truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
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
        Events.on(ModInvokeEvent.class, ignored -> {
            invoke("inf", InformatronicsInvoker::load);
        });

        Events.on(EventType.ClientLoadEvent.class, ignored -> {
            MaterialEnergyInvoker.load(); //loading self-invoker
            Events.fire(new ModInvokeEvent());
        });
    }

    public static void invoke(String name, Runnable runnable) {
        var meta = Vars.mods.getMod(name);
        if(meta != null && meta.enabled()) {
            runnable.run();
        }
    }
}
