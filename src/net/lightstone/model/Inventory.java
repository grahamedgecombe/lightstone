package net.lightstone.model;

import net.lightstone.msg.SetWindowSlotMessage;

/**
 * Represents a player's inventory.
 * @author Zhuowei Zhang
 */

public class Inventory {

	/**
	 * The number of slots in a player's inventory.
	 */
	public static final int NUM_SLOTS = 36;

	public static final int NUM_ARMOUR_SLOTS = 4;

	//TODO: Add support for smaller stacks for e.g. snowballs
	public static final int MAX_STACK = 64;

	/**
	 * The player associated with this inventory.
	 */
	private final Player player;

	/**
	 * The items contained in this inventory.
	 */
	private Item[] slots = new Item[NUM_SLOTS + NUM_ARMOUR_SLOTS];

	/** Creates an inventory for the specified player.
	 * @param player The player that owns this inventory.
	 */
	public Inventory(Player player){
		this.player=player;
	}

	/**
	 * Adds the item to the inventory, filling existing stacks first.
	 * @param item The item to add.
	 */
	public boolean add(Item item) {
		int count = item.getCount();
		int id = item.getId();
		int damage = item.getDamage();
		boolean success=false;
		for (int i = 0; i< NUM_SLOTS; i++) {
			Item cur=get(i);
			if (cur!=null && cur.getId() == id && cur.getDamage() == damage &&
				cur.getCount() <= MAX_STACK) {
				Item oldItem = cur;
				int canAddNum = MAX_STACK - oldItem.getCount();
				int newNum;
				if (count <= canAddNum) {
					newNum=oldItem.getCount() + count;
					success=true;
				} else {
					newNum = MAX_STACK;
					count-=canAddNum;
				}
				Item newItem = new Item(oldItem.getId(), newNum, oldItem.getDamage());
				set(i, newItem);
			}
			if (success) {
				return true;
			}
		}
		//add a new slot
		for (int i = 0; i < NUM_SLOTS; i++) {
			Item cur=get(i);
			if (cur == null) {
				if (count <= MAX_STACK) {
					set(i, new Item(id, count, damage));
					return true;
				} else {
					set(i, new Item(id, MAX_STACK, damage));
					count-=MAX_STACK;
				}
			}
		}		
		return false;
	}
	/**
	 * Gets the item at the selected slot.
	 * @param slot The slot of the item.
	 * @return The Item at the slot.
	 */
	public Item get(int slot) {
		return slots[slot];
	}

	/**
	 * Sets the item at the selected slot and notify the client about the change.
	 * @param slot The slot to place the item into.
	 * @param item The new Item.
	 */
	public void set(int slot, Item item) {
		slots[slot]=item;
		if(item == null) {
			player.getSession().send(new SetWindowSlotMessage(SetWindowSlotMessage.ID_INVENTORY, slot));
		} else {
			player.getSession().send(new SetWindowSlotMessage(SetWindowSlotMessage.ID_INVENTORY, slotToWindow(slot), item.getId(), 
				item.getCount(), item.getDamage()));
		}
	}
	/**
	 * Reduce the amount of an item in a slot.
	 * @param slot The slot to decrement from.
	 * @param count The amount to decrease.
	 */
	public void use(int slot, int count) {
		Item oldItem = get(slot);
		int newCount = oldItem.getCount() - count;
		Item newItem;
		if(newCount <= 0){
			newItem = null;
		} else {
			newItem = new Item(oldItem.getId(), newCount, oldItem.getDamage());
		}
		set(slot, newItem);
	}
	/**
	 * Converts a slot number to a window index for a Minecraft inventory window.
	 * @param slot The slot number.
	 * @return The window index.
	 */
	public static int slotToWindow(int slot) {
		if (slot < 10) {
			return 36 + slot;
		}
		if (slot < NUM_SLOTS) {
			return slot;
		}
		if (slot < NUM_SLOTS + NUM_ARMOUR_SLOTS) {
			return slot -32;
		}
		return -1;
	}
}
