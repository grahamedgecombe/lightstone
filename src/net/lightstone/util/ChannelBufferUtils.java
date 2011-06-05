package net.lightstone.util;

import java.nio.charset.Charset;

import net.lightstone.model.Coordinate;
import net.lightstone.model.Item;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Contains several {@link ChannelBuffer}-related utility methods.
 * @author Graham Edgecombe
 */
public final class ChannelBufferUtils {

	/**
	 * The UTF-8 character set.
	 */
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	/**
	 * Writes a list of parameters (e.g. mob metadata) to the buffer.
	 * @param buf The buffer.
	 * @param parameters The parameters.
	 */
	@SuppressWarnings("unchecked")
	public static void writeParameters(ChannelBuffer buf, Parameter<?>[] parameters) {
		for (Parameter<?> parameter : parameters) {
			if (parameter == null)
				continue;

			int type  = parameter.getType();
			int index = parameter.getIndex();

			buf.writeByte(((type & 0x07) << 5) | (index & 0x1F));

			switch (type) {
			case Parameter.TYPE_BYTE:
				buf.writeByte(((Parameter<Byte>) parameter).getValue());
				break;
			case Parameter.TYPE_SHORT:
				buf.writeShort(((Parameter<Short>) parameter).getValue());
				break;
			case Parameter.TYPE_INT:
				buf.writeInt(((Parameter<Integer>) parameter).getValue());
				break;
			case Parameter.TYPE_FLOAT:
				buf.writeFloat(((Parameter<Float>) parameter).getValue());
				break;
			case Parameter.TYPE_STRING:
				writeString(buf, ((Parameter<String>) parameter).getValue());
				break;
			case Parameter.TYPE_ITEM:
				Item item = ((Parameter<Item>) parameter).getValue();
				buf.writeShort(item.getId());
				buf.writeByte(item.getCount());
				buf.writeShort(item.getDamage());
				break;
			case Parameter.TYPE_COORDINATE:
				Coordinate coord = ((Parameter<Coordinate>) parameter).getValue();
				buf.writeInt(coord.getX());
				buf.writeInt(coord.getY());
				buf.writeInt(coord.getZ());
				break;
			}
		}

		buf.writeByte(0x7F);
	}

	/**
	 * Reads a list of parameters from the buffer.
	 * @param buf The buffer.
	 * @return The parameters.
	 */
	public static Parameter<?>[] readParameters(ChannelBuffer buf) {
		Parameter<?>[] parameters = new Parameter<?>[Parameter.METADATA_SIZE];

		int b;
		while ((b = buf.readUnsignedByte()) != 0x7F) {
			int type  = (b >> 5) & 0x07;
			int index = b & 0x1F;

			switch (type) {
			case Parameter.TYPE_BYTE:
				parameters[index] = new Parameter<Byte>(type, index, buf.readByte());
				break;
			case Parameter.TYPE_SHORT:
				parameters[index] = new Parameter<Short>(type, index, buf.readShort());
				break;
			case Parameter.TYPE_INT:
				parameters[index] = new Parameter<Integer>(type, index, buf.readInt());
				break;
			case Parameter.TYPE_FLOAT:
				parameters[index] = new Parameter<Float>(type, index, buf.readFloat());
				break;
			case Parameter.TYPE_STRING:
				parameters[index] = new Parameter<String>(type, index, readString(buf));
				break;
			case Parameter.TYPE_ITEM:
				int id = buf.readShort();
				int count = buf.readByte();
				int damage = buf.readShort();
				Item item = new Item(id, count, damage);
				parameters[index] = new Parameter<Item>(type, index, item);
				break;
			case Parameter.TYPE_COORDINATE:
				int x = buf.readInt();
				int y = buf.readInt();
				int z = buf.readInt();
				Coordinate coordinate = new Coordinate(x, y, z);
				parameters[index] = new Parameter<Coordinate>(type, index, coordinate);
				break;
			}
		}

		return parameters;
	}

	/**
	 * Writes a string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long
	 * <em>after</em> it is encoded.
	 */
	public static void writeString(ChannelBuffer buf, String str) {
		int len = str.length();
		if (len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}

		buf.writeShort(len);
		for (int i = 0; i < len; i++) {
			buf.writeChar(str.charAt(i));
		}
	}

	/**
	 * Writes a UTF-8 string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long
	 * <em>after</em> it is encoded.
	 */
	public static void writeUtf8String(ChannelBuffer buf, String str) {
		byte[] bytes = str.getBytes(CHARSET_UTF8);
		if (bytes.length >= 65536) {
			throw new IllegalArgumentException("Encoded UTF-8 string too long.");
		}

		buf.writeShort(bytes.length);
    	buf.writeBytes(bytes);
	}

	/**
	 * Reads a string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readString(ChannelBuffer buf) {
		int len = buf.readUnsignedShort();

		char[] characters = new char[len];
		for (int i = 0; i < len; i++) {
			characters[i] = buf.readChar();
		}

		return new String(characters);
	}


	/**
	 * Reads a UTF-8 encoded string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readUtf8String(ChannelBuffer buf) {
		int len = buf.readUnsignedShort();

		byte[] bytes = new byte[len];
		buf.readBytes(bytes);

		return new String(bytes, CHARSET_UTF8);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private ChannelBufferUtils() {

	}

}

