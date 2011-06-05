package net.lightstone.util;

import static org.junit.Assert.*;

import java.util.List;

import net.lightstone.model.Coordinate;
import net.lightstone.model.Item;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

/**
 * A unit test for the {@link ChannelBufferUtils} class.
 * @author Graham Edgecombe
 */
public final class ChannelBufferUtilsTest {

	/**
	 * Tests the
	 * {@link ChannelBufferUtils#writeParameters(ChannelBuffer, List)}
	 * method.
	 */
	@Test
	public void testWriteParameters() {
		Parameter<?>[] params = new Parameter<?>[Parameter.METADATA_SIZE];
		params[0] = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x12);
		params[1] = new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 0x1234);
		params[2] = new Parameter<Integer>(Parameter.TYPE_INT, 2, (int) 0x12345678);
		params[3] = new Parameter<Float>(Parameter.TYPE_FLOAT, 3, 1234.5678F);
		params[4] = new Parameter<String>(Parameter.TYPE_STRING, 4, "test");
		params[5] = new Parameter<Item>(Parameter.TYPE_ITEM, 5, new Item(1, 64, 0));
		params[6] = new Parameter<Coordinate>(Parameter.TYPE_COORDINATE, 6, new Coordinate(10, 11, 12));

		ChannelBuffer buffer = ChannelBuffers.buffer(46);
		ChannelBufferUtils.writeParameters(buffer, params);

		assertEquals(0x00, buffer.readUnsignedByte());
		assertEquals(0x12, buffer.readUnsignedByte());

		assertEquals(0x21, buffer.readUnsignedByte());
		assertEquals(0x1234, buffer.readUnsignedShort());

		assertEquals(0x42, buffer.readUnsignedByte());
		assertEquals(0x12345678, buffer.readInt());

		assertEquals(0x63, buffer.readUnsignedByte());
		assertEquals(1234.5678F, buffer.readFloat(), 0);

		assertEquals(0x84, buffer.readUnsignedByte());
		assertEquals("test", ChannelBufferUtils.readString(buffer));

		assertEquals(0xA5, buffer.readUnsignedByte());
		assertEquals(1, buffer.readUnsignedShort());
		assertEquals(64, buffer.readUnsignedByte());
		assertEquals(0, buffer.readUnsignedShort());

		assertEquals(0xC6, buffer.readUnsignedByte());
		assertEquals(10, buffer.readInt());
		assertEquals(11, buffer.readInt());
		assertEquals(12, buffer.readInt());

		assertEquals(0x7F, buffer.readUnsignedByte());
	}

	/**
	 * Tests the {@link ChannelBufferUtils#readParameters(ChannelBuffer)}
	 * method.
	 */
	@Test
	public void testReadParameters() {
		ChannelBuffer buffer = ChannelBuffers.buffer(46);
		buffer.writeByte(0x00); // type 0 index 0
		buffer.writeByte(0x12);

		buffer.writeByte(0x21); // type 1 index 1
		buffer.writeShort(0x1234);

		buffer.writeByte(0x42); // type 2 index 2
		buffer.writeInt(0x12345678);

		buffer.writeByte(0x63); // type 3 index 3
		buffer.writeFloat(1234.5678F);

		buffer.writeByte(0x84); // type 4 index 4
		ChannelBufferUtils.writeString(buffer, "test");

		buffer.writeByte(0xA5); // type 5 index 5
		buffer.writeShort(1);
		buffer.writeByte(64);
		buffer.writeShort(0);

		buffer.writeByte(0xC6); // type 6 index 6
		buffer.writeInt(10);
		buffer.writeInt(11);
		buffer.writeInt(12);

		buffer.writeByte(0x7F); // end of list

		Parameter<?>[] params = ChannelBufferUtils.readParameters(buffer);
		int size = 0;
		for (Parameter<?> param : params)
			if (param != null)
				size++;
		assertEquals(7, size);

		for (int index = 0; index < size; index++) {
			assertEquals(index, params[index].getIndex());
		}

		Parameter<?> byteParam = params[0];
		assertEquals(Parameter.TYPE_BYTE, byteParam.getType());
		assertEquals((byte) 0x12, byteParam.getValue());

		Parameter<?> shortParam = params[1];
		assertEquals(Parameter.TYPE_SHORT, shortParam.getType());
		assertEquals((short) 0x1234, shortParam.getValue());

		Parameter<?> intParam = params[2];
		assertEquals(Parameter.TYPE_INT, intParam.getType());
		assertEquals((int) 0x12345678, intParam.getValue());

		Parameter<?> floatParam = params[3];
		assertEquals(Parameter.TYPE_FLOAT, floatParam.getType());
		assertEquals(1234.5678F, floatParam.getValue());

		Parameter<?> stringParam = params[4];
		assertEquals(Parameter.TYPE_STRING, stringParam.getType());
		assertEquals("test", stringParam.getValue());

		Parameter<?> itemParam = params[5];
		assertEquals(Parameter.TYPE_ITEM, itemParam.getType());
		assertEquals(new Item(1, 64, 0), itemParam.getValue());

		Parameter<?> coordinateParam = params[6];
		assertEquals(Parameter.TYPE_COORDINATE, coordinateParam.getType());
		assertEquals(new Coordinate(10, 11, 12), coordinateParam.getValue());
	}

	/**
	 * Tests the
	 * {@link ChannelBufferUtils#writeString(ChannelBuffer, String)} method.
	 */
	@Test
	public void testWriteString() {
		ChannelBuffer buffer = ChannelBuffers.buffer(12);
		ChannelBufferUtils.writeString(buffer, "hello");

		assertEquals(5, buffer.readUnsignedShort());
		assertEquals('h', buffer.readChar());
		assertEquals('e', buffer.readChar());
		assertEquals('l', buffer.readChar());
		assertEquals('l', buffer.readChar());
		assertEquals('o', buffer.readChar());
	}

	/**
	 * Tests the {@link ChannelBufferUtils#readString(ChannelBuffer)}
	 * method.
	 */
	@Test
	public void testReadString() {
		ChannelBuffer buffer = ChannelBuffers.buffer(12);
		buffer.writeShort(5);
		buffer.writeChar('h');
		buffer.writeChar('e');
		buffer.writeChar('l');
		buffer.writeChar('l');
		buffer.writeChar('o');

		assertEquals("hello", ChannelBufferUtils.readString(buffer));
	}

	/**
	 * Tests the
	 * {@link ChannelBufferUtils#writeUtf8String(ChannelBuffer, String)}
	 * method.
	 */
	@Test
	public void testWriteUtf8String() {
		ChannelBuffer buffer = ChannelBuffers.buffer(7);
		ChannelBufferUtils.writeUtf8String(buffer, "hello");

		assertEquals(5, buffer.readUnsignedShort());
		assertEquals('h', buffer.readUnsignedByte());
		assertEquals('e', buffer.readUnsignedByte());
		assertEquals('l', buffer.readUnsignedByte());
		assertEquals('l', buffer.readUnsignedByte());
		assertEquals('o', buffer.readUnsignedByte());
	}

	/**
	 * Tests the {@link ChannelBufferUtils#readUtf8String(ChannelBuffer)}
	 * method.
	 */
	@Test
	public void testReadUtf8String() {
		ChannelBuffer buffer = ChannelBuffers.buffer(7);
		buffer.writeShort(5);
		buffer.writeBytes("hello".getBytes());

		assertEquals("hello", ChannelBufferUtils.readUtf8String(buffer));
	}

}

