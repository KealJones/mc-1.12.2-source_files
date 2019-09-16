package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat
{
    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    boolean isOldMapFormat(String saveName);

    /**
     * converts the map to mcRegion
     */
    boolean convertMapFormat(String filename, IProgressUpdate progressCallback);

    File getFile(String p_186352_1_, String p_186352_2_);
}
