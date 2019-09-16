package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider
{
    public static final float[] MOON_PHASE_FACTORS = new float[] {1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};

    /** world object being used */
    protected World world;
    private WorldType terrainType;
    private String generatorSettings;

    /** World chunk manager being used to generate chunks */
    protected BiomeProvider biomeProvider;

    /**
     * States whether the Hell world provider is used(true) or if the normal world provider is used(false)
     */
    protected boolean doesWaterVaporize;

    /**
     * Whether this dimension should be treated as the nether.
     *  
     * @see <a href="https://github.com/ModCoderPack/MCPBot-Issues/issues/330">https://github.com/ModCoderPack/MCPBot-
     * Issues/issues/330</a>
     */
    protected boolean nether;
    protected boolean hasSkyLight;

    /** Light to brightness conversion table */
    protected final float[] lightBrightnessTable = new float[16];

    /** Array for sunrise/sunset colors (RGBA) */
    private final float[] colorsSunriseSunset = new float[4];

    /**
     * associate an existing world with a World provider, and setup its lightbrightness table
     */
    public final void setWorld(World worldIn)
    {
        this.world = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.init();
        this.generateLightBrightnessTable();
    }

    /**
     * Creates the light to brightness table
     */
    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F + 0.0F;
        }
    }

    /**
     * Creates a new {@link BiomeProvider} for the WorldProvider, and also sets the values of {@link #hasSkylight} and
     * {@link #hasNoSky} appropriately.
     *  
     * Note that subclasses generally override this method without calling the parent version.
     */
    protected void init()
    {
        this.hasSkyLight = true;
        WorldType worldtype = this.world.getWorldInfo().getTerrainType();

        if (worldtype == WorldType.FLAT)
        {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.world.getWorldInfo().getGeneratorOptions());
            this.biomeProvider = new BiomeProviderSingle(Biome.getBiome(flatgeneratorinfo.getBiome(), Biomes.DEFAULT));
        }
        else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.biomeProvider = new BiomeProviderSingle(Biomes.PLAINS);
        }
        else
        {
            this.biomeProvider = new BiomeProvider(this.world.getWorldInfo());
        }
    }

    public IChunkGenerator createChunkGenerator()
    {
        if (this.terrainType == WorldType.FLAT)
        {
            return new ChunkGeneratorFlat(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
        }
        else if (this.terrainType == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return new ChunkGeneratorDebug(this.world);
        }
        else
        {
            return this.terrainType == WorldType.CUSTOMIZED ? new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings);
        }
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        BlockPos blockpos = new BlockPos(x, 0, z);

        if (this.world.getBiome(blockpos).ignorePlayerSpawnSuitability())
        {
            return true;
        }
        else
        {
            return this.world.getGroundAboveSeaLevel(blockpos).getBlock() == Blocks.GRASS;
        }
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        int i = (int)(worldTime % 24000L);
        float f = ((float)i + partialTicks) / 24000.0F - 0.25F;

        if (f < 0.0F)
        {
            ++f;
        }

        if (f > 1.0F)
        {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    public int getMoonPhase(long worldTime)
    {
        return (int)(worldTime / 24000L % 8L + 8L) % 8;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    public boolean isSurfaceWorld()
    {
        return true;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean canRespawnHere()
    {
        return true;
    }

    @Nullable
    public BlockPos getSpawnCoordinate()
    {
        return null;
    }

    public int getAverageGroundLevel()
    {
        return this.terrainType == WorldType.FLAT ? 4 : this.world.getSeaLevel() + 1;
    }

    public BiomeProvider getBiomeProvider()
    {
        return this.biomeProvider;
    }

    public boolean doesWaterVaporize()
    {
        return this.doesWaterVaporize;
    }

    public boolean hasSkyLight()
    {
        return this.hasSkyLight;
    }

    public boolean isNether()
    {
        return this.nether;
    }

    public float[] getLightBrightnessTable()
    {
        return this.lightBrightnessTable;
    }

    public WorldBorder createWorldBorder()
    {
        return new WorldBorder();
    }

    /**
     * Called when a Player is added to the provider's world.
     */
    public void onPlayerAdded(EntityPlayerMP player)
    {
    }

    /**
     * Called when a Player is removed from the provider's world.
     */
    public void onPlayerRemoved(EntityPlayerMP player)
    {
    }

    public abstract DimensionType getDimensionType();

    /**
     * Called when the world is performing a save. Only used to save the state of the Dragon Boss fight in
     * WorldProviderEnd in Vanilla.
     */
    public void onWorldSave()
    {
    }

    /**
     * Called when the world is updating entities. Only used in WorldProviderEnd to update the DragonFightManager in
     * Vanilla.
     */
    public void onWorldUpdateEntities()
    {
    }

    /**
     * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
     * in WorldProviderSurface to prevent spawn chunks from being unloaded.
     */
    public boolean canDropChunk(int x, int z)
    {
        return true;
    }
}
