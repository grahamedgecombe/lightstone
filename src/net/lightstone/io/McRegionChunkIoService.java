package net.lightstone.io;

import java.io.File;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

import org.jnbt.*;

import net.lightstone.model.Chunk;
import net.lightstone.io.region.*;

/**
 * An implementation of the {@link ChunkIoService} which reads and writes
 * McRegion maps.
 * @author Graham Edgecombe
 */
public final class McRegionChunkIoService implements ChunkIoService {
	private File baseDir;
	private RegionFileCache cache;
	public McRegionChunkIoService(){
		this(new File("world"));
	}
	public McRegionChunkIoService(File dir){
		baseDir=dir;
	} 
	@Override
	public Chunk read(int x, int z) {
		Chunk chunk;
		RegionFile region=RegionFileCache.getRegionFile(baseDir, x, z);
		int regionX=x&31; //from http://mojang.com/2011/02/16/minecraft-save-file-format-in-beta-1-3/
		int regionZ=z&31;
		System.out.println("X: " + x + " Z: " + z + "RegionX:" + regionX + "RegionZ: " +regionZ);
		if(!region.hasChunk(regionX, regionZ)){
			return null;
		}
		DataInputStream in=region.getChunkDataInputStream(regionX, regionZ);
		System.out.println("hasinstream");
		try{
			chunk=new Chunk(x, z);
			byte[] tileData, skylightData, blockLightData, metadata;
			NBTInputStream nbt=new NBTInputStream(in, false);
			CompoundTag tag=(CompoundTag)nbt.readTag();
			Map<String, Tag> levelTags = ((CompoundTag)tag.getValue().get("Level")).getValue();
			tileData=((ByteArrayTag)levelTags.get("Blocks")).getValue();
			chunk.setTypes(tileData);
			skylightData=((ByteArrayTag)levelTags.get("SkyLight")).getValue();
			blockLightData=((ByteArrayTag)levelTags.get("BlockLight")).getValue();
			metadata=((ByteArrayTag)levelTags.get("Data")).getValue();
			for (int cx = 0; cx < Chunk.WIDTH; cx++) {
				for (int cz = 0; cz < Chunk.HEIGHT; cz++) {
					for (int cy = 0; cy < Chunk.DEPTH; cy++) {
						int offset=((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy)/2;
						//TODO: Read the lighting and metadata.
						/*System.out.println((((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy)%2==0)? 
								(((int)skylightData[offset])+128) >>> 4 : (((int)skylightData[offset])+128) & 15);
						chunk.setSkyLight(cx, cz, cy, (((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy)%2==0)? 
								(((int)skylightData[offset])+128) >>> 4 : (((int)skylightData[offset])+128) & 15);
						chunk.setBlockLight(cx, cz, cy, (((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy)%2==0)? 
								blockLightData[offset] >>> 4 : blockLightData[offset] & 15);
						chunk.setMetaData(cx, cz, cy, (((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy)%2==0)? 
								metadata[offset] >> 4 : metadata[offset] & 15);*/
						chunk.setSkyLight(cx, cz, cy, 15);
						chunk.setBlockLight(cx, cz, cy, 0);
						chunk.setMetaData(cx, cz, cy, 0);
					}
				}
			}
			return chunk;
		}
		catch(Exception e){
			System.err.println("Error while loading chunk: ");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void write(int x, int z, Chunk chunk) throws IOException {
		//TODO
	}

}

