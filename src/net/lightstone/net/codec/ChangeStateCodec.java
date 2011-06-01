package net.lightstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.lightstone.msg.ChangeStateMessage;

public final class ChangeStateCodec extends MessageCodec<ChangeStateMessage> {

	public ChangeStateCodec() {
		super(ChangeStateMessage.class, 0x46);
	}

	@Override
	public ChangeStateMessage decode(ChannelBuffer buffer) throws IOException {
		int state = buffer.readUnsignedByte();
		return new ChangeStateMessage(state);
	}

	@Override
	public ChannelBuffer encode(ChangeStateMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		buffer.writeByte(message.getState());
		return buffer;
	}

}

