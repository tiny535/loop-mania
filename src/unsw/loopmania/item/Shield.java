package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Shield extends Item implements EquipableItem{

    private static final String IMAGE_SOURCE = "shield";

    private static final BodyPart BODY_PART = BodyPart.ARM;
    private static final String NAME = "SHIELD";
    private static final int BUY_PRICE = 150;
    private static final int ATTACK_EFFECT = 0;
    private static final int DEFENCE_EFFECT = 2;
    private static final String DESCRIPTION = "Defends against enemy attacks, critical vampire attacks have a 60% lower chance of occurring";
    
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Shield.NAME, Shield.BUY_PRICE, Shield.ATTACK_EFFECT, Shield.DEFENCE_EFFECT, Shield.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Shield.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - Shield.DEFENCE_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return Shield.IMAGE_SOURCE;
    }    

    @Override
    public boolean isProtectiveItem(){
        return true;
    }
}
