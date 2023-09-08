package unsw.loopmania.main;

import javafx.beans.property.SimpleIntegerProperty;

public class Gold extends StaticEntity {

    private static final String IMAGE_SOURCE = "gold_pile";

    /**
     * Number of gold pieces in this gold object
     */
    private int goldQuantity; 

    public Gold(SimpleIntegerProperty x, SimpleIntegerProperty y, int quantity) {
        super(x, y);
        
        this.goldQuantity = quantity;
    }

    /**
     * Returns the num of gold pieces
     * @return
     */
    public int getGoldQuantity() {
        return this.goldQuantity;
    }

    @Override
    public String getImageSrc() {
        return Gold.IMAGE_SOURCE;
    }
}
