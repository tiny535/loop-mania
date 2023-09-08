package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.main.StaticEntity;

public class DoggieCoin extends StaticEntity {

    private static final String IMAGE_SOURCE = "doggie_coin";
    public static final int STARTING_DOGGIE_COIN_VALUE = 100; 
    private int doggieCoinQuantity;

    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y, int quantity) {
        super(x, y);
        
        this.doggieCoinQuantity = quantity;
    }

    public int getDoggieCoinQuantity() {
        return this.doggieCoinQuantity;
    }

    @Override
    public String getImageSrc() {
        return DoggieCoin.IMAGE_SOURCE;
    }
}
