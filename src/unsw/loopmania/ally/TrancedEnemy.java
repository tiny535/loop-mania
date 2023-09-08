package unsw.loopmania.ally;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.enemy.BasicEnemy;

public class TrancedEnemy extends Ally{
    
    private static final int MAX_NUM_ROUNDS_TRANCED = 1;

    private int numRoundsLeftTranced;
    private BasicEnemy previousVersion;

    public TrancedEnemy(SimpleIntegerProperty x, SimpleIntegerProperty y, BasicEnemy enemy){
        super(x, y);
        this.numRoundsLeftTranced = MAX_NUM_ROUNDS_TRANCED;
        this.previousVersion = enemy;
    }

    @Override
    public int getHp(){
        return this.previousVersion.getHp();
    }

    @Override
    public int getDamage(){
        numRoundsLeftTranced--;
        return this.previousVersion.getDamage();
    }

    @Override
    public void takeDamage(int damage){
        this.previousVersion.takeDamage(damage);
    }

    public BasicEnemy getPreviousVersion(){
        return this.previousVersion;
    }

    public boolean isStillTranced(){
        return this.numRoundsLeftTranced > 0;
    }
}
