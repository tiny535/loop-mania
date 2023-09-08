package unsw.loopmania.sorter;

import java.util.Comparator;

import unsw.loopmania.building.Building;

public class SortByCardClass implements Comparator<Class<? extends Building>>{

    /**
     * Compares two Building Class objects to determine which one is ordered first.
     * 
     * @param a
     * @param b 
     * @return
     */
    @Override
    public int compare(Class<? extends Building> a, Class<? extends Building> b) {
        return a.getName().compareTo(b.getName());
    }
    
}