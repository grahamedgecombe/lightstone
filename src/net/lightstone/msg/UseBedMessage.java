package net.lightstone.msg;

public final class UseBedMessage extends Message {

	private final int id, status, x, y, z;

	public UseBedMessage(int id, int status, int x, int y, int z) {
		this.id = id;
		this.status = status;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getId() {
		return id;
	}

	public int getStatus() {
		return status;
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

