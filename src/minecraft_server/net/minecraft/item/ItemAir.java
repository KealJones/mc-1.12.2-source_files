package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAir extends Item
{
    private final Block block;

    public ItemAir(Block blockIn)
    {
        this.block = blockIn;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return this.block.getTranslationKey();
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getTranslationKey()
    {
        return this.block.getTranslationKey();
    }
}
