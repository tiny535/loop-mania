package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Shield;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.map.PathPosition;

public class SpecialAttackerTest {
    
    // All seeds are less than their named value, and greater than the one before them
    // e.g LESS_THAN_50_SEED's random value will be: 40 < x < 50
    private static final long LESS_THAN_30_SEED = 19; // 20 < x < 30
    private static final long LESS_THAN_40_SEED = 11; // 30 < x < 40
    private static final long LESS_THAN_50_SEED = 15; // 40 < x < 50

    private List<Pair<Integer, Integer>> orderedPath;
    
    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
    }

    // The character's special attack is tested with the item tests as
    // it's special attack is based off the weapon it currently has equipped

    @Test
    @DisplayName("Test zombie bite success on ally")
    public void ZombieSuccessSpecialAttackTest(){
        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));
        Ally ally = new Ally(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        zombie.specialAttack(ally, LESS_THAN_30_SEED);

        assertTrue(ally.isZombie());
    }

    @Test
    @DisplayName("Test zombie bite success on ally")
    public void ZombieFailSpecialAttackTest(){
        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));
        Ally ally = new Ally(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        zombie.specialAttack(ally, LESS_THAN_40_SEED);

        assertFalse(ally.isZombie());
    }

    @Test
    @DisplayName("Test vampire critical success on character")
    public void VampireSuccessSpecialAttackTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(1, orderedPath));

        // Character will take bonus damage on the follow up special attack,
        // whether the next special attack is not successful.
        vampire.specialAttack(character, LESS_THAN_40_SEED);

        vampire.specialAttack(character, LESS_THAN_50_SEED);

        assertEquals(97, character.getHp());
    }

    @Test
    @DisplayName("Test vampire critical fails on character")
    public void VampiteFailSpecialAttackTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(1, orderedPath));

        // Character will take no bonus damage on the follow-up special attack.
        vampire.specialAttack(character, LESS_THAN_50_SEED);

        vampire.specialAttack(character, LESS_THAN_50_SEED);

        assertEquals(100, character.getHp());
    }

    @Test
    @DisplayName("Test vampire critical hits on Character wearing shield successfully occurs")
    public void VampiteFailSpecialAttackWithShieldSuccessTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(1, orderedPath));
        
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Inventory inventory = character.getInventory();
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        // Character will take no bonus damage on the follow-up special attack.
        vampire.specialAttack(character, LESS_THAN_50_SEED);

        vampire.specialAttack(character, LESS_THAN_50_SEED);

        assertEquals(100, character.getHp());
    }

    @Test
    @DisplayName("Test vampire critical hits on Character wearing shield fails to occurs")
    public void VampiteFailSpecialAttackWithShieldFailsTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(1, orderedPath));
        
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Inventory inventory = character.getInventory();
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        // Character will take no bonus damage on the follow-up special attack.
        vampire.specialAttack(character, 50);

        vampire.specialAttack(character, 50);

        assertEquals(100, character.getHp());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //*************************** Testing Milestone 3 Enemies ************************************//
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test doggie successful special attack")
    public void doggieSpecialAttackSuccess(){
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(0, orderedPath));

        doggie.specialAttack(character, LESS_THAN_30_SEED);

        assertTrue(character.getIsStunned());

        // Assume two attacks have been done, character should become unstunned
        character.decrementStunRounds();
        character.decrementStunRounds();

        assertTrue(!character.getIsStunned());
    }

    @Test
    @DisplayName("Test doggie falied special attack")
    public void doggieSpecialAttackFailed(){
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(0, orderedPath));

        doggie.specialAttack(character, LESS_THAN_40_SEED);

        assertTrue(!character.getIsStunned());
    }
    
    @Test
    @DisplayName("Test elanMuske successful special attack, heals damaged enemy")
    public void elanMuskeSpecialAttackSuccess(){
        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        
        vampire.takeDamage(20); // More than 1/4 of his hp

        elanMuske.specialAttack(vampire, LESS_THAN_40_SEED);

        assertEquals(55, vampire.getHp());   
    }

    @Test
    @DisplayName("Test elanMuske successful special attack does not heal enemy over their maxHp")
    public void elanMuskeSpecialAttackSuccessMaxHp(){
        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));

        elanMuske.specialAttack(vampire, LESS_THAN_40_SEED);

        assertEquals(60, vampire.getHp());   
    }
}
