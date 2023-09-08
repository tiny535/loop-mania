package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Entity;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.main.Shop;
import unsw.loopmania.map.PathPosition;

public class LoopManiaWorldTest {

    private List<Pair<Integer, Integer>> orderedPath;
    private Character character;
    private Inventory inventory; 
    private LoopManiaWorld world; 
    List<Class<? extends Item>> rareItems;

    @BeforeEach
    public void setup() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        this.character = new Character(new PathPosition(0, orderedPath));

        GameMode gameMode = GameMode.NORMAL;
        this.world = new LoopManiaWorld(10, 10, orderedPath, gameMode);

        this.world.setCharacter(character);
        this.inventory = character.getInventory();
        this.rareItems = new ArrayList<>();
    }

    @Test
    @DisplayName("Test get height")
    public void getHeightTest() {
        assertEquals(world.getHeight(), 10);
    }

    @Test
    @DisplayName("Reward xp")
    public void rewardXPTest() {
        int prevXP = character.getXPQuantity();

        world.rewardXP(100);

        assertEquals(prevXP + 100, character.getXPQuantity());
    }
    
    @Test
    @DisplayName("Reward gold")
    public void rewardGoldTest() {
        int prevGold = character.getGoldQuantity();

        world.rewardGold(100);

        assertEquals(prevGold + 100, character.getGoldQuantity());
    }

    @Test
    @DisplayName("Test if character is alive at the start of game")
    public void characterAliveAtGameStartTest() {
        assertTrue(world.isCharacterAlive());
    }
    
    @Test
    @DisplayName("Test if character is alive at the start of game")
    public void characterDeadAfterDamageTakenTest() {
        character.takeDamage(200);
        assertTrue(!world.isCharacterAlive());
    }

    private List<Pair<Integer, Integer>> getTempOrderedPath() {
        List<Pair<Integer, Integer>> tempOrderedPath = new ArrayList<>();
        tempOrderedPath.add(new Pair<Integer, Integer>(0,0));
        
        // Sets a simple path loop
        int height = world.getHeight();
        int width = world.getWidth();
        for (int y = 0; y < height; y++){
            if (y == 0) {
                for (int x = 0; x < width; x++) {
                    tempOrderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            } else if (y == height - 1) {
                for (int x = width - 1; x >= 0; x--) {
                    tempOrderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            } else {
                tempOrderedPath.add(new Pair<Integer, Integer>(width - 1,y));
            }
        }

        // The first column is the last to be added to the path in reverse
        for (int y = height - 1; y >= 0; y--){
            tempOrderedPath.add(new Pair<Integer, Integer>(0,y));
        }
        
        return tempOrderedPath;
    }

    @Test
    @DisplayName("Test character defeating enemies in battle")
    public void characterInBattleTest() {
        orderedPath = getTempOrderedPath();

        Entity slug = new SlugEnemy(new PathPosition(2, orderedPath));

        world.addEnemy((BasicEnemy) slug);

        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        defeatedEnemies.add((BasicEnemy) slug);

        assertEquals(defeatedEnemies, world.runBattles());
    }

    @Test
    @DisplayName("Test printing state")
    public void printStateTest() {
        String newLine = System.getProperty("line.separator");
        String state =  String.join(
            newLine
            , "---World State---"
            , "Cycle: 0"
            , "Gold : 100"
            , "XP   : 0"
            , "HP   : 100"
            , "Lvl  : 1"
            , "EnHp : []" 
            , "-----------------"
            );

        assertEquals(state, world.worldState());
    }

    @Test
    @DisplayName("Adding item to space with unequipped items via the world")
    public void addUnequippedItemsToInventoryWithSpaceTest() {
        // public Item addUnequippedItem(Class<? extends Item> itemType){  
        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        world.addUnequippedItem(oneRing.getClass());
        
        assertTrue(inventory.inventoryContainsOneRing());
    }
    
    @Test
    @DisplayName("Adding item to full unequipped inventory via the world")
    public void addUnequippedItemsToInventoryWithoutSpaceTest() {
        world.addUnequippedItem(OneRing.class);
        for (int i = 0; i < Inventory.UNEQUIPPED_LIMIT; i++) {
            // Simulate how the world controller adds items
            Item shield = world.addUnequippedItem(Shield.class);
            if (shield == null){
                world.convertOldestItem();
                world.addUnequippedItem(Shield.class);
            }
        }
        assertTrue(!inventory.inventoryContainsOneRing());
    }

    @Test
    @DisplayName("Removing unequipped inventory item by coordinates")
    public void removingUnequippedInventoryItemByCoordinatesTest() {
        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        world.addUnequippedItem(oneRing.getClass());
        
        assertTrue(inventory.inventoryContainsOneRing());

        world.removeUnequippedInventoryItemByCoordinates(0, 0);

        assertTrue(!inventory.inventoryContainsOneRing());
    }

    @Test
    @DisplayName("Removing unequipped inventory item by coordinates")
    public void addEntityTest() {
        orderedPath = getTempOrderedPath();

        Entity slug = new SlugEnemy(new PathPosition(2, orderedPath));
        world.addEntity(slug);
    }

    @Test
    @DisplayName("Test that Entity's BooleanProperty shouldExist returns true") 
    public void entityShouldExistTest() {
        orderedPath = getTempOrderedPath();

        Entity slug = new SlugEnemy(new PathPosition(2, orderedPath));
        world.addEntity(slug);

        assertTrue(slug.shouldExist().getValue());
    }

    @Test
    @DisplayName("Test that doggie coin value changes after a cycle") 
    public void doggieCoinValueChangesAfterCycleTest() {
        int doggieCoinValue = world.getDoggieCoinValue();
        world.runTickMoves();

        assertTrue(doggieCoinValue != world.getDoggieCoinValue());
    }

    @Test
    @DisplayName("Get class of unequipped item in inventory test") 
    public void getClassOfUnequippedItemTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        assertEquals(world.getUnequippedItemByClass(Stake.class), stake);
    }

    @Test
    @DisplayName("Get class of equipped item in inventory test should fail") 
    public void getClassOfEquippedItemTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        character.equipItem(stake);

        assertEquals(world.getUnequippedItemByClass(Stake.class), null);
    }

    @Test
    @DisplayName("Get class of item not in inventory test") 
    public void getClassOfItemNotInInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        assertEquals(world.getUnequippedItemByClass(Sword.class), null);
    }

    @Test
    @DisplayName("Removing equipped item")
    public void removeEquippedItemTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        character.equipItem(stake);

        world.removeEquippedItem(stake);
        assertTrue(character.isInventoryEmpty());
    }

    @Test
    @DisplayName("Consuming health potion that is in the inventory test")
    public void consumeHealthPotionInInventoryTest() {
        Item healthPotion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(healthPotion, character);

        character.takeDamage(90);
        world.characterConsume(healthPotion);

        assertTrue(character.getHp() == 70);
    }

    @Test
    @DisplayName("Consume health potion not in the inventory test")
    public void consumeHealthPotionNotInInventoryTest() {
        Item healthPotion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        character.takeDamage(90);
        world.characterConsume(healthPotion);

        assertTrue(character.getHp() == 10);
    }

    @Test
    @DisplayName("Try to consume item that is not a health potion test")
    public void consumeEquippableItemTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);

        character.takeDamage(90);
        world.characterConsume(stake);

        assertTrue(character.getHp() == 10);
    }

    // Resetting the world 
    @Test
    @DisplayName("Testing that the world is reset after calling the LoopManiaWorld reset method")
    public void resetWorldTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);
        character.equipItem(staff);

        assertTrue(!inventory.isInventoryEmpty());
        assertTrue(inventory.getEquippedItems().size() == 1);

        Ally ally = new Ally(new SimpleIntegerProperty(), new SimpleIntegerProperty()); 
        character.addPartyMember(ally);
        assertTrue(!character.isPartyEmpty());

        // VampireEnemy vampire = new VampireEnemy(new PathPosition(1, orderedPath));
        // world.addEnemy(vampire);
        // System.out.println(world.getEnemies());

        character.addGold(10000);
        character.gainExperience(10000);
        assertTrue(character.getGoldQuantity() == 10100);
        assertTrue(character.getXPQuantity() == 10000);

        VillageBuilding villageBuilding = new VillageBuilding();
        Card villageCard = new Card(new SimpleIntegerProperty(), new SimpleIntegerProperty(), villageBuilding);
        character.addCard(villageCard);
        assertTrue(!character.isDeckEmpty());

        world.resetWorld();

        assertTrue(inventory.isInventoryEmpty());
        assertTrue(character.isPartyEmpty());
        assertTrue(character.isDeckEmpty());
        assertTrue(character.getXPQuantity() == 0);
    }

    @Test
    @DisplayName("Testing Hero Castle and Shop time")
    public void testHeroCastle() {
        HeroCastleBuilding castle = new HeroCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setHeroCastle(castle);
        assertEquals(world.getHeroCastle(), castle);

        Shop shop = new Shop(character, LoopManiaWorld.getGameMode(), world.getDoggieCoinValue(), rareItems);
        world.setShop(shop);
        assertEquals(world.getShop(), shop);

        assertTrue(!world.isItShopTime());
    }

    // Testing Shop 
    @Test
    @DisplayName("Testing cycle methods for shop are working")
    public void testShop() {
        Shop shop = new Shop(character, LoopManiaWorld.getGameMode(), world.getDoggieCoinValue(), rareItems);
        world.setShop(shop);

        assertEquals(shop.getCharacter(), character); 
        assertEquals(1, shop.getCyclesToNextShop());   
        shop.incrementCyclesToNextShop();
        assertEquals(2, shop.getCyclesToNextShop());   
    }

    // Get equipped item coordinates 
    @Test
    @DisplayName("Testing that equipped items return their correct coordinates")
    public void testEquippedItemsCoordinates() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        character.equipItem(stake);

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);

        assertEquals(world.getEquippedItemByCoordinate(0, 0), stake);
    }
}
