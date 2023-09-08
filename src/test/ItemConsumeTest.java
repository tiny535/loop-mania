package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Stake;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.map.PathPosition;

public class ItemConsumeTest {
    private Character character;
    private Inventory inventory;

    @BeforeEach
    public void setup() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        this.character = new Character(new PathPosition(0, orderedPath));

        this.inventory = character.getInventory();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////// Potions

    @Test 
    @DisplayName("Trying to consume a health potion when there is no health potion in the inventory") 
    public void consumingHealthPotionNotInInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        
        character.consumeHealthPotion();
        int currentHP = this.character.getHp();

        assertEquals(currentHP, 100);
    }

    @Test 
    @DisplayName("Consuming a health potion on full HP") 
    public void consumingHealthPotionOnFullHPTest() {
        Potion potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(potion, character);

        character.consumeHealthPotion();

        int currentHP = character.getHp();
        assertEquals(currentHP, 100);
    }

    @Test 
    @DisplayName("Consuming a health potion on high HP") 
    public void consumingHealthPotionOnHighHPTest() {
        Potion potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(potion, character);

        character.takeDamage(30);
        assertEquals(character.getHp(), 70);

        character.consumeHealthPotion();
        assertEquals(character.getHp(), 100);
    } 

    @Test 
    @DisplayName("Consuming a health potion on low HP") 
    public void consumingHealthPotionOnLowHPTest() {
        Potion potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(potion, character);

        character.takeDamage(80);
        assertEquals(character.getHp(), 20);

        character.consumeHealthPotion();
        assertEquals(character.getHp(), 80);
    }  

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////// OneRing 

    @Test 
    @DisplayName("Trying to consume the one ring when there is no one ring in the inventory") 
    public void consumingOneRingNotInInventoryTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        
        assertEquals(inventory.inventoryContainsOneRing(), false);
        character.consumeOneRing();
        assertEquals(character.getHp(), 100);
    }

    @Test 
    @DisplayName("Consuming the one ring on full HP") 
    public void consumingOneRingOnFullHPTest() {
        OneRing oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);
        assertEquals(inventory.inventoryContainsOneRing(), true);

        character.consumeOneRing();
        assertEquals(character.getHp(), 100);
    }

    @Test 
    @DisplayName("Consuming the one ring on high HP") 
    public void consumingOneRingOnHighHPTest() {
        OneRing oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);

        character.takeDamage(20);
        assertEquals(character.getHp(), 80);

        character.consumeOneRing();
        assertEquals(character.getHp(), 100);
    } 

    @Test 
    @DisplayName("Consuming the one ring on low HP") 
    public void consumingOneRingOnLowHPTest() {
        OneRing oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);

        character.takeDamage(80);
        assertEquals(character.getHp(), 20);

        character.consumeOneRing();
        assertEquals(character.getHp(), 100);
    }  

    @Test 
    @DisplayName("Consuming the one ring on no HP") 
    public void consumingOneRingOnNoHPTest() {
        OneRing oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oneRing, character);

        character.takeDamage(100);
        assertEquals(character.getHp(), 0);

        character.consumeOneRing();
        assertEquals(character.getHp(), 100);
    }  
}
