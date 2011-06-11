package net.lightstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.lightstone.msg.SoundEffectMessage;

public final class SoundEffectCodec extends MessageCodec<SoundEffectMessage> {

	public SoundEffectCodec() {
		super(SoundEffectMessage.class, 0x3d);
	}

	@Override
	public SoundEffectMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int x = buffer.readInt();
		int y = buffer.readUnsignedByte();
		int z = buffer.readInt();
		int data = buffer.readInt();
		return new SoundEffectMessage(id, x, y, z, data);
	}

	@Override
	public ChannelBuffer encode(SoundEffectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(17);
		buffer.writeInt(message.getId());
		buffer.writeInt(message.getX());
		buffer.writeByte(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeInt(message.getData());
		return buffer;
	}

}

