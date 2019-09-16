package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemEnchantedBook extends Item
{
    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return getEnchantments(stack).isEmpty() ? super.getRarity(stack) : EnumRarity.UNCOMMON;
    }

    public static NBTTagList getEnchantments(ItemStack p_92110_0_)
    {
        NBTTagCompound nbttagcompound = p_92110_0_.getTagCompound();
        return nbttagcompound != null ? nbttagcompound.getTagList("StoredEnchantments", 10) : new NBTTagList();
    }

    /**
     * Adds an stored enchantment to an enchanted book ItemStack
     */
    public static void addEnchantment(ItemStack p_92115_0_, EnchantmentData stack)
    {
        NBTTagList nbttaglist = getEnchantments(p_92115_0_);
        boolean flag = true;

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);

            if (Enchantment.getEnchantmentByID(nbttagcompound.getShort("id")) == stack.enchantment)
            {
                if (nbttagcompound.getShort("lvl") < stack.enchantmentLevel)
                {
                    nbttagcompound.setShort("lvl", (short)stack.enchantmentLevel);
                }

                flag = false;
                break;
            }
        }

        if (flag)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setShort("id", (short)Enchantment.getEnchantmentID(stack.enchantment));
            nbttagcompound1.setShort("lvl", (short)stack.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound1);
        }

        if (!p_92115_0_.hasTagCompound())
        {
            p_92115_0_.setTagCompound(new NBTTagCompound());
        }

        p_92115_0_.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }

    /**
     * Returns the ItemStack of an enchanted version of this item.
     */
    public static ItemStack getEnchantedItemStack(EnchantmentData p_92111_0_)
    {
        ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        addEnchantment(itemstack, p_92111_0_);
        return itemstack;
    }

    public void func_150895_a(CreativeTabs p_150895_1_, NonNullList<ItemStack> p_150895_2_)
    {
        if (p_150895_1_ == CreativeTabs.SEARCH)
        {
            for (Enchantment enchantment : Enchantment.REGISTRY)
            {
                if (enchantment.type != null)
                {
                    for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i)
                    {
                        p_150895_2_.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
                    }
                }
            }
        }
        else if (p_150895_1_.func_111225_m().length != 0)
        {
            for (Enchantment enchantment1 : Enchantment.REGISTRY)
            {
                if (p_150895_1_.func_111226_a(enchantment1.type))
                {
                    p_150895_2_.add(getEnchantedItemStack(new EnchantmentData(enchantment1, enchantment1.getMaxLevel())));
                }
            }
        }
    }
}
