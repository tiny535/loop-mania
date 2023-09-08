package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.building.Building;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class BuildingsSpawnEnemiesTest {
    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    private void setup() {
        // Creates an ordered path in a 5 by 5 grid of path tiles
        // The path in this grid is the same pattern used in the setup
        //      ^ > > > v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ < < < <
        int width = 5;
        int height = 5;
        this.orderedPath = new ArrayList<>();

        orderedPath.add(new Pair<Integer, Integer>(0,0));
        
        // Sets a simple path loop
        for (int y = 0; y < height; y++){
            if (y == 0) {
                for (int x = 0; x < width; x++) {
                    orderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            }

            else if (y == height - 1) {
                for (int x = width - 1; x >= 0; x--) {
                    orderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            }

            else {
                orderedPath.add(new Pair<Integer, Integer>(width - 1,y));
            }
        }

        // The first column is the last to be added to the path in reverse
        for (int y = height - 1; y >= 0; y--){
            orderedPath.add(new Pair<Integer, Integer>(0,y));
        }

        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(character);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************************  Test Vampire Spawning ************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test whether vampire is spawned at the correct time from a VampireCastle")
    public void testSpawnVampireCastle() {
        Card card = world.loadCard(new VampireCastleBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        building.setCycleSpawn(1);

        for (int i = 0; i < 6; i++) {
            assertEquals(null, building.spawnEnemy(i, world.getOrderedPath()));
        }

        BasicEnemy enemy = building.spawnEnemy(6, world.getOrderedPath());
        assertTrue(enemy instanceof VampireEnemy);
        assertTrue(enemy.getX() == 2);
        assertTrue(enemy.getY() == 0);

        for (int i = 6; i < 11; i++) {
            assertEquals(null, building.spawnEnemy(i, world.getOrderedPath()));
        }

        enemy = building.spawnEnemy(11, world.getOrderedPath());
        assertTrue(enemy instanceof VampireEnemy);
        assertTrue(enemy.getX() == 2);
        assertTrue(enemy.getY() == 0);
    }

    @Test
    @DisplayName("Test whether vampire is spawned at the correct location when VampireCastle is placed adjacent two path tiles")
    public void testSpawnTwoPathTilesVampireCastle() {
        Card card = world.loadCard(new VampireCastleBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        building.setCycleSpawn(1);

        BasicEnemy enemy = building.spawnEnemy(6, world.getOrderedPath());
        assertTrue(enemy instanceof VampireEnemy);
        assertTrue(enemy.getX() == 0);
        assertTrue(enemy.getY() == 1);
    }

    @Test
    @DisplayName("Test vampire spawning for possiblySpawnEnemies()")
    public void testPossiblySpawnEnemiesVampireCastle() {
        Card card = world.loadCard(new VampireCastleBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        building.setCycleSpawn(1);
        world.setCycleCount(1);

        List<BasicEnemy> spawnedEnemies = world.possiblySpawnEnemies();
        for (BasicEnemy e : spawnedEnemies) {
            assertFalse(e instanceof VampireEnemy);
        }

        world.setCycleCount(6);
        spawnedEnemies = world.possiblySpawnEnemies();
        boolean vampireSpawned = false;
        for (BasicEnemy e : spawnedEnemies) {
            if (e instanceof VampireEnemy) {
                if (vampireSpawned) {
                    vampireSpawned = false;
                    break;
                }
                vampireSpawned = true;
            }
        }
        assertTrue(vampireSpawned);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// *****************************************  Test Zombie Spawning ************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test whether zombie is spawned at the correct time from a ZombiePit")
    public void testSpawnZombiePit() {
        Card card = world.loadCard(new ZombiePitBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        building.setCycleSpawn(1);
        assertEquals(null, building.spawnEnemy(1, world.getOrderedPath()));

        for (int i = 2; i < 6; i++) {
            BasicEnemy enemy = building.spawnEnemy(i, world.getOrderedPath());
            assertTrue(enemy instanceof ZombieEnemy);
            assertTrue(enemy.getX() == 2);
            assertTrue(enemy.getY() == 0);
            assertEquals(null, building.spawnEnemy(i, world.getOrderedPath()));
        }
    }

    @Test
    @DisplayName("Test whether zombie is spawned at the correct location when ZombiePit is placed adjacent two path tiles")
    public void testSpawnTwoPathTilesZombiePit() {
        Card card = world.loadCard(new ZombiePitBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        building.setCycleSpawn(1);

        BasicEnemy enemy = building.spawnEnemy(2, world.getOrderedPath());
        assertTrue(enemy instanceof ZombieEnemy);
        assertTrue(enemy.getX() == 0);
        assertTrue(enemy.getY() == 1);
    }

    @Test
    @DisplayName("Test zombie spawning for possiblySpawnEnemies()")
    public void testPossiblySpawnEnemiesZombiePit() {
        Card card = world.loadCard(new ZombiePitBuilding());
        Building building = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        building.setCycleSpawn(1);
        world.setCycleCount(1);

        List<BasicEnemy> spawnedEnemies = world.possiblySpawnEnemies();
        for (BasicEnemy e : spawnedEnemies) {
            assertFalse(e instanceof ZombieEnemy);
        }

        world.setCycleCount(2);
        spawnedEnemies = world.possiblySpawnEnemies();
        boolean zombieSpawned = false;
        for (BasicEnemy e : spawnedEnemies) {
            if (e instanceof ZombieEnemy) {
                if (zombieSpawned) {
                    zombieSpawned = false;
                    break;
                }
                zombieSpawned = true;
            }
        }
        assertTrue(zombieSpawned);
    }
}
