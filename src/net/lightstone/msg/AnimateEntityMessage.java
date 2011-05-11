package net.lightstone.msg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jruby.org.objectweb.asm.Type;

public final class AnimateEntityMessage extends Message {
	
	public static final int[] ANIMATION_IDS;

	// To add new animation ids just make a field like the ones below.
	public static final int ANIMATION_NONE = 0;
	public static final int ANIMATION_SWING_ARM = 1;
	public static final int ANIMATION_DAMAGE = 2;
	public static final int ANIMATION_LEAVE_BED = 3;
	public static final int ANIMATION_CROUCH = 104;
	public static final int ANIMATION_UNCROUCH = 105;
	

	private final int entityId, animation;

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger
			.getLogger(AnimateEntityMessage.class.getName());

	static {
		final Field[] fields = AnimateEntityMessage.class.getDeclaredFields();
		final List<Integer> animationIds = new ArrayList<Integer>();
		for (final Field f : fields) {
			if (f.getType().equals(Type.INT_TYPE)
					&& f.getName().startsWith("ANIMATION_")) {
				try {
					animationIds.add(f.getInt(null));
				} catch (IllegalAccessException e) {
					logger.warning("Unable to access animation id field: "
							+ f.getName());
				}
			}
		}
		ANIMATION_IDS = new int[animationIds.size()];
		for (int i = 0; i < ANIMATION_IDS.length; i++) {
			ANIMATION_IDS[i] = animationIds.get(i);
		}
	}

	public AnimateEntityMessage(int id, int animation) {
		this.entityId = id;
		this.animation = animation;
	}

	public int getId() {
		return entityId;
	}

	public int getAnimation() {
		return animation;
	}

	public boolean isValid() {
		for (int i = 0; i < ANIMATION_IDS.length; i++) {
			if (ANIMATION_IDS[i] == this.animation) {
				return true;
			}
		}
		return false;
	}

}
