package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;

/**
 * A Tower Building
 */
public class TowerBuilding extends Building {

    private static final String IMAGE_SOURCE = "tower";

    private static final int TOWER_DAMAGE = 5;
    private static final int TOWER_RANGE = 5;

    private int damage;
    private int range;

    /**
     * Constructor for TowerBuilding
     * Used when creating Card objects
     */
    public TowerBuilding() {
        super();
        super.setStrategy(new PlaceAdjacentPath());
        this.damage = TOWER_DAMAGE;
        this.range = TOWER_RANGE;
    }

    /**
     * Constructor for TowerBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlaceAdjacentPath());
        this.damage = TOWER_DAMAGE;
        this.range = TOWER_RANGE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TowerBuilding)) {
            return false;
        }

        TowerBuilding b = (TowerBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    public void attack(Fighter enemy){
        enemy.takeDamage(this.damage);
    }

    public int getRange(){
        return this.range;
    }

    @Override
    public String getImageSrc() {
        return TowerBuilding.IMAGE_SOURCE;
    }
}
