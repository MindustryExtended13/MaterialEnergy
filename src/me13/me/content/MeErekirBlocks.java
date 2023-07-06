package me13.me.content;

import me13.core.block.instance.EnumTextureMapping;
import me13.core.block.instance.Layer;
import me13.me.world.blocks.Cable;
import me13.me.world.blocks.Controller;
import me13.me.world.blocks.Terminal;
import me13.me.world.blocks.bus.EIBus;
import me13.me.world.blocks.storage.Adapter;
import me13.me.world.blocks.storage.MeStorageBlock;
import me13.me.world.blocks.storage.StorageMonitor;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

public class MeErekirBlocks {
    public static Block cable, adapter, importBus, exportBus, terminal,
            blockFrame, largeBlockFrame, storageScreen, k1Storage, k4Storage,
            k16Storage, k64Storage, liquidStorage, controller;

    public static void load() {
        cable = new Cable("erekir-cable") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 2));
            health = 90;
        }};

        controller = new Controller("erekir-controller") {{
            drawBase = false;
            requirements(Category.effect, ItemStack.with(Items.tungsten, 100, Items.beryllium, 75));
            top = () -> new Layer(this, "-top", EnumTextureMapping.REGION) {{
                rotate = false;
            }};
            layers.add(new Layer("me-erekir-block-frame", EnumTextureMapping.REGION) {{
                rotate = false;
            }});
        }};

        adapter = new Adapter("erekir-adapter") {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 20, Items.beryllium, 12));
            layers.add(
                    new Layer("me-erekir-block-frame", EnumTextureMapping.REGION) {{
                        rotate = false;
                    }},
                    new Layer(this, "-arrow", EnumTextureMapping.REGION) {{
                        color = null;
                    }}
            );
            health = 90;
        }};

        importBus = new EIBus("erekir-import-bus", true) {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 10, Items.beryllium, 5));
            health = 100;
        }};

        exportBus = new EIBus("erekir-export-bus", false) {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 10, Items.beryllium, 5));
            health = 100;
        }};

        terminal = new Terminal("erekir-terminal") {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 40,
                    Items.beryllium, 24, Items.oxide, 30));
            health = 180;
            size = 3;
        }};

        blockFrame = new Wall("erekir-block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 4, Items.beryllium, 2));
            health = 100;
        }};

        largeBlockFrame = new Wall("erekir-large-block-frame") {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 16, Items.beryllium, 8));
            health = 400;
            size = 2;
        }};

        storageScreen = new StorageMonitor("erekir-storage-screen") {{
            requirements(Category.effect, ItemStack.with(Items.tungsten, 16, Items.beryllium, 8));
            textWidth = 8*2-((9/64f)*(8*2))*2;
            health = 350;
            size = 2;
        }};

        k1Storage = new MeStorageBlock("erekir-1k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 200));
            item(1000);
            size = 2;
            health = 150;
        }};

        k4Storage = new MeStorageBlock("erekir-4k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 400, Items.tungsten, 200));
            item(4000);
            size = 2;
            health = 300;
        }};

        k16Storage = new MeStorageBlock("erekir-16k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 800,
                    Items.tungsten, 400, Items.carbide, 200));
            item(16000);
            channels = 2;
            size = 2;
            health = 450;
        }};

        k64Storage = new MeStorageBlock("erekir-64k-storage") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 1600,
                    Items.tungsten, 800, Items.carbide, 400, Items.oxide, 200));
            item(64000);
            channels = 2;
            size = 2;
            health = 600;
        }};

        liquidStorage = new MeStorageBlock("erekir-liquid-storage") {{
            requirements(Category.effect, ItemStack.with(Items.beryllium, 300));
            liquid(1800*5);
            size = 2;
            health = 400;
        }};
    }
}
