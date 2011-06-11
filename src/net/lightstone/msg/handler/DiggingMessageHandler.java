package net.lightstone.msg.handler;

import net.lightstone.model.Blocks;
import net.lightstone.model.Player;
import net.lightstone.msg.DiggingMessage;
import net.lightstone.model.Chunk;
import net.lightstone.msg.BlockChangeMessage;
import net.lightstone.msg.SoundEffectMessage;
import net.lightstone.net.Session;
import net.lightstone.world.World;

/**
 * A {@link MessageHandler} which processes digging messages.
 * @author Zhuowei Zhang
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

			// TODO it might be nice to move these calculations somewhere else since they will need to be reused
			int chunkX = x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
			int chunkZ = z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);

			int localX = (x - chunkX * Chunk.WIDTH) % Chunk.WIDTH;
			int localZ = (z - chunkZ * Chunk.HEIGHT) % Chunk.HEIGHT;

			Chunk chunk = world.getChunks().getChunk(chunkX, chunkZ);
			int oldType = chunk.getType(localX, localZ, y);
			chunk.setType(localX, localZ, y, Blocks.TYPE_AIR);

			// TODO this should also be somewhere else as well... perhaps in the chunk.setType() method itself?
			BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, 0, 0);
			SoundEffectMessage soundMsg = new SoundEffectMessage(SoundEffectMessage.DIG_SOUND, x, y, z, oldType);
			for (Player p: world.getPlayers()) {
				p.getSession().send(bcmsg);
				if(p != player && player.isWithinDistance(p)) {
					p.getSession().send(soundMsg);
				}
			}
		}
	}

}

