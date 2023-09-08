package unsw.loopmania.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.javatuples.Pair;

import unsw.loopmania.battle.Fighter;
import unsw.loopmania.item.DoggieCoin;
import unsw.loopmania.item.EquipableItem;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.SpecialEffectAttack;
import unsw.loopmania.item.SpecialEffectDefence;
import unsw.loopmania.sorter.SortByEquippedItem;

public class Inventory {

    public static final int INVENTORY_WIDTH = 4;
    public static final int INVENTORY_HEIGHT = 4;
    public static final int INVENTORY_LIMIT = 20; 
    public static final int EQUIPPED_LIMIT = 4; 
    public static final int UNEQUIPPED_LIMIT = Inventory.INVENTORY_HEIGHT * Inventory.INVENTORY_WIDTH; 
    
    private List<Item> equippedItems; 
    private List<Item> unequippedItems; 
    private int gold; 
    private int doggieCoin; 

    public Inventory() {
        this.equippedItems = new ArrayList<Item>(); 
        this.unequippedItems = new ArrayList<Item>(); 
        // TODO: revise this to account for starting gold based on game modes 
        // (e.g. berserker mode = 50 pieces of gold to start)
        this.gold = 0;  
        this.doggieCoin = 0;
    }

    /**
     * Reset the inventory to be empty. Destroys all previous items.
     */
    public void empty() {
        gold = 0;
        doggieCoin = 0;
        for (Item e : equippedItems) e.destroy();
        equippedItems.clear();
        for (Item e : unequippedItems) e.destroy();
        unequippedItems.clear();
    }

    /**
     * Determine how much gold is in this inventory
     * @return int gold
     */
    public int getGoldQuantity() {
        return this.gold;
    }

    /**
     * Adds gold to the character's gold collection in their inventory
     * @param gold - to add
     */
    public void addGold(Gold gold) {
        this.gold += gold.getGoldQuantity(); 
        gold.destroy();
    }

    /**
     * Adds an amount of gold to the character's gold in their inventory
     * @param gold - amount to add
     */
    public void addGold(int gold) {
        this.gold += gold; 
    }

    /**
     * Remove a quantity of gold from a characters inventory
     * @param quantity - to remove
     */
    public void removeGold(int quantity) {
        this.gold -= quantity; 
    }

    public int getDoggieCoinQuantity() {
        return this.doggieCoin;
    }

    public void addDoggieCoin(DoggieCoin doggieCoin) {
        this.doggieCoin += doggieCoin.getDoggieCoinQuantity(); 
    }

    public void addDoggieCoin(int doggieCoin) {
        this.doggieCoin += doggieCoin;
    }

    public void removeDoggieCoin(int doggieCoin) {
        this.doggieCoin -= doggieCoin; 
    }

    public List<Item> getEquippedItems() {
        return this.equippedItems;
    }

    public List<Item> getUnequippedItems() {
        return this.unequippedItems;
    }

    /**
     * Check if unequipped inventory is full
     * @return true if full
     */
    public boolean unequippedInventoryIsFull() {
        return this.unequippedItems.size() == UNEQUIPPED_LIMIT;
    }

    /**
     * Check if equipped inventory is full
     * @return true if full
     */
    public boolean equippedInventoryIsFull() {
        return this.equippedItems.size() == EQUIPPED_LIMIT;
    }

    public boolean isInventoryEmpty(){
        return this.unequippedItems.isEmpty() && this.equippedItems.isEmpty();
    }

    /**
     * Check if item exists in the inventory
     * @return true if exists
     */
    public boolean containsItem(Item item) {
        return unequippedItems.contains(item) || equippedItems.contains(item);
    }

    /**
     * Check if a Shield is equipped by the player 
     * @return true if Shield is equipped
     */
    public boolean isShieldEquipped() {
        for (Item equippedItem : this.equippedItems) {
            if (equippedItem instanceof Shield) {
                return true; 
            }
        }

        return false; 
    }

    /**
     * Given an Entity item adds it to the unequipped inventory
     * @param item
     */
    public void addItemToInventory(Item item, Character player) {
        Pair<Integer, Integer> gridLocation = getFirstAvailableSlotForItem();
        item.updateCoordinates(gridLocation.getValue0(), gridLocation.getValue1());
        unequippedItems.add(item);
    }

    /**
     * Given the item, removes the item from the inventory from the unequipped items first. 
     * If the item is equipped, it will unequip the item and remove it from the inventory. 
     * @param item
     */
    public void removeItemFromInventory(Item item) {
        // If the item is not in the inventory, do not change state
        if (!unequippedItems.contains(item) && !equippedItems.contains(item)) {
            return; 
        }
        
        // Remove from the unequipped items first 
        if (unequippedItems.contains(item)) {
            unequippedItems.remove(item);

            // Collections.sort(unequippedItems, new SortByUnequippedItem());

            // // Shuffle all items back
            // int currScore = SortByUnequippedItem.calculateItemCoordinateScore(item);
            // for (Item updatingItem : unequippedItems) {
            //     int newScore = SortByUnequippedItem.calculateItemCoordinateScore(updatingItem);
            //     if (newScore > currScore) {
            //         Pair<Integer, Integer> newLocation = getFirstAvailableSlotForItem();
            //         System.out.println("\n\n\n\n" + updatingItem + " \t" + newLocation + "\n\n\n\n");
            //         updatingItem.updateCoordinates(newLocation.getValue0(), newLocation.getValue1());
            //         currScore = newScore;
            //     }
            // }
        }

        // Remove from the equipped items
        if (equippedItems.contains(item)) {
            unequipItem(item);
            unequippedItems.remove(item);
        }
    }

