package net.minecraft.world.storage;

import java.io.File;
import java.io.FileInputStream;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Reference to the File object representing the directory for the world saves
     */
    protected final File savesDirectory;
    protected final DataFixer dataFixer;

    public SaveFormatOld(File savesDirectoryIn, DataFixer dataFixerIn)
    {
        this.dataFixer = dataFixerIn;

        if (!savesDirectoryIn.exists())
        {
            savesDirectoryIn.mkdirs();
        }

        this.savesDirectory = savesDirectoryIn;
    }

    @Nullable

    /**
     * Returns the world's WorldInfo object
     */
    public WorldInfo getWorldInfo(String saveName)
    {
        File file1 = new File(this.savesDirectory, saveName);

        if (!file1.exists())
        {
            return null;
        }
        else
        {
            File file2 = new File(file1, "level.dat");

            if (file2.exists())
            {
                WorldInfo worldinfo = getWorldData(file2, this.dataFixer);

                if (worldinfo != null)
                {
                    return worldinfo;
                }
            }

            file2 = new File(file1, "level.dat_old");
            return file2.exists() ? getWorldData(file2, this.dataFixer) : null;
        }
    }

    @Nullable
    public static WorldInfo getWorldData(File p_186353_0_, DataFixer dataFixerIn)
    {
        try
        {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(p_186353_0_));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
            return new WorldInfo(dataFixerIn.process(FixTypes.LEVEL, nbttagcompound1));
        }
        catch (Exception exception)
        {
            LOGGER.error("Exception reading {}", p_186353_0_, exception);
            return null;
        }
    }

    /**
     * Returns back a loader for the specified save directory
     */
    public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata)
    {
        return new SaveHandler(this.savesDirectory, saveName, storePlayerdata, this.dataFixer);
    }

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    public boolean isOldMapFormat(String saveName)
    {
        return false;
    }

    /**
     * converts the map to mcRegion
     */
    public boolean convertMapFormat(String filename, IProgressUpdate progressCallback)
    {
        return false;
    }

    public File getFile(String p_186352_1_, String p_186352_2_)
    {
        return new File(new File(this.savesDirectory, p_186352_1_), p_186352_2_);
    }
}
