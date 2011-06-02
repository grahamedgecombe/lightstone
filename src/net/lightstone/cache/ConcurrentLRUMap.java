package net.lightstone.cache;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a generic implementation of a thread-safe LRU map.
 * 
 * @author Joe Pritzel
 * 
 * @param <K>
 * @param <V>
 */
public class ConcurrentLRUMap<K, V> extends AbstractMap<K, V> {

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int DEFAULT_MAX_SIZE = 100;

	private AtomicInteger maxSize = new AtomicInteger(0);;
	private final float loadFactor;

	private final Map<K, Value<V>> map;

	public ConcurrentLRUMap() {
		this(DEFAULT_MAX_SIZE, DEFAULT_LOAD_FACTOR);
	}

	public ConcurrentLRUMap(final int maxSize) {
		this(maxSize, DEFAULT_LOAD_FACTOR);
	}

	public ConcurrentLRUMap(final int maxSize, final float loadFactor) {
		this.maxSize.set(maxSize);
		this.loadFactor = loadFactor;
		map = new ConcurrentHashMap<K, Value<V>>(this.maxSize.get(),
				this.loadFactor);
	}

	/**
	 * @return The max size for the LRUMap
	 */
	protected int getMaxSize() {
		return maxSize.get();
	}

	/**
	 * Increases the max size for the LRUMap by one.
	 * @return The new max size
	 */
	protected int incrementMaxSize() {
		return maxSize.incrementAndGet();
	}

	/**
	 * Decreases the max size for the LRUMap by one.
	 * @return
	 */
	protected int decrementMaxSize() {
		return maxSize.decrementAndGet();
	}

	/**
	 * @return The LRU entry.
	 */
	private Map.Entry<K, Value<V>> getLRU() {
		Map.Entry<K, Value<V>> e1 = null;
		long t = Long.MAX_VALUE;
		final Set<Map.Entry<K, Value<V>>> m = map.entrySet();
		for (Map.Entry<K, Value<V>> e : m) {
			if (e.getValue().getTime() <= t) {
				e1 = e;
				t = e.getValue().getTime();
			}
		}
		return e1;
	}

	/**
	 * Removes entry if shouldRemove the key is non-null, and shouldRemove returns true.
	 */
	protected boolean tryRemove(K k, V v) {
		if (k != null && shouldRemove(k, v)) {
			map.remove(k);
			dispose(k, v);
			return true;
		}
		return false;
	}

	/**
	 * This method is called when an entry is removed due to it being the least
	 * recently used.
	 * 
	 * @param k
	 * @param v
	 */
	protected void dispose(K k, V v) {
	}
	
	/**
	 * This method is called from the tryRemove method.
	 * @param key
	 * @param value
	 * @return
	 */
	protected boolean shouldRemove(K key, V value) {
		return true;
	}

	/**
	 * This method is called when the size of the LRU map is
	 * greater-than-or-equal-to the max size, and a new entry is being added.
	 * 
	 * @return If the LRU entry should be removed.
	 */
	protected boolean shouldRemoveLRU(K key, V value) {
		return true;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		for (Value<V> v : map.values()) {
			if (v.get().equals(arg0)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		final Map<K, V> m = new HashMap<K, V>();
		for (Map.Entry<K, Value<V>> e : map.entrySet()) {
			m.put(e.getKey(), e.getValue().get());
		}
		return m.entrySet();
	}

	@Override
	public V get(Object arg0) {
		Value<V> v1 = map.get(arg0);
		if (v1 != null)
			return v1.get();
		return null;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(Object arg0, Object arg1) {
		final Value<V> val = map.put((K) arg0, new Value<V>((V) arg1));
		if (map.size() >= maxSize.get()) {
			Map.Entry<K, Value<V>> e1 = getLRU();
			if (shouldRemoveLRU(e1.getKey(), e1.getValue().get())) {
				tryRemove(e1.getKey(), e1.getValue().get());
			}
		}
		if (val != null)
			return val.get();
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		for (Entry<? extends K, ? extends V> e : arg0.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public V remove(Object arg0) {
		return map.remove(arg0).get();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		final Collection<V> c = new HashSet<V>();
		for (Map.Entry<K, Value<V>> e : map.entrySet()) {
			c.add(e.getValue().get());
		}
		return c;
	}

	private class Value<V1> {

		private AtomicReference<V1> v;
		private AtomicLong t;

		public Value(V1 v) {
			this(v, System.nanoTime());
		}

		public Value(V1 v, long t) {
			this.v = new AtomicReference<V1>(v);
			this.t = new AtomicLong(t);
		}

		public V1 get() {
			t.set(System.nanoTime());
			return v.get();
		}

		public long getTime() {
			return t.get();
		}

	}

}
