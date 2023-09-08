package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;

import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.DoggieCoin;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.Gold;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.main.Shop;
import unsw.loopmania.map.PathPosition;

public class BasicInventoryTest {

    private Inventory inventory;
    private Character character;
    private Shop shop;
    private List<Pair<Integer, Integer>> orderedPath;
    List<Class<? extends Item>> rareItems;

    @BeforeEach
    public void setup() {

        orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        this.character = new Character(new PathPosition(0, orderedPath));

        this.inventory = character.getInventory();
        this.rareItems = new ArrayList<>();
        this.shop = new Shop(this.character, GameMode.NORMAL, DoggieCoin.STARTING_DOGGIE_COIN_VALUE, rareItems);
    }

    private boolean unequippedInventoryContains(Item item) {
        return inventory.getUnequippedItems().contains(item);
    }

    private boolean equippedInventoryContains(Item item) {
        return inventory.getEquippedItems().contains(item);
    }

    private boolean inventoryIsEmpty() {
        return inventory.getUnequippedItems().isEmpty() && inventory.getEquippedItems().isEmpty();
    }

    private boolean unequippedInventoryIsEmpty() {
        return inventory.getUnequippedItems().isEmpty();
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Starting State

    @Test
    @DisplayName("Test equipped and unequipped inventory is empty when starting")
    public void startWithEmptyInventory() {
        assertTrue(inventory.getUnequippedItems().isEmpty());
        assertTrue(inventory.getEquippedItems().isEmpty());
        assertEquals(inventory.isInventoryEmpty(), true);
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Adding and Removing Items from Inventory

    @Test
    @DisplayName("Test adding one item to unequipped inventory with space and doesn't equip item")
    public void addOneItemToInventoryWithSpaceTest() {

        // Add sword
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(sword, character);

        // sword is in unequipped inventory
        assertTrue(unequippedInventoryContains(sword));
        assertTrue(!equippedInventoryContains(sword));
        assertEquals(inventory.isInventoryEmpty(), false);
    }

    @Test
    @DisplayName("Test adding multiple items of the same type to unequipped inventory with space and doesn't equip item")
    public void addMultipleSameItemToInventoryWithSpaceTest() {

        // Add 10 swords
        for (int i = 0; i < 10; i++) {
            Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(sword, character);
        }

        // All swords are in unequipped inventory
        assertEquals(10, inventory.getUnequippedItems().size());

    }

    @Test
    @DisplayName("Test adding multiple items to unequipped inventory with space and doesn't equip item")
    public void addMultipleItemsToInventoryWithSpaceTest() {

        // Sword, Staff, Stake, Armour, Helmet, Shield, One Ring, Potion

        // Staff
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);

        assertTrue(unequippedInventoryContains(staff));
        assertTrue(!equippedInventoryContains(staff));

        // Stake
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        assertTrue(unequippedInventoryContains(stake));
        assertTrue(!equippedInventoryContains(stake));

        // Armour
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);

        assertTrue(unequippedInventoryContains(armour));
        assertTrue(!equippedInventoryContains(armour));

        // Helmet
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);

        assertTrue(unequippedInventoryContains(helmet));
        assertTrue(!equippedInventoryContains(helmet));

