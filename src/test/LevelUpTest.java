package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class LevelUpTest {
    
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(0,1));
        orderedPath.add(new Pair<Integer, Integer>(1,1));
        orderedPath.add(new Pair<Integer, Integer>(1,2));
    } 

    @Test
    @DisplayName("Check character level up values")
    public void checkCharacterLevelUpValues(){

        Character character = new Character(new PathPosition(0, orderedPath));

        character.gainExperience(Character.BASE_LEVEL_UP_BOUNDARY);

        assertEquals(105, character.getMaxHp());
        assertEquals(105, character.getHp());
        assertEquals(7, character.getDamage());
        assertEquals(2, character.getLevel());
    }

    @Test
    @DisplayName("Check level up boundaries")
    public void checkLevelUpBoundaries(){

        Character character = new Character(new PathPosition(0, orderedPath));

        // Requires total 100xp to reach level 2, and a total of 400xp to reach level 3.
        assertEquals(1, character.getLevel());

        character.gainExperience(Character.BASE_LEVEL_UP_BOUNDARY);
        
        assertEquals(2, character.getLevel());

        character.gainExperience(Character.BASE_LEVEL_UP_BOUNDARY);
        
        assertEquals(2, character.getLevel());

        character.gainExperience(Character.BASE_LEVEL_UP_BOUNDARY);
        
        assertEquals(2, character.getLevel());
        
        character.gainExperience(Character.BASE_LEVEL_UP_BOUNDARY);
    
        assertEquals(3, character.getLevel());
    }

    @Test
    @DisplayName("Check enemy level up values")
    public void checkEnemyLevelUpValues(){
        LoopManiaWorld world = new LoopManiaWorld(2, 2, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        BasicEnemy enemy = new SlugEnemy(new PathPosition(0, orderedPath));

        world.setCharacter(character);
        world.addEnemy(enemy);
        for (int i = 0; i < orderedPath.size(); i++){
            world.runTickMoves();
        }

        assertEquals(12, enemy.getMaxHp());
        assertEquals(6, enemy.getDamage());
        assertEquals(1, enemy.getLevel());
    }

    @Test
    @DisplayName("Check enemy levels up every cycle")
    public void checkEnemyLevelUpPerCycle(){
        LoopManiaWorld world = new LoopManiaWorld(2, 2, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        BasicEnemy enemy = new SlugEnemy(new PathPosition(0, orderedPath));

        world.setCharacter(character);
        world.addEnemy(enemy);
        int expectedLevel = 0;
        for (int i = 0; i < 4*orderedPath.size(); i++){
            world.runTickMoves();
            if ((i + 1) % orderedPath.size() == 0){
                expectedLevel++;
                assertEquals(expectedLevel, enemy.getLevel());
            }
        }
    }
}
