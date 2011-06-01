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

public class ConcurrentLRUMap<K, V> extends AbstractMap<K, V> {

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private static final int DEFAULT_MAX_SIZE = 100;

	private transient final int maxSize;
	private transient final float loadFactor;

	private final Map<K, Value<V>> map;
	private final AtomicInteger size = new AtomicInteger(0);

	public ConcurrentLRUMap() {
		this(DEFAULT_MAX_SIZE, DEFAULT_LOAD_FACTOR);
	}

	public ConcurrentLRUMap(final int maxSize) {
		this(maxSize, DEFAULT_LOAD_FACTOR);
	}

	public ConcurrentLRUMap(final int maxSize, final float loadFactor) {
		this.maxSize = maxSize;
		this.loadFactor = loadFactor;
		map = new ConcurrentHashMap<K, Value<V>>(this.maxSize, this.loadFactor);
	}

	private void removeLRU() {
		K k = null;
		long t = Long.MAX_VALUE;
		final Set<Map.Entry<K, Value<V>>> m = map.entrySet();
		for (Map.Entry<K, Value<V>> e : m) {
			if (e.getValue().getTime() < t) {
				k = e.getKey();
				t = e.getValue().getTime();
			}
		}
		if (k != null)
			map.remove(k);
		System.out.println("Removed " + k);
	}

	@Override
	public void clear() {
		for (Value<V> v : map.values()) {
			v.set(null);
			size.decrementAndGet();
		}
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
		return size.get() == 0;
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(Object arg0, Object arg1) {
		if (size.get() >= maxSize)
			removeLRU();
		size.incrementAndGet();
		final Value<V> val = map.put((K) arg0, new Value<V>((V) arg1));
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
		size.decrementAndGet();
		return map.remove(arg0).get();
	}

	@Override
	public int size() {
		return size.get();
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

		public V1 set(V1 v) {
			return set(v, System.nanoTime()).get();
		}

		public Value<V1> set(V1 v, long t) {
			final Value<V1> v1 = this;
			this.v.set(v);
			this.t.set(t);
			return v1;
		}

	}

}
