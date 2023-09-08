package unsw.loopmania.building;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.main.Character;

/**
 * A Village Building
 */
public class VillageBuilding extends Building {

    private static final String IMAGE_SOURCE = "village";

    private static final int HP_HEAL = 50;

    /**
     * Constructor for VillageBuilding
     * Used when creating Card objects
     */
    public VillageBuilding() {
        super();
        super.setStrategy(new PlacePath());
    }

    /**
     * Constructor for VillageBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlacePath());
    }

    /**
     * Heals the character by 50 HP and all allies in party to max HP
     * @param character
     */
    public void healCharacter(Character character) {
        List<Ally> party = character.getParty().getAllies();
        for (Ally ally : party) {
            ally.heal();
        }
        character.healCharacter(HP_HEAL);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof VillageBuilding)) {
            return false;
        }

        VillageBuilding b = (VillageBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    @Override
    public String getImageSrc() {
        return VillageBuilding.IMAGE_SOURCE;
    }
}
