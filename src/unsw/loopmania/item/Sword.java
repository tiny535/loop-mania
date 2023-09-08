package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends Item implements EquipableItem{

    private static final String IMAGE_SOURCE = "sword";

    private static final BodyPart BODY_PART = BodyPart.HAND;
    private static final String NAME = "SWORD";
    private static final int BUY_PRICE = 50;
    private static final int ATTACK_EFFECT = 12;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "A standard melee weapon. Increases damage dealt by Character";
    
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Sword.NAME, Sword.BUY_PRICE, Sword.ATTACK_EFFECT, Sword.DEFENCE_EFFECT, Sword.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Sword.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - Sword.DEFENCE_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return Sword.IMAGE_SOURCE;
    }  
}
