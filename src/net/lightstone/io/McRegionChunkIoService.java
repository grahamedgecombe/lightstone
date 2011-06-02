package net.lightstone.io;

import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import net.lightstone.io.region.RegionFile;
import net.lightstone.io.region.RegionFileCache;
import net.lightstone.model.Chunk;
import net.lightstone.util.nbt.ByteTag;
import net.lightstone.util.nbt.ByteArrayTag;
import net.lightstone.util.nbt.CompoundTag;
import net.lightstone.util.nbt.IntTag;
import net.lightstone.util.nbt.ListTag;
import net.lightstone.util.nbt.LongTag;
import net.lightstone.util.nbt.NBTInputStream;
import net.lightstone.util.nbt.NBTOutputStream;
import net.lightstone.util.nbt.Tag;

/**
 * An implementation of the {@link ChunkIoService} which reads and writes
 * McRegion maps.
 * <p />
 * Information on the McRegion file format can be found on the
 * <a href="http://mojang.com/2011/02/16/minecraft-save-file-format-in-beta-1-3">Mojang</a>
 * blog.
 * @author Zhuowei Zhang
 * @author Graham Edgecombe
 */
public final class McRegionChunkIoService implements ChunkIoService {

	/**
	 * The size of a region - a 32x32 group of chunks.
	 */
	private static final int REGION_SIZE = 32;

	/**
	 * The root directory of the map.
	 */
	private File dir;

	/**
	 * The region file cache.
	 */
	private RegionFileCache cache = new RegionFileCache();

	// TODO: consider the session.lock file

	public McRegionChunkIoService() {
		this(new File("world"));
	}

	public McRegionChunkIoService(File dir) {
		this.dir = dir;
	}

