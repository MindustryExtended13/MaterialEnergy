package example;

import mindustry.game.EventType;

import net.tmmc.graphics.ImageBackgroundRender;
import net.tmmc.util.events.Events;
import net.tmmc.ApplicationMod;
import net.tmmc.util.UIUtils;

@net.tmmc.annotations.Mod
public class ExampleJavaMod extends ApplicationMod {
    {
        contentLoad(() -> {
            logger.info("Loading some example content.");
            ExampleContent.load();
        });

        init(() -> {
            logger.info("Inited " + loadedMod.getDisplayName());
            logger.info(loadedMod.getRepoURL());
        });
    }

    public ExampleJavaMod() {
        Events.currentEvent = EventType.ClientLoadEvent.class;
        Events.register(10, () -> UIUtils.invoke("frog", table -> {
            var reg = this.atlas.createAnimation("frog", 108);
            reg.runTrigger(30f);

            table.add("behold").row();
            table.image(reg).pad(20f).size(108).row();
            table.button("play siren", adapter.get("siren")::play).size(200, 50);

            ImageBackgroundRender.pack(atlas.get("vietnam"));
        }));
    }
}
