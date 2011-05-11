package net.lightstone.io.service.impl;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlatFileIoServiceTest {

	private final File file = new File("Test");
	private FlatFileIoService ffios;

	@Before
	public void setUp() throws Exception {
		if (!file.exists()) {
			file.createNewFile();
			BufferedWriter o = new BufferedWriter(new FileWriter(file));
			o.write("Test=Pass");
			o.close();
		}
		 ffios = new FlatFileIoService();
		 ffios.setLocation(file);
	}

	@After
	public void tearDown() throws Exception {
		file.delete();
	}

	@Test
	public void testSetLocation() {
		assertFalse("Null test", ffios.setLocation(null));
		assertTrue("String test", ffios.setLocation("Test"));
		assertTrue("File test", ffios.setLocation(file));
	}

	@Test
	public void testReadObject() {
		assertTrue("File test", ffios.setLocation(file));
		try {
			assertNull("Null test", ffios.read(null));
			assertEquals("String test", "Pass", ffios.read("Test"));
			assertNull("String test", ffios.read("Test1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWriteObjectString() {
		try {
			assertNull("String test", ffios.read("Test1"));
			assertNull(ffios.write("Test1", "Testing"));
			assertEquals("String test", "Testing", ffios.read("Test1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
