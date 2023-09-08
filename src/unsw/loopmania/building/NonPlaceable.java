package unsw.loopmania.building;

import java.util.List;

import org.javatuples.Pair;

/**
 * Strategy for a non-placeable building (e.g. Hero's Castle)
 */
public class NonPlaceable implements PlacementStrategy {
    @Override
    public boolean placeTile(int x, int y, List<Pair<Integer, Integer>> orderedPath) {
        return false;
    }
}
