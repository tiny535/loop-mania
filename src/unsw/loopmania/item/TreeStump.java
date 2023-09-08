package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;

public class TreeStump extends Item implements EquipableItem, SpecialEffectDefence {

    private static final String IMAGE_SOURCE = "tree_stump";

    private static final BodyPart BODY_PART = BodyPart.ARM;
    private static final String NAME = "TREE STUMP";
    private static final int BUY_PRICE = 600;
    private static final int ATTACK_EFFECT = 0;
    private static final int DEFENCE_EFFECT = 5;
    private static final String DESCRIPTION = "An especially powerful shield, which provides higher defence against bosses";
    
    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, TreeStump.NAME, TreeStump.BUY_PRICE, TreeStump.ATTACK_EFFECT, TreeStump.DEFENCE_EFFECT, TreeStump.DESCRIPTION);
    }  

    @Override
    public int applySpecialEffectDefence(Fighter fighter, int damage) {
        if (fighter instanceof DoggieEnemy || fighter instanceof ElanMuskeEnemy) {
            damage -= 3; 
        }
        
        return damage > 0 ? damage : 0; 
    }

    @Override
    public BodyPart getBodyPart() {
        return TreeStump.BODY_PART;
    }

    @Override
    public int getAttackEffect() {
        return TreeStump.ATTACK_EFFECT;
    }

    @Override
    public int getDefenceEffect(int enemyDamage) {
        return enemyDamage - TreeStump.DEFENCE_EFFECT;
    }

    @Override
    public String getImageSrc() {
        return TreeStump.IMAGE_SOURCE;
    }
    
    @Override
    public boolean isProtectiveItem(){
        return true;
    }

    @Override
    public boolean isRareItem(){
        return true;
    }
}
