package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
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
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class BuildingsEqualsTest {
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
// ******************************************  Test Equals Method *************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Tests equals method of VampireCastle")
    public void testEqualsVampireCastle() {
        Building building = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new ZombiePitBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of ZombiePit")
    public void testEqualsZombiePit() {
        Building building = new ZombiePitBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new ZombiePitBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of Tower")
    public void testEqualsTower() {
        Building building = new TowerBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new TowerBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new TowerBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of Campfire")
    public void testEqualsCampfire() {
        Building building = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new CampfireBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of HeroCastle")
    public void testEqualsHeroCastle() {
        Building building = new HeroCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new HeroCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new HeroCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of Village")
    public void testEqualsVillage() {
        Building building = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new VillageBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of Barracks")
    public void testEqualsBarracks() {
        Building building = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new BarracksBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }

    @Test
    @DisplayName("Tests equals method of Trap")
    public void testEqualsTrap() {
        Building building = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(building));

        Building b2 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(building.equals(b2));

        Building b1 = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(building.equals(b1));

        Building b3 = new TrapBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertFalse(building.equals(b3));
    }
}
