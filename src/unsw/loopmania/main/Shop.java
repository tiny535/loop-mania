package unsw.loopmania.main;

import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Potion;

public class Shop {

    private static final int STARTING_CYCLES = 1;
    private static final int STARTING_NEXT_CYCLE = 1;
    
    private Character character; 
    private GameMode gameMode;

    private int hpPotsPurchased;
    private int protectiveGearsPurchased;

    private int doggieCoinValue;

    private int cyclesToNextShop;
    private int nextShopCycle;
    private List<Item> boughtItems = new ArrayList<Item>();
    private List<Class<? extends Item>> rareItems;

    public Shop(Character character, GameMode gameMode, int doggieCoinValue, List<Class<? extends Item>> rareItems) {
        this.character = character;
        this.gameMode = LoopManiaWorld.getGameMode();
        reset(doggieCoinValue);
        this.rareItems = rareItems;
    }
    
    /**
     * Reset the shop to it's starting state
     * @param doggieCoinValue
     */
    public void reset(int doggieCoinValue) {
        this.doggieCoinValue = doggieCoinValue;
        this.cyclesToNextShop = STARTING_CYCLES;
        this.nextShopCycle = STARTING_NEXT_CYCLE;
        this.hpPotsPurchased = 0;
        this.protectiveGearsPurchased = 0;
        emptyBoughtItems();
    }

    /**
     * Buys an item if the character has space in their inventory and can afford it. 
     * @param character
     * @param item
     */
    public void buyItem(Item item) {
        Inventory inventory = character.getInventory();
        this.gameMode = LoopManiaWorld.getGameMode();
        // Return is the character's inventory is full or they do not have enough gold
        if (inventory.unequippedInventoryIsFull() || 
            inventory.getGoldQuantity() < item.getBuyPrice()) return; 

        // Return if the character can not buy the item due to the gamemode
        if (!canBuyItemByGameMode(item)) return;

        if (!canBuyItemRare(item)) return;
        inventory.addItemToInventory(item, character);
        boughtItems.add(item);
        inventory.removeGold(item.getBuyPrice());
    }

    private boolean canBuyItemRare(Item item){
        if (!item.isRareItem()) return true;
        return this.rareItems.contains(item.getClass());
    }

    /**
     * Checks whether or not the item is purchasable due to the gameMode
     * @param item
     * @return
     */
    private boolean canBuyItemByGameMode(Item item){
        switch (this.gameMode){
            case BERSERKER:
                if (item.isProtectiveItem()){
                    return berserkerCondition();
                } 
                return true;
            case SURVIVAL:
                if (isHpPotion(item)){
                    return survivalCondition();
                }
                return true;
            default:
                return true;
        }
    }

    /**
     * Checks if the character has already bough a piece of protective gear in this shop
     * @return
     */
    private boolean berserkerCondition(){
        if (this.protectiveGearsPurchased < 1){
            this.protectiveGearsPurchased++;
            return true;
        }
        return false;
    }

    private boolean isHpPotion(Item item){
        return item instanceof Potion;
    }

    /**
     * Checks if the character has already bought a hp potion in this shop
     * @return
     */
    private boolean survivalCondition(){
        if (this.hpPotsPurchased < 1){
            this.hpPotsPurchased++;
            return true;
        }
        return false;
    }

    /**
     * Sells an item that is in the character's inventory. The item can be 
     * equipped or unequipped. Gold will be given to the character for the trade. 
     * @param character
     * @param item
     */
    public void sellItem(Item item) {
        Inventory inventory = character.getInventory();

        // In case we sold an item we just bought remove it from the bought items
        if (boughtItems.contains(item)) {
            boughtItems.remove(item);
            return;
        }

        if (!inventory.containsItem(item)) return;

        Gold gold = new Gold(new SimpleIntegerProperty(), new SimpleIntegerProperty(), item.getSellPrice());
        inventory.removeItemFromInventory(item);
        item.destroy();
        inventory.addGold(gold);
    }

    /**
     * Sells the specified quantity of DoggieCoins. The gold received by the sale will depend on the current 
     * Doggie Coin Value. 
     * If the inventory contains less than the quantity of DoggieCoins intended to be sold, nothing will happen. 
     * @param quantity of DoggieCoins to be sold 
     */
    public void sellDoggieCoin(int quantity) {
        Inventory inventory = character.getInventory();
        
        if (quantity > inventory.getDoggieCoinQuantity()) {
            return; 
        }

        Gold gold = new Gold(new SimpleIntegerProperty(), new SimpleIntegerProperty(), doggieCoinValue * quantity);
        inventory.removeDoggieCoin(quantity);
        inventory.addGold(gold);
    }

    public void incrementCyclesToNextShop() {
        this.nextShopCycle += cyclesToNextShop;
        cyclesToNextShop++;
    }

    public int getCyclesToNextShop() {
        return cyclesToNextShop;
    }

    public Character getCharacter() {
        return this.character;
    }

    public int getNextShopCycle() {
        return this.nextShopCycle;
    }

    public int getDoggieCoinValue() {
        return this.doggieCoinValue;
    }

    public List<Item> getBoughtItems() {
        return boughtItems;
    }

    public void emptyBoughtItems() {
        this.boughtItems = new ArrayList<Item>();
    }

    public void changeDoggieCoinValue(double multiplier) {
        this.doggieCoinValue *= multiplier; 
    }
}
