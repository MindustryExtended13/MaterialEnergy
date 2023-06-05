package me13.me;

import arc.struct.Seq;
import me13.me.content.MeBlocks;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import net.tmmc.annotations.Mod;
import net.tmmc.ApplicationMod;
import net.tmmc.graphics.ModAtlas;
import net.tmmc.json.JsonMod;
import net.tmmc.registry.ModContentRegistryEvent;
import net.tmmc.util.ModLogger;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Mod
public class MaterialEnergy extends ApplicationMod {
    private static final NavigableMap<Integer, String> suffixes = new TreeMap<>();

    public static JsonMod modME;
    public static ModLogger logger3;
    public static ModAtlas atlas3;

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

    {
        contentLoad(() -> {
            logger.info("Loading some example content.");
        });

        init(() -> {
            modME = loadedMod;
            logger3 = logger;
            atlas3 = atlas;

            logger.info("Inited " + loadedMod.getDisplayName());
            logger.info(loadedMod.getRepoURL());
        });
    }

    public MaterialEnergy() {
        final var event = new ModContentRegistryEvent();
        event.registry(MeBlocks.BLOCKS);
        event.activate(this);
    }
}
