package unsw.loopmania.item;

public interface EquipableItem {

    public abstract BodyPart getBodyPart();
    public abstract int getAttackEffect();
    public abstract int getDefenceEffect(int enemyDamage); 
}