	@Override
	public Chunk read(int x, int z) throws IOException {
		RegionFile region = cache.getRegionFile(dir, x, z);
		int regionX = x & (REGION_SIZE - 1);
		int regionZ = z & (REGION_SIZE - 1);
		if (!region.hasChunk(regionX, regionZ)){
			return null;
		}

		DataInputStream in = region.getChunkDataInputStream(regionX, regionZ);
		Chunk chunk = new Chunk(x, z);

		NBTInputStream nbt = new NBTInputStream(in, false);
		CompoundTag tag = (CompoundTag) nbt.readTag();
		Map<String, Tag> levelTags = ((CompoundTag) tag.getValue().get("Level")).getValue();

		byte[] tileData = ((ByteArrayTag) levelTags.get("Blocks")).getValue();
		chunk.setTypes(tileData);

		byte[] skyLightData = ((ByteArrayTag) levelTags.get("SkyLight")).getValue();
		byte[] blockLightData = ((ByteArrayTag) levelTags.get("BlockLight")).getValue();
		byte[] metaData = ((ByteArrayTag) levelTags.get("Data")).getValue();

		for (int cx = 0; cx < Chunk.WIDTH; cx++) {
			for (int cz = 0; cz < Chunk.HEIGHT; cz++) {
				for (int cy = 0; cy < Chunk.DEPTH; cy++) {
					boolean mostSignificantNibble = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy) % 2 == 1;
					int offset = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy) / 2;

					int skyLight, blockLight, meta;
					if (mostSignificantNibble) {
						skyLight = (skyLightData[offset] & 0xF0) >> 4;
						blockLight = (blockLightData[offset] & 0xF0) >> 4;
						meta = (metaData[offset] & 0xF0) >> 4;
					} else {
						skyLight = skyLightData[offset] & 0x0F;
						blockLight = blockLightData[offset] & 0x0F;
						meta = metaData[offset] & 0x0F;
					}

					chunk.setSkyLight(cx, cz, cy, skyLight);
					chunk.setBlockLight(cx, cz, cy, blockLight);
					chunk.setMetaData(cx, cz, cy, meta);
				}
			}
		}

		return chunk;
	}


	/** Writes a chunk to a McRegion file.
	 * WARNING! The files written by this method probably won't load in the Notchian server. Make backups.
	 */

	@Override
	public void write(int x, int z, Chunk chunk) throws IOException {
		CompoundTag levelTag = chunkToTag(chunk);
		RegionFile region = cache.getRegionFile(dir, x, z);
		int regionX = x & (REGION_SIZE - 1);
		int regionZ = z & (REGION_SIZE - 1);
		DataOutputStream out = region.getChunkDataOutputStream(regionX, regionZ);
		NBTOutputStream nbtOut = new NBTOutputStream(out, false);
		Map<String, Tag> tagMap = new HashMap<String, Tag>(1);
		tagMap.put("Level", levelTag);
		CompoundTag tag = new CompoundTag("", tagMap);
		nbtOut.writeTag(tag);
		out.close();
		//TODO: Close the regionfile
	}

	private CompoundTag chunkToTag(Chunk chunk){
		byte[] tileData = new byte[Chunk.WIDTH * Chunk.HEIGHT * Chunk.DEPTH];
		byte[] skyLightData = new byte[(Chunk.WIDTH * Chunk.HEIGHT * Chunk.DEPTH) / 2];
		byte[] blockLightData = new byte[(Chunk.WIDTH * Chunk.HEIGHT * Chunk.DEPTH) / 2];
		byte[] metaData = new byte[(Chunk.WIDTH * Chunk.HEIGHT * Chunk.DEPTH) / 2];
		byte[] heightMapData = new byte[Chunk.WIDTH * Chunk.HEIGHT];
		for (int cx = 0; cx < Chunk.WIDTH; cx++) {
			for (int cz = 0; cz < Chunk.HEIGHT; cz++) {
				for (int cy = 0; cy < Chunk.DEPTH; cy+=2) {
					int blockOffset = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy);
					int offset = blockOffset / 2;
					tileData[blockOffset] = (byte) chunk.getType(cx, cz, cy);
					tileData[blockOffset + 1] = (byte) chunk.getType(cx, cz, cy + 1);
					skyLightData[offset] = (byte) ((chunk.getSkyLight(cx, cz, cy + 1) << 4) | chunk.getSkyLight(cx, cz, cy));
					blockLightData[offset] = (byte) ((chunk.getBlockLight(cx, cz, cy + 1) << 4) | chunk.getBlockLight(cx, cz, cy));
					metaData[offset] = (byte) ((chunk.getMetaData(cx, cz, cy + 1) << 4) | chunk.getMetaData(cx, cz, cy));
				}
			}
		}
		Map<String, Tag> levelTags = new HashMap<String, Tag>();
		levelTags.put("Blocks", new ByteArrayTag("Blocks", tileData));
		levelTags.put("Data", new ByteArrayTag("Data", metaData));
		levelTags.put("SkyLight", new ByteArrayTag("SkyLight", skyLightData));
		levelTags.put("BlockLight", new ByteArrayTag("BlockLight", blockLightData));
		//TODO: Heightmap, entities, tileentities, lastupdate
		levelTags.put("HeightMap", new ByteArrayTag("HeightMap", heightMapData));
		levelTags.put("Entities", chunkEntitiesToTag(chunk));
		levelTags.put("TileEntities", chunkTileEntitiesToTag(chunk));
		levelTags.put("LastUpdate", new LongTag("LastUpdate", 0));

		levelTags.put("xPos", new IntTag("xPos", chunk.getX()));
		levelTags.put("zPos", new IntTag("zPos", chunk.getZ()));
		//TODO: terrainpopulated
		levelTags.put("TerrainPopulated", new ByteTag("TerrainPopulated", (byte) 0));
		return new CompoundTag("Level", levelTags);
	}

	//TODO
	private ListTag<CompoundTag> chunkEntitiesToTag(Chunk chunk){
		List<CompoundTag> entityTags = new ArrayList<CompoundTag>();
		return new ListTag<CompoundTag>("Entities", CompoundTag.class, entityTags);
	}

	//TODO
	private ListTag<CompoundTag> chunkTileEntitiesToTag(Chunk chunk){
		List<CompoundTag> entityTags = new ArrayList<CompoundTag>();
		return new ListTag<CompoundTag>("TileEntities", CompoundTag.class, entityTags);
	}

}

