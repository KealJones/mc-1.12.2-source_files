package net.minecraft.creativetab;

import javax.annotation.Nullable;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs
{
    public static final CreativeTabs[] CREATIVE_TAB_ARRAY = new CreativeTabs[12];
    public static final CreativeTabs BUILDING_BLOCKS = new CreativeTabs(0, "buildingBlocks")
    {
    };
    public static final CreativeTabs DECORATIONS = new CreativeTabs(1, "decorations")
    {
    };
    public static final CreativeTabs REDSTONE = new CreativeTabs(2, "redstone")
    {
    };
    public static final CreativeTabs TRANSPORTATION = new CreativeTabs(3, "transportation")
    {
    };
    public static final CreativeTabs MISC = new CreativeTabs(6, "misc")
    {
    };
    public static final CreativeTabs SEARCH = (new CreativeTabs(5, "search")
    {
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs FOOD = new CreativeTabs(7, "food")
    {
    };
    public static final CreativeTabs TOOLS = (new CreativeTabs(8, "tools")
    {
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs COMBAT = (new CreativeTabs(9, "combat")
    {
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] {EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs BREWING = new CreativeTabs(10, "brewing")
    {
    };
    public static final CreativeTabs MATERIALS = MISC;
    public static final CreativeTabs HOTBAR = new CreativeTabs(4, "hotbar")
    {
    };
    public static final CreativeTabs INVENTORY = (new CreativeTabs(11, "inventory")
    {
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int index;
    private final String tabLabel;

    /** Texture to use. */
    private String backgroundTexture = "items.png";
    private boolean hasScrollbar = true;

    /** Whether to draw the title in the foreground of the creative GUI */
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes = new EnumEnchantmentType[0];
    private ItemStack icon;

    public CreativeTabs(int index, String label)
    {
        this.index = index;
        this.tabLabel = label;
        this.icon = ItemStack.EMPTY;
        CREATIVE_TAB_ARRAY[index] = this;
    }

    public CreativeTabs setBackgroundImageName(String texture)
    {
        this.backgroundTexture = texture;
        return this;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }

    public EnumEnchantmentType[] func_111225_m()
    {
        return this.enchantmentTypes;
    }

    /**
     * Sets the enchantment types for populating this tab with enchanting books
     */
    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types)
    {
        this.enchantmentTypes = types;
        return this;
    }

    public boolean func_111226_a(@Nullable EnumEnchantmentType p_111226_1_)
    {
        if (p_111226_1_ != null)
        {
            for (EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes)
            {
                if (enumenchantmenttype == p_111226_1_)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
