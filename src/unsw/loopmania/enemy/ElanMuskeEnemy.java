package unsw.loopmania.enemy;

import java.util.Random;

import unsw.loopmania.battle.Fighter;
import unsw.loopmania.battle.SpecialAttacker;
import unsw.loopmania.map.PathPosition;

public class ElanMuskeEnemy extends BasicEnemy implements SpecialAttacker{

    public static final int SPAWNABLE_CYCLE = 40;
    public static final int XP_REQUIRED_TO_SPAWN = 10000;
    public static final int FIGHT_CHANCE = 50;

    private static final String IMAGE_SOURCE = "elanMuske";

    private static final int BATTLE_RADIUS = 2;
    private static final int SUPPORT_RADIUS = 2;

    private static final int STARTING_HP = 100;
    private static final int STARTING_DMG = 15;

    private static final int SPECIAL_ATTACK_CHANCE = 40;

    public ElanMuskeEnemy(PathPosition position) {
        super(position, BATTLE_RADIUS, SUPPORT_RADIUS, STARTING_HP, STARTING_DMG);
    }

    @Override
    public void specialAttack(Fighter fighter, long seed) {
        Random r = new Random(seed);
        if (r.nextInt(100) < SPECIAL_ATTACK_CHANCE)
            fighter.takeDamage(specialAttackHealAmount((BasicEnemy)fighter));
    }

    private int specialAttackHealAmount(BasicEnemy enemy){
        return -enemy.getMaxHp() / 4;
    }

    @Override
    public void specialAttack(Fighter fighter) {
        specialAttack(fighter, System.currentTimeMillis());       
    }

    @Override
    public String getImageSrc() {
        return IMAGE_SOURCE;
    }
}
