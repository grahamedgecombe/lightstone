package net.lightstone.msg;

public final class EntityActionMessage extends Message {

	public static final int ACTION_CROUCH = 1;
	public static final int ACTION_UNCROUCH = 2;
	public static final int ACTION_LEAVE_BED = 3;

	private final int id, action;

	public EntityActionMessage(int id, int action) {
		this.id = id;
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public int getAction() {
		return action;
	}

}
