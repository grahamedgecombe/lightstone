package net.lightstone.model.mob;
import net.lightstone.model.mob.ai.*;
import java.util.*;
public class MobLookup{
	public static final int TYPE_CREEPER = 50;
	public static final int TYPE_SKELETON = 51;
	public static final int TYPE_PIG = 90;
	private static Map<Integer, String> mobMap = new HashMap<Integer, String>();
	static {
		mobMap.put(new Integer(TYPE_CREEPER), "Creeper");
		mobMap.put(new Integer(TYPE_SKELETON), "Skeleton");
		mobMap.put(new Integer(TYPE_PIG), "Pig");
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
