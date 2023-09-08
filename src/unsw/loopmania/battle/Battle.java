package unsw.loopmania.battle;

import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.ally.TrancedEnemy;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Entity;
import unsw.loopmania.map.PathPosition;

import java.util.ArrayList;

public class Battle {

    private static final int LIVING_THRESHOLD = 0;
    private Character character;
    private List<BasicEnemy> battleEnemies;
    private List<BasicEnemy> defeatedEnemies;

    // Side whose turn it is to attack
    // 0 - character and allies side
    // 1 - enemies side
    private int side;

    // boolean if the character has attacked yet
    private boolean characterHasAttacked;

    private int turn;

    private List<CampfireBuilding> campfires;

    private List<TowerBuilding> towers;

    private boolean towersHaveAttacked;
    
    public Battle(){
        this.battleEnemies = new ArrayList<BasicEnemy>();
        this.campfires = new ArrayList<CampfireBuilding>();
        this.towers = new ArrayList<TowerBuilding>();
        this.defeatedEnemies = new ArrayList<BasicEnemy>();
    }

    /**
     * Sets the character from the world into the battle
     * @param character
     */
    public void setCharacter(Character character){
        this.character = character;
    }

    /**
     * Gets all the towers and campfires, and puts them in their respective lists.
     * @param buildings The list of buildings to check for towers and campfires from
     */
    public void setBuildings(List<Building> buildings){
        for (Building building: buildings){
            if (building instanceof TowerBuilding){
                TowerBuilding tempTower = (TowerBuilding)building;
                if (withinRangeOfCharacter(tempTower, tempTower.getRange())){
                    this.towers.add(tempTower);
                }
            } else if (building instanceof CampfireBuilding){
                CampfireBuilding tempCampfire = (CampfireBuilding)building;
                if (withinRangeOfCharacter(tempCampfire, tempCampfire.getRange())){
                    this.campfires.add(tempCampfire);
                }
            }
        }
    }

    /**
     * Calculates whether within radius of the character
     * @return
     */
    private boolean withinRangeOfCharacter(Entity e, int radius) {
        // Pythagoras: a^2+b^2 < radius^2 to see if within radius
        return (Math.pow((character.getX() - e.getX()), 2) +  Math.pow((character.getY() - e.getY()), 2) < Math.pow(radius, 2));
    }

    /**
     * Get all enemies that are within battle radius of the character.
     * @return List<BasicEnemy> that are in battle radius of the character.
     */
    public void setBattleEnemies(List<BasicEnemy> enemies, long seed){
        // Check which enemies are in the battle radius
        for (BasicEnemy e: enemies){
            if (withinRangeOfCharacter(e, e.getBattleRadius())){
                if (checkFightElanMuske(e, seed))
                    this.battleEnemies.add(e);            
            }
        }

        // If there exists a battle, add all enemies within support range
        if (!this.battleEnemies.isEmpty()) {
            for (BasicEnemy e: enemies) {
                if (withinRangeOfCharacter(e, e.getSupportRadius())
                    && !this.battleEnemies.contains(e)) {
                    if (checkFightElanMuske(e, seed))
                        this.battleEnemies.add(e);
                } 
            }
        }
    }

    public void setBattleEnemies(List<BasicEnemy> enemies){
        setBattleEnemies(enemies, System.currentTimeMillis());
    }

    /**
     * Checks if the elanMuske is an enemy, if he is, there is a 50% chance of fighting him, 
     * otherwise elanMuske does not get added to the list of battleEnemies
     * @param e
     * @param seed
     * @return
     */
    private boolean checkFightElanMuske(BasicEnemy e, long seed){
        if (!(e instanceof ElanMuskeEnemy)) return true;
        Random r = new Random(seed);
        if (r.nextInt(100) < ElanMuskeEnemy.FIGHT_CHANCE)
            return true;
        return false;
    }

    /**
     * Each tower in the tower list attack the first living enemy.
     */
    private void towersAttack(){
        for (TowerBuilding tower: this.towers){
            if (this.battleEnemies.isEmpty()) break;
            Fighter enemy = getCurrentDefender();
            tower.attack(enemy);
            tryKill(enemy);
        }
        this.towersHaveAttacked = true;
    }

    /**
     * Checks if the given ally has become a zombie, make them one if so.
     * @param ally The ally to be checked and possibly zombified.
     */
    private void possiblyZombify(Ally ally){
        if (ally.isZombie()){
            this.character.removeAlly(ally);
            List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
            orderedPath.add(new Pair<Integer,Integer>(0, 0));
            this.battleEnemies.add(new ZombieEnemy(new PathPosition(0, orderedPath)));
        }
    }

    /** 
     * Checks if the given enemy is tranced, turns them into a trancedAlly if they are.
     * @param enemy The enemy that may be tranced.
     */
    private void possiblyTrance(BasicEnemy enemy){
        if (enemy.isTranced()){
            TrancedEnemy newAlly = new TrancedEnemy(new SimpleIntegerProperty(), 
                                   new SimpleIntegerProperty(), enemy);
            this.character.addPartyMember(newAlly);
            this.battleEnemies.remove(enemy);
        }
    }

    /**
     * Checks if the given TrancedEnemy is still tranced, turns them back
     * into an enemy if they are not.
     * @param enemy The TrancedEnemy to be checked.
     */
    private void possiblyUntrance(TrancedEnemy enemy){
        if (!enemy.isStillTranced()){
            this.battleEnemies.add(enemy.getPreviousVersion());
            this.character.removeAlly(enemy);
        }
    }

    /**
     * Runs the battle loop in the following order:
     *  - Towers attack the first living enemy in the battleEnemies list.
     *  - Character attacks the first enemy in the battleEnemies list.
     *  - Allies attack the first living enemy in the battleEnemies list.
     *  - Enemies attack the first ally in the character's party, then attacks the character
     * if there are none remaining.
     *  - Kills all tranced enemies.
     *  - Returns the list of killed enemies from the battle
     * @return The list of enemies killed in the battle
     */
    public List<BasicEnemy> runBattleLoop(){
        // CHOSEN METHOD
        turn = 0;

        while (isCharacterAlive() && !this.battleEnemies.isEmpty()){
            if (!this.towersHaveAttacked && this.side == 0) towersAttack();
            if (this.battleEnemies.isEmpty()) break;
            // Get the current attacker and defender
            Fighter currentAttacker = getCurrentAttacker();
            Fighter currentDefender = getCurrentDefender();
            
            // Skip current attack if it is the character and they are stunned
            if (stunnedCharacterAttack(currentAttacker)) {
                this.characterHasAttacked = true;
                if (isTurnIsOver()) {
                    swapSide();
                }
                continue;
            }

            // Side 0 is the Character's side 
            // Attackers do basic damage and Defender take basic damage
            int attackerDamage = currentAttacker.getDamage();
            int addedEnvironmentDamage = attackerDamage;
            if (this.side == 0 && !this.characterHasAttacked){
                addedEnvironmentDamage = applyCampfireEffect(attackerDamage);
                this.characterHasAttacked = true;
            }
            
            // If the attacker is a boss, and the defender is the character
            int finalDamage = addedEnvironmentDamage; 
            if (currentDefender instanceof Character) {
                finalDamage = ((Character) currentDefender).specialDefence(currentAttacker, finalDamage);
            }
            currentDefender.takeDamage(finalDamage);

            // Special Attackers directly perform their SpecialAttacks e.g. Trance, Zombification, higher weapon damage for specific enemies
            if (currentAttacker instanceof SpecialAttacker){
                if (currentAttacker instanceof ElanMuskeEnemy){
                    ((SpecialAttacker)currentAttacker).specialAttack(this.battleEnemies.get(0));
                } else {
                    ((SpecialAttacker)currentAttacker).specialAttack(currentDefender);
                }
            }
 
            tryKill(currentDefender);

            if (this.character.checkPartyContains(currentDefender)){
                possiblyZombify((Ally)currentDefender);
            } else if (this.battleEnemies.contains(currentDefender)){
                possiblyTrance((BasicEnemy)currentDefender);
            }

            if (this.character.checkPartyContains(currentDefender)){
                if ((Ally)currentDefender instanceof TrancedEnemy){
                    possiblyUntrance((TrancedEnemy)currentDefender);
                }
            }

            if (isTurnIsOver()) {
                swapSide();
            }
        }

        killTrancedEnemies();

        return this.defeatedEnemies;
    }

