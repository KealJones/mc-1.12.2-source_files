package net.minecraft.potion;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class PotionUtils
{
    public static List<PotionEffect> getEffectsFromStack(ItemStack stack)
    {
        return getEffectsFromTag(stack.getTagCompound());
    }

    public static List<PotionEffect> mergeEffects(PotionType potionIn, Collection<PotionEffect> effects)
    {
        List<PotionEffect> list = Lists.<PotionEffect>newArrayList();
        list.addAll(potionIn.getEffects());
        list.addAll(effects);
        return list;
    }

    public static List<PotionEffect> getEffectsFromTag(@Nullable NBTTagCompound tag)
    {
        List<PotionEffect> list = Lists.<PotionEffect>newArrayList();
        list.addAll(getPotionTypeFromNBT(tag).getEffects());
        addCustomPotionEffectToList(tag, list);
        return list;
    }

    public static List<PotionEffect> getFullEffectsFromItem(ItemStack itemIn)
    {
        return getFullEffectsFromTag(itemIn.getTagCompound());
    }

    public static List<PotionEffect> getFullEffectsFromTag(@Nullable NBTTagCompound tag)
    {
        List<PotionEffect> list = Lists.<PotionEffect>newArrayList();
        addCustomPotionEffectToList(tag, list);
        return list;
    }

    public static void addCustomPotionEffectToList(@Nullable NBTTagCompound tag, List<PotionEffect> effectList)
    {
        if (tag != null && tag.hasKey("CustomPotionEffects", 9))
        {
            NBTTagList nbttaglist = tag.getTagList("CustomPotionEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

                if (potioneffect != null)
                {
                    effectList.add(potioneffect);
                }
            }
        }
    }

    public static int getColor(ItemStack p_190932_0_)
    {
        NBTTagCompound nbttagcompound = p_190932_0_.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99))
        {
            return nbttagcompound.getInteger("CustomPotionColor");
        }
        else
        {
            return getPotionFromItem(p_190932_0_) == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList(getEffectsFromStack(p_190932_0_));
        }
    }

    public static int getPotionColor(PotionType potionIn)
    {
        return potionIn == PotionTypes.EMPTY ? 16253176 : getPotionColorFromEffectList(potionIn.getEffects());
    }

    public static int getPotionColorFromEffectList(Collection<PotionEffect> effects)
    {
        int i = 3694022;

        if (effects.isEmpty())
        {
            return 3694022;
        }
        else
        {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            int j = 0;

            for (PotionEffect potioneffect : effects)
            {
                if (potioneffect.doesShowParticles())
                {
                    int k = potioneffect.getPotion().getLiquidColor();
                    int l = potioneffect.getAmplifier() + 1;
                    f += (float)(l * (k >> 16 & 255)) / 255.0F;
                    f1 += (float)(l * (k >> 8 & 255)) / 255.0F;
                    f2 += (float)(l * (k >> 0 & 255)) / 255.0F;
                    j += l;
                }
            }

            if (j == 0)
            {
                return 0;
            }
            else
            {
                f = f / (float)j * 255.0F;
                f1 = f1 / (float)j * 255.0F;
                f2 = f2 / (float)j * 255.0F;
                return (int)f << 16 | (int)f1 << 8 | (int)f2;
            }
        }
    }

    public static PotionType getPotionFromItem(ItemStack itemIn)
    {
        return getPotionTypeFromNBT(itemIn.getTagCompound());
    }

    /**
     * If no correct potion is found, returns the default one : PotionTypes.water
     */
    public static PotionType getPotionTypeFromNBT(@Nullable NBTTagCompound tag)
    {
        return tag == null ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(tag.getString("Potion"));
    }

    public static ItemStack addPotionToItemStack(ItemStack itemIn, PotionType potionIn)
    {
        ResourceLocation resourcelocation = PotionType.REGISTRY.getNameForObject(potionIn);

        if (potionIn == PotionTypes.EMPTY)
        {
            if (itemIn.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = itemIn.getTagCompound();
                nbttagcompound.removeTag("Potion");

                if (nbttagcompound.isEmpty())
                {
                    itemIn.setTagCompound((NBTTagCompound)null);
                }
            }
        }
        else
        {
            NBTTagCompound nbttagcompound1 = itemIn.hasTagCompound() ? itemIn.getTagCompound() : new NBTTagCompound();
            nbttagcompound1.setString("Potion", resourcelocation.toString());
            itemIn.setTagCompound(nbttagcompound1);
        }

        return itemIn;
    }

    public static ItemStack appendEffects(ItemStack itemIn, Collection<PotionEffect> effects)
    {
        if (effects.isEmpty())
        {
            return itemIn;
        }
        else
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)MoreObjects.firstNonNull(itemIn.getTagCompound(), new NBTTagCompound());
            NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 9);

            for (PotionEffect potioneffect : effects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
            itemIn.setTagCompound(nbttagcompound);
            return itemIn;
        }
    }
}
