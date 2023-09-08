package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * A Hero's Castle Building
 */
public class HeroCastleBuilding extends Building {

    private static final String IMAGE_SOURCE = "heros_castle";

    /**
     * Constructor for HeroCastleBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public HeroCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new NonPlaceable());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HeroCastleBuilding)) {
            return false;
        }

        HeroCastleBuilding b = (HeroCastleBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    @Override
    public String getImageSrc() {
        return HeroCastleBuilding.IMAGE_SOURCE;
    }
}
