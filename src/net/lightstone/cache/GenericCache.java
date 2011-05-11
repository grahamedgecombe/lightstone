package net.lightstone.cache;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.eviction.EvictionStrategy;

public class GenericCache {
	private static final Configuration CONFIG;
	static {
		CONFIG = new Configuration();
		CONFIG.setCacheMode(CacheMode.LOCAL);
		CONFIG.setEvictionStrategy(EvictionStrategy.LRU);
		CONFIG.setExpirationMaxIdle(120000);
	}

	private static int numOfGenericCaches = 0;

	private final Cache<?, ?> cache;

	@SuppressWarnings("unchecked")
	public GenericCache() {
		cache = (Cache<Object, Object>) createCache();
	}

	private Cache<?, ?> createCache() {
		return CacheManager.createCache("generic" + numOfGenericCaches++, CONFIG);
	}
	
	public Cache<?, ?> getCache() {
		return cache;
	}
}
