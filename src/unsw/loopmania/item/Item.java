package unsw.loopmania.item;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.main.StaticEntity;

public abstract class Item extends StaticEntity {

    private static final int GOLD_TO_XP_RATIO = 5;

    private String name;
    private int buyPrice;
    private int attackEffect;
    private double defenceEffect;
    private String description;

    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y, String name, int price, int attack, double defence, String description) {
        super(x, y);

        this.name = name;
        this.buyPrice = price;
        this.attackEffect = attack;
        this.defenceEffect = defence;
        this.description = description;
    }

    /**
     * Getter for name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for attackEffect
     * @return
     */
    public int getAttackEffect() {
        return this.attackEffect;
    }

    /**
     * Getter for defenceEffect
     * @return
     */
    public double getDefenceEffect() {
        return this.defenceEffect;
    }

    /**
     * Getter for description
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the buy price of the item (ie quantity of gold required to purchase the item)
     * @return
     */
    public int getBuyPrice() {
        return this.buyPrice; 
    }
    
    /**
     * Returns the sell price of the item (ie quantity of gold received from selling the item)
     * The sell price is always half the buy price, unless the item is gold
     * @return
     */
    public int getSellPrice() {
        return this.buyPrice / 2; 
    }

    /**
     * Returns the gold conversion of the item when it's the oldest and is recycled
     * @return
     */
    public int getConversionPrice() {
        return this.getSellPrice();
    }

    /**
     * Returns the xp conversion of the item when it's the oldest and is recycled
     * @return
     */
    public int getConversionXP() {
        return this.getSellPrice() / Item.GOLD_TO_XP_RATIO;
    }

    /**
     * Return whether or not the item is protective, set to false as default 
     */
    public boolean isProtectiveItem(){
        return false;
    }

    public boolean isRareItem(){
        return false;
    }
}
