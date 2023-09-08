package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.sorter.SortByUnequippedItem;

public class SortByUnequippedItemTest {

    private void addToBoth(List<Item> a, List<Item> b, Item item) {
        a.add(item);
        b.add(item);
    }

    @Test
    @DisplayName("Calculation results test")
    public void itemCoordinateScoreCalculationTest() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                assertEquals(((y + 1) * 4) + (x + 1), 
                    SortByUnequippedItem.calculateItemCoordinateScore(
                        new Sword(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y))
                    )
                );
            }
        }
    }

    @Test
    @DisplayName("Sorted in original order")
    public void originalOrderTest() {

        List<Item> expectedOrder = new ArrayList<>();
        List<Item> sortedOrder = new ArrayList<>();
        
        Item i1 = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        Item i2 = new Stake(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        Item i3 = new Armour(new SimpleIntegerProperty(2), new SimpleIntegerProperty(0));
        Item i4 = new Staff(new SimpleIntegerProperty(3), new SimpleIntegerProperty(0));
        Item i5 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        Item i6 = new Potion(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        Item i7 = new Potion(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        Item i8 = new Potion(new SimpleIntegerProperty(3), new SimpleIntegerProperty(1));
        Item i9 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(2));
        Item i10 = new Potion(new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        Item i11 = new Potion(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        Item i12 = new Potion(new SimpleIntegerProperty(3), new SimpleIntegerProperty(2));
        Item i13 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(3));
        addToBoth(expectedOrder, sortedOrder, i1);
        addToBoth(expectedOrder, sortedOrder, i2);
        addToBoth(expectedOrder, sortedOrder, i3);
        addToBoth(expectedOrder, sortedOrder, i4);
        addToBoth(expectedOrder, sortedOrder, i5);
        addToBoth(expectedOrder, sortedOrder, i6);
        addToBoth(expectedOrder, sortedOrder, i7);
        addToBoth(expectedOrder, sortedOrder, i8);
        addToBoth(expectedOrder, sortedOrder, i9);
        addToBoth(expectedOrder, sortedOrder, i10);
        addToBoth(expectedOrder, sortedOrder, i11);
        addToBoth(expectedOrder, sortedOrder, i12);
        addToBoth(expectedOrder, sortedOrder, i13);

        SortByUnequippedItem comparator = new SortByUnequippedItem();
        sortedOrder.sort(comparator);

        assertEquals(expectedOrder, sortedOrder);
    }

    @Test
    @DisplayName("Sorted in different order based on new coordinates")
    public void newOrderTest() {

        List<Item> expectedOrder = new ArrayList<>();
        List<Item> sortedOrder = new ArrayList<>();
        
        Item i1 = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        Item i2 = new Stake(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        Item i4 = new Staff(new SimpleIntegerProperty(3), new SimpleIntegerProperty(0));

        Item i5 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        Item i6 = new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        Item i8 = new TreeStump(new SimpleIntegerProperty(3), new SimpleIntegerProperty(1));

        Item i9 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(2));
        Item i10 = new Sword(new SimpleIntegerProperty(1), new SimpleIntegerProperty(2));
        Item i11 = new OneRing(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));

        Item i13 = new Potion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(3));
        expectedOrder.add(i1);
        expectedOrder.add(i2);
        expectedOrder.add(i4);
        expectedOrder.add(i5);
        expectedOrder.add(i6);
        expectedOrder.add(i8);
        expectedOrder.add(i9);
        expectedOrder.add(i10);
        expectedOrder.add(i11);
        expectedOrder.add(i13);

        for(int i = expectedOrder.size() - 1; i >= 0; i--) {
            sortedOrder.add(expectedOrder.get(i));
        }

        SortByUnequippedItem comparator = new SortByUnequippedItem();
        sortedOrder.sort(comparator);

        assertEquals(expectedOrder, sortedOrder);
    }

}
