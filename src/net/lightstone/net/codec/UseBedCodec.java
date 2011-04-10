package net.lightstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.lightstone.msg.UseBedMessage;

public final class UseBedCodec extends MessageCodec<UseBedMessage> {

	public UseBedCodec() {
		super(UseBedMessage.class, 0x11);
	}

	@Override
	public UseBedMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int status = buffer.readUnsignedByte();
		int x = buffer.readInt();
		int y = buffer.readUnsignedByte();
		int z = buffer.readInt();
		return new UseBedMessage(id, status, x, y, z);
	}

	@Override
	public ChannelBuffer encode(UseBedMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(14);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getStatus());
		buffer.writeInt(message.getX());
		buffer.writeByte(message.getY());
		buffer.writeInt(message.getZ());
		return buffer;
	}

}