        // Shield
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);

        assertTrue(unequippedInventoryContains(shield));
        assertTrue(!equippedInventoryContains(shield));

        // One ring
        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);

        assertTrue(unequippedInventoryContains(oneRing));
        assertTrue(!equippedInventoryContains(oneRing));

        // Potion
        Item potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(potion, character);

        assertTrue(unequippedInventoryContains(potion));
        assertTrue(!equippedInventoryContains(potion));
    }

    @Test
    @DisplayName("Add an item to unequipped inventory without space")
    public void addItemToFullInventoryTest() {

        LoopManiaWorld world = new LoopManiaWorld(0, 0, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        // Starting gold and xp
        int prevGold = character.getGoldQuantity();
        int prevXP = character.getXPQuantity();

        // Add what will be the oldest item
        Item oldest = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oldest, character);

        // Fill inventory
        for (int i = 1; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(stake, character);
        }

        // Add new item to full unequipped inventory
        //Item newStake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        //inventory.addItemToInventory(newStake, character);
        Item newStake = world.addUnequippedItem(Stake.class);
        // Simulate the way the world controller adds items
        if (newStake == null){
            world.convertOldestItem();
            newStake = world.addUnequippedItem(Stake.class);
        }

        // Check both inventory states are consistent
        assertTrue(unequippedInventoryContains(newStake));
        assertTrue(!equippedInventoryContains(newStake));

        assertTrue(!unequippedInventoryContains(oldest));
        assertTrue(!equippedInventoryContains(oldest));

        // Ending gold and xp
        int gold = character.getGoldQuantity();
        int xp = character.getXPQuantity();

        // Check final amount of gold and experience increases
        // Gold = previous amount of gold + the gold gained by the conversion of the oldest item
        // XP = previous amount of xp + the xp gained by the conversion of the oldest item
        // Gold gained = buy price / 2
        // XP gained = gold gained / 5
        assertTrue(gold == prevGold + 150 / 2);
        assertTrue(xp == prevXP + 75 / 5 );

    }

    @Test
    @DisplayName("Remove item not in inventory")
    public void removeItemNotInInventoryTest() {
        // Add item
        Item item = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        // Remove item and check is not longer in inventory
        inventory.removeItemFromInventory(item);
        assertTrue(!unequippedInventoryContains(item));
        assertTrue(!equippedInventoryContains(item));
    }

    @Test
    @DisplayName("Remove an item from unequipped inventory")
    public void removeUnequippedItemTest() {
        // Add item
        Item item = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(item, character);

        // Remove item and check is not longer in inventory
        inventory.removeItemFromInventory(item);
        assertTrue(!unequippedInventoryContains(item));
        assertTrue(!equippedInventoryContains(item));
    }

    @Test
    @DisplayName("Remove multiple items from unequipped inventory")
    public void removeMultipleUnequippedItemTest() {
        // Fill up inventory with items
        List<Item> addedItems = new ArrayList<>();
        for (int i = 0; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            Item item = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(item, character);
            addedItems.add(item);
        }

        // Remove all items and check is not longer in inventory
        for (int i = 0; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            Item toRemove = addedItems.get(i);
            inventory.removeItemFromInventory(toRemove);
            assertTrue(!unequippedInventoryContains(toRemove));
            assertTrue(!equippedInventoryContains(toRemove));
        }

        assertTrue(inventoryIsEmpty());

    }

    // Note: There is no case we would need to remove an equipped item

///////////////////////////////////////////////////////////////////////////////////////
//// Equipping

    @Test
    @DisplayName("Equipping item that is not part of the inventory")
    public void equipItemNotInInventoryTest() {
        Item item = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        inventory.equipItem(item);

        assertTrue(!equippedInventoryContains(item));
        assertTrue(!unequippedInventoryContains(item));
    }

    @Test
    @DisplayName("Equipping item in unequipped inventory")
    public void equippingItemInUnequippedInventoryTest() {
        Item item = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(item, character);
        inventory.equipItem(item);

        assertTrue(equippedInventoryContains(item));
        assertTrue(!unequippedInventoryContains(item));
    }

    @Test
    @DisplayName("Equipping multiple items on different body parts")
    public void equipMultipleItemsOnDifferentBodyParts() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        assertTrue(inventory.equippedInventoryIsFull());
        assertTrue(equippedInventoryContains(stake));
        assertTrue(equippedInventoryContains(helmet));
        assertTrue(equippedInventoryContains(armour));
        assertTrue(equippedInventoryContains(shield));
        assertTrue(unequippedInventoryIsEmpty());
    }

    @Test
    @DisplayName("Equipping an Andruil Sword")
    public void equippingAndruilSword() {

        Item andurilSword = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(andurilSword, character);
        inventory.equipItem(andurilSword);

        assertTrue(equippedInventoryContains(andurilSword));
        assertTrue(!unequippedInventoryContains(andurilSword));
    }

    @Test
    @DisplayName("Equipping protective item that is already equipped for the same body part")
    public void equippingTreeStump() {
        
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(treeStump, character);
        inventory.equipItem(treeStump);

        assertTrue(equippedInventoryContains(treeStump));
        assertTrue(!unequippedInventoryContains(treeStump));
    }

    @Test
    @DisplayName("Cannot equipconsumable items")
    public void notEquipConsumableItemsTest() {
        Item potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(potion, character);
        inventory.equipItem(potion);

        assertTrue(!equippedInventoryContains(potion));
        assertTrue(unequippedInventoryContains(potion));

        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);
        inventory.equipItem(oneRing);

        assertTrue(!equippedInventoryContains(oneRing));
        assertTrue(unequippedInventoryContains(oneRing));
    }


