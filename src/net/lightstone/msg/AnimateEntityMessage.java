package net.lightstone.msg;

public final class AnimateEntityMessage extends Message {

	public static final int ANIMATION_NONE = 0;
	public static final int ANIMATION_SWING_ARM = 1;
	public static final int ANIMATION_DAMAGE = 2;
	public static final int ANIMATION_LEAVE_BED = 3;
	public static final int ANIMATION_CROUCH = 104;
	public static final int ANIMATION_UNCROUCH = 105;

	private final int id, animation;

	public AnimateEntityMessage(int id, int animation) {
		this.id = id;
		this.animation = animation;
	}

	public int getId() {
		return id;
	}

	public int getAnimation() {
		return animation;
	}

}

