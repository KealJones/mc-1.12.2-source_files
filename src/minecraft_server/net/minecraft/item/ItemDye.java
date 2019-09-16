package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDye extends Item
{
    public static final int[] DYE_COLORS = new int[] {1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye()
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
        int i = stack.getMetadata();
        return super.getTranslationKey() + "." + EnumDyeColor.byDyeDamage(i).getTranslationKey();
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

            if (enumdyecolor == EnumDyeColor.WHITE)
            {
                if (applyBonemeal(itemstack, worldIn, pos))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.playEvent(2005, pos, 0);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
            else if (enumdyecolor == EnumDyeColor.BROWN)
            {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if (block == Blocks.LOG && iblockstate.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE)
                {
                    if (facing == EnumFacing.DOWN || facing == EnumFacing.UP)
                    {
                        return EnumActionResult.FAIL;
                    }

                    pos = pos.offset(facing);

                    if (worldIn.isAirBlock(pos))
                    {
                        IBlockState iblockstate1 = Blocks.COCOA.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player);
                        worldIn.setBlockState(pos, iblockstate1, 10);

                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.FAIL;
            }

            return EnumActionResult.PASS;
        }
    }

    public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target)
    {
        IBlockState iblockstate = worldIn.getBlockState(target);

        if (iblockstate.getBlock() instanceof IGrowable)
        {
            IGrowable igrowable = (IGrowable)iblockstate.getBlock();

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote))
            {
                if (!worldIn.isRemote)
                {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
                    {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }

                    stack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)target;
            EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor)
            {
                entitysheep.setFleeceColor(enumdyecolor);
                stack.shrink(1);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void func_150895_a(CreativeTabs p_150895_1_, NonNullList<ItemStack> p_150895_2_)
    {
        if (this.func_194125_a(p_150895_1_))
        {
            for (int i = 0; i < 16; ++i)
            {
                p_150895_2_.add(new ItemStack(this, 1, i));
            }
        }
    }
}
