package net.lightstone.msg;

public final class SoundEffectMessage extends Message {

	public static final int CLICK2 = 1000;

	public static final int CLICK1 = 1001;

	public static final int BOW_FIRE = 1002;

	public static final int RECORD_PLAY = 1005;

	public static final int DIG_SOUND = 2001;

	private final int id, x, y, z, data;

	public SoundEffectMessage(int id, int x, int y, int z, int data) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getData() {
		return data;
	}

}

