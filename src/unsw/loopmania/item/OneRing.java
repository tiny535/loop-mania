package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class OneRing extends Item implements ConsumableItem {

    private static final String IMAGE_SOURCE = "the_one_ring";

    private static final String NAME = "ONE RING";
    private static final int BUY_PRICE = 300;
    private static final int ATTACK_EFFECT = 0;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "If Character is killed, revives Character and fully restores HP. Single use only"; 
    private static final int HEALTH_EFFECT = 100;
    
    public OneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, OneRing.NAME, OneRing.BUY_PRICE, OneRing.ATTACK_EFFECT, OneRing.DEFENCE_EFFECT, OneRing.DESCRIPTION);
    }

    @Override
    public int consumeItem() {
        return OneRing.HEALTH_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return OneRing.IMAGE_SOURCE;
    }    

    @Override
    public boolean isRareItem(){
        return true;
    }
}
