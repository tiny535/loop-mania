package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Potion extends Item implements ConsumableItem {

    private static final String IMAGE_SOURCE = "potion";

    private static final String NAME = "POTION";
    private static final int BUY_PRICE = 120;
    private static final int ATTACK_EFFECT = 0;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "Refills Character health by " + Potion.HEALTH_EFFECT + " HP"; 
    private static final int HEALTH_EFFECT = 60; 

    public Potion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Potion.NAME, Potion.BUY_PRICE, Potion.ATTACK_EFFECT, Potion.DEFENCE_EFFECT, Potion.DESCRIPTION);
    }

    @Override
    public int consumeItem() {
        return Potion.HEALTH_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return Potion.IMAGE_SOURCE;
    }    
}
