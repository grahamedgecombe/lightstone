package net.lightstone.cache;

import net.lightstone.model.Chunk;
import net.lightstone.model.Chunk.Key;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.eviction.EvictionStrategy;

/**
 * A cache for {@link Chunk}s.
 * 
 * @author Joe Pritzel
 * 
 */
public class ChunkCache {
	private static final Configuration CONFIG;
	static {
		CONFIG = new Configuration();
		CONFIG.setCacheMode(CacheMode.LOCAL);
		CONFIG.setEvictionStrategy(EvictionStrategy.LRU);
		CONFIG.setExpirationMaxIdle(120000);
	}

	private static int numOfChunkCaches = 0;
	
	private final Cache<Chunk.Key, Chunk> cache;

	public ChunkCache() {
		cache = createCache();
	}

	@SuppressWarnings("unchecked")
	private Cache<Key, Chunk> createCache() {
		return (Cache<Key, Chunk>) CacheManager.createCache("chunks"
				+ numOfChunkCaches, CONFIG);
	}

	public Chunk get(final Chunk.Key key) {
		return cache.get(key);
	}

	public Chunk put(final Chunk.Key key, final Chunk chunk) {
		return cache.put(key, chunk);
	}
}