    /**
     * Equip an item from the unequipped items in the inventory 
     * @param item
     */
    public void equipItem(Item item) {
        // If the item is not in the inventory, or equippedItems is full, or if item is not an equipableItem
        if (!unequippedItems.contains(item) || equippedInventoryIsFull() || 
            !(item instanceof EquipableItem)) {
            return; 
        } 

        equippedItems.add(item);
        unequippedItems.remove(item); 

    }

    /**
     * Unequip an item and add it back to the list of unequipped items in the inventory
     * If the item is not already equipped, or the unequipped inventory is full, the state of 
     * the inventory does not change. 
     * @param item
     */
    public void unequipItem(Item item) {
        // If the item is not equipped
        if (!equippedItems.contains(item) || unequippedInventoryIsFull()) {
            return; 
        }

        unequippedItems.add(item);
        equippedItems.remove(item); 
    }

    /**
     * Remove item from unequipped inventory based on its index
     * @param index - of item to remove in the list, 0..size-1
     * indexes should be based on age
     */
    public void removeItemByPositionInUnequippedInventoryItems(int index) {
        Entity item = this.unequippedItems.get(index);
        item.destroy();
        this.unequippedItems.remove(index);
    }

    /**
     * Get the first available slot an item is able to be placed into on a gridpane
     * @return
     */
    public Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y = 0; y < INVENTORY_HEIGHT; y++){
            for (int x = 0; x < INVENTORY_WIDTH; x++){
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Given the items coordinates on gridpane retrieves item
     * @param x - x coordinate of inventory item on gridpane
     * @param y - y coordinate of inventory item on gridpane
     * @return
     */
    public Item getUnequippedInventoryItemEntityByCoordinates(int x, int y) {
        for (Item e: this.unequippedItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * Given the class of item we are searching for look and 
     * return the first reference of that object in unequipped inventory.
     * @param itemClass - Class of item we want
     * @return
     */
    public Item getUnequippedItemByClass(Class<? extends Item> itemClass) {
        for (Item e: this.unequippedItems) {
            if (e.getClass() == itemClass) return e;
        }
        return null;
    }

    public void removeEquippedItem(Item item){
        item.destroy();
        this.equippedItems.remove(item);
    }

    public Item getEquippedItemByCoordinate(int x, int y){
        for (Item e: this.equippedItems){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * Apply the combined effect of all equipped items on a characters attack
     * @return the damage to be added
     */
    public int applyEquippedAttacking(){
        int characterAttack = 0; 

        for (Item equippedItem : this.equippedItems) {
            characterAttack += ((EquipableItem) equippedItem).getAttackEffect();
        }

        return characterAttack;
    }

    /**
     * Apply the combined effect of all equipped items on a characters attack
     * @return the new damage the enemy will deal
     */
    public int applyEquippedProtection(int enemyDamage){
        // Sorts the equipped items 
        Comparator<Item> compareByEquippedItemType = new SortByEquippedItem();
        Collections.sort(this.equippedItems, compareByEquippedItemType);

        for (Item equippedItem : this.equippedItems) {
            enemyDamage = ((EquipableItem) equippedItem).getDefenceEffect(enemyDamage);
        }

        // If the enemyDamage is negative, the enemyDamage dealt is 0
        return enemyDamage > 0 ? enemyDamage : 0;
    }

    // TODO: 
    public void applyEquippedSpecialAttack(Fighter fighter, long seed) {
        for (Item equippedItem : this.equippedItems) {
            if (equippedItem instanceof SpecialEffectAttack) {
                ((SpecialEffectAttack) equippedItem).applySpecialEffectAttack(fighter);
            }
        }
    }

    public int applyEquippedSpecialDefence(Fighter fighter, int damage) {
        int newDamage = damage;
        for (Item equippedItem : this.equippedItems) {
            if (equippedItem instanceof SpecialEffectDefence) {
                newDamage = ((SpecialEffectDefence) equippedItem).applySpecialEffectDefence(fighter, damage);
            }
        }

        return newDamage;
    }

    /**
     * If there is a oneRing in the character's inventory, return the health 
     * recovered by consuming the oneRing. 
     * @return value of HP recovered
     */
    public int consumeOneRing() {
        OneRing oneRing; 
        for (Item unequippedItem : this.unequippedItems) {
            if (unequippedItem instanceof OneRing) {
                oneRing = (OneRing) unequippedItem; 
                
                this.unequippedItems.remove(oneRing);

                return oneRing.consumeItem();
            }
        }

        return 0;
    }

    /**
     * If there is a potion in the character's inventory, return the health 
     * recovered by consuming the potion. 
     * @return value of HP recovered
     */
    public int consumeHealthPotion() {
        Potion potion; 
        for (Item unequippedItem : this.unequippedItems) {
            if (unequippedItem instanceof Potion) {
                potion = (Potion) unequippedItem; 
                
                this.unequippedItems.remove(potion);
                potion.destroy();

                return potion.consumeItem();
            }
        }

        return 0;
    }

    /**
     * Determines if the inventory contains the OneRing. 
     * @return true if it contains the OneRing
     */
    public boolean inventoryContainsOneRing() {
        for (Item unequippedItem : this.unequippedItems) {
            if (unequippedItem instanceof OneRing) {
                return true; 
            }
        }

        return false; 
    }

}

