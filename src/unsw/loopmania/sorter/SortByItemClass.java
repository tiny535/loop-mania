package unsw.loopmania.sorter;

import java.util.Comparator;

import unsw.loopmania.item.Item;

public class SortByItemClass implements Comparator<Class<? extends Item>>{

    /**
     * Compares two Item Class objects to determine which one is ordered first.
     * 
     * @param a
     * @param b 
     * @return
     */
    @Override
    public int compare(Class<? extends Item> a, Class<? extends Item> b) {
        return a.getName().compareTo(b.getName());
    }
    
}