package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * A Campfire Building
 */
public class CampfireBuilding extends Building {

    private static final String IMAGE_SOURCE = "campfire";

    private static final int CAMPFIRE_BUFF = 2;
    private static final int CAMPFIRE_RANGE = 3;

    private int buff;
    private int range;

    /**
     * Constructor for CampfireBuilding
     * Used when creating Card objects
     */
    public CampfireBuilding() {
        super();
        super.setStrategy(new PlaceOffPath());
        this.buff = CAMPFIRE_BUFF;
        this.range = CAMPFIRE_RANGE;
    }

    /**
     * Constructor for CampfireBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlaceOffPath());
        this.buff = CAMPFIRE_BUFF;
        this.range = CAMPFIRE_RANGE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CampfireBuilding)) {
            return false;
        }

        CampfireBuilding b = (CampfireBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    public int applyEffect(int damage){
        return damage * this.buff;
    }

    public int getRange(){
        return this.range;
    }

    @Override
    public String getImageSrc() {
        return CampfireBuilding.IMAGE_SOURCE;
    }
}
