package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.msg.EntityActionMessage;
//import net.lightstone.msg.AnimateEntityMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which handles Entity action messages.
 * @author Zhuowei Zhang
 */
public final class EntityActionMessageHandler extends MessageHandler<EntityActionMessage> {

	@Override
	public void handle(Session session, Player player, EntityActionMessage message) {
		/* I don't know the correct packet to send to the client to make the player appear to sneak. */
		int animation;
		switch(message.getAction()){
			case EntityActionMessage.ACTION_CROUCH:
				player.setCrouching(true);
				//animation=AnimateEntityMessage.ANIMATION_CROUCH;
				break;
			case EntityActionMessage.ACTION_UNCROUCH:
				player.setCrouching(false);
				//animation=AnimateEntityMessage.ANIMATION_UNCROUCH;
				break;
			case EntityActionMessage.ACTION_LEAVE_BED:
				//animation=AnimateEntityMessage.ANIMATION_LEAVE_BED;
				break;
			default:
				System.err.println("Unknown Entity Action.");
				return;
		}
		/*AnimateEntityMessage animmsg=new AnimateEntityMessage(player.getId(), animation);
		EntityActionMessage actionmsg=new EntityActionMessage(player.getId(), message.getAction());
		for (Player p: player.getWorld().getPlayers()) {
			if(!p.equals(player)){
				System.out.println("send message");
				p.getSession().send(actionmsg);
				p.getSession().send(animmsg);
			}
		}*/
	}


}

