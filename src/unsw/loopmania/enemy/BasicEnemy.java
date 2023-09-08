package unsw.loopmania.enemy;

import java.util.List;
import java.util.Random;

import unsw.loopmania.battle.DropRewards;
import unsw.loopmania.battle.DropTable;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.building.Building;
import unsw.loopmania.item.Item;
import unsw.loopmania.main.MovingEntity;
import unsw.loopmania.map.PathPosition;

/**
 * a basic form of enemy in the world
 */
public abstract class BasicEnemy extends MovingEntity implements Fighter, DropRewards{

    private static final int HEALTH_INCREMENT = 2;
    private static final int DAMAGE_INCREMENT = 1;
    
    private int battleRadius;
    private int supportRadius;

    private int maxHp;
    private int hp;
    private int baseDamage;
    private boolean isTranced;
    
    private DropTable dropTable;

    private int level = 0;
    private int startingHp;
    private int damage;

    // TODO = modify this, and add additional forms of enemy
    public BasicEnemy(PathPosition position, int battleRadius, int supportRadius, int hp, int baseDamage) {
        super(position);
        this.maxHp = HEALTH_INCREMENT * level + hp;
        this.startingHp = hp;
        this.hp = this.maxHp;
        this.baseDamage = baseDamage;
        this.isTranced = false; 
        this.battleRadius = battleRadius;
        this.supportRadius = supportRadius;
        this.dropTable = new DropTable();
        this.damage = DAMAGE_INCREMENT * level + baseDamage;
    }

    @Override
    public int getHp() {
        return this.hp;
    }

    public int getMaxHp(){
        return this.maxHp;
    }

    public int getBaseDamage() {
        return this.baseDamage;
    }

    /**
     * Returns the battle radius of the enemy.
     * @return
     */
    public int getBattleRadius() {
        return this.battleRadius;
    }

    /**
     * Returns the support radius of the enemy. 
     * Relevant if the character is within the battle radius of another enemy.
     * @return
     */
    public int getSupportRadius() {
        return this.supportRadius;
    }

    /**
     * move the enemy
     */
    public void move(long seed){
        int directionChoice = (new Random(seed)).nextInt(100);
        if (directionChoice < 50){
            moveUpPath();
        } else {
            moveDownPath();
        }
    }

    /**
     * Wrapper for moving the enemy
     */
    public void move(){
        move(System.currentTimeMillis());
    }

    @Override
    public void takeDamage(int damage){
        this.hp = (this.hp - damage > this.maxHp) ? this.maxHp : this.hp - damage; 
    }

    @Override
    public int getDamage(){
        return this.damage;
    }
    
    /**
     * Sets the enemy as tranced. 
     */
    public void trance() {
        this.isTranced = true; 
    }
    
    /**
     * Untrances the enemy. 
     */
    public void untrance() {
        this.isTranced = false; 
    }

    /**
     * Determines if the enemy is tranced. 
     * @return true if tranced. 
     */
    public boolean isTranced() {
        return this.isTranced; 
    }

/////////////////////////////////////////////////////////////
//// Normal Drop Rewards Interace Implementation
//// Can be overridden in specific enemies for custom drops

    @Override
    public int getXPDrop() {
        return this.dropTable.getXPDrop();
    }

    @Override
    public int getGoldDrop() {
        return this.dropTable.getGoldDrop();
    }

    @Override
    public List<Class<? extends Item>> getItemDrops() {
        return this.dropTable.getItemDrops(generateRandomInRange(DropTable.LUCK_RANGE));
    }

    @Override
    public List<Class<? extends Item>> getItemDrops(long luckSeed, long shuffleSeed) {
        return this.dropTable.getItemDrops(luckSeed, shuffleSeed);
    }

    @Override
    public List<Class<? extends Building>> getCardDrops() {
        return this.dropTable.getCardDrops(generateRandomInRange(DropTable.LUCK_RANGE));
    }

    @Override
    public  List<Class<? extends Building>> getCardDrops(long luckSeed, long shuffleSeed) {
        return this.dropTable.getCardDrops(luckSeed, shuffleSeed);
    }

    /**
     * Generate a random number in 0..range inclusive
     * @param range
     * @return
     */
    private int generateRandomInRange(int range) {
        Random rnd = new Random();
        return rnd.nextInt(range);
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int cycle){
        this.level = cycle;
        levelUp();
    }

    public void levelUp(){
        this.maxHp = HEALTH_INCREMENT * level + this.startingHp;
        this.hp = this.maxHp;
        this.damage = DAMAGE_INCREMENT * level + this.baseDamage;
    }

    public void incrementLevel(){
        level++;
    }
}
