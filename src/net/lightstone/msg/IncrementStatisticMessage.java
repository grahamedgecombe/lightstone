package net.lightstone.msg;

public final class IncrementStatisticMessage extends Message {

	private final int id, amount;

	public IncrementStatisticMessage(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

}

