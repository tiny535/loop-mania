package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;

public class AndurilSword extends Item implements EquipableItem, SpecialEffectAttack {

    private static final String IMAGE_SOURCE = "anduril_sword";

    private static final BodyPart BODY_PART = BodyPart.HAND;
    private static final String NAME = "ANDURIL";
    private static final int BUY_PRICE = 600;
    private static final int ATTACK_EFFECT = 12;
    private static final int DEFENCE_EFFECT = 0;
    private static final String DESCRIPTION = "A very high damage sword which causes triple damage against bosses";
    
    public AndurilSword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, AndurilSword.NAME, AndurilSword.BUY_PRICE, AndurilSword.ATTACK_EFFECT, AndurilSword.DEFENCE_EFFECT, AndurilSword.DESCRIPTION);
    }  

    /**
     * The Anduril Sword will cause triple damage to Boss Enemies.
     * The first 12 HP damage was dealt by the getAttackEsffect() method. 
     */
    @Override
    public void applySpecialEffectAttack(Fighter fighter) {
        if (fighter instanceof DoggieEnemy || fighter instanceof ElanMuskeEnemy) {
            fighter.takeDamage(ATTACK_EFFECT * 2);
        }
    }

    @Override
    public BodyPart getBodyPart() {
        return AndurilSword.BODY_PART;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - AndurilSword.DEFENCE_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return AndurilSword.IMAGE_SOURCE;
    }

    @Override
    public boolean isRareItem(){
        return true;
    }
}
