package net.lightstone.cache;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Allows easy creation of a {@link Cache}.
 * 
 * @author Joe Pritzel
 * 
 */
public class CacheManager {
	private static final EmbeddedCacheManager manager = new DefaultCacheManager();

	public static Cache<?, ?> createCache(final String name,
			final Configuration config) {
		manager.defineConfiguration(name, config);
		return manager.getCache(name);
	}
}
