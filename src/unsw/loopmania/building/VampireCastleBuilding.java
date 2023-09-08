package unsw.loopmania.building;

import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.map.PathPosition;

/**
 * A Vampire Castle Building
 */
public class VampireCastleBuilding extends Building {

    private static final String IMAGE_SOURCE = "vampire_castle";

    private int cycleSpawn; // the next cycle number this building spawns a VampireEnemy
    private Pair<Integer, Integer> adjacentPathTile; // coordinate of path tile adjacent to building

    /**
     * Constructor for VampireCastleBuilding
     * Used when creating Card objects
     */
    public VampireCastleBuilding() {
        super();
        super.setStrategy(new PlaceAdjacentPath());
    }

    /**
     * Constructor for VampireCastleBuilding
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setStrategy(new PlaceAdjacentPath());
    }

    /**
     * Checks if building can be placed at coordinate (x, y) according to
     * placement strategy of building.
     * If building can be placed, stores coordinate of adjacent path tile in adjacentPathTile.
     * @param x coordinate of building to be created
     * @param y coordinate of building to be created
     * @param orderedPath list containing the coordinates of the path
     * @return boolean if building can be created at coordinate (x,y)
     */
    @Override
    public boolean canPlaceBuilding(int x, int y, List<Building> buildings,
                                    List<Pair<Integer, Integer>> orderedPath) {
        if (super.canPlaceBuilding(x, y, buildings, orderedPath)) {
            PlaceAdjacentPath strategy = (PlaceAdjacentPath) super.getStrategy();
            this.adjacentPathTile = strategy.getAdjacentTile();
            return true;
        }
        return false;
    }

    /**
     * Setter for cycleSpawn. Sets cycleSpawn to be 5 cycles after the current cycle.
     * @param cycle the current cycle number
     */
    @Override
    public void setCycleSpawn(int cycle) {
        this.cycleSpawn = cycle + 5;
    }

    /**
     * Spawns a VampireEnemy if the current cycle number matches cycleSpawn
     * @param cycle the current cycle number
     * @return the spawned VampireEnemy object. Null if nothing was spawned.
     */
    @Override
    public VampireEnemy spawnEnemy(int cycle, List<Pair<Integer, Integer>> orderedPath) {
        if (cycle == cycleSpawn) {
            PathPosition p = new PathPosition(orderedPath.indexOf(adjacentPathTile), orderedPath);
            VampireEnemy e = new VampireEnemy(p);
            cycleSpawn += 5;
            return e;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof VampireCastleBuilding)) {
            return false;
        }

        VampireCastleBuilding b = (VampireCastleBuilding) o;
        return getX() == b.getX() && getY() == b.getY();
    }

    @Override
    public String getImageSrc() {
        return VampireCastleBuilding.IMAGE_SOURCE;
    }
}
