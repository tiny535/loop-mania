package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class EnemyMovementTest {

    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    private void createBasicLoop() {
        // Creates an ordered path in a 10 by 10 grid of path tiles
        // The path in this grid is the same pattern used in the setup
        //      ^ > > > v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ < < < <
        int width = 10;
        int height = 10;
        this.orderedPath = new ArrayList<>();

        orderedPath.add(new Pair<Integer, Integer>(0,0));
        
        // Sets a simple path loop
        for (int y = 0; y < height; y++){
            if (y == 0) {
                for (int x = 1; x < width; x++) {
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
        for (int y = height - 1; y > 0; y--){
            orderedPath.add(new Pair<Integer, Integer>(0,y));
        }
        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
    }

    @Test
    @DisplayName("Test slug movement")
    public void slugMovementTest(){
        SlugEnemy slug = new SlugEnemy(new PathPosition(0, orderedPath));
        long moveUpSeed = 2;
        long moveDownSeed = 1;

        // Slug moves in the following pattern: up, down, down, up, down

        slug.move(moveUpSeed);
        assertEquals(0, slug.getX()); 
        assertEquals(1, slug.getY()); 

        slug.move(moveDownSeed);
        assertEquals(0, slug.getX()); 
        assertEquals(0, slug.getY()); 

        slug.move(moveDownSeed);
        assertEquals(1, slug.getX()); 
        assertEquals(0, slug.getY()); 

        slug.move(moveUpSeed);
        assertEquals(0, slug.getX()); 
        assertEquals(0, slug.getY()); 

        slug.move(moveDownSeed);
        assertEquals(1, slug.getX()); 
        assertEquals(0, slug.getY()); 
    }

    @Test
    @DisplayName("Test zombie movement")
    public void zombieMovementTest(){
        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));
        long moveUpSeed = 2;
        long moveDownSeed = 11;
        long stationarySeed = 17;

        // Zombie moves in the following pattern: up, down, up, stationary, down, stationary

        zombie.move(moveUpSeed);
        assertEquals(0, zombie.getX());
        assertEquals(1, zombie.getY());

        zombie.move(moveDownSeed);
        assertEquals(0, zombie.getX());
        assertEquals(0, zombie.getY());

        zombie.move(moveUpSeed);
        assertEquals(0, zombie.getX());
        assertEquals(1, zombie.getY());

        zombie.move(stationarySeed);
        assertEquals(0, zombie.getX());
        assertEquals(1, zombie.getY());

        zombie.move(moveDownSeed);
        assertEquals(0, zombie.getX());
        assertEquals(0, zombie.getY());

        zombie.move(stationarySeed);
        assertEquals(0, zombie.getX());
        assertEquals(0, zombie.getY());
    }

    @Test
    @DisplayName("Test vampire basic movement")
    public void vampireMovementTest(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        long moveOneDownSeed = 1;
        long moveTwoDownSeed = 3;
        long moveOneUpSeed = 8;
        long moveTwoUpSeed = 6;

        // Vampire moves in the following pattern: 2up, down, 2up, 2down, up, down

        vampire.move(this.world, moveTwoUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(2, vampire.getY());

        vampire.move(this.world, moveOneDownSeed);
        assertEquals(0, vampire.getX());
        assertEquals(1, vampire.getY());

        vampire.move(this.world, moveTwoUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(3, vampire.getY());

        vampire.move(this.world, moveTwoDownSeed);
        assertEquals(0, vampire.getX());
        assertEquals(1, vampire.getY());

        vampire.move(this.world, moveOneUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(2, vampire.getY());

        vampire.move(this.world, moveOneDownSeed);
        assertEquals(0, vampire.getX());
        assertEquals(1, vampire.getY());
    }

    @Test
    @DisplayName("Test vampire returns to original spot if new location is in range of a campfire")
    public void vampireRunFromCampfire(){
        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        Character character = new Character(new PathPosition(8, orderedPath));
        this.world.setCharacter(character);
        Card campfireCard = world.loadCard(new CampfireBuilding());
        world.convertCardToBuildingByCoordinates(campfireCard.getX(), campfireCard.getY(), 1, 1);
        
        long moveTwoDownSeed = 3;
        long moveOneUpSeed = 8;
        long moveTwoUpSeed = 6;

        vampire.move(this.world, moveTwoDownSeed);
        assertEquals(0, vampire.getX());
        assertEquals(0, vampire.getX());

        vampire.move(this.world, moveOneUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(0, vampire.getX());

        vampire.move(this.world, moveTwoUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(0, vampire.getX());

        vampire.move(this.world, moveOneUpSeed);
        assertEquals(0, vampire.getX());
        assertEquals(0, vampire.getX());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //*************************** Testing Milestone 3 Enemies ************************************//
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test doggie movement")
    public void doggieMovementTest(){
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        long moveUpSeed = 2;
        long moveDownSeed = 1;

        // Doggie moves in the following pattern: up, down, down, up, down

        doggie.move(moveUpSeed);
        assertEquals(0, doggie.getX()); 
        assertEquals(1, doggie.getY()); 

        doggie.move(moveDownSeed);
        assertEquals(0, doggie.getX()); 
        assertEquals(0, doggie.getY()); 

        doggie.move(moveDownSeed);
        assertEquals(1, doggie.getX()); 
        assertEquals(0, doggie.getY()); 

        doggie.move(moveUpSeed);
        assertEquals(0, doggie.getX()); 
        assertEquals(0, doggie.getY()); 

        doggie.move(moveDownSeed);
        assertEquals(1, doggie.getX()); 
        assertEquals(0, doggie.getY()); 
    }

    @Test
    @DisplayName("Test elanMuske movement")
    public void elanMuskeMovementTest(){
        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        long moveUpSeed = 2;
        long moveDownSeed = 1;

        // ElanMuske moves in the following pattern: up, down, down, up, down

        elanMuske.move(moveUpSeed);
        assertEquals(0, elanMuske.getX()); 
        assertEquals(1, elanMuske.getY()); 

        elanMuske.move(moveDownSeed);
        assertEquals(0, elanMuske.getX()); 
        assertEquals(0, elanMuske.getY()); 

        elanMuske.move(moveDownSeed);
        assertEquals(1, elanMuske.getX()); 
        assertEquals(0, elanMuske.getY()); 

        elanMuske.move(moveUpSeed);
        assertEquals(0, elanMuske.getX()); 
        assertEquals(0, elanMuske.getY()); 

        elanMuske.move(moveDownSeed);
        assertEquals(1, elanMuske.getX()); 
        assertEquals(0, elanMuske.getY()); 
    }
}
