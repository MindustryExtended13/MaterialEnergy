package example;

import mindustry.type.Category;
import mindustry.type.ItemStack;

import net.tmmc.util.GraphBlock;

public class ExampleContent {
    public static GraphBlock block1, block2;

    public static void load() {
        block1 = new GraphBlock("example-block") {{
            this.requirements(Category.defense, ItemStack.empty);
        }};

        block2 = new GraphBlock("example-block2") {{
            this.requirements(Category.defense, ItemStack.empty);
            this.hasRange = true;
            this.range = 64;
        }};
    }
}