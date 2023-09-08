package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Armour extends Item implements EquipableItem{

    private static final String IMAGE_SOURCE = "armour";

    private static final BodyPart BODY_PART = BodyPart.TORSO;
    private static final String NAME = "ARMOUR";
    private static final int BUY_PRICE = 200;
    private static final int ATTACK_EFFECT = 0;
    private static final double DEFENCE_EFFECT = 0.5;
    private static final String DESCRIPTION = "Body armour, provides defence and halves enemy attack";
    
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Armour.NAME, Armour.BUY_PRICE, Armour.ATTACK_EFFECT, Armour.DEFENCE_EFFECT, Armour.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Armour.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return (int) (enemyDamage * Armour.DEFENCE_EFFECT);
    }

    @Override
    public String getImageSrc() {
        return Armour.IMAGE_SOURCE;
    }  

    @Override
    public boolean isProtectiveItem(){
        return true;
    }  
}
