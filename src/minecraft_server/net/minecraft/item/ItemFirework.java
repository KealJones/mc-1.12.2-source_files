package net.minecraft.item;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFirework extends Item
{
    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            ItemStack itemstack = player.getHeldItem(hand);
            EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), itemstack);
            worldIn.spawnEntity(entityfireworkrocket);

            if (!player.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (playerIn.isElytraFlying())
        {
            ItemStack itemstack = playerIn.getHeldItem(handIn);

            if (!worldIn.isRemote)
            {
                EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, itemstack, playerIn);
                worldIn.spawnEntity(entityfireworkrocket);

                if (!playerIn.capabilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
    }
}
