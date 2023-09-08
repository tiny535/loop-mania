package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.ally.Ally;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.main.Character;
import unsw.loopmania.map.PathPosition;

public class FighterTest {
    
    private List<Pair<Integer, Integer>> orderedPath;
    
    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
    } 

    @Test
    @DisplayName("Test slug getDamage and takeDamage")
    public void SlugFighterTest(){
        SlugEnemy slug = new SlugEnemy(new PathPosition(0, orderedPath));
        
        assertEquals(5, slug.getDamage());
        
        slug.takeDamage(5);

        assertEquals(5, slug.getHp());
    }

    @Test
    @DisplayName("Test zombie getDamage and takeDamage")
    public void ZombieFighterTest(){
        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));

        assertEquals(10, zombie.getDamage());

        zombie.takeDamage(5);

        assertEquals(15, zombie.getHp());
    }

    @Test
    @DisplayName("Test vampire getDamage and takeDamage")
    public void VampireFighterTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));

        assertEquals(10, vampire.getDamage());

        vampire.takeDamage(5);

        assertEquals(55, vampire.getHp());
    }

    @Test
    @DisplayName("Test character getDamage and takeDamage")
    public void CharacterFighterTest(){
        Character character = new Character(new PathPosition(0, orderedPath));

        assertEquals(5, character.getDamage());

        character.takeDamage(5);

        assertEquals(95, character.getHp());
    }

    @Test
    @DisplayName("Test ally getDamage and takeDamage")
    public void AllyFighterTest(){
        Ally ally = new Ally(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        assertEquals(5, ally.getDamage());

        ally.takeDamage(5);

        assertEquals(15, ally.getHp());
    }

}
