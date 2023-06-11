package me13.me.content;

import mindustry.content.Blocks;
import mindustry.content.TechTree;
import mindustry.content.TechTree.TechNode;
import mindustry.ctype.UnlockableContent;
import mindustry.type.ItemStack;

public class MeTech {
    static TechTree.TechNode context = null;

    public static void load() {
        serpuloLoad();
        erekirLoad();
    }

    public static void erekirLoad() {
        margeNode(Blocks.duct, () -> {
            node(MeErekirBlocks.cable, () -> {
                node(MeErekirBlocks.controller);
                node(MeErekirBlocks.storageScreen, () -> {
                    node(MeErekirBlocks.terminal);
                });
                node(MeErekirBlocks.adapter, () -> {
                    node(MeErekirBlocks.importBus);
                    node(MeErekirBlocks.exportBus);
                });
                node(MeErekirBlocks.k1Storage, () -> {
                    node(MeErekirBlocks.k4Storage, () -> {
                        node(MeErekirBlocks.k16Storage, () -> {
                            node(MeErekirBlocks.k64Storage);
                        });
                    });
                });
            });
        });

        margeNode(Blocks.berylliumWall, () -> {
            node(MeErekirBlocks.blockFrame, () -> {
                node(MeErekirBlocks.largeBlockFrame);
            });
        });
    }

    public static void serpuloLoad() {
        margeNode(Blocks.conveyor, () -> {
            node(MeSerpuloBlocks.cable, () -> {
                node(MeSerpuloBlocks.controller);
                node(MeSerpuloBlocks.storageScreen, () -> {
                    node(MeSerpuloBlocks.terminal);
                });
                node(MeSerpuloBlocks.adapter, () -> {
                    node(MeSerpuloBlocks.importBus);
                    node(MeSerpuloBlocks.exportBus);
                });
                node(MeSerpuloBlocks.k1Storage, () -> {
                    node(MeSerpuloBlocks.k4Storage, () -> {
                        node(MeSerpuloBlocks.k16Storage, () -> {
                            node(MeSerpuloBlocks.k64Storage);
                        });
                    });
                });
            });
        });

        margeNode(Blocks.copperWall, () -> {
            node(MeSerpuloBlocks.blockFrame, () -> {
                node(MeSerpuloBlocks.largeBlockFrame);
            });
        });
    }

    private static void margeNode(UnlockableContent parent, Runnable children) {
        context = TechTree.all.find(t -> t.content == parent);
        children.run();
    }

    private static void node(UnlockableContent content, Runnable children) {
        TechNode node = new TechNode(context, content, content.researchRequirements());
        TechNode prev = context;
        context = node;
        children.run();
        context = prev;
    }

    private static void node(UnlockableContent block) {
        node(block, () -> {});
    }
}