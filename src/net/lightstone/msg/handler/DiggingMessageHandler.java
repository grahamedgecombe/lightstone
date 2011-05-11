package net.lightstone.msg.handler;

import net.lightstone.model.Blocks;
import net.lightstone.model.ChunkManager;
import net.lightstone.model.Player;
import net.lightstone.model.Position;
import net.lightstone.msg.DiggingMessage;
import net.lightstone.model.Chunk;
import net.lightstone.msg.BlockChangeMessage;
import net.lightstone.net.Session;
import net.lightstone.util.MathUtils;
import net.lightstone.world.World;

/**
 * A {@link MessageHandler} which processes digging messages.
 * 
 * @author Zhuowei Zhang
 * @author Joe Pritzel
 */
public final class DiggingMessageHandler extends MessageHandler<DiggingMessage> {

	@Override
	public void handle(Session session, Player player, DiggingMessage message) {
		if (player == null)
			return;

		if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
			World world = player.getWorld();

			int x = message.getX();
			int z = message.getZ();
			int y = message.getY();
			
			final Position pos = player.getPosition();
			
			if(MathUtils.distance(x, z, y, pos.getX(), pos.getZ(), pos.getY()) > 7) {
				return;
			}
			
			int chunkX = ChunkManager.getChunkX(x);
			int chunkZ = ChunkManager.getChunkZ(z);

			int localX = (x - chunkX * Chunk.WIDTH) % Chunk.WIDTH;
			int localZ = (z - chunkZ * Chunk.HEIGHT) % Chunk.HEIGHT;

			Chunk chunk = world.getChunks().getChunk(chunkX, chunkZ);
			chunk.setType(localX, localZ, y, Blocks.TYPE_AIR);

			// TODO this should also be somewhere else as well... perhaps in the
			// chunk.setType() method itself?
			BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, 0, 0);
			for (Player p : world.getPlayers()) {
				if (p.isWithinDistance(x, z)) {
					p.getSession().send(bcmsg);
				}
			}
		}
	}

}
