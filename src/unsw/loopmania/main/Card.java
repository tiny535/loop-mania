package unsw.loopmania.main;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.DropTable;
import unsw.loopmania.building.Building;
import unsw.loopmania.item.Item;

import java.util.List;
import java.util.Random;

/**
 * a Card in the world
 * which doesn't move
 */
public class Card extends StaticEntity{

    public static final int XP_CHANCE = 60;
    public static final int GOLD_CHANCE = 30;
    public static final int MAX_BOUND = 100;

    private Building type;
    private DropTable dropTable;

    // when the card is actually used, either clone the object or use the existing
    // object and reset the coordinates as needed
    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y, Building type) {
        super(x, y);
        this.type = type;
        this.dropTable = new DropTable();
    }

    public Building getBuilding() {
        return type;
    }

    @Override
    public String getImageSrc() {
        return type.getImageSrc() + "_card";
    }

    public int getXPDrop() {
        return this.dropTable.getXPDrop();
    }

    public int getGoldDrop() {
        return this.dropTable.getGoldDrop();
    }

    public List<Class<? extends Item>> getItemDrops() {
        Random rnd = new Random();
        return this.dropTable.getItemDrops(rnd.nextInt(DropTable.LUCK_RANGE));
    }
}
