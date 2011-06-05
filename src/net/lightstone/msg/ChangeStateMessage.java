package net.lightstone.msg;

public final class ChangeStateMessage extends Message {

	public static final int INVALID_BED = 0;
	public static final int START_RAINING = 1;
	public static final int STOP_RAINING = 2;

	private final int state;

	public ChangeStateMessage(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

}

