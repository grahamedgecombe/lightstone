package net.lightstone.net.codec;

import net.lightstone.msg.MapDataMessage;
import net.lightstone.util.ChannelBufferUtils;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class MapDataCodec extends MessageCodec<MapDataMessage> {

	public MapDataCodec() {
		super(MapDataMessage.class, 0x83);
	}

	@Override
	public MapDataMessage decode(ChannelBuffer buffer) {
		//first short, according to mc.kev009.com:
		//"Unknown constant value(s)"
		int constant = buffer.readShort();
		int id = buffer.readShort();
		int length = buffer.readUnsignedByte();
		byte[] data = new byte[length];
		buffer.readBytes(data);
		return new MapDataMessage(id, data);
	}

	@Override
	public ChannelBuffer encode(MapDataMessage message) {
		byte[] data = message.getData();
		ChannelBuffer buffer = ChannelBuffers.buffer(5 + data.length);
		buffer.writeShort(0); //FIXME
		buffer.writeShort(message.getId());
		buffer.writeByte(data.length);
		buffer.writeBytes(data, 0, data.length);
		return buffer;
	}

}

