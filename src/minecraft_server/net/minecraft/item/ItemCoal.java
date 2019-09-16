package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;

public class ItemCoal extends Item
{
    public ItemCoal()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return stack.getMetadata() == 1 ? "item.charcoal" : "item.coal";
    }

    public void func_150895_a(CreativeTabs p_150895_1_, NonNullList<ItemStack> p_150895_2_)
    {
        if (this.func_194125_a(p_150895_1_))
        {
            p_150895_2_.add(new ItemStack(this, 1, 0));
            p_150895_2_.add(new ItemStack(this, 1, 1));
        }
    }
}
