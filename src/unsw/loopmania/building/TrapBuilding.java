package unsw.loopmania.building;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemy.BasicEnemy;

/**
 * A Trap Building
 */
public class TrapBuilding extends Building {

    private static final String IMAGE_SOURCE = "trap";

    public static final int TRAP_DAMAGE = 15;

    /**
     * Constructor for TrapBuilding
     * Used when creating Card objects
     */
    public TrapBuilding() {
        super();
        super.setStrategy(new PlacePath());
    }

    /**
     * Constructor for TrapBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlacePath());
    }

    /**
     * Determines whether an enemy triggers this trap. If it does, return the affected enemy.
     * Returns null if the trap was not triggered.
     * @param enemies
     * @return
     */
    public BasicEnemy possiblyTriggerTrap(List<BasicEnemy> enemies) {
        for (BasicEnemy e : enemies) {
            if (super.isSameCoordinate(e.getX(), e.getY())) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TrapBuilding)) {
            return false;
        }

        TrapBuilding b = (TrapBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    @Override
    public String getImageSrc() {
        return TrapBuilding.IMAGE_SOURCE;
    }
}
