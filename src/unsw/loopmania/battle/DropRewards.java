package unsw.loopmania.battle;

import java.util.List;

import unsw.loopmania.building.Building;
import unsw.loopmania.item.Item;

public interface DropRewards {
    /**
     * Get the amount of XP rewarded
     * @return - integer amount of XP
     */
    public abstract int getXPDrop();

    /**
     * Get the amount of Gold rewarded
     * @return - integer amount of gold
     */
    public abstract int getGoldDrop();

    /**
     * Gets the items drops as a list of classes of all Items rewarded
     * @return - List of Classes which are of Item type
     */
    public abstract List<Class<? extends Item>> getItemDrops();
    /**
     * 
     * @param luckSeed - determined luck between 0 and 100 
     * @param shuffleSeed - seed to determine shuffle order of items
     * @return
     */
    public abstract List<Class<? extends Item>> getItemDrops(long luckSeed, long shuffleSeed);
    
    /**
     * Gets the card drops as a list of classes of all buildings rewarded
     * @return - List of Classes which are of Building type
     */
    public abstract List<Class<? extends Building>> getCardDrops();
    /**
     * 
     * @param luckSeed - determined luck between 0 and 100 
     * @param shuffleSeed - seed to determine shuffle order of cards
     * @return
     */
    public abstract List<Class<? extends Building>> getCardDrops(long luckSeed, long shuffleSeed);
}
