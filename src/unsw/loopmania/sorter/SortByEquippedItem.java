package unsw.loopmania.sorter;

import java.util.Comparator;

import unsw.loopmania.item.Armour;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Shield;

public class SortByEquippedItem implements Comparator<Item>{

    private static final int HIGH_SCORE = 1;
    private static final int MED_SCORE = 5; 
    private static final int LOW_SCORE = 10; 

    
    /**
     * Compares two Items to determine which one is ordered first.
     * 
     * @param a - a Item object
     * @param b - another Item object
     * @return
     */
    @Override
    public int compare(Item a, Item b) {
        return getItemCompareScore(a) - getItemCompareScore(b);
    }

    /**
     * Compare method to sort equipped items by their type
     * so that effects are always applied consistently
     * @param a
     * @param b
     * @return
     */
    private int getItemCompareScore(Item item) {
        if (item instanceof Armour) return HIGH_SCORE;
        if (item instanceof Shield) return MED_SCORE;
        if (item instanceof Helmet) return LOW_SCORE;
        return 0;
    }

}