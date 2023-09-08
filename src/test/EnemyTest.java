package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.building.Building;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.Item;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class EnemyTest {

    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
    } 

    @Test
    @DisplayName("Check starting hp, damage, and radii of Slugs")
    public void slugStartingStatistics() {
        SlugEnemy slug = new SlugEnemy(new PathPosition(0, orderedPath));
        
        assertEquals(slug.getHp(), 10);
        assertEquals(slug.getBaseDamage(), 5);
        assertEquals(slug.getBattleRadius(), 2);
        assertEquals(slug.getSupportRadius(), 2);
    }

    @Test
    @DisplayName("Check starting hp, damage, and radii of Zombies")
    public void zombieStartingStatistics() {
        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));
        
        assertEquals(zombie.getHp(), 20);
        assertEquals(zombie.getBaseDamage(), 10);
        assertEquals(zombie.getBattleRadius(), 3);
        assertEquals(zombie.getSupportRadius(), 0);
    }

    @Test
    @DisplayName("Check starting hp, damage, and radii of Vampires")
    public void vampireStartingStatistics() {
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        
        assertEquals(vampire.getHp(), 60);
        assertEquals(vampire.getBaseDamage(), 10);
        assertEquals(vampire.getBattleRadius(), 3);
        assertEquals(vampire.getSupportRadius(), 5);
    }

    @Test
    @DisplayName("Move enemy test") 
    public void moveEnemyTest() {
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        
        vampire.move();
        vampire.move(10);

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        GameMode gameMode = GameMode.NORMAL;
        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath, gameMode);
        vampire.move(world);
    }

    @Test
    @DisplayName("Trance enemy test") 
    public void tranceEnemyTest() {
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));

        vampire.trance(); 
        assertTrue(vampire.isTranced());
    }

    @Test
    @DisplayName("Untrance enemy test") 
    public void untranceEnemyTest() {
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));

        vampire.trance(); 
        assertTrue(vampire.isTranced());

        vampire.untrance(); 
        assertTrue(!vampire.isTranced());
    }

    @Test
    @DisplayName("Testing that enemies will have drops")
    public void noDropsWhenNoEnemiesUndefeatedTest() {
        List<Class<? extends Item>> itemDrops = new ArrayList<>();
        List<Class<? extends Building>> cardDrops = new ArrayList<>();

        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));

        // Invoke the getItemDrops lots of times 
        for (int i = 0; i < 10000; i++) {
            itemDrops.addAll(vampire.getItemDrops());
            cardDrops.addAll(vampire.getCardDrops());
        }

        assertTrue(!itemDrops.isEmpty());
        assertTrue(!cardDrops.isEmpty());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //*************************** Testing Milestone 3 Enemies ************************************//
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test   
    @DisplayName("Check starting hp, damage, and radii of Doggie")
    public void doggieStartingStatistcs(){
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));

        assertEquals(doggie.getHp(), 80);
        assertEquals(doggie.getBaseDamage(), 12);
        assertEquals(doggie.getBattleRadius(), 2);
        assertEquals(doggie.getSupportRadius(), 2);
    }

    @Test   
    @DisplayName("Check starting hp, damage, and radii of ElanMuske")
    public void elanMuskeStartingStatistcs(){
        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));

        assertEquals(elanMuske.getHp(), 100);
        assertEquals(elanMuske.getBaseDamage(), 15);
        assertEquals(elanMuske.getBattleRadius(), 2);
        assertEquals(elanMuske.getSupportRadius(), 2);
    }
}
