package net.lightstone.model.mob.ai;
import net.lightstone.model.Monster;
/* a mob ai that does nothing.*/
public class DummyAI extends AI{
	public DummyAI(Monster mob){
		super(mob);
	}
	/* Do nothing. */
	public void pulse(){
	}
}
