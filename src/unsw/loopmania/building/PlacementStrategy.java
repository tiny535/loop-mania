package unsw.loopmania.building;

import java.util.List;

import org.javatuples.Pair;

/**
 * Interface to check whether a building can be placed at coordinate (x, y)
 * Returns:
 * - true: if the building can be placed at coordinate (x, y)
 * - false: if the building cannot be place at coordinate (x, y)
 */
public interface PlacementStrategy {
    /**
     * 
     * @param x coordinate of building to be created
     * @param y coordinate of building to be created
     * @param orderedPath list containing the coordinates of the path
     * @return boolean if building can be created at coordinate (x,y)
     */
    public boolean placeTile(int x, int y, List<Pair<Integer, Integer>> orderedPath);
}
