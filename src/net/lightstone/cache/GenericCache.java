package net.lightstone.cache;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.eviction.EvictionStrategy;

/**
 * A generic cache.<br>
 * The {@link Configuration} used has these parameters:<br>
 * CacheMode: Local<br>
 * EvictionStrategy: LRU<br>
 * ExpirationMaxIdle: 2 minutes
 * 
 * @author Joe Pritzel
 * 
 */
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
	/**
	 * Creates a {@link GenericCache}
	 */
	public GenericCache() {
		cache = (Cache<Object, Object>) createCache();
	}

	private Cache<?, ?> createCache() {
		return CacheManager.createCache("generic" + numOfGenericCaches++,
				CONFIG);
	}

	/**
	 * 
	 * @return the cache
	 */
	public Cache<?, ?> getCache() {
		return cache;
	}
}
