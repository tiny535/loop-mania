package unsw.loopmania.building;

import java.util.List;

import org.javatuples.Pair;

/**
 * Checks whether the coordinate (x, y) is a path tile
 */
public class PlacePath implements PlacementStrategy {
    @Override
    public boolean placeTile(int x, int y, List<Pair<Integer, Integer>> orderedPath) {
        Pair <Integer, Integer> p = new Pair<Integer, Integer>(x, y);
        return orderedPath.contains(p);
    }
}
