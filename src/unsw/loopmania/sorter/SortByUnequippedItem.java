package unsw.loopmania.sorter;

import java.util.Comparator;

import unsw.loopmania.item.Item;
import unsw.loopmania.main.Inventory;

public class SortByUnequippedItem implements Comparator<Item>{

    @Override
    public int compare(Item o1, Item o2) {

        int o1Score = calculateItemCoordinateScore(o1);
        int o2Score = calculateItemCoordinateScore(o2);

        return o1Score - o2Score;
    }

    public static int calculateItemCoordinateScore(Item item) {
        return ((item.getY() + 1) * Inventory.INVENTORY_HEIGHT) + (item.getX() + 1);
    }
    
}
