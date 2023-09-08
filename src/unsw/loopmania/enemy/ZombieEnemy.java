package unsw.loopmania.enemy;

import java.util.Random;

import unsw.loopmania.ally.Ally;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.battle.SpecialAttacker;
import unsw.loopmania.map.PathPosition;

public class ZombieEnemy extends BasicEnemy implements SpecialAttacker{

    private static final String IMAGE_SOURCE = "zombie";
    
    private static final int BATTLE_RADIUS = 3;
    private static final int SUPPORT_RADIUS = 0;

    private static final int STARTING_HP = 20;
    private static final int STARTING_DMG = 10;

    public ZombieEnemy(PathPosition pathPosition){
        super(pathPosition, BATTLE_RADIUS, SUPPORT_RADIUS, STARTING_HP, STARTING_DMG);
    }

    @Override
    public void specialAttack(Fighter fighter, long seed){
        Random r = new Random(seed);
        if (r.nextInt(100) < 30){
            if (fighter instanceof Ally){
                Ally ally = (Ally)fighter;
                ally.makeZombie();
            }
        }
    }
    @Override
    public void specialAttack(Fighter fighter){
        this.specialAttack(fighter, System.currentTimeMillis());
    }

    @Override
    public String getImageSrc() {
        return ZombieEnemy.IMAGE_SOURCE;
    }

    @Override
    public void move(long seed){
        int directionChoice = (new Random(seed)).nextInt(100);
        if (directionChoice < 25){
            moveUpPath();
        } else if (directionChoice < 50){
            moveDownPath();
        }
    }
}
