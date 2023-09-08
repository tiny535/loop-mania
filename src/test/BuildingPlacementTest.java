package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class BuildingPlacementTest {
    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;
    private int width;
    private int height;

    private void createBasicLoop() {
        // Creates an ordered path in a 5 by 5 grid of path tiles
        // The path in this grid is the same pattern used in the setup
        //      ^ > > > v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ < < < <
        this.width = 5;
        this.height = 5;
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
    }

    public void basicLoopNoMiddleTile() {
        createBasicLoop();

        // Create a basic LoopManiaWorld
        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(character);
    }

    public void basicLoopMiddleTile() {
        createBasicLoop();
        int x = (int) Math.ceil(width/2);
        int y = (int) Math.ceil(height/2);
        orderedPath.add(new Pair<Integer, Integer>(x, y));
        
        // Create a basic LoopManiaWorld
        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
        Character character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(character);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ********************************  Test VampireCastle, ZombiePit, Tower ******************************* //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing VampireCastle on non-path tile adjacent to one path tile")
    public void testAdjacentOnePathTileVampireCastle() {
        basicLoopNoMiddleTile();

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing VampireCastle on non-path tile adjacent to two path tiles")
    public void testAdjacentTwoPathTileVampireCastle() {
        basicLoopNoMiddleTile();

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing VampireCastle on path tile adjacent to one path tile")
    public void testPathTileAdjacentVampireCastle() {
        basicLoopNoMiddleTile();

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        assertFalse(building.canPlaceBuilding(1, 0, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 0);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing VampireCastle on path tile not adjacent to any path tiles")
    public void testPathTileNotAdjacentVampireCastle() {
        basicLoopMiddleTile();

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing VampireCastle on non-path tile not adjacent to any path tiles")
    public void testNotAdjacentVampireCastle() {
        basicLoopNoMiddleTile();

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing VampireCastle on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingVampireCastle() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new VampireCastleBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);

        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new VampireCastleBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing ZombiePit on non-path tile adjacent to one path tile")
    public void testAdjacentOnePathTileZombiePit() {
        basicLoopNoMiddleTile();

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing ZombiePit on non-path tile adjacent to two path tiles")
    public void testAdjacentTwoPathTileZombiePit() {
        basicLoopNoMiddleTile();

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing ZombiePit on path tile adjacent to one path tile")
    public void testPathTileAdjacentZombiePit() {
        basicLoopNoMiddleTile();

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        assertFalse(building.canPlaceBuilding(1, 0, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 0);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing ZombiePit on path tile not adjacent to any path tiles")
    public void testPathTileNotAdjacentZombiePit() {
        basicLoopMiddleTile();

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing ZombiePit on non-path tile not adjacent to any path tiles")
    public void testNotAdjacentZombiePit() {
        basicLoopNoMiddleTile();

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing ZombiePit on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingZombiePit() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new ZombiePitBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);

        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new ZombiePitBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing Tower on non-path tile adjacent to one path tile")
    public void testAdjacentOnePathTileTower() {
        basicLoopNoMiddleTile();

        Building building = new TowerBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Tower on non-path tile adjacent to two path tiles")
    public void testAdjacentTwoPathTileTower() {
        basicLoopNoMiddleTile();

        Building building = new TowerBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Tower on path tile adjacent to one path tile")
    public void testPathTileAdjacentTower() {
        basicLoopNoMiddleTile();

        Building building = new TowerBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        assertFalse(building.canPlaceBuilding(1, 0, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 0);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Tower on path tile not adjacent to any path tiles")
    public void testPathTileNotAdjacentTower() {
        basicLoopMiddleTile();

        Building building = new TowerBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Tower on non-path tile not adjacent to any path tiles")
    public void testNotAdjacentTower() {
        basicLoopNoMiddleTile();

        Building building = new TowerBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
        assertFalse(building.canPlaceBuilding(2, 2, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 2);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Tower on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingTower() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new TowerBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);

        Building building = new TowerBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new TowerBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ************************************  Test Village, Barracks, Trap *********************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing Village on path tile")
    public void testPathVillage() {
        basicLoopMiddleTile();

        Building building = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VillageBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Village on non-path tile")
    public void testNonPathVillage() {
        basicLoopMiddleTile();

        Building building = new VillageBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new VillageBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Village on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingVillage() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new VillageBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);

        Building building = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new VillageBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing Barracks on path tile")
    public void testPathBarracks() {
        basicLoopMiddleTile();

        Building building = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new BarracksBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Barracks on non-path tile")
    public void testNonPathBarracks() {
        basicLoopMiddleTile();

        Building building = new BarracksBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new BarracksBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Barracks on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingBarracks() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new BarracksBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);

        Building building = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new BarracksBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing Trap on path tile")
    public void testPathTrap() {
        basicLoopMiddleTile();

        Building building = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TrapBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Trap on non-path tile")
    public void testNonPathTrap() {
        basicLoopMiddleTile();

        Building building = new TrapBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new TrapBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Trap on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingTrap() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new TrapBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);

        Building building = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new TrapBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ******************************************** Test Campfire ******************************************* //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing Campfire on non-path tile")
    public void testPathCampfire() {
        basicLoopMiddleTile();

        Building building = new CampfireBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertTrue(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new CampfireBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertTrue(building.equals(newBuilding));
        assertTrue(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Campfire on path tile")
    public void testNonPathCampfire() {
        basicLoopMiddleTile();

        Building building = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new CampfireBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing Campfire on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingCampfire() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new CampfireBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);

        Building building = new CampfireBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new CampfireBuilding());
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************************** Test Hero's Castle **************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test placing HeroCastle on non-path tile")
    public void testPathHeroCastle() {
        basicLoopMiddleTile();

        Building building = new HeroCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(1, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new HeroCastleBuilding(new SimpleIntegerProperty(-1), new SimpleIntegerProperty(-1)));
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 1, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing HeroCastle on path tile")
    public void testNonPathHeroCastle() {
        basicLoopMiddleTile();

        Building building = new HeroCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(0, 1, world.getBuildings(), world.getOrderedPath()));

        Card card = world.loadCard(new HeroCastleBuilding(new SimpleIntegerProperty(-1), new SimpleIntegerProperty(-1)));
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }

    @Test
    @DisplayName("Test placing HeroCastle on a valid tile that is already occupied by a building")
    public void testOccupiedBuildingHeroCastle() {
        basicLoopNoMiddleTile();

        Card card = world.loadCard(new HeroCastleBuilding(new SimpleIntegerProperty(-1), new SimpleIntegerProperty(-1)));
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);

        Building building = new HeroCastleBuilding(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
        assertFalse(building.canPlaceBuilding(2, 1, world.getBuildings(), world.getOrderedPath()));

        card = world.loadCard(new HeroCastleBuilding(new SimpleIntegerProperty(-1), new SimpleIntegerProperty(-1)));
        Building newBuilding = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 2, 1);
        assertEquals(null, newBuilding);
        assertFalse(world.getBuildings().contains(newBuilding));
    }
}