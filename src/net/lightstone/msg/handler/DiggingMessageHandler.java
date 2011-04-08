package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.msg.DiggingMessage;
import net.lightstone.model.Chunk;
import net.lightstone.msg.BlockChangeMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which processes digging messages.
 * @author Zhuowei Zhang
 */
public final class DiggingMessageHandler extends MessageHandler<DiggingMessage> {

	@Override
	public void handle(Session session, Player player, DiggingMessage message) {
		if(message.getState()==2){ //done digging
			int x=message.getX();
			int z=message.getZ();
			int chunkX=x/Chunk.WIDTH+((x<0&&x%16!=0)?-1:0);
			int chunkZ=z/Chunk.HEIGHT+((z<0&&z%16!=0)?-1:0);
			Chunk chunk=session.getServer().getWorld().getChunks().getChunk(chunkX, chunkZ);
			int localX=(x-(chunk.getX()*Chunk.WIDTH))%Chunk.WIDTH;
			int localZ=(z-(chunk.getZ()*Chunk.HEIGHT))%Chunk.HEIGHT;
			chunk.setType(localX, localZ, message.getY(), 0); //set to air
			BlockChangeMessage bcmsg=new BlockChangeMessage(x, message.getY(), z, 0, 0);
			for(Player p: player.getWorld().getPlayers()){
				p.getSession().send(bcmsg);
			}
		}
	}

}

