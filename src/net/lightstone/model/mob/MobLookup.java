package net.lightstone.model.mob;
import net.lightstone.model.mob.ai.*;
import java.util.*;
public class MobLookup{
	public static final int TYPE_CREEPER = 50;
	public static final int TYPE_SKELETON = 51;
	public static final int TYPE_SPIDER = 52;
	public static final int TYPE_GIANT = 53;
	public static final int TYPE_ZOMBIE = 54;
	public static final int TYPE_SLIME = 55;
	public static final int TYPE_GHAST = 56;
	public static final int TYPE_ZOMBIE_PIGMAN = 57;
	public static final int TYPE_PIG = 90;
	public static final int TYPE_SHEEP = 91;
	public static final int TYPE_COW = 92;
	public static final int TYPE_CHICKEN = 93;
	public static final int TYPE_SQUID = 94;
	public static final int TYPE_WOLF = 95;
	private static Map<Integer, String> mobMap = new HashMap<Integer, String>();
	static {
		mobMap.put(new Integer(TYPE_CREEPER), "Creeper");
		mobMap.put(new Integer(TYPE_SKELETON), "Skeleton");
		mobMap.put(new Integer(TYPE_SPIDER), "Spider");
		mobMap.put(new Integer(TYPE_GIANT), "Giant");
		mobMap.put(new Integer(TYPE_ZOMBIE), "Zombie");
		mobMap.put(new Integer(TYPE_SLIME), "Slime");
		mobMap.put(new Integer(TYPE_GHAST), "Ghast");
		mobMap.put(new Integer(TYPE_ZOMBIE_PIGMAN), "PigZombie");
		mobMap.put(new Integer(TYPE_PIG), "Pig");
		mobMap.put(new Integer(TYPE_SHEEP), "Sheep");
		mobMap.put(new Integer(TYPE_COW), "Cow");
		mobMap.put(new Integer(TYPE_CHICKEN), "Chicken");
		mobMap.put(new Integer(TYPE_WOLF), "Wolf");

	}

	public static String idToName(int id){
		return mobMap.get(new Integer(id));
	}
	public static int nameToId(String name, boolean caseSensitive) throws IllegalArgumentException{
		for(Map.Entry<Integer, String> entry: mobMap.entrySet()){
			if(entry.getValue().equals(name) || 
				(!caseSensitive && entry.getValue().toLowerCase().equals(name.toLowerCase()))){
				return entry.getKey().intValue();
			}
		}
		throw new IllegalArgumentException("name " + name + " not found in mobMap");
	}
		
	public static Class getAIOfType(int type){
		return DummyAI.class;
	}
	public static Map<Integer, String> getMobMap(){
		return Collections.unmodifiableMap(mobMap);
	}
}
