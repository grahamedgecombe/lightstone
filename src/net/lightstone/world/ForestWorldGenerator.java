package net.lightstone.world;

import java.util.Random;

import net.lightstone.model.Chunk;
import net.lightstone.model.Blocks;

/**
 * A {@link WorldGenerator} that generates chunks with trees randomly placed.
 * @author Zhuowei Zhang
 */
public final class ForestWorldGenerator extends FlatGrassWorldGenerator {

	private Random rand=new Random();

	private static final int MAX_TREES = 2;

	private static final int TREE_MIN_HEIGHT = 5;

	private static final int TREE_MAX_HEIGHT = 8;

	private static final int TREE_CANOPY_HEIGHT = 4;

	private static final int TREE_CANOPY_WIDTH = 5;

	public static final int TREE_TYPE_NORMAL = 0;

	public static final int TREE_TYPE_REDWOOD = 1;

	public static final int TREE_TYPE_BIRCH = 2;

	@Override
	public Chunk generate(int chunkX, int chunkZ) {
		Chunk chunk = super.generate(chunkX, chunkZ);
		int numTrees=rand.nextInt(MAX_TREES + 1);
		for(int i=0;i<numTrees;i++){
			int treeX=rand.nextInt(Chunk.WIDTH - (TREE_CANOPY_WIDTH * 2) + TREE_CANOPY_WIDTH);
			int treeZ=rand.nextInt(Chunk.HEIGHT- (TREE_CANOPY_WIDTH * 2) + TREE_CANOPY_WIDTH);
			int treeHeight=rand.nextInt(TREE_MAX_HEIGHT - TREE_MIN_HEIGHT) + TREE_MIN_HEIGHT;
			int treeType = rand.nextInt(3); //standard, redwood, birch
			makeTree(chunk, treeX, treeZ, 61, treeHeight, treeType);
		}
		return chunk;
	}
	/** Grows a tree in a chunk. */
	public static void makeTree(Chunk chunk, int x, int z, int y, int height, int type){
		if(!(type==TREE_TYPE_NORMAL||type==TREE_TYPE_REDWOOD||type==TREE_TYPE_BIRCH)){
			throw new IllegalArgumentException("Type of tree not valid");
		}
		int center = (TREE_CANOPY_WIDTH)/2;
		int trunkX=x + center;
		int trunkZ=z + center;
		for(int i=0;i<height-TREE_CANOPY_HEIGHT;i++){  //Generate the trunk
			chunk.setType(trunkX, trunkZ, y + i, Blocks.TYPE_WOOD);
			chunk.setMetaData(trunkX, trunkZ, y + i, type);
		}
		for(int cy=height-TREE_CANOPY_HEIGHT;cy<height;cy++){ //Generate leaves
			for(int cx=x;cx<x + TREE_CANOPY_WIDTH;cx++){
				for(int cz=z;cz<z + TREE_CANOPY_WIDTH;cz++){
					if(cx == trunkX && cz == trunkZ){ //trunk
						chunk.setType(trunkX, trunkZ, y + cy, Blocks.TYPE_WOOD);
						chunk.setMetaData(trunkX, trunkZ, y + cy, type);
					}
					else{
						chunk.setType(cx, cz, y + cy, Blocks.TYPE_LEAVES);
						chunk.setMetaData(cx, cz, y + cy, type);
					}
				}
			}
		}
	}
}