    /**
     * Checks if the fighter is a character, if it is, check if the chracter is stunned
     * @param fighter
     * @return true if the fighter is a character and is stunned
     */
    private boolean stunnedCharacterAttack(Fighter fighter){
        if (!(fighter instanceof Character)) return false;

        return this.character.getIsStunned();
    }

    /**
     * Checks if the character is alive
     * @return Boolean, true if the character is alive, false if not
     */
    private boolean isCharacterAlive(){
        return this.character.getHp() > 0;
    }

    /**
     * Checks if either sides turn is over
     * @return
     */
    private boolean isTurnIsOver() {
        return (side == 0 && turn >= this.character.getPartySize()
            || (side == 1 && turn >= this.battleEnemies.size())); 
    }

    /**
     * Swaps who is attacking between the character side and enemy side.
     */
    private void swapSide() {
        // If we are at the end of the enemies turn, it is now the characters turn to attack
        this.characterHasAttacked = (this.side == 1) ? false : true;
        // Flip the side, reset the turn
        this.side = (this.side == 0) ? 1 : 0;
        this.turn = 0;
        this.towersHaveAttacked = false;
    }

    /**
     * Gets the Fighter that is going to attack.
     * @return The attacking Fighter
     */
    private Fighter getCurrentAttacker(){
        if (side == 0 && !characterHasAttacked) {
            return this.character;
        } else if (side == 0 && characterHasAttacked) {
            Ally ally = this.character.getAlly(this.turn);
            this.turn++;
           return ally;
        } else {
            BasicEnemy enemy = this.battleEnemies.get(this.turn);
            this.turn++;
            return enemy;
        } 
    }

    /**
     * Gets the Fighter currently being attacked.
     * @return The Fighter being attacked
     */
    private Fighter getCurrentDefender(){

        // If the current Attackers is the Player Party return the first enemy
        if (side == 0) {
            return this.battleEnemies.get(0);
        } 

        // Otherwise return the first ally, or the character
        if (this.character.isPartyEmpty()) {
            return this.character;
        } else {
            return this.character.getAlly(0);
        }

    }

    /**
     * 'Kills' the given defender if they are below or equal to the LIVING_THRESHOLD.
     * @param enemy  The enemy that has been attacked.
     */
    private void tryKill(Fighter defender){
        if (defender.getHp() > LIVING_THRESHOLD) return;
        if (this.battleEnemies.contains(defender)){
            killEnemy(defender);
        } else if (this.character.checkPartyContains(defender)){
            killAlly(defender);
        } else {
            tryKillCharacter();
        }
    }

    /**
     * Kills all tranced enemies in the list of character's allies.
     */
    private void killTrancedEnemies(){
        List<TrancedEnemy> trancedEnemies = new ArrayList<TrancedEnemy>();
        for (Ally ally: this.character.getAllies()){
            if (ally instanceof TrancedEnemy) trancedEnemies.add((TrancedEnemy)ally);
        }
        for (TrancedEnemy enemy: trancedEnemies){
            killAlly(enemy);
        }
    }

    /**
     * Kills the ally by removing them from the character's list of allies,
     * if it is a tranced enemy, adds it to the list of defeatedEnemies as well.
     * @param defender The ally to be killed
     */
    private void killAlly(Fighter defender){
        if (defender instanceof TrancedEnemy){
            this.defeatedEnemies.add(((TrancedEnemy)defender).getPreviousVersion());
        }
        this.character.removeAlly(defender);
    }

    /**
     * Kills the enemy by removing them from the battleEnemies list and adding them
     * to the defeatedEnemies list.
     * @param defender The enemy to be killed
     */
    private void killEnemy(Fighter defender){
        this.battleEnemies.remove(defender);
        this.defeatedEnemies.add((BasicEnemy)defender);
    }

    /**
     * Kills the chracter, uses the One Ring to resurrect the character if they have it,
     * ends the game if they do not.
     */
    private void tryKillCharacter(){
        if (this.character.inventoryContainsOneRing()){
            this.character.consumeOneRing();
        } 
    }

    /**
     * Applies all nearby building buffs to the character
     */
    private int applyCampfireEffect(int damage){
        for (CampfireBuilding campfire: this.campfires){
            damage = campfire.applyEffect(damage);
        }
        return damage;
    }
}