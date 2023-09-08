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

import unsw.loopmania.ally.Ally;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;
import javafx.beans.property.SimpleIntegerProperty;

public class BuildingEffectsTest {
    
    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;
    private Character character;

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
        this.character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(character);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// *****************************************  Test VillageBuilding ************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test if character only is healed by villageBuilding")
    public void testHealCharacterVillage() {
        Card card = world.loadCard(new VillageBuilding());
        VillageBuilding building = (VillageBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        character.takeDamage(60);

        building.healCharacter(character);
        assertEquals(90, character.getHp());
    }

    @Test
    @DisplayName("Test if characters and allies are healed by villageBuilding")
    public void testHealCharacterAndAlliesVillage() {
        Card card = world.loadCard(new VillageBuilding());
        VillageBuilding building = (VillageBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        character.takeDamage(60);
        List<Ally> allies = character.getParty().getAllies();
        for (Ally a : allies) {
            a.takeDamage(10);
        }

        building.healCharacter(character);
        assertEquals(90, character.getHp());
        for (Ally a : allies) {
            assertEquals(20, a.getHp());
        }
    }

    @Test
    @DisplayName("Test if characters and allies are affected by VillageBuilding if at full HP")
    public void testHealCharacterAndAlliesFullHPVillage() {
        Card card = world.loadCard(new VillageBuilding());
        VillageBuilding building = (VillageBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));

        building.healCharacter(character);
        assertEquals(100, character.getHp());
        List<Ally> allies = character.getParty().getAllies();
        for (Ally a : allies) {
            assertEquals(20, a.getHp());
        }
    }

    @Test
    @DisplayName("Test possiblyApplyVillageTile() with multiple VillageBuildings in the world")
    public void testPossiblyApplyVillageTileMultipleBuilding() {
        Card card = world.loadCard(new VillageBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        character.takeDamage(60);
        List<Ally> allies = character.getParty().getAllies();
        for (Ally a : allies) {
            a.takeDamage(10);
        }

        world.possiblyApplyVillageTile();
        assertEquals(40, character.getHp());
        for (Ally a : allies) {
            assertEquals(10, a.getHp());
        }

        character.setPosition(new PathPosition(9, orderedPath));
        world.possiblyApplyVillageTile();
        assertEquals(90, character.getHp());
        for (Ally a : allies) {
            assertEquals(20, a.getHp());
        }

        character.setPosition(new PathPosition(8, orderedPath));
        character.takeDamage(60);
        allies = character.getParty().getAllies();
        for (Ally a : allies) {
            a.takeDamage(10);
        }
        assertEquals(30, character.getHp());
        for (Ally a : allies) {
            assertEquals(10, a.getHp());
        }

        card = world.loadCard(new VillageBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 4);
        character.setPosition(new PathPosition(13, orderedPath));
        world.possiblyApplyVillageTile();
        assertEquals(80, character.getHp());
        for (Ally a : allies) {
            assertEquals(20, a.getHp());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ****************************************  Test BarracksBuilding ************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test if ally is produced by BarracksBuilding")
    public void testProduceAllyBarracks() {
        Card card = world.loadCard(new BarracksBuilding());
        BarracksBuilding building = (BarracksBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        
        Ally ally = building.produceAlly(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0), character.getParty());
        assertEquals(ally.getX(), 0);
        assertEquals(ally.getY(), 0);
    }

    @Test
    @DisplayName("Test if ally is produced by BarracksBuilding when party is full")
    public void testProduceAllyPartyFullBarracks() {
        Card card = world.loadCard(new BarracksBuilding());
        BarracksBuilding building = (BarracksBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        for (int i = 0; i < 8; i++) {
            character.addPartyMember(new Ally(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)));
        }

        Ally ally = building.produceAlly(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0), character.getParty());
        assertEquals(ally, null);
    }

    @Test
    @DisplayName("Test possiblyApplyBarracksTile() with multiple BarracksBuildings in the world")
    public void testPossiblyApplyBarracksTileMultipleBuilding() {
        Card card = world.loadCard(new BarracksBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        card = world.loadCard(new BarracksBuilding());
        world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 4);

        assertEquals(null, world.possiblyApplyBarracksTile());
        List<Ally> allies = character.getParty().getAllies();
        assertTrue(allies.isEmpty());

        character.setPosition(new PathPosition(9, orderedPath));
        Ally ally = world.possiblyApplyBarracksTile();
        // TODO: change when proper coordinates for allies is implemented
        assertEquals(ally.getX(), 0);
        assertEquals(ally.getY(), 0);
        assertTrue(allies.size() == 1);
        assertTrue(allies.contains(ally));

        character.setPosition(new PathPosition(13, orderedPath));
        ally = world.possiblyApplyBarracksTile();
        // TODO: change when proper coordinates for allies is implemented
        assertEquals(ally.getX(), 0);
        assertEquals(ally.getY(), 0);
        assertTrue(allies.size() == 2);
        assertTrue(allies.contains(ally));

        for (int i = 0; i < 6; i++) {
            world.possiblyApplyBarracksTile();
            assertTrue(allies.size() == 3 + i);
        }
        assertEquals(null, world.possiblyApplyBarracksTile());
        assertTrue(allies.size() == 8);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ******************************************  Test TrapBuilding **************************************** //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("Test if enemy triggers TrapBuilding")
    public void testTriggerTrap() {
        Card card = world.loadCard(new TrapBuilding());
        TrapBuilding building = (TrapBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        List<BasicEnemy> enemies = new ArrayList<BasicEnemy>();
        BasicEnemy enemy = new VampireEnemy(new PathPosition(9, orderedPath));
        enemies.add(enemy);
        enemies.add(new ZombieEnemy(new PathPosition(7, orderedPath)));
        enemies.add(new ZombieEnemy(new PathPosition(8, orderedPath)));

        assertEquals(enemy, building.possiblyTriggerTrap(enemies));
    }

    @Test
    @DisplayName("Test if enemy does not trigger TrapBuilding")
    public void testDoesNotTriggerTrap() {
        Card card = world.loadCard(new TrapBuilding());
        TrapBuilding building = (TrapBuilding) world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        List<BasicEnemy> enemies = new ArrayList<BasicEnemy>();
        assertEquals(null, building.possiblyTriggerTrap(enemies));

        enemies.add(new VampireEnemy(new PathPosition(7, orderedPath)));
        enemies.add(new ZombieEnemy(new PathPosition(8, orderedPath)));
        assertEquals(null, building.possiblyTriggerTrap(enemies));
    }

    @Test
    @DisplayName("Test possiblyApplyTrapTile() with multiple TrapBuildings in the world")
    public void testPossiblyApplyTrapTileMultipleBuilding() {
        Card card = world.loadCard(new TrapBuilding());
        Building trap1 = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 4, 4);
        card = world.loadCard(new TrapBuilding());
        Building trap2 = world.convertCardToBuildingByCoordinates(card.getX(), card.getY(), 0, 4);
        assertTrue(world.possiblyApplyTrapTile().isEmpty());

        List<BasicEnemy> enemies = world.getEnemies();
        BasicEnemy vampire = new VampireEnemy(new PathPosition(7, orderedPath));
        enemies.add(vampire);
        assertTrue(world.possiblyApplyTrapTile().isEmpty());
        assertEquals(60, vampire.getHp());
        assertTrue(world.getBuildings().contains(trap1));
        assertTrue(world.getBuildings().contains(trap2));

        BasicEnemy zombie = new ZombieEnemy(new PathPosition(9, orderedPath));
        enemies.add(zombie);
        assertTrue(world.possiblyApplyTrapTile().isEmpty());
        assertTrue(enemies.contains(vampire));
        assertTrue(enemies.contains(zombie));
        assertEquals(5, zombie.getHp());
        assertEquals(60, vampire.getHp());
        assertFalse(world.getBuildings().contains(trap1));
        assertTrue(world.getBuildings().contains(trap2));

        BasicEnemy slug = new SlugEnemy(new PathPosition(13, orderedPath));
        enemies.add(slug);
        List<BasicEnemy> defeatedEnemies = world.possiblyApplyTrapTile();
        assertTrue(defeatedEnemies.size() == 1);
        assertTrue(defeatedEnemies.contains(slug));
        assertTrue(enemies.size() == 2);
        assertTrue(enemies.contains(vampire));
        assertTrue(enemies.contains(zombie));
        assertEquals(60, vampire.getHp());
        assertEquals(5, zombie.getHp());
        assertFalse(world.getBuildings().contains(trap1));
    }
}
