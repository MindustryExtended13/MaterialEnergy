package me13.me.content;

import me13.core.layers.layout.DrawRegion;
import me13.me.world.blocks.*;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.storage.StorageBlock;
import net.tmmc.registry.Registry;
import net.tmmc.registry.RegistryObject;

import java.util.List;

public class MeBlocks {
    public static final Registry<Block> BLOCKS = new Registry<>(Block.class);

    public static final RegistryObject<Cable> cable = BLOCKS.registry(Cable.class, () -> {
        return new Cable("cable") {{
            requirements(Category.effect, ItemStack.with(Items.copper, 2));
            health = 90;
        }};
    });

    public static final RegistryObject<Adapter> adapter = BLOCKS.registry(Adapter.class, () -> {
        return new Adapter("adapter") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 20, Items.copper, 12));
            layers = List.of(
                    new DrawRegion() {{
                        this.rotateDraw = false;
                        this.prefix = "-base";
                    }},
                    new DrawRegion() {{
                        this.rotateDraw = true;
                        this.prefix = "-arrow";
                    }}
            );
            health = 90;
        }};
    });

    public static final RegistryObject<ImportBus> importBus = BLOCKS.registry(ImportBus.class, () -> {
        return new ImportBus("import-bus") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 10, Items.copper, 5));
            health = 100;
        }};
    });

    public static final RegistryObject<ExportBus> exportBus = BLOCKS.registry(ExportBus.class, () -> {
        return new ExportBus("export-bus") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 10, Items.copper, 5));
            health = 100;
        }};
    });

    public static final RegistryObject<Terminal> terminal = BLOCKS.registry(Terminal.class, () -> {
        return new Terminal("terminal") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 40, Items.copper, 24,
                    Items.plastanium, 30, Items.metaglass, 20, Items.titanium, 10));
            health = 180;
        }};
    });

    public static final RegistryObject<StorageBlock> blockFrame = BLOCKS.registry(StorageBlock.class, () -> {
        return new StorageBlock("block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 4, Items.copper, 2));
            itemCapacity = 15;
            health = 100;
        }};
    });
}