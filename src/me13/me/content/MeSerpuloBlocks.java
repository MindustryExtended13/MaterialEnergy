package me13.me.content;

import me13.core.block.instance.EnumTextureMapping;
import me13.core.block.instance.Layer;
import me13.me.world.blocks.Cable;
import me13.me.world.blocks.Controller;
import me13.me.world.blocks.Terminal;
import me13.me.world.blocks.bus.ExportBus;
import me13.me.world.blocks.bus.ImportBus;
import me13.me.world.blocks.storage.Adapter;
import me13.me.world.blocks.storage.MeStorageBlock;
import me13.me.world.blocks.storage.StorageMonitor;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

public class MeSerpuloBlocks {
    public static Block cable, adapter, importBus, exportBus, terminal,
            blockFrame, largeBlockFrame, storageScreen, k1Storage, k4Storage,
            k16Storage, k64Storage, liquidStorage, controller;

    public static void load() {
        cable = new Cable("cable") {{
            requirements(Category.effect, ItemStack.with(Items.copper, 2));
            health = 90;
        }};

        controller = new Controller("controller") {{
            drawBase = false;
            requirements(Category.effect, ItemStack.with(Items.graphite, 100,
                    Items.copper, 75, Items.titanium, 25, Items.silicon, 10));
            top = () -> new Layer(this, "-top", EnumTextureMapping.REGION) {{
                rotate = false;
            }};
            layers.add(new Layer("me-block-frame", EnumTextureMapping.REGION) {{
                rotate = false;
            }});
        }};

        adapter = new Adapter("adapter") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 20, Items.copper, 12));
            layers.add(
                    new Layer("me-block-frame", EnumTextureMapping.REGION) {{
                        rotate = false;
                    }},
                    new Layer(this, "-arrow", EnumTextureMapping.REGION) {{
                        color = null;
                    }}
            );
            health = 90;
        }};

        importBus = new ImportBus("import-bus") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 10, Items.copper, 5));
            health = 100;
        }};

        exportBus = new ExportBus("export-bus") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 10, Items.copper, 5));
            health = 100;
        }};

        terminal = new Terminal("terminal") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 40, Items.copper, 24,
                    Items.plastanium, 30, Items.metaglass, 20, Items.titanium, 10));
            health = 180;
            size = 3;
        }};

        blockFrame = new Wall("block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 4, Items.copper, 2));
            health = 100;
        }};

        largeBlockFrame = new Wall("large-block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 16, Items.copper, 8));
            health = 400;
            size = 2;
        }};

        storageScreen = new StorageMonitor("storage-screen") {{
            requirements(Category.effect, ItemStack.with(Items.graphite, 16, Items.copper, 8));
            textWidth = 8*2-((9/64f)*(8*2))*2;
            health = 350;
            size = 2;
        }};

        k1Storage = new MeStorageBlock("1k-storage") {{
            requirements(Category.effect, Blocks.vault.requirements);
            item(1000);
            size = 2;
            health = 150;
        }};

        k4Storage = new MeStorageBlock("4k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 1000, Items.thorium, 2000,
                    Items.graphite, 500, Items.copper, 1500));
            item(4000);
            size = 2;
            health = 300;
        }};

        k16Storage = new MeStorageBlock("16k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 2000, Items.thorium, 4000,
                    Items.graphite, 1000, Items.copper, 3000, Items.plastanium, 750, Items.phaseFabric, 250));
            item(16000);
            channels = 2;
            size = 2;
            health = 450;
        }};

        k64Storage = new MeStorageBlock("64k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 4000, Items.thorium, 8000,
                    Items.graphite, 2000, Items.copper, 6000, Items.plastanium, 1500,
                    Items.phaseFabric, 500, Items.surgeAlloy, 250, Items.silicon, 1000));
            item(64000);
            channels = 2;
            size = 2;
            health = 600;
        }};

        liquidStorage = new MeStorageBlock("liquid-storage") {{
            requirements(Category.effect, ItemStack.with(Items.titanium, 1000, Items.thorium, 2000,
                    Items.graphite, 500, Items.copper, 1500));
            liquid(1800*5);
            size = 2;
            health = 400;
        }};
    }
}
