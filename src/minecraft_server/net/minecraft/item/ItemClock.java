package net.minecraft.item;

import net.minecraft.util.ResourceLocation;

public class ItemClock extends Item
{
    public ItemClock()
    {
        this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter()
        {
        });
    }
}
