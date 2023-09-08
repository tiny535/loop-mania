package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class BossEnemySpawnTest {

    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Creates an ordered path in a 17 by 18 grid of path tiles
        // The path in this grid is the same pattern used in the setup
        //      ^ > > > v
        //      ^ v < < <
        //      ^ > > > v
        //      ^ < < < <
        int width = 17;
        int height = 18;
        this.orderedPath = new ArrayList<>();

        orderedPath.add(new Pair<Integer, Integer>(0,0));

        // For the below path setting algorithm to work
        // The height MUST be EVEN, so the path loops back around
    
        // Sets a winding path going left to right, and then right to left
        for (int y = 0; y < height; y++){
            int x = (y % 2 == 0) ? 1 : width - 1;
            while (x > 0 && x < width){
                orderedPath.add(new Pair<Integer, Integer>(x,y));
                x = (y % 2 == 0) ? (x + 1) : (x - 1);
            }
        }

        // The first column is the last to be added to the path in reverse
        for (int y = height - 1; y >= 0; y--){
            orderedPath.add(new Pair<Integer, Integer>(0,y));
        }
    
        // Create a basic LoopManiaWorld
        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);

        // Create and link a character to the world
        Character character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(character);
    }

    @Test
    @DisplayName("Doggie will spawn on the 20th cycle")
    public void spawnDoggieCycleTwentyTest(){

        world.setCycleCount(DoggieEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();

        assertTrue(enemiesSpawnedContainsDoggieEnemy(enemiesSpawned));
    }

    @Test
    @DisplayName("Doggie will not spawn on a non-20th cycle")
    public void notSpawnDoggieNonCycleTwentyTest(){

        for (int i = 0; i < DoggieEnemy.SPAWNABLE_CYCLE; i++){
            world.setCycleCount(i);

            world.possiblySpawnEnemies();

            List<BasicEnemy> enemiesSpawned = world.getEnemies();

            assertTrue(!enemiesSpawnedContainsDoggieEnemy(enemiesSpawned));
        }
    }

    @Test
    @DisplayName("Doggie defeat will drop doggie coins")
    public void doggieDropsDoggieCoinsAfterDefeatTest(){

        world.setCycleCount(DoggieEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();
        assertTrue(enemiesSpawnedContainsDoggieEnemy(enemiesSpawned));

        assertTrue(world.getCharacter().getDoggieCoinQuantity() == 0); 

        for (BasicEnemy e : enemiesSpawned) {
            if (e instanceof DoggieEnemy) {
                world.killEnemy(e);
                break; 
            }
        }

        assertTrue(world.getCharacter().getDoggieCoinQuantity() > 0); 
    }

    @Test
    @DisplayName("ElanMuske will spawn on a 40th cycle if character has 10000 xp")
    public void spawnElanMuskeValidCondtions(){

        world.getCharacter().gainExperience(ElanMuskeEnemy.XP_REQUIRED_TO_SPAWN);
        world.setCycleCount(ElanMuskeEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();

        assertTrue(enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));
    }

    @Test
    @DisplayName("ElanMuske will not spawn on a 40th cycle if character does not have 10000 xp")
    public void notSpawnElanMuskeInvalidXp(){

        world.setCycleCount(ElanMuskeEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();

        assertTrue(!enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));
    }

    @Test
    @DisplayName("ElanMuske will not spawn on a non-40th cycle even if character has 10000 xp")
    public void spawnElanMuskeInvalidCycle(){

        world.getCharacter().gainExperience(ElanMuskeEnemy.XP_REQUIRED_TO_SPAWN);

        for (int i = 0; i < ElanMuskeEnemy.SPAWNABLE_CYCLE; i++){
            world.possiblySpawnEnemies();

            List<BasicEnemy> enemiesSpawned = world.getEnemies();

            assertTrue(!enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));
        }
    }
    
    @Test
    @DisplayName("ElanMuske will not spawn if all conditions are invalid")
    public void spawnElanMuskeAllConditionsInvalid(){

        for (int i = 0; i < ElanMuskeEnemy.SPAWNABLE_CYCLE; i++){
            world.possiblySpawnEnemies();

            List<BasicEnemy> enemiesSpawned = world.getEnemies();

            assertTrue(!enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));
        }
    }
    
    @Test
    @DisplayName("When ElanMuske spawns, the doggie coin value increases by 5x its current value")
    public void spawnElanMuskeChangesDoggieCoinValue(){
        int doggieCoinValuePreSpawn = world.getDoggieCoinValue();

        world.getCharacter().gainExperience(ElanMuskeEnemy.XP_REQUIRED_TO_SPAWN);
        world.setCycleCount(ElanMuskeEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();

        assertTrue(enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));

        int doggieCoinValuePostSpawn = world.getDoggieCoinValue();
        assertTrue(doggieCoinValuePostSpawn == doggieCoinValuePreSpawn * 5);
    }

    @Test 
    @DisplayName("Test that doggie coin value changes after ElanMuske defeated") 
    public void doggieCoinValueDecreasesAfterDefeatingElanMuskeTest() {
        world.getCharacter().gainExperience(ElanMuskeEnemy.XP_REQUIRED_TO_SPAWN);
        world.setCycleCount(ElanMuskeEnemy.SPAWNABLE_CYCLE);

        world.possiblySpawnEnemies();

        List<BasicEnemy> enemiesSpawned = world.getEnemies();
        assertTrue(enemiesSpawnedContainsElanMuskeEnemy(enemiesSpawned));

        int doggieCoinValue = world.getDoggieCoinValue();

        for (BasicEnemy e : enemiesSpawned) {
            if (e instanceof ElanMuskeEnemy) {
                world.killEnemy(e);
                break; 
            }
        }

        assertTrue(doggieCoinValue == world.getDoggieCoinValue() * 5);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //**************************** HELPER FUNCTIONS FOR THE TESTS ********************************//
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean enemiesSpawnedContainsDoggieEnemy(List<BasicEnemy> enemies){
        for (BasicEnemy enemy: enemies){
            if (enemy instanceof DoggieEnemy)
                return true;
        }
        return false;
    }

    private boolean enemiesSpawnedContainsElanMuskeEnemy(List<BasicEnemy> enemies){
        for (BasicEnemy enemy: enemies){
            if (enemy instanceof ElanMuskeEnemy)
                return true;
        }
        return false;
    }
}
