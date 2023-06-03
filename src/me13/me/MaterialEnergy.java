package me13.me;

import me13.me.content.MeBlocks;
import net.tmmc.annotations.Mod;
import net.tmmc.ApplicationMod;
import net.tmmc.graphics.ModAtlas;
import net.tmmc.json.JsonMod;
import net.tmmc.registry.ModContentRegistryEvent;
import net.tmmc.util.ModLogger;

@Mod
public class MaterialEnergy extends ApplicationMod {
    public static JsonMod modME;
    public static ModLogger logger3;
    public static ModAtlas atlas3;

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
