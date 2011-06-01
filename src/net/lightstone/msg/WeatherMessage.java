package net.lightstone.msg;

public final class WeatherMessage extends Message {

	private final int id, mode, x, y, z;

	public WeatherMessage(int id, int mode, int x, int y, int z) {
		this.id = id;
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getId() {
		return id;
	}

	public int getMode() {
		return mode;
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


}

