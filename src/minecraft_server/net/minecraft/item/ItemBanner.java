package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock
{
    public ItemBanner()
    {
        super(Blocks.STANDING_BANNER);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = iblockstate.getBlock().isReplaceable(worldIn, pos);

        if (facing != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || facing == EnumFacing.UP))
        {
            pos = pos.offset(facing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && Blocks.STANDING_BANNER.canPlaceBlockAt(worldIn, pos))
            {
                if (worldIn.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    pos = flag ? pos.down() : pos;

                    if (facing == EnumFacing.UP)
                    {
                        int i = MathHelper.floor((double)((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                        worldIn.setBlockState(pos, Blocks.STANDING_BANNER.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, Blocks.WALL_BANNER.getDefaultState().withProperty(BlockWallSign.FACING, facing), 3);
                    }

                    TileEntity tileentity = worldIn.getTileEntity(pos);

                    if (tileentity instanceof TileEntityBanner)
                    {
                        ((TileEntityBanner)tileentity).setItemValues(itemstack, false);
                    }

                    if (player instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = "item.banner.";
        EnumDyeColor enumdyecolor = getBaseColor(stack);
        s = s + enumdyecolor.getTranslationKey() + ".name";
        return I18n.translateToLocal(s);
    }

    public void func_150895_a(CreativeTabs p_150895_1_, NonNullList<ItemStack> p_150895_2_)
    {
        if (this.func_194125_a(p_150895_1_))
        {
            for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
            {
                p_150895_2_.add(makeBanner(enumdyecolor, (NBTTagList)null));
            }
        }
    }

    public static ItemStack makeBanner(EnumDyeColor color, @Nullable NBTTagList patterns)
    {
        ItemStack itemstack = new ItemStack(Items.BANNER, 1, color.getDyeDamage());

        if (patterns != null && !patterns.isEmpty())
        {
            itemstack.getOrCreateSubCompound("BlockEntityTag").setTag("Patterns", patterns.copy());
        }

        return itemstack;
    }

    public CreativeTabs func_77640_w()
    {
        return CreativeTabs.DECORATIONS;
    }

    public static EnumDyeColor getBaseColor(ItemStack stack)
    {
        return EnumDyeColor.byDyeDamage(stack.getMetadata() & 15);
    }
}
