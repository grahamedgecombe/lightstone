package net.lightstone.cache;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class ConcurrentLRUMapTest {
	@Test
	public void test() {
		final Map<String, Character> map = new ConcurrentLRUMap<String, Character>(5);
		final String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
		final char[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		
		for(int i = 0; i<names.length; i++) {
			map.put(names[i], values[i]);
		}
		
		int skip = 3;
		for(int i = 0; i<names.length; i++) {
			if(i != skip)
			map.get(names[i]);
		}
		map.put(names[0], values[0]);
		
		assertTrue(map.containsKey(names[0]));		
		assertTrue(map.containsKey(names[5]));
		assertTrue(map.containsKey(names[6]));
		assertTrue(map.containsKey(names[7]));
		assertTrue(map.containsKey(names[8]));
		assertFalse(map.containsKey(names[4]));
		assertFalse(map.containsKey(names[2]));
		assertFalse(map.containsKey(names[1]));
		assertFalse(map.containsKey(names[skip]));
	}
}
