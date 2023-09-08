package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.enemy.VampireEnemy;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Stake extends Item implements EquipableItem, SpecialEffectAttack {

    private static final String IMAGE_SOURCE = "stake";

    private static final BodyPart BODY_PART = BodyPart.HAND;
    private static final String NAME = "STAKE";
    private static final int BUY_PRICE = 150;
    private static final int ATTACK_EFFECT = 10;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "A melee weapon with lower stats than the sword, but causes very high damage to vampires";
    
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, Stake.NAME, Stake.BUY_PRICE, Stake.ATTACK_EFFECT, Stake.DEFENCE_EFFECT, Stake.DESCRIPTION);
    }

    @Override
    public BodyPart getBodyPart() {
        return Stake.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - Stake.DEFENCE_EFFECT;
    }

    /**
     * If the enemy is a Vampire, apply an additional 10 HP damage.
     * @param fighter
     */
    @Override
    public void applySpecialEffectAttack(Fighter fighter) {
        if (fighter instanceof VampireEnemy) {
            fighter.takeDamage(Stake.ATTACK_EFFECT);
        }
    }

    @Override
    public String getImageSrc() {
        return Stake.IMAGE_SOURCE;
    }  
}
