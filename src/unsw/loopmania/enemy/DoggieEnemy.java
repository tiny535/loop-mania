package unsw.loopmania.enemy;

import java.util.Random;

import unsw.loopmania.battle.Fighter;
import unsw.loopmania.battle.SpecialAttacker;
import unsw.loopmania.main.Character;
import unsw.loopmania.map.PathPosition;

public class DoggieEnemy extends BasicEnemy implements SpecialAttacker{

    public static final int SPAWNABLE_CYCLE = 20;

    private static final String IMAGE_SOURCE = "doggie";

    private static final int BATTLE_RADIUS = 2;
    private static final int SUPPORT_RADIUS = 2;

    private static final int STARTING_HP = 80;
    private static final int STARTING_DMG = 12;

    private static final int SPECIAL_ATTACK_CHANCE = 30;
    private static final int STUN_DURATION = 2;
    private static final int STUN_COOLDOWN = 5;

    private int stunCooldown = 0;

    public DoggieEnemy(PathPosition position) {
        super(position, BATTLE_RADIUS, SUPPORT_RADIUS, STARTING_HP, STARTING_DMG);
    }

    @Override
    public void specialAttack(Fighter fighter, long seed) {
        stunCooldown = (stunCooldown - 1 < 0) ? stunCooldown : stunCooldown - 1;
        if (!(fighter instanceof Character)) return;
        Random r = new Random(seed);
        if (r.nextInt(100) < SPECIAL_ATTACK_CHANCE && stunCooldown == 0){
            Character character = (Character)fighter;
            character.stun(STUN_DURATION);
            stunCooldown = STUN_COOLDOWN;
        }
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
