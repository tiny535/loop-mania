package unsw.loopmania.building;

import java.util.List;

import org.javatuples.Pair;

import java.lang.Math;

/**
 * Checks whether the coordinate (x, y) is a non-path tile and is adjacent to a path tile
 */
public class PlaceAdjacentPath implements PlacementStrategy {
    Pair<Integer, Integer> adjacentTile = null;

    @Override
    public boolean placeTile(int x, int y, List<Pair<Integer, Integer>> orderedPath) {
        boolean isAdjacent = false;
        for (Pair<Integer, Integer> c : orderedPath) {
            int cx = c.getValue0();
            int cy = c.getValue1();
            if (x == cx && y == cy) {
                adjacentTile = null;
                return false;
            }
            if (isCoordinateAdjacent(x, y, cx, cy)) {
                this.adjacentTile = new Pair<Integer, Integer>(cx, cy);
                isAdjacent = true;
            }
        }
        return isAdjacent;
    }

    /**
     * Getter for adjacentTile
     * @return
     */
    public Pair<Integer, Integer> getAdjacentTile() {
        return adjacentTile;
    }

    /**
     * Checks if coordinate (x, y) is adjacent to coordinate (cx, cy)
     * @param x
     * @param y
     * @param cx
     * @param cy
     * @return boolean whether (x, y) is adjacent to (cx, cy)
     */
    private boolean isCoordinateAdjacent(int x, int y, int cx, int cy) {
        if (x == cx) {
            if (Math.abs(y-cy) == 1) {
                return true;
            }
        }
        else if (y == cy) {
            if (Math.abs(x-cx) == 1) {
                return true;
            }
        }
        return false;
    }
}
