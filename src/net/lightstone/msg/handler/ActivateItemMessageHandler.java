package net.lightstone.msg.handler;

import net.lightstone.model.Entity;
import net.lightstone.model.Player;
import net.lightstone.model.Item;
import net.lightstone.model.SlottedItem;
import net.lightstone.msg.ActivateItemMessage;
import net.lightstone.msg.EntityEquipmentMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which handles ActivateItemMessages.
 * @author Zhuowei Zhang
 */
public final class ActivateItemMessageHandler extends MessageHandler<ActivateItemMessage> {

	@Override
	public void handle(Session session, Player player, ActivateItemMessage message) {
		int slot = message.getSlot();
		player.setActiveSlot(slot);
		Item item = player.getInventory().get(slot);
		int id, damage;
		if(item == null){
			id = -1;
			damage = 0;
		}
		else{
			id = item.getId();
			damage = item.getDamage();
		}

		EntityEquipmentMessage equipMessage = new EntityEquipmentMessage(player.getId(), 0, id, damage);
		for (Player p : player.getWorld().getPlayers()) {
			if (p != player) {
				p.getSession().send(equipMessage);
			}
		}
	}

}

