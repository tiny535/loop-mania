package unsw.loopmania.main;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.battle.Battle;
import unsw.loopmania.battle.DropTable;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.goal.GoalNode;
import unsw.loopmania.item.DoggieCoin;
import unsw.loopmania.item.Item;
import unsw.loopmania.map.PathPosition;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * list of goals for the character to complete
     */
    private GoalNode endGoal;
    
    /**
     * Gamemode the player is playing in for this world
     */
    private static GameMode gameMode = GameMode.NORMAL;

    /**
     * Number of cycles the character has travelled in this world
     */
    private int cycleCount;

    /**
     * The player character - our protagonist
     */
    private Character character;

    /**
     * The player's inventory in this world
     */
    private Inventory inventory;

    /**
     * The shop in this world
     */
    private Shop shop;

    /**
     * The Hero Castle Building in this world
     */
    private HeroCastleBuilding heroCastleBuilding;
    
    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;
    
    /**
     * List of enemies in the world
     */
    private List<BasicEnemy> enemies;

    /**
     * List of buildings in the world
     */
    private List<Building> buildingEntities;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    /**
     * The Classes of Rare Items that are available in this world
     */
    private List<Class<? extends Item>> rareItemClasses;

    /**
     * Boolean to make sure the world only has one doggie at a time
     */
    private boolean isDoggieSpawnedThisCycle;

    /**
     * Boolean to make sure the world only has one ElanMuske at a time
     */
    private boolean isElanMuskeSpawnedThisCycle;

    /**
     * DoggieCoinValue starts with a value of 100 gold coins. 
     */
    private int doggieCoinValue; 

    /**
     * Boolean for if Doggie Boss is defeated.
     */
    private boolean doggieBossDefeated;

    /**
     * Boolean for if Elan Muske Boss is defeated.
     */
    private boolean elanMuskeBossDefeated;

    /**
     * The number of ticks the game has been alive for.
     */
    private int numTicks;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath, GameMode newGameMode) {
        this.width = width;
        this.height = height;
        this.orderedPath = orderedPath;
        
        this.endGoal = null;
        gameMode = newGameMode;
        
        this.numTicks = 0;

        nonSpecifiedEntities = new ArrayList<>();
        enemies = new ArrayList<>();
        buildingEntities = new ArrayList<>();
        rareItemClasses = new ArrayList<>();

        this.doggieCoinValue = DoggieCoin.STARTING_DOGGIE_COIN_VALUE; 
        this.doggieBossDefeated = false; 
        this.elanMuskeBossDefeated = false; 

        character = null;
        inventory = null;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    public static void setGameMode(GameMode newGameMode){
        gameMode = newGameMode;
    }

    /**
     * Get the goals as a goal node to display on the frontend
     * @return
     */
    public GoalNode getGoals(){
        return this.endGoal;
    }

    /**
     * Check is the game is won each cycle
     * @return
     */
    public boolean isGameWon() {
        return this.endGoal.isGoalMet(this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
        this.character.addGold(Character.STARTING_GOLD);
        this.inventory = character.getInventory();
        this.shop = new Shop(this.character, gameMode, this.doggieCoinValue, this.rareItemClasses);
    }

    /**
     * Set the goal of the game after the world has been created
     * @param goal - high level encompassing goal
     */
    public void setEndGoal(GoalNode goal){
        this.endGoal = goal;
    }

    /**
     * Sets the available rare items in this world
     * @param rareItemClasses - a list of item classes available 
     */
    public void setRareItems(List<Class<? extends Item>> rareItemClasses) {
        this.rareItemClasses = rareItemClasses;
    }

    /**
     * Returns the list of rare items available in this world
     * Dependent on a condition to unlock the chance to recieve the rare item
     * @param enemiesKilled - number of enemies killed in the most recent battle
     * @return
     */
    public List<Class<? extends Item>> getRareItemsAvailable(int enemiesKilled) {
        return getRareItemsAvailable(enemiesKilled, System.currentTimeMillis());
    }

    /**
     * Returns the list of rare items available in this world
     * @param enemiesKilled
     * @param seed - to determine the players luck
     * @return
     */
    public List<Class<? extends Item>> getRareItemsAvailable(int enemiesKilled, long seed) {
        DropTable dropTable = new DropTable();
        
        List<Class<? extends Item>> potentialDrops = new ArrayList<>();
        if (isRareItemAvailable(enemiesKilled)) {
            for (Class<? extends Item> rareItemClass: this.rareItemClasses) {
                if (dropTable.doesRareItemDrop(rareItemClass, seed)) {
                    potentialDrops.add(rareItemClass);
                    break;
                }
            }
        }
        return potentialDrops;
    }

    /**
     * Checks if rare item is unlocked to be dropped
     * @param enemiesKilled - dependent on enemies killed
     * @return
     */
    private boolean isRareItemAvailable(int enemiesKilled) {
        return enemiesKilled >= DropTable.KILLS_TO_UNLOCK_RARE_ITEM;
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        nonSpecifiedEntities.add(entity);
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies(){
        Pair<Integer, Integer> pos = possiblyGetBasicEnemySpawnPosition();
        List<BasicEnemy> spawningEnemies = new ArrayList<>();

        if (pos != null){
            int indexInPath = orderedPath.indexOf(pos);

            // Spawning slugs
            BasicEnemy enemy = new SlugEnemy(new PathPosition(indexInPath, orderedPath));
            addEnemy(enemy);
            spawningEnemies.add(enemy);
        }

        // Spawning vampires and zombies
        for (Building b : buildingEntities) {
            BasicEnemy enemy = b.spawnEnemy(cycleCount, orderedPath);
            if (enemy != null) {
                addEnemy(enemy);
                spawningEnemies.add(enemy);
            }
        }

        // Possibly spawn boss enemies
        List<BasicEnemy> bossEnemiesToSpawn = spawnBossEnemies();
        if (bossEnemiesToSpawn != null){
            for (BasicEnemy e: bossEnemiesToSpawn){
                addEnemy(e);
            }
            spawningEnemies.addAll(bossEnemiesToSpawn);
        }

        return spawningEnemies;
    }

    /**
     * Spawns all non-building spawning enemeies if they can spawn
     * @param indexInPath
     * @return list of enemies to spawn
     */
    private List<BasicEnemy> spawnBossEnemies(){
        List<BasicEnemy> enemiesToSpawn = new ArrayList<>();

        int indexInPath = orderedPath.indexOf(getEnemySpawnPosition());

        DoggieEnemy doggie = spawnDoggie(indexInPath);
        if (doggie != null)
            enemiesToSpawn.add(doggie);
        
        
        indexInPath = orderedPath.indexOf(getEnemySpawnPosition());
        ElanMuskeEnemy elanMuske = spawnElanMuskeEnemy(indexInPath);
        if (elanMuske != null) {
            enemiesToSpawn.add(elanMuske);
        }

        return enemiesToSpawn;
    }

    private DoggieEnemy spawnDoggie(int indexInPath){
        if (this.cycleCount % DoggieEnemy.SPAWNABLE_CYCLE == 0 && this.cycleCount != 0 && 
            !this.isDoggieSpawnedThisCycle){
            this.isDoggieSpawnedThisCycle = true;
            return new DoggieEnemy(new PathPosition(indexInPath, this.orderedPath));
        }
        return null;
    }

    private ElanMuskeEnemy spawnElanMuskeEnemy(int indexInPath){
        if (this.cycleCount % ElanMuskeEnemy.SPAWNABLE_CYCLE == 0 && this.cycleCount != 0 && 
            character.getXPQuantity() >= ElanMuskeEnemy.XP_REQUIRED_TO_SPAWN && 
            !this.isElanMuskeSpawnedThisCycle){
                this.isElanMuskeSpawnedThisCycle = true;
                changeDoggieCoinValue(5);
                return new ElanMuskeEnemy(new PathPosition(indexInPath, orderedPath));
            }
        return null;
    }

    /**
     * Add an enemy to the world - used for testing purposes.
     * @param enemy
     */
    public void addEnemy(BasicEnemy enemy) {
        enemies.add(enemy);
        enemy.setLevel(cycleCount);
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(BasicEnemy enemy){
        enemy.destroy();
        enemies.remove(enemy);

        if (enemy instanceof DoggieEnemy) {
            this.doggieBossDefeated = true; 

            Random random = new Random();
            int numCoinsDropped = random.nextInt(4) + 1;
            addDoggieCoin(numCoinsDropped);
        } else if (enemy instanceof ElanMuskeEnemy) {
            this.elanMuskeBossDefeated = true; 

            changeDoggieCoinValue(0.2);
        }
    }

    /**
     * run the expected battles in the world, based on current world state
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles() {
        Battle battle = new Battle();
        battle.setCharacter(this.character);
        battle.setBattleEnemies(this.enemies);
        battle.setBuildings(this.buildingEntities);
        List<BasicEnemy> defeatedEnemies = battle.runBattleLoop();

        for (BasicEnemy e: defeatedEnemies){
            killEnemy(e);
        }

        return defeatedEnemies;
    }

    /**
     * After a battle reward the player xp
     * @param xpReward - experience to add
     */
    public void rewardXP(int xpReward) {
        this.character.gainExperience(xpReward);
    }

    /**
     * After a battle reward the player gold
     * @param goldReward - gold to add
     */
    public void rewardGold(int goldReward) {
        this.character.addGold(goldReward);
    }

    /**
     * Checks if the character is alive or not
     * @return
     */
    public boolean isCharacterAlive(){
        return this.character.getHp() > 0;
    }

    /**
     * spawn a card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(Building type){
        Card card = new Card(new SimpleIntegerProperty(character.getDeckSize()), new SimpleIntegerProperty(0), type);
        character.addCard(card);
        return card;
    }

    public Card addCard(Class<? extends Building> buildingClass){
        if (character.isDeckFull()) return null;

        try {
            Constructor<? extends Building> builder = buildingClass.getDeclaredConstructor();
            Building type = (Building) builder.newInstance();
            Card card = loadCard(type);
            return card;
        } catch (NoSuchMethodException e) {
            System.err.println("Building is missing declared constructor: " + e);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public List<Class<? extends Item>> convertOldestCard(){
        Card oldestCard = character.getOldestCard();
        character.removeCard(oldestCard);
        Random rnd = new Random();
        int chance = rnd.nextInt(Card.MAX_BOUND);
        if (chance < Card.XP_CHANCE){
            this.character.gainExperience(oldestCard.getXPDrop());
        } else if (chance < (Card.XP_CHANCE + Card.GOLD_CHANCE)){
            this.character.addGold(oldestCard.getGoldDrop());
        } else {
            List<Class<? extends Item>> itemTypes = oldestCard.getItemDrops();
            return itemTypes;
        }
        return null;
    }

    /**
     * spawn a item in the world and return the item entity
     * @return a item to be spawned in the controller as a JavaFX node
     */
    public Item addUnequippedItem(Class<? extends Item> itemType){  
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (character.isUnequippedInventoryFull()) return null;
        
        // now we insert the new item, as we know we have at least made a slot available...
        try {
            Constructor<? extends Item> builder = itemType.getDeclaredConstructor(SimpleIntegerProperty.class, SimpleIntegerProperty.class);
            Item item = builder.newInstance(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
            character.addItemToInventory(item);
            return item;
        } catch (NoSuchMethodException nme) {
            System.out.println("No such construction for class " + itemType + " | Exception: " + nme);
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    /**
     * Converts the oldest item in a player's inventory to gold and xp
     */
    public void convertOldestItem(){
        List<Item> unequippedItems = character.getUnequippedItems();
        Item oldestItem = unequippedItems.get(0);
        character.addGold(oldestItem.getConversionPrice());
        character.gainExperience(oldestItem.getConversionXP());
        removeUnequippedInventoryItem(oldestItem);
    }

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y){
        Item item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    public void removeEquippedItem(Item item){
        character.removeEquippedItem(item);
    }

    public Item getEquippedItemByCoordinate(int x, int y){
        return character.getEquippedItemByCoordinate(x, y);
    }

    public void equipItemByCoordinate(int x, int y){
        Item item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        character.equipItem(item);
    }

    public Item getUnequippedItemByClass(Class<? extends Item> itemClass) {
        return character.getUnequippedItemByClass(itemClass);
    }

    public void characterConsume(Item item) {
        this.character.consumeItem(item);
    } 

    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        character.moveDownPath();
        moveBasicEnemies();
        this.numTicks++;
        if (isCycleCompleted()){
            this.cycleCount++;
            this.isDoggieSpawnedThisCycle = false;
            this.isElanMuskeSpawnedThisCycle = false;

            // Change value of doggie coin in this cycle
            Random random = new Random();
            if (random.nextInt(10) > 5) {
                changeDoggieCoinValue(random.nextDouble() + 0.1);
            } else {
                changeDoggieCoinValue(random.nextInt(5) + 0.1);
            }

            levelUpEnemies();
        } 
    }

    private void levelUpEnemies(){
        for (BasicEnemy e: this.enemies){
            e.incrementLevel();
            e.levelUp();
        }
    }

    /**
     * Check if we've made it to the next cycle
     * @return true if we have, false otherwise
     */
    private boolean isCycleCompleted() {
        return (this.numTicks % orderedPath.size() == 0);
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Item item){
        item.destroy();
        inventory.removeItemFromInventory((Item)item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    public Item getUnequippedInventoryItemEntityByCoordinates(int x, int y){
        return inventory.getUnequippedInventoryItemEntityByCoordinates(x, y);
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        return inventory.getFirstAvailableSlotForItem();
    }

    /**
     * move all enemies
     */
    private void moveBasicEnemies() {
        // TODO = expand to more types of enemy
        for (BasicEnemy e: enemies){
            if (e instanceof VampireEnemy)
                ((VampireEnemy)e).move(this);
            e.move();
        }
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * @return null if random choice is that wont be spawning an enemy or it isn't possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetBasicEnemySpawnPosition(){
        // TODO = modify this
        
        // has a chance spawning a basic enemy on a tile the character isn't on or immediately before or after (currently space required = 2)...
        Random rand = new Random();
        int choice = rand.nextInt(2); // TODO = change based on spec... currently low value for dev purposes...
        // TODO = change based on spec
        if ((choice == 0) && (enemies.size() < 2)){
            return getEnemySpawnPosition();
        }
        return null;
    }

    private Pair<Integer, Integer> getEnemySpawnPosition(){

        Random rand = new Random();

        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        // inclusive start and exclusive end of range of positions not allowed
        int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 3)%orderedPath.size();
        // note terminating condition has to be != rather than < since wrap around...
        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // choose random choice
        Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));

        return spawnPosition;
    }

    /**
     * remove a card by its x, y coordinates
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = character.getCardByCordinates(cardNodeX, cardNodeY);
        
        if (card == null) {
            return null;
        }

        // now spawn building
        Building newBuilding = card.getBuilding();
        if (!newBuilding.canPlaceBuilding(buildingNodeX, buildingNodeY,
                            this.buildingEntities, this.orderedPath)) {
            return null;
        }
        newBuilding.updateCoordinates(buildingNodeX, buildingNodeY);
        newBuilding.setCycleSpawn(cycleCount);
        buildingEntities.add(newBuilding);

        // destroy the card
        character.removeCard(card);

        return newBuilding;
    }

    /**
     * Getter for buildingEntities
     * @return
     */
    public List<Building> getBuildings() {
        return buildingEntities;
    }

    /**
     * Getter for orderedPath
     * @return
     */
    public List<Pair<Integer, Integer>> getOrderedPath() {
        return orderedPath;
    }
    
    /**
     * Setter for cycleCount
     * @param cycleCount
     */
    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    public Character getCharacter(){
        return this.character;
    }

    public List<BasicEnemy> getEnemies() {
        return this.enemies;
    }

    /**
     * Checks if character is currently on a village tile.
     * If character is on a village tile, heals character and party.
     */
    public void possiblyApplyVillageTile() {
        for (Building b : buildingEntities) {
            if (b.isSameCoordinate(character.getX(), character.getY()) && b instanceof VillageBuilding) {
                    VillageBuilding building = (VillageBuilding) b;
                    building.healCharacter(character);
            }
        }
    }

    /**
     * Checks if character is currently on a barracks tile.
     * If character is on a barracks tile, creates and returns an ally.
     * If no ally was added or could not be added, returns null.
     * @return
     */
    // TODO: fix. Ally probably doesn't need to be returned if allies are represented by int
    public Ally possiblyApplyBarracksTile() {
        for (Building b : buildingEntities) {
            if (b.isSameCoordinate(character.getX(), character.getY()) && b instanceof BarracksBuilding) {
                BarracksBuilding building = (BarracksBuilding) b;
                Ally ally = building.produceAlly(new SimpleIntegerProperty(), new SimpleIntegerProperty(), character.getParty());
                if (ally != null) {
                    character.addPartyMember(ally);
                }
                return ally;
            }
        }
        return null;
    }

    /**
     * Checks if there is an enemy currently on each trap tile.
     * If an enemy is on a trap tile, deals 15 damage to enemy.
     * If enemy taking damage has 15 HP or less, enemy will be killed. The enemy killed will be added to a list and returned.
     * @return
     */
    public List<BasicEnemy> possiblyApplyTrapTile() {
        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        List<Building> destroyedTraps = new ArrayList<Building>();

        for (Building b : buildingEntities) {
            if (b instanceof TrapBuilding) {
                TrapBuilding building = (TrapBuilding) b;
                BasicEnemy e = building.possiblyTriggerTrap(enemies);
                if (e != null) {
                    if (e.getHp() <= TrapBuilding.TRAP_DAMAGE) {
                        defeatedEnemies.add(e);
                    } else {
                        e.takeDamage(TrapBuilding.TRAP_DAMAGE);
                    }
                    destroyedTraps.add(b);
                }
            }
        }

        for (Building b : destroyedTraps) {
            destroyBuilding(b);
        }

        for (BasicEnemy e: defeatedEnemies){
            killEnemy(e);
        }
        return defeatedEnemies;
    }

    /**
     * destroy a building
     * @param building building to be destroyed
     */
    private void destroyBuilding(Building building){
        building.destroy();
        buildingEntities.remove(building);
    }


/////////////////////////////////////////////////////////////////////
// Goal relevant questions

    /**
     * Get the current gold count in the world owned by the character
     * @return an integer count
     */
    public int getGoldCount() {
        return this.character.getGoldQuantity();
    }
    
    /**
     * Get the current xp count in the world experienced by the character
     * @return an integer count
     */
    public int getXPCount() {
        return this.character.getXPQuantity();
    }

    /**
     * Get the current cycle count in the world travelled by the character
     * @return an integer count
     */
    public int getCycleCount() {
        return this.cycleCount;
    }

    public String worldState() {
        String newLine = System.getProperty("line.separator");
        return String.join(
            newLine
            , "---World State---"
            , "Cycle: " + getCycleCount()
            , "Gold : " + getGoldCount()
            , "XP   : " + getXPCount()
            , "HP   : " + getCharacter().getHp()
            , "Lvl  : " + getCharacter().getLevel()
            , "EnHp : " + getEnemyHps()
            , "-----------------"
            );
    }

    private List<Integer> getEnemyHps(){
        List<Integer> hps = new ArrayList<>();
        for (BasicEnemy e: this.enemies){
            hps.add(e.getHp());
        }
        return hps;
    }

    public int getDoggieCoinValue() {
        return doggieCoinValue;
    }

    /**
     * Changes the DoggieCoin value by the multiplier value
     */
    public void changeDoggieCoinValue(double multiplier) {
        doggieCoinValue *= multiplier;
        shop.changeDoggieCoinValue(multiplier);
    }

    public void addDoggieCoin(int numCoins) {
        inventory.addDoggieCoin(numCoins);
    }
    
    public boolean isDoggieDefeated() {
        return this.doggieBossDefeated;
    }

    public boolean isElanMuskeDefeated() {
        return this.elanMuskeBossDefeated;
    }

/////////////////////////////////////////////////////////////////////
// Goal relevant methods
    public void setHeroCastle(HeroCastleBuilding castle) {
        this.heroCastleBuilding = castle;
    }
    
    public HeroCastleBuilding getHeroCastle() {
        return heroCastleBuilding;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public boolean isItShopTime() {
        return isCharacterOnHeroCastle() && this.cycleCount == shop.getNextShopCycle();
    }

    private boolean isCharacterOnHeroCastle() {
        return heroCastleBuilding.isSameCoordinate(character.getX(), character.getY());
    }


/////////////////////////////////////////////////////////////////////

    /**
     * Reset the world for a new game.
     */
    public void resetWorld() {
        this.numTicks = 0;
        setCycleCount(0);

        for (BasicEnemy e : enemies) e.destroy();
        enemies.clear();
        for (Building b : buildingEntities) b.destroy();
        buildingEntities.clear();

        this.doggieCoinValue = DoggieCoin.STARTING_DOGGIE_COIN_VALUE; 
        this.doggieBossDefeated = false; 
        this.elanMuskeBossDefeated = false; 

        this.character.initialise();
        this.shop.reset(this.doggieCoinValue);
    }
}