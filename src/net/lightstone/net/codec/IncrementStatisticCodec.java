package net.lightstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.lightstone.msg.IncrementStatisticMessage;

public final class IncrementStatisticCodec extends MessageCodec<IncrementStatisticMessage> {

	public IncrementStatisticCodec() {
		super(IncrementStatisticMessage.class, 0xC8);
	}

	@Override
	public IncrementStatisticMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int amount = buffer.readUnsignedByte();
		return new IncrementStatisticMessage(id, amount);
	}

	@Override
	public ChannelBuffer encode(IncrementStatisticMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getAmount());
		return buffer;
	}

}

