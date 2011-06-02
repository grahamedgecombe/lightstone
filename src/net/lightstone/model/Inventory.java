package net.lightstone.model;

import net.lightstone.msg.SetWindowSlotMessage;

/** Represents a player's inventory.
 * @author Zhuowei Zhang
 */

public class Inventory{
	public static final int NUM_SLOTS = 36;
	public static final int NUM_ARMOUR_SLOTS = 4;
	//TODO: Add support for smaller stacks for e.g. snowballs
	public static final int MAX_STACK = 64;

	private Player player;
	public Inventory(Player player){
		this.player=player;
	}

	private Item[] slots = new Item[NUM_SLOTS + NUM_ARMOUR_SLOTS];
	/** Adds the item to the inventory, filling existing stacks first. */
	public boolean add(Item item){
		int count = item.getCount();
		int id = item.getId();
		int damage = item.getDamage();
		boolean success=false;
		for(int i=0;i<NUM_SLOTS;i++){
			Item cur=get(i);
			if(cur!=null&&cur.getId() == id && cur.getDamage() == damage &&
				cur.getCount() <= MAX_STACK){
				Item oldItem = cur;
				int canAddNum = MAX_STACK - oldItem.getCount();
				int newNum;
				if(count<=canAddNum){
					newNum=oldItem.getCount() + count;
					success=true;
				}
				else{
					newNum = MAX_STACK;
					count-=canAddNum;
				}
				Item newItem = new Item(oldItem.getId(), newNum, oldItem.getDamage());
				set(i, newItem);
			}
			if(success){
				return true;
			}
		}
		//add a new slot
		for(int i=0;i<NUM_SLOTS;i++){
			Item cur=get(i);
			if(cur==null){
				if(count<=MAX_STACK){
					set(i, new Item(id, count, damage));
					return true;
				}
				else{
					set(i, new Item(id, MAX_STACK, damage));
					count-=MAX_STACK;
				}
			}
		}		
		return false;
	}
	public Item get(int slot){
		return slots[slot];
	}

	public void set(int slot, Item item){
		slots[slot]=item;
		if(item == null){
			player.getSession().send(new SetWindowSlotMessage(SetWindowSlotMessage.ID_INVENTORY, slot));
		}
		else{
			player.getSession().send(new SetWindowSlotMessage(SetWindowSlotMessage.ID_INVENTORY, slotToWindow(slot), item.getId(), 
				item.getCount(), item.getDamage()));
		}
	}

	public void use(int slot, int count){
		Item oldItem = get(slot);
		int newCount = oldItem.getCount() - count;
		Item newItem;
		if(newCount <= 0){
			newItem = null;
		}
		else{
			newItem = new Item(oldItem.getId(), newCount, oldItem.getDamage());
		}
		set(slot, newItem);
	}
	public static int slotToWindow(int slot){
		if(slot<10){
			return 36 + slot;
		}
		if(slot<NUM_SLOTS){
			return slot;
		}
		if(slot<NUM_SLOTS+NUM_ARMOUR_SLOTS){
			return slot -32;
		}
		return -1;
	}
}
