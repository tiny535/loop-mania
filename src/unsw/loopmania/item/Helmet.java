package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Helmet extends Item implements EquipableItem{

    private static final String IMAGE_SOURCE = "helmet";

    private static final BodyPart BODY_PART = BodyPart.HEAD;
    private static final String NAME = "HELMET";
    private static final int BUY_PRICE = 50;
    private static final int ATTACK_EFFECT = -2;
    private static final int DEFENCE_EFFECT = 2;
    private static final String DESCRIPTION = "Defends against enemy attacks, enemy attacks are reduced by a scalar value. The damage inflicted by the Character against enemies is reduced (since it is harder to see)";

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Helmet.NAME, Helmet.BUY_PRICE, Helmet.ATTACK_EFFECT, Helmet.DEFENCE_EFFECT, Helmet.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Helmet.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - Helmet.DEFENCE_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return Helmet.IMAGE_SOURCE;
    }    
    
    @Override
    public boolean isProtectiveItem(){
        return true;
    }
}
