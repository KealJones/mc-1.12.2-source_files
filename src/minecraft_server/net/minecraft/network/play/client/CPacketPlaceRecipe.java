package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlaceRecipe implements Packet<INetHandlerPlayServer>
{
    private int field_194320_a;
    private IRecipe field_194321_b;
    private boolean field_194322_c;

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_194320_a = buf.readByte();
        this.field_194321_b = CraftingManager.getRecipeById(buf.readVarInt());
        this.field_194322_c = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_194320_a);
        buf.writeVarInt(CraftingManager.getIDForRecipe(this.field_194321_b));
        buf.writeBoolean(this.field_194322_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_194308_a(this);
    }

    public int func_194318_a()
    {
        return this.field_194320_a;
    }

    public IRecipe func_194317_b()
    {
        return this.field_194321_b;
    }

    public boolean func_194319_c()
    {
        return this.field_194322_c;
    }
}
