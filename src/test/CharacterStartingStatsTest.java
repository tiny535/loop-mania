package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class CharacterStartingStatsTest {

    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
    } 

    @Test
    @DisplayName("Check character starting hp")
    public void checkCharacterStartHpTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertEquals(100, protagonist.getHp());
    }

    @Test
    @DisplayName("Check character starting gold in normal mode")
    public void checkStartGoldNormalModeTest(){
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>(), GameMode.NORMAL);
        Character protagonist = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(protagonist);

        assertEquals(100, protagonist.getGoldQuantity());
    }

    @Test
    @DisplayName("Check character starting gold in berserker mode")
    public void checkStartGoldBerserkerModeTest(){
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>(), GameMode.BERSERKER);
        Character protagonist = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(protagonist);

        assertEquals(100, protagonist.getGoldQuantity());
    }

    @Test
    @DisplayName("Check character starting gold in survival mode")
    public void checkStartGoldSurvivalModeTest(){
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>(), GameMode.SURVIVAL);
        Character protagonist = new Character(new PathPosition(0, orderedPath));
        world.setCharacter(protagonist);

        assertEquals(100, protagonist.getGoldQuantity());
    }

    @Test
    @DisplayName("Check character empty inventory")
    public void checkEmptyInventoryTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertTrue(protagonist.isInventoryEmpty());
    }

    @Test
    @DisplayName("Check character empty deck")
    public void checkEmptyDeckTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertTrue(protagonist.isDeckEmpty());
    }
    
    @Test
    @DisplayName("Check character's party is empty")
    public void checkEmptyPartyTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertTrue(protagonist.isPartyEmpty());
    }

    @Test
    @DisplayName("Check starting damage is 5")
    public void checkStartingDamageTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertEquals(5, protagonist.getDamage());
    }

    @Test
    @DisplayName("Check starting defense is 0")
    public void checkStartingDefenseTest(){
        Character protagonist = new Character(new PathPosition(0, orderedPath));

        assertEquals(0, protagonist.getDefense());
    }
}