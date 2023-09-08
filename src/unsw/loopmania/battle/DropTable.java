package unsw.loopmania.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.sorter.SortByCardClass;
import unsw.loopmania.sorter.SortByItemClass;

public class DropTable {

    /**
     * The range of luck a player can have
     * The worst luck you can have is 100, best is 0
     */
    public static final int LUCK_RANGE = 100;

    /**
     * The minimum number of enemies killed for a rare item to drop
     */
    public static final int KILLS_TO_UNLOCK_RARE_ITEM = 1;
    private static final Map<Class<? extends Item>, Integer> RARE_ITEM_DROP_CHANCES = new HashMap<>()
    {
        {
            put(OneRing.class, 5);
            put(TreeStump.class, 5);
            put(AndurilSword.class, 5);
        }
    };

    /**
     * Base amount of XP dropped when an enemy is killed
     */
    private static final int BASE_XP_DROP = 10;
    /**
    * Base amount of gold dropped when an enemy is killed
    */
    private static final int BASE_GOLD_DROP = 25;

    /**
    * Maximum amount of items dropped when an enemy is killed
    */
    private static final int BASE_MAX_ITEMS_DROPS = 2;
    /**
     * Map representing the base drop rate of certain items in the game
     * The lower the number, the lower the chance to gain that item
     */
    private static final Map<Class<? extends Item>, Integer> BASE_ITEM_DROP_CHANCES = new HashMap<>()
    {
        {
            put(Staff.class, 10);
            put(Armour.class, 10);
            put(Stake.class, 20);
            put(Shield.class, 20);
            put(Potion.class, 30);
            put(Helmet.class, 40);
            put(Sword.class, 50);
        }
    };;

    /**
    * Maximum amount of cards dropped when an enemy is killed
    */
    private static final int BASE_MAX_CARDS_DROPS = 1;
    /**
     * Map representing the base drop rate of certain card in the game
     * The lower the number, the lower the chance to gain that building
     */
    private static final Map<Class<? extends Building>, Integer> BASE_CARD_DROP_CHANCES = new HashMap<>()
    {
        {
            put(CampfireBuilding.class, 10);
            put(TowerBuilding.class, 10);
            put(ZombiePitBuilding.class, 20);
            put(BarracksBuilding.class, 30);
            put(TrapBuilding.class, 40);
            put(VillageBuilding.class, 50);
            put(VampireCastleBuilding.class, 50);
        }
    };

    private int xpDrop;
    private int goldDrop;
    private int maxItemDrops;
    private int maxCardDrops;
    private Map<Class<? extends Item>, Integer> itemDropChances;
    private Map<Class<? extends Building>, Integer> cardDropChances;

    public DropTable() {
        this.xpDrop = DropTable.BASE_XP_DROP;
        this.goldDrop = DropTable.BASE_GOLD_DROP;
        this.maxItemDrops = DropTable.BASE_MAX_ITEMS_DROPS;
        this.maxCardDrops = DropTable.BASE_MAX_CARDS_DROPS;
        this.itemDropChances = DropTable.BASE_ITEM_DROP_CHANCES;
        this.cardDropChances = DropTable.BASE_CARD_DROP_CHANCES;
    }

    /**
     * Get the amount of XP dropped on defeat
     * @return Integer amount of experience to gain
     */
    public int getXPDrop() {
        return this.xpDrop;
    }

    /**
     * Get the amount of gold dropped on defeat
     * @return Integer amount of gold to gain
     */
    public int getGoldDrop() {
        return this.goldDrop;
    }

    /**
     * Determines whether a rare item will drop based on it's drop table of luck and condition
     * @param rareItemClass - rare item class to check if it will drop
     * @return true if will drop, false otherwise
     */
    public boolean doesRareItemDrop(Class<? extends Item> rareItemClass) {
       return doesRareItemDrop(rareItemClass, System.currentTimeMillis());
    }

    /**
     * Determines whether a rare item will drop
     * @param rareItemClass
     * @param seed - to determine players luck
     * @return
     */
    public boolean doesRareItemDrop(Class<? extends Item> rareItemClass, long seed) {
        Random rnd = new Random(seed);
        int luck = rnd.nextInt(DropTable.LUCK_RANGE);
        return isLuckyEnough(luck, RARE_ITEM_DROP_CHANCES.get(rareItemClass));
    }

    /**
     * Get the classes of items dropped on defeat
     * Based on a randomised luck chance and the maximum amount of items that can be dropped on defeat 
     * @param luck - seed of the characters luck, the lower it is the higher chance for rare items
     * @return
     */
    public List<Class<? extends Item>> getItemDrops(long luck) {
        return getItemDrops(luck, System.currentTimeMillis());
    } 
    /**
     * Get the classes of items dropped on defeat
     * @param luck - seed for chance for a particular item to drop
     * @param shuffleSeed - seed for order to consider dropping items 
     * @return
     */
    public List<Class<? extends Item>> getItemDrops(long luck, long shuffleSeed) {
        // Randomise the order of items we'll search by
        List<Class<? extends Item>> possibleItemDrops = new ArrayList<>(BASE_ITEM_DROP_CHANCES.keySet());
        Collections.sort(possibleItemDrops, new SortByItemClass());
        possibleItemDrops = shuffleItems(possibleItemDrops, shuffleSeed);
        

        // For a random order of items, check each one and possible add it to our rewards
        List<Class<? extends Item>> itemRewards = new ArrayList<>();
        for (Class<? extends Item> itemClass : possibleItemDrops) {
            // Check we have not exceeded max amount of items
            if (itemRewards.size() >= this.maxItemDrops) break;

            // If our luck is within items acceptable range add it
            if (isLuckyEnough(luck, this.itemDropChances.get(itemClass))) {
                itemRewards.add(itemClass);
            }
        }

        return itemRewards;
    } 
    
    /**
     * Get the classes of building cards dropped on defeat
     * Based on a randomised luck chance and the maximum amount of cards that can be dropped on defeat 
     * @param luck - seed of the characters luck, the lower it is the higher chance for strong cards
     * @return
     */
    public List<Class<? extends Building>> getCardDrops(long luck) {
        return getCardDrops(luck, System.currentTimeMillis());
    } 

    /**
     * Get the classes of building cards dropped on defeat
     * @param luck - seed for chance for a particular item to drop
     * @param shuffleSeed - seed for order to consider dropping cards 
     * @return
     */
    public List<Class<? extends Building>> getCardDrops(long luck, long shuffleSeed) {
         // Randomise the order of cards we'll search by
        List<Class<? extends Building>> possibleCardDrops = new ArrayList<>(BASE_CARD_DROP_CHANCES.keySet());
        Collections.sort(possibleCardDrops, new SortByCardClass());
        shuffleCards(possibleCardDrops, shuffleSeed);

        // For a random order of Buildings, check each one and possible add it to our rewards
        List<Class<? extends Building>> cardRewards = new ArrayList<>();
        for (Class<? extends Building> cardClass : possibleCardDrops) {
            // Check we have not exceeded max amount of items
            if (cardRewards.size() >= this.maxCardDrops) break;

            // If our luck is within items acceptable range add it
            if (isLuckyEnough(luck, this.cardDropChances.get(cardClass))) {
                cardRewards.add(cardClass);
            }
        }
        return cardRewards;
    } 

    /**
     * Shuffle a list of Item classes based on a seed
     * @param list - list to be shuffled in place by reference
     * @param seed - seed to be given to the randomiser
     */
    private static List<Class<? extends Item>> shuffleItems(List<Class<? extends Item>> list, long seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < list.size(); i++) {
            int randomIndex = rand.nextInt(list.size());
            // Swap a random index with the current index
            Class<? extends Item> temp = list.get(i);
            list.set(i, list.get(randomIndex));
            list.set(randomIndex, temp);
        }

        return list;
    }

    /**
     * Shuffle a list of Building classes based on a seed
     * @param list - list to be shuffled in place by reference
     * @param seed - seed to be given to the randomiser
     */
    private static void shuffleCards(List<Class<? extends Building>> list, long seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < list.size(); i++) {
            int randomIndex = rand.nextInt(list.size());
            // Swap a random index with the current index
            Class<? extends Building> temp = list.get(i);
            list.set(i, list.get(randomIndex));
            list.set(randomIndex, temp);
        }
    }

    /**
     * Determine if lucky enough to recieve a reward
     * @param luck - your luck
     * @param luckToAchieve - the maximum luck you can have to get the reward
     * @return
     */
    private boolean isLuckyEnough(long luck, int luckToAchieve) {
        return luck <= luckToAchieve;
    }

}
