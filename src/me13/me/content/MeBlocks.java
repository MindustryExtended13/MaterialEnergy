package me13.me.content;

import me13.core.layers.layout.DrawRegion;
import me13.me.world.blocks.*;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import net.tmmc.registry.Registry;
import net.tmmc.registry.RegistryObject;

import java.util.List;

@SuppressWarnings("unused")
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
            size = 3;
        }};
    });

    public static final RegistryObject<Wall> blockFrame = BLOCKS.registry(Wall.class, () -> {
        return new Wall("block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 4, Items.copper, 2));
            health = 100;
        }};
    });

    public static final RegistryObject<Wall> largeBlockFrame = BLOCKS.registry(Wall.class, () -> {
        return new Wall("large-block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 16, Items.copper, 8));
            health = 400;
            size = 2;
        }};
    });

    public static final RegistryObject<StorageMonitor> storageScreen = BLOCKS.registry(StorageMonitor.class,
            () -> {
        return new StorageMonitor("storage-screen") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 16, Items.copper, 8));
            textWidth = 8*2 - ((9/64f)*(8*2))*2;
            health = 350;
            size = 2;
        }};
    });

    public static final RegistryObject<MeStorageBlock> k1Storage = BLOCKS.registry(MeStorageBlock.class, () -> {
       return new MeStorageBlock("1k-storage") {{
            requirements(Category.effect, Blocks.vault.requirements);
            item(1000);
            size = 2;
            health = 150;
       }};
    });

    public static final RegistryObject<MeStorageBlock> k4Storage = BLOCKS.registry(MeStorageBlock.class, () -> {
        return new MeStorageBlock("4k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 1000, Items.thorium, 2000,
                    Items.graphite, 500, Items.copper, 1500));
            item(4000);
            size = 2;
            health = 300;
        }};
    });

    public static final RegistryObject<MeStorageBlock> k16Storage = BLOCKS.registry(MeStorageBlock.class, () -> {
        return new MeStorageBlock("16k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 2000, Items.thorium, 4000,
                    Items.graphite, 1000, Items.copper, 3000, Items.plastanium, 750, Items.phaseFabric, 250));
            item(16000);
            size = 2;
            health = 450;
        }};
    });

    public static final RegistryObject<MeStorageBlock> k64Storage = BLOCKS.registry(MeStorageBlock.class, () -> {
        return new MeStorageBlock("64k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 4000, Items.thorium, 8000,
                    Items.graphite, 2000, Items.copper, 6000, Items.plastanium, 1500,
                    Items.phaseFabric, 500, Items.surgeAlloy, 250, Items.silicon, 1000));
            item(64000);
            size = 2;
            health = 600;
        }};
    });

    public static final RegistryObject<MeStorageBlock> liquidStorage = BLOCKS.registry(MeStorageBlock.class,
            () -> {
        return new MeStorageBlock("liquid-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 1000, Items.thorium, 2000,
                    Items.graphite, 500, Items.copper, 1500));
            liquid(1800*5);
            size = 2;
            health = 400;
        }};
    });
}