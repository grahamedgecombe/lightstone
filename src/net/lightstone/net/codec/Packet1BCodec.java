package net.lightstone.net.codec;

import net.lightstone.msg.Packet1BMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * A codec that codes for packet 0x1B, a mystery packet.
 */
public final class Packet1BCodec extends MessageCodec<Packet1BMessage> {

	public Packet1BCodec() {
		super(Packet1BMessage.class, 0x1B);
	}

	@Override
	public Packet1BMessage decode(ChannelBuffer buffer) {
		float first = buffer.readFloat();
		float second = buffer.readFloat();
		float third = buffer.readFloat();
		float fourth = buffer.readFloat();
		boolean fifth = buffer.readUnsignedByte() != 0;
		boolean sixth = buffer.readUnsignedByte() != 0;
		return new Packet1BMessage(first, second, third, fourth, fifth, sixth);
	}

	@Override
	public ChannelBuffer encode(Packet1BMessage message) {
		ChannelBuffer buffer = ChannelBuffers.buffer(18);
		buffer.writeFloat(message.getFirst());
		buffer.writeFloat(message.getSecond());
		buffer.writeFloat(message.getThird());
		buffer.writeFloat(message.getFourth());
		buffer.writeByte(message.getFifth() ? 1 : 0);
		buffer.writeByte(message.getSixth() ? 1 : 0);
		return buffer;
	}

}