///////////////////////////////////////////////////////////////////////////////////////
//// UnEquipping

    @Test
    @DisplayName("Unequip an item that is not in the inventory")
    public void unequipItemNotInInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.equipItem(stake);

        assertTrue(inventoryIsEmpty());
    }

    @Test
    @DisplayName("Unequip an item when there is space in inventory")
    public void unequippingOneItemIntoInventoryWithSpaceTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        inventory.unequipItem(stake);

        assertTrue(!equippedInventoryContains(stake));
        assertTrue(unequippedInventoryContains(stake));
    }

    @Test
    @DisplayName("Unequip an item when the unequiped inventory is full")
    public void unequipItemWhenUnequipedInventoryIsFullTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        assertTrue(inventory.containsItem(stake));

        // Fill up inventory
        List<Item> addedItems = new ArrayList<>();
        for (int i = 0; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            Item item = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(item, character);
            addedItems.add(item);
        }

        inventory.unequipItem(stake);

        // As the unequiped inventory is full, trying to unequip the stake should fail and not
        // result in any change in state
        assertTrue(inventory.containsItem(stake));
        assertTrue(equippedInventoryContains(stake));
        assertTrue(!unequippedInventoryContains(stake));
    }

    @Test
    @DisplayName("Unequipping item makes it the newest item")
    public void equipThenUnequipItemToMakeItNewestItemTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        // Fill up inventory
        List<Item> addedItems = new ArrayList<>();
        for (int i = 1; i < Inventory.UNEQUIPPED_LIMIT - 1; i++) {
            Item item = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(item, character);
            addedItems.add(item);
        }

        inventory.equipItem(stake);
        inventory.unequipItem(stake);

        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);

        // As the stake was the newest uneqiped item, it should not be converted to gold/XP and
        // shoudl still exist in the unequiped inventory
        assertTrue(!equippedInventoryContains(stake));
        assertTrue(unequippedInventoryContains(stake));

    }

    @Test
    @DisplayName("Unequipping and removing from character action")
    public void inventoryChangesThroughCharacterWrapperTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        assertTrue(equippedInventoryContains(stake));
        character.unequipItem(stake);
        assertTrue(unequippedInventoryContains(stake));
        character.removeItemFromInventory(stake);
        assertTrue(!unequippedInventoryContains(stake));
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Buying and Selling

    @Test
    @DisplayName("Buying an item when there is no space in unequipped inventory")
    public void buyingItemWhenInventoryIsFullTest() {
        List<Item> addedItems = new ArrayList<>();
        for (int i = 0; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
            inventory.addItemToInventory(sword, character);
            addedItems.add(sword);
        }

        Gold gold = new Gold(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 200);
        character.addGold(gold);
        int prevGold = character.getGoldQuantity();

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        shop.buyItem(helmet);

        assertTrue(!unequippedInventoryContains(helmet));
        int currentGold = character.getGoldQuantity();
        assertTrue(prevGold == currentGold);
    }

    @Test
    @DisplayName("Buying an item when there is space in unequipped inventory")
    public void buyingItemWhenInventoryIsNotFullTest() {
        Gold gold = new Gold(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 200);
        character.addGold(gold);
        int prevGold = character.getGoldQuantity();

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        shop.buyItem(helmet);

        assertTrue(unequippedInventoryContains(helmet));

        // Ensure gold decreases by the item's sale price
        int currentGold = character.getGoldQuantity();
        assertTrue(currentGold == prevGold - helmet.getBuyPrice());
    }

    @Test
    @DisplayName("Buying an item that cannot be afforded")
    public void buyingItemWhenCannotAffordTest() {
        int prevGold = character.getGoldQuantity();

        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        shop.buyItem(oneRing);

        // Should not change state of inventory or gold quantity
        assertTrue(inventoryIsEmpty());
        int currentGold = character.getGoldQuantity();
        assertTrue(currentGold == prevGold);
    }

    @Test
    @DisplayName("Buying two helmets in berserker gameMode")
    public void buyingDuplicateProtectiveGearBerserkerMode(){
        LoopManiaWorld.setGameMode(GameMode.BERSERKER);
        Shop newShop = new Shop(character, GameMode.BERSERKER, DoggieCoin.STARTING_DOGGIE_COIN_VALUE, rareItems);

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        character.addGold(2 * helmet.getBuyPrice());

        newShop.buyItem(helmet);

        assertTrue(unequippedInventoryContains(helmet));

        Item newHelmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        newShop.buyItem(newHelmet);

        assertTrue(!unequippedInventoryContains(newHelmet));
    }

    @Test
    @DisplayName("Buying two different protective items in berserker gameMode")
    public void buyingDifferentProtectiveGearBerserkerMode(){
        LoopManiaWorld.setGameMode(GameMode.BERSERKER);
        Shop newShop = new Shop(character, GameMode.BERSERKER, DoggieCoin.STARTING_DOGGIE_COIN_VALUE, rareItems);

        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        character.addGold(shield.getBuyPrice() + armour.getBuyPrice());

        newShop.buyItem(shield);
        assertTrue(newShop.getBoughtItems().contains(shield));
        assertTrue(unequippedInventoryContains(shield));

        newShop.buyItem(armour);

        assertTrue(!unequippedInventoryContains(armour));
    }

    @Test
    @DisplayName("Buying two hp potions in survival gameMode")
    public void buyingMultipleHpPotionsSurvivalMode(){
        LoopManiaWorld.setGameMode(GameMode.SURVIVAL);
        Shop newShop = new Shop(character, GameMode.SURVIVAL, DoggieCoin.STARTING_DOGGIE_COIN_VALUE, rareItems);

        Item potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        character.addGold(2 * potion.getBuyPrice());

        newShop.buyItem(potion);

        assertTrue(unequippedInventoryContains(potion));

        Item newpotion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        newShop.buyItem(newpotion);

        assertTrue(!unequippedInventoryContains(newpotion));
    }

    @Test
    @DisplayName("Selling an item not in unequipped inventory")
    public void sellingItemNotInInventoryTest() {
        int prevGold = character.getGoldQuantity();
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        shop.sellItem(stake);

        assertTrue(!unequippedInventoryContains(stake));
        int currentGold = character.getGoldQuantity();
        assertTrue(currentGold == prevGold);
    }

    @Test
    @DisplayName("Selling an item from unequipped inventory")
    public void sellingItemFromUnequippedInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        int prevGold = character.getGoldQuantity();

        shop.sellItem(stake);

        // Some amount of gold is recieved equivalent to items sale price
        // Removed from inventory (equipped or unequipped)
        assertTrue(!unequippedInventoryContains(stake));
        int currentGold = character.getGoldQuantity();
        assertTrue(currentGold == prevGold + stake.getSellPrice());
    }

    @Test
    @DisplayName("Selling an item from equipped inventory")
    public void sellingItemFromEquippedInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        int prevGold = character.getGoldQuantity();

        shop.sellItem(stake);

        assertTrue(!equippedInventoryContains(stake));
        assertTrue(!unequippedInventoryContains(stake));

        // Some amount of gold is recieved equivalent to items sale price
        int currentGold = character.getGoldQuantity();
        assertTrue(currentGold == prevGold + stake.getSellPrice());
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Specialised methods

    @Test
    @DisplayName("Returns true when a shield is equipped")
    public void sheildIsEquippedTest() {
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        assertTrue(inventory.isShieldEquipped());
    }

    @Test
    @DisplayName("Knows no shield equipped when there is nothing is equipped")
    public void equippedIsEmptyNoSheildTest() {
        assertTrue(!inventory.isShieldEquipped());
    }

    @Test
    @DisplayName("Returns false when a shield is not equipped")
    public void shieldIsNotEquippedTest() {
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        assertTrue(!inventory.isShieldEquipped());
    }


