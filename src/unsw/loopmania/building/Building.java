package unsw.loopmania.building;

import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.main.StaticEntity;

/**
 * An abstract class that represents a basic form of building in the world
 */
public abstract class Building extends StaticEntity {
    private PlacementStrategy strategy;

    /**
     * Constructor for Building()
     * @param x coordinate of building
     * @param y coordinate of building
     */
    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public Building() {
        super(new SimpleIntegerProperty(-1), new SimpleIntegerProperty(-1));
    }

    /**
     * Checks if building can be placed at coordinate (x, y) according to
     * placement strategy of building
     * @param x coordinate of building to be created
     * @param y coordinate of building to be created
     * @param orderedPath list containing the coordinates of the path
     * @return boolean if building can be created at coordinate (x,y)
     */
    public boolean canPlaceBuilding(int x, int y, List<Building> buildings,
                                    List<Pair<Integer, Integer>> orderedPath) {
        return strategy.placeTile(x, y, orderedPath) &&
            isCoordinateFree(x, y, buildings);
    }
    
    /**
     * Setter for strategy
     * @param strategy
     */
    public void setStrategy(PlacementStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Getter for strategy
     * @return
     */
    public PlacementStrategy getStrategy() {
        return strategy;
    }

    public void setCycleSpawn(int cycle) {
    }

    /**
     * Spawns a BasicEnemy if the conditions are correct. Returns null by default
     * @param cycle the current cycle number
     * @return the spawned BasicEnemy object. Null if nothing was spawned.
     */
    public BasicEnemy spawnEnemy(int cycle, List<Pair<Integer, Integer>> orderedPath) {
        return null;
    }

    /**
     * Checks if there is a Building in buildings with coordinate (x, y)
     * @param x coordinate of building to be created
     * @param y coordinate of building to be created
     * @param buildings list of buildings in world
     * @return boolean if coordinate (x, y) is occupied with another building
     */
    private boolean isCoordinateFree(int x, int y, List<Building> buildings) {
        for (Building b : buildings) {
            if (b.isSameCoordinate(x, y)) {
                return false;
            }
        }
        return true;
    }
}
