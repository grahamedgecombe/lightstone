package net.lightstone.msg;

public final class RespawnMessage extends Message {

	public final int dimension;

	public RespawnMessage(int dimension) {
		this.dimension = dimension;
	}

	public int getDimension() {
		return dimension;
	}

}