///////////////////////////////////////////////////////////////////////////////////////
//// Get first available slot
    @Test
    @DisplayName("If no items in inventory, first available slot should be 0, 0")
    public void getFirstAvailableSlotWithEmptyInventoryTest() {
        Pair<Integer, Integer> firstAvailableSlot = new Pair<Integer, Integer>(0, 0);

        assertEquals(firstAvailableSlot, inventory.getFirstAvailableSlotForItem());
    }

    @Test
    @DisplayName("If items in inventory first available slot will not be 0, 0")
    public void getFirstAvailableSlotWithItemsInInventoryTest() {
        Item sword = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        inventory.addItemToInventory(sword, character);
        Item staff = new Staff(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        inventory.addItemToInventory(staff, character);

        Pair<Integer, Integer> firstAvailableSlot = new Pair<Integer, Integer>(2, 0);

        assertEquals(firstAvailableSlot, inventory.getFirstAvailableSlotForItem());
    }

    @Test
    @DisplayName("If items in full unequipped inventory, first available slot should be null")
    public void getFirstAvailableSlotWithFullUnequippedInventoryTest() {
        List<Item> addedItems = new ArrayList<>();
        for (int y = 0; y < Inventory.INVENTORY_HEIGHT; y++) {
            for (int x = 0; x < Inventory.INVENTORY_WIDTH; x++) {
                Item sword = new Sword(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
                inventory.addItemToInventory(sword, character);
                addedItems.add(sword);
            }
        }

        assertEquals(null, inventory.getFirstAvailableSlotForItem());
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Get unequipped inventory item entity by coordinates 
    @Test
    @DisplayName("If item exists in unequipped inventory, coordinates should be returned")
    public void getUnequippedInventoryItemByCoordinatesTest() {
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(sword, character);

        assertEquals(sword, inventory.getUnequippedInventoryItemEntityByCoordinates(0, 0));
    }

    @Test
    @DisplayName("If unequipped inventory is empty, null should be returned")
    public void getEmptyUnequippedInventoryItemByCoordinatesTest() {
        assertEquals(null, inventory.getUnequippedInventoryItemEntityByCoordinates(0, 0));
    }

    @Test
    @DisplayName("If coordinates is not populated by item in unequipped inventory, null should be returned")
    public void getItemNotInUnequippedInventoryByCoordinatesTest() {
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(sword, character);

        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);

        assertEquals(null, inventory.getUnequippedInventoryItemEntityByCoordinates(2, 0));
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Doggie Coins
    @Test
    @DisplayName("Starting quantity of doggie coins is zero")
    public void startingDoggieCoinQuantityTest() {
        assertEquals(inventory.getDoggieCoinQuantity(), 0);
    }

    @Test
    @DisplayName("Add doggie coins to the inventory")
    public void addDoggieCoinToInventoryTest() {
        inventory.addDoggieCoin(1);
        assertEquals(inventory.getDoggieCoinQuantity(), 1);

        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 1);
        inventory.addDoggieCoin(doggieCoin);
        assertEquals(inventory.getDoggieCoinQuantity(), 2);
    }

    @Test
    @DisplayName("Removing doggie coins from the inventory")
    public void removeDoggieCoinFromInventoryTest() {
        inventory.addDoggieCoin(5);
        assertEquals(inventory.getDoggieCoinQuantity(), 5);

        inventory.removeDoggieCoin(3);
        assertEquals(inventory.getDoggieCoinQuantity(), 2);
    }

    @Test
    @DisplayName("Getting value of doggie coin test")
    public void getDoggieCoinValueTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        assertEquals(world.getDoggieCoinValue(), 100);
    }

    @Test
    @DisplayName("Getting value of doggie coin after increase test")
    public void getDoggieCoinValueAfterIncreaseTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        world.changeDoggieCoinValue(2);
        assertEquals(world.getDoggieCoinValue(), 200);
    }

    @Test
    @DisplayName("Getting value of doggie coin after decrease test")
    public void getDoggieCoinValueAfterDecreaseTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        world.changeDoggieCoinValue(0.5);
        assertEquals(world.getDoggieCoinValue(), 50);
    }

    @Test
    @DisplayName("Getting value of doggie coin after fluctuation test")
    public void getDoggieCoinValueAfterFluctuationTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        world.changeDoggieCoinValue(0.5);
        assertEquals(world.getDoggieCoinValue(), 50);

        world.changeDoggieCoinValue(20);
        assertEquals(world.getDoggieCoinValue(), 1000);
    }

    @Test
    @DisplayName("Selling existing quantity of doggie coin test")
    public void sellingValidQuantityOfDoggieCoinTest() {
        inventory.addDoggieCoin(5);
        shop.sellDoggieCoin(10);
        assertEquals(inventory.getDoggieCoinQuantity(), 5);
    }

    @Test
    @DisplayName("Selling non-existing quantity of doggie coin test")
    public void sellingInvalidQuantityOfDoggieCoinTest() {
        inventory.addDoggieCoin(10);
        shop.sellDoggieCoin(7);
        assertEquals(inventory.getDoggieCoinQuantity(), 3);
    }

    @Test
    @DisplayName("Testing gold received for sale of doggie coin with no value fluctuations")
    public void goldReceivedForDoggieCoinSaleWithNoFluctuationsTest() {
        inventory.addDoggieCoin(10);
        shop.sellDoggieCoin(7);

        assertEquals(inventory.getGoldQuantity(), 700);
    }

    @Test
    @DisplayName("Testing gold received for sale of doggie coin after doggie coin value increase")
    public void goldReceivedForDoggieCoinSaleWithValueIncreaseTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 10);
        inventory.addDoggieCoin(doggieCoin);
        world.changeDoggieCoinValue(2);

        Shop newShop = new Shop(character, GameMode.NORMAL, world.getDoggieCoinValue(), rareItems);
        newShop.sellDoggieCoin(10);
        assertEquals(inventory.getGoldQuantity(), 2100);
    }

    @Test
    @DisplayName("Testing gold received for sale of doggie coin after doggie coin value decrease")
    public void goldReceivedForDoggieCoinSaleWithValueDecreaseTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 10);
        inventory.addDoggieCoin(doggieCoin);
        world.changeDoggieCoinValue(0.2);

        Shop newShop = new Shop(character, GameMode.NORMAL, world.getDoggieCoinValue(),rareItems);
        newShop.sellDoggieCoin(10);

        assertEquals(300, inventory.getGoldQuantity());
    }

    @Test
    @DisplayName("Testing gold received for sale of doggie coin after doggie coin value fluctuations")
    public void goldReceivedForDoggieCoinSaleWithValueFluctuationTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        world.setCharacter(character);
        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 10);
        inventory.addDoggieCoin(doggieCoin);
        
        world.changeDoggieCoinValue(2);
        Shop newShop1 = new Shop(character, GameMode.NORMAL, world.getDoggieCoinValue(),rareItems);
        newShop1.sellDoggieCoin(5);
        assertEquals(inventory.getGoldQuantity(), 1100);

        world.changeDoggieCoinValue(0.25);
        Shop newShop2 = new Shop(character, GameMode.NORMAL, world.getDoggieCoinValue(),rareItems);
        newShop2.sellDoggieCoin(2);
        assertEquals(inventory.getGoldQuantity(), 1200);
    }

    @Test
    @DisplayName("Testing doggie coin value in the shop")
    public void doggieCoinValueInShopTest() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(character);
        Shop shop = world.getShop(); 

        assertTrue(shop.getDoggieCoinValue() == 100);
        
        world.changeDoggieCoinValue(5);

        assertTrue(world.getShop().getDoggieCoinValue() == 500);
    }

    // Testing rare items
    @Test
    @DisplayName("Testing rare items method in items")
    public void isRareItemsTest() {
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item andurilSword = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        assertTrue(!sword.isRareItem());
        assertTrue(oneRing.isRareItem());
        assertTrue(treeStump.isRareItem());
        assertTrue(andurilSword.isRareItem());
    }
}