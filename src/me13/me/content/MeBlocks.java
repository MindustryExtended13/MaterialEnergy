package me13.me.content;

import me13.me.world.blocks.Cable;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import net.tmmc.registry.Registry;
import net.tmmc.registry.RegistryObject;

public class MeBlocks {
    public static final Registry<Block> BLOCKS = new Registry<>(Block.class);

    public static final RegistryObject<Cable> cable = BLOCKS.registry(Cable.class, () -> {
        return new Cable("cable") {{
            requirements(Category.distribution, ItemStack.with(Items.copper, 2));
            health = 90;
        }};
    });
}