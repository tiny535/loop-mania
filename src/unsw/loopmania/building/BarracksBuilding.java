package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.main.Party;

/**
 * A Barracks Building
 */
public class BarracksBuilding extends Building {

    private static final String IMAGE_SOURCE = "barracks";

    /**
     * Constructor for BarracksBuilding
     * Used when creating Card objects
     */
    public BarracksBuilding() {
        super();
        super.setStrategy(new PlacePath());
    }

    /**
     * Constructor for BarracksBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlacePath());
    }

    /**
     * Creates and returns an ally object. If party is full, returns null.
     * @param x coordinate of ally on UI
     * @param y coordinate of ally on UI
     * @param party list of allies that the character currently has
     * @return created Ally object
     */
    public Ally produceAlly(SimpleIntegerProperty x, SimpleIntegerProperty y, Party party) {
        if (party.isPartyFull()) {
            return null;
        }
        return new Ally(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof BarracksBuilding)) {
            return false;
        }

        BarracksBuilding b = (BarracksBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    @Override
    public String getImageSrc() {
        return BarracksBuilding.IMAGE_SOURCE;
    }
}
