package unsw.loopmania.main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 * represents a non-moving entity
 * unlike the moving entities, this can be placed anywhere on the game map
 */
public abstract class StaticEntity extends Entity {
    /**
     * x and y coordinates represented by IntegerProperty, so ChangeListeners can be added
     */
    private IntegerProperty x, y;

    public StaticEntity(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super();
        this.x = x;
        this.y = y;
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getX() {
        return x().get();
    }

    public int getY() {
        return y().get();
    }

    public void updateCoordinates(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
    }

    public boolean isSameCoordinate(int a, int b) {
        return (getX() == a) && (getY() == b);
    }
}
