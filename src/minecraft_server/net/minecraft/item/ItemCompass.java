package net.minecraft.item;

import net.minecraft.util.ResourceLocation;

public class ItemCompass extends Item
{
    public ItemCompass()
    {
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
        });
    }
}
