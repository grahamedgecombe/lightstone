package net.lightstone.msg.handler;

import net.lightstone.model.Blocks;
import net.lightstone.model.Player;
import net.lightstone.model.Item;
import net.lightstone.msg.BlockPlacementMessage;
import net.lightstone.model.Chunk;
import net.lightstone.msg.BlockChangeMessage;
import net.lightstone.net.Session;
import net.lightstone.world.World;

/**
 * A {@link MessageHandler} which processes block placement messages.
 * @author Zhuowei Zhang
 */
public final class BlockPlacementMessageHandler extends MessageHandler<BlockPlacementMessage> {

	@Override
	public void handle(Session session, Player player, BlockPlacementMessage message) {
		if (player == null)
			return;

		World world = player.getWorld();

		int msgX = message.getX();
		int msgZ = message.getZ();
		int msgY = message.getY();
		int direction = message.getDirection();

		if (msgX == -1 && msgZ == -1 && (msgY == -1 || msgY == 255) && direction == -1) {
			//TODO:Special cases eg bucket
			player.sendMessage("Not implemented");
			return;
		}
		int id, damage;
		int slot = player.getActiveSlot();
		Item item = player.getInventory().get(slot);
		if (item == null) {
			return;
		} else {
			id = item.getId();
			damage = item.getDamage();
		}
		if (id > 0x100) {
			System.out.println("Placing items not implemented");
			player.sendMessage("Not implemented");
			return;
		}
		int x = msgX;
		int z = msgZ;
		int y = msgY;
		switch (direction) {
			case 0: 
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
		}
		// Copied from DiggingMessageHandler
		int chunkX = x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
		int chunkZ = z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);
		try {
			Chunk chunk = world.getChunks().getChunk(chunkX, chunkZ);
			int[] chunkCoords = chunk.globalToLocal(x, z);
			chunk.setType(chunkCoords[0], chunkCoords[1], y, id);
			chunk.setMetaData(chunkCoords[0], chunkCoords[1], y, damage); // damage becomes metadata
			player.getInventory().use(slot, 1);
			BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, id, damage);
			for (Player p: world.getPlayers()) {
				p.getSession().send(bcmsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

