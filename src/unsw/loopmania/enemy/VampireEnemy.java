package unsw.loopmania.enemy;

import java.util.Random;

import unsw.loopmania.battle.Fighter;
import unsw.loopmania.battle.SpecialAttacker;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class VampireEnemy extends BasicEnemy implements SpecialAttacker{

    private static final String IMAGE_SOURCE = "vampire";

    private static final int BATTLE_RADIUS = 3;
    private static final int SUPPORT_RADIUS = 5;

    private static final int STARTING_HP = 60;
    private static final int STARTING_DMG = 10;

    private int bonusDamage;
    private int numRounds;

    public VampireEnemy(PathPosition pathPosition){
        super(pathPosition, BATTLE_RADIUS, SUPPORT_RADIUS, STARTING_HP, STARTING_DMG);
    }

    @Override
    public void specialAttack(Fighter fighter, long seed){
        if (!(fighter instanceof Character)) return;
        if (checkCharacterStillWeak()){
            fighter.takeDamage(this.bonusDamage);
            this.numRounds--;
        }
        Random r = new Random(seed);
        if (((Character)fighter).isShieldEquipped()){
            if (checkSpecialAttackSuccessShielded(r)){
                specialAttackSuccess(r);
            }
        } else {
            if (checkSpecialAttackSuccess(r)){
                specialAttackSuccess(r);
            }
        }
    }

    @Override
    public void specialAttack(Fighter fighter){
        this.specialAttack(fighter, System.currentTimeMillis());
    }

    private boolean checkCharacterStillWeak(){
        return (this.numRounds > 0);
    }

    // Check if specialAttack is successful with no shield equppied.
    // Base chance is 40%.
    private boolean checkSpecialAttackSuccess(Random r){
        return (r.nextInt(100) < 40);
    }

    // Check if specialAttack is successful against a character
    // with a shield equipped (lowers chance by 60% -> 40 * 0.4 = 16).
    private boolean checkSpecialAttackSuccessShielded(Random r){
        return (r.nextInt(100) < 16);
    }

    // Initiate the specialAttack, giving the vampire a bonus amount of
    // damage for a random number of rounds.
    private void specialAttackSuccess(Random r){
        this.bonusDamage = r.nextInt(5);
        this.numRounds = r.nextInt(5);
    }

    @Override
    public String getImageSrc() {
        return VampireEnemy.IMAGE_SOURCE;
    }

    public void move(LoopManiaWorld world, long seed){
        int directionChoice = (new Random(seed)).nextInt(100);
        if (directionChoice < 25){
            moveUpPath();
            moveUpPath();
            if (withinCampfire(world)) {
                moveDownPath();
                moveDownPath();
            }
        } else if (directionChoice < 50){
            moveDownPath();
            moveDownPath();
            if (withinCampfire(world)) {
                moveUpPath();
                moveUpPath();
            }
        } else if (directionChoice < 75){
            moveUpPath();
            if (withinCampfire(world)) {
                moveDownPath();
            }
        } else {
            moveDownPath();
            if (withinCampfire(world)) {
                moveUpPath();
            }
        }
    }

    public void move(LoopManiaWorld world){
        move(world, System.currentTimeMillis());
    }

    private boolean withinCampfire(LoopManiaWorld world){
        boolean campfireFound = false;
        for (Building building: world.getBuildings()){
            if (building instanceof CampfireBuilding){
                campfireFound = checkWithinRange(building.getX(), building.getY(), 
                                                ((CampfireBuilding)building).getRange());
            }
            if (campfireFound) return true;
        }
        return false;
    }

    private boolean checkWithinRange(int x, int y, int campfireRange){
        return Math.pow(this.getX() - x, 2) + Math.pow(this.getY(), 2) < Math.pow(campfireRange, 2);
    }
}
