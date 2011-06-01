package net.lightstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.lightstone.msg.WeatherMessage;

public final class WeatherCodec extends MessageCodec<WeatherMessage> {

	public WeatherCodec() {
		super(WeatherMessage.class, 0x47);
	}

	@Override
	public WeatherMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int mode = buffer.readUnsignedByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		return new WeatherMessage(id, mode, x, y, z);
	}

	@Override
	public ChannelBuffer encode(WeatherMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(17);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getMode());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		return buffer;
	}

}

