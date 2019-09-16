package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.ResourceLocation;

public class CPacketSeenAdvancements implements Packet<INetHandlerPlayServer>
{
    private CPacketSeenAdvancements.Action action;
    private ResourceLocation tab;

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.action = (CPacketSeenAdvancements.Action)buf.readEnumValue(CPacketSeenAdvancements.Action.class);

        if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB)
        {
            this.tab = buf.readResourceLocation();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.action);

        if (this.action == CPacketSeenAdvancements.Action.OPENED_TAB)
        {
            buf.writeResourceLocation(this.tab);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.handleSeenAdvancements(this);
    }

    public CPacketSeenAdvancements.Action getAction()
    {
        return this.action;
    }

    public ResourceLocation getTab()
    {
        return this.tab;
    }

    public static enum Action
    {
        OPENED_TAB,
        CLOSED_SCREEN;
    }
}
