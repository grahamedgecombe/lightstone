package net.lightstone.msg;

public final class Packet1BMessage extends Message {

	private final float first, second, third, fourth;
	private final boolean fifth, sixth;

	public Packet1BMessage(float first, float second, float third, float fourth, boolean fifth, boolean sixth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
		this.sixth = sixth;
	}

	public float getFirst() {
		return first;
	}

	public float getSecond() {
		return second;
	}

	public float getThird() {
		return third;
	}

	public float getFourth() {
		return fourth;
	}

	public boolean getFifth() {
		return fifth;
	}

	public boolean getSixth() {
		return sixth;
	}

}

