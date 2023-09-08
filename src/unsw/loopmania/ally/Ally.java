package unsw.loopmania.ally;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.main.StaticEntity;

public class Ally extends StaticEntity implements Fighter{
    
    private static final String IMAGE_SOURCE = "ally";
    
    private static final int STARTING_HP = 20;
    private static final int STARTING_DMG = 5;

    private int hp;
    private int damage;
    private boolean isZombie;

    public Ally(SimpleIntegerProperty x, SimpleIntegerProperty y){
        super(x, y);
        this.hp = STARTING_HP;
        this.damage = STARTING_DMG;
        this.isZombie = false;
    }

    @Override
    public int getHp(){
        return this.hp;
    }

    @Override
    public void takeDamage(int damage){
        this.hp = this.hp - damage;
    }

    @Override
    public int getDamage(){
        return this.damage;
    }

    public void makeZombie(){
        this.isZombie = true;
    }

    public boolean isZombie(){
        return this.isZombie;
    }

    /**
     * Heals ally to max HP
     */
    public void heal() {
        this.hp = STARTING_HP;
    }

    @Override
    public String getImageSrc() {
        return Ally.IMAGE_SOURCE;
    }
}
