package net.lightstone.model.mob.ai;
import net.lightstone.model.Mob;
public abstract class AI{
	private Mob mob;
	public AI(Mob mob){
		this.mob = mob;
	}
	public Mob getMob(){
		return mob;
	}
	public abstract void pulse();
}
