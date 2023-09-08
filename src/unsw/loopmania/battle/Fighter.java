package unsw.loopmania.battle;

public interface Fighter {
    
    // Damage to be done
    public abstract void takeDamage(int damage);

    // Amount of damage to be dealt
    public abstract int getDamage();

    // Gets the remaining amount of Health points from the fighter.
    public abstract int getHp();
}
