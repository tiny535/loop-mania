package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.building.Building;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.item.Item;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

import org.javatuples.Pair;

public class DeckTest {
    
    private List<Pair<Integer, Integer>> orderedPath;
    private Character character;
    private LoopManiaWorld world;

    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        this.character = new Character(new PathPosition(0, orderedPath));
        this.world = new LoopManiaWorld(0, 0, orderedPath, GameMode.NORMAL);
        this.world.setCharacter(character);
    } 

    @Test
    @DisplayName("Test card is added to the deck when not full")
    public void addCardToDeckWithSpace(){
        loadCard(TowerBuilding.class);

        assertTrue(!character.isDeckEmpty());
    }

    @Test
    @DisplayName("Test card can be converted to gold, xp, or items")
    public void addCardToFullDeckConvertsOldestCard(){
        // Adds 11 cards, the 11th one should add gold to the character with 
        // 8 as the seed
        Card oldestCard = loadCard(TowerBuilding.class);
        for (int i = 0; i < 1000; i++){
            loadCard(TowerBuilding.class);
        }

        assertTrue(!character.isCardContained(oldestCard));
        assertTrue(character.getGoldQuantity() > 0);
        assertTrue(character.getXPQuantity() > 0);
        assertTrue(!character.isInventoryEmpty());
    }

//----------------------- HELPER METHODS TO SIMULATE WORLD CONTROLLER ------------------------------
    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private Card loadCard(Class<? extends Building> buildingClass) {
        Card card = world.addCard(buildingClass);
        if (card == null){
            // Give the character either gold, xp, or an item
            convertOldestCard();
            card = world.addCard(buildingClass);
        }
        return card;
    }

    private void convertOldestCard(){
        List<Class<? extends Item>> items = world.convertOldestCard();
        if (items != null){
            for (Class<? extends Item> item: items){
                loadItem(item);
            }
        }
    }

    /**
     * load a item from the world, and pair it with an image in the GUI
     */
    private void loadItem(Class<? extends Item> itemType){
        // start by getting first available coordinates
        Item item = world.addUnequippedItem(itemType);
        if (item == null){
            // Give some cash/experience rewards for the discarding of the oldest item
            world.convertOldestItem();
            item = world.addUnequippedItem(itemType);
        }
        assert item != null;
    }
}
