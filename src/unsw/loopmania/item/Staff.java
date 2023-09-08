package unsw.loopmania.item;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.enemy.BasicEnemy;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Staff extends Item implements EquipableItem, SpecialEffectAttack {

    private static final String IMAGE_SOURCE = "staff";

    private static final BodyPart BODY_PART = BodyPart.HAND;
    private static final String NAME = "STAFF";
    private static final int BUY_PRICE = 180;
    private static final int ATTACK_EFFECT = 8;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "A melee weapon with very low stats, which has a random chance of inflicting a trance, which transforms the attacked enemy into an allied soldier temporarily. If the trance ends during the fight, the affected enemy reverts back to an enemy";
    
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Staff.NAME, Staff.BUY_PRICE, Staff.ATTACK_EFFECT, Staff.DEFENCE_EFFECT, Staff.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Staff.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - Staff.DEFENCE_EFFECT;
    }

    /**
     * The enemy will be tranced.
     */
    @Override
    public void applySpecialEffectAttack(Fighter fighter) {
        applySpecialEffectAttack(fighter, System.currentTimeMillis());
    }  

    public void applySpecialEffectAttack(Fighter fighter, long seed) {
        Random r = new Random(seed);

        if (r.nextInt(100) < 30 && fighter instanceof BasicEnemy) {
            BasicEnemy enemy = (BasicEnemy)fighter;
            enemy.trance();
        }
    }

    @Override
    public String getImageSrc() {
        return Staff.IMAGE_SOURCE;
    }
}
