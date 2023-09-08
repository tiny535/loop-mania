package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import unsw.loopmania.goal.ANDGoalNode;
import unsw.loopmania.goal.CycleGoalNode;
import unsw.loopmania.goal.DefeatBossesGoalNode;
import unsw.loopmania.goal.GoalNode;
import unsw.loopmania.goal.GoldGoalNode;
import unsw.loopmania.goal.ORGoalNode;
import unsw.loopmania.goal.XPGoalNode;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

public class GoalCreationTest {

    private LoopManiaWorld world;
    private Character character;
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Create a simple ordered path
        orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(0,1));
        orderedPath.add(new Pair<Integer, Integer>(1,1));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        // Create a basic LoopManiaWorld that can be use globally
        // Normal Mode - they start with 100 gold
        // Beserker Mode - they start with 50 gold
        // Survival Mode - they start with 50 gold
        this.world = new LoopManiaWorld(2, 2, orderedPath, GameMode.NORMAL);
        
        // Create a character we can give xp and gold to onto the world 
        this.character = new Character(new PathPosition(0, orderedPath));
        this.world.setCharacter(this.character);
    }

    /**
     * Increment the cycle count for the world
     */
    private void runACycle() {
        int prevCycles = this.world.getCycleCount();
        for (int i = 0; i < 5; i++) this.world.runTickMoves();
        int postCycles = this.world.getCycleCount();
        assertTrue(prevCycles + 1 == postCycles);
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Primitive Individual Goals

    @Test
    @DisplayName("Just meet gold goal")
    public void meetGoldGoalTest() {
        GoalNode goldGoal = new GoldGoalNode(200);
        assertTrue(!goldGoal.isGoalMet(this.world));

        this.world.setEndGoal(goldGoal);
        assertTrue(this.world.getGoals().equals(goldGoal));
        assertTrue(!this.world.isGameWon());
        assertEquals("☐  Earn 200 Gold", goldGoal.asString(world));

        this.character.addGold(100);
        assertTrue(goldGoal.isGoalMet(this.world));
        assertTrue(this.world.isGameWon());
        assertEquals("✅  Earn 200 Gold", goldGoal.asString(world));
    }

    @Test
    @DisplayName("Just not meet gold goal")
    public void failGoldGoalTest() {
        GoalNode goldGoal = new GoldGoalNode(200);
        assertTrue(!goldGoal.isGoalMet(this.world));

        this.world.setEndGoal(goldGoal);
        assertTrue(!this.world.isGameWon());

        this.character.addGold(99);
        assertTrue(!goldGoal.isGoalMet(this.world));
        assertTrue(!this.world.isGameWon());
    }

    @Test
    @DisplayName("Just meet xp goal")
    public void meetXPGoalTest() {
        GoalNode xpGoal = new XPGoalNode(200);
        assertTrue(!xpGoal.isGoalMet(this.world));

        this.world.setEndGoal(xpGoal);
        assertTrue(!this.world.isGameWon());

        this.character.gainExperience(200);
        assertTrue(xpGoal.isGoalMet(this.world));
        assertTrue(this.world.isGameWon());

        assertEquals("✅  Gain 200 XP", xpGoal.asString(world));

    }

    @Test
    @DisplayName("Just not meet xp goal")
    public void failXPGoalTest() {
        GoalNode xpGoal = new XPGoalNode(200);
        assertTrue(!xpGoal.isGoalMet(this.world));

        this.world.setEndGoal(xpGoal);
        assertTrue(!this.world.isGameWon());
        
        this.character.gainExperience(199);
        assertTrue(!xpGoal.isGoalMet(this.world));
        assertTrue(!this.world.isGameWon());
    }

    @Test
    @DisplayName("Just meet cycle goal")
    public void meetCycleGoalTest() {
        GoalNode cycleGoal = new CycleGoalNode(5);
        assertTrue(!cycleGoal.isGoalMet(this.world));

        this.world.setEndGoal(cycleGoal);
        assertTrue(!this.world.isGameWon());

        assertEquals("☐  Reach 5 Cycles", cycleGoal.asString(world));
        
        for (int i = 0; i < 5; i++) runACycle();
        assertTrue(cycleGoal.isGoalMet(this.world));
        assertTrue(this.world.isGameWon());

        assertEquals("✅  Reach 5 Cycles", cycleGoal.asString(world));
    }

    @Test
    @DisplayName("Just not meet cycle goal")
    public void failCycleGoalTest() {
        GoalNode cycleGoal = new CycleGoalNode(5);
        assertTrue(!cycleGoal.isGoalMet(this.world));

        this.world.setEndGoal(cycleGoal);
        assertTrue(!this.world.isGameWon());

        for (int i = 0; i < 4; i++) runACycle();
        assertTrue(!cycleGoal.isGoalMet(this.world));
        assertTrue(!this.world.isGameWon());
    }

    @Test
    @DisplayName("Just meet defeat bosses goal")
    public void meetDefeatBossesGoalTest() {
        GoalNode defeatBossesGoal = new DefeatBossesGoalNode();
        assertTrue(!defeatBossesGoal.isGoalMet(this.world));

        this.world.setEndGoal(defeatBossesGoal);
        assertTrue(!this.world.isGameWon());

        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(1, orderedPath));
        world.addEnemy(elanMuske);
        world.addEnemy(doggie);
        assertTrue(!defeatBossesGoal.isGoalMet(this.world));

        for (BasicEnemy e : world.getEnemies()) {
            e.takeDamage(1000);
        }

        assertEquals("☐  Defeat the Doggie and Elan Muske Bosses", defeatBossesGoal.asString(world));

        world.killEnemy(doggie);
        world.killEnemy(elanMuske);

        assertTrue(defeatBossesGoal.isGoalMet(this.world));
        assertTrue(this.world.isGameWon());

        assertEquals("✅  Defeat the Doggie and Elan Muske Bosses", defeatBossesGoal.asString(world));
    }

    @Test
    @DisplayName("Just not meet defeat bosses goal, doggie not defeated")
    public void failDefeatBossesDoggieNotDefeatedTest() {
        GoalNode defeatBossesGoal = new DefeatBossesGoalNode();
        assertTrue(!defeatBossesGoal.isGoalMet(this.world));

        this.world.setEndGoal(defeatBossesGoal);
        assertTrue(!this.world.isGameWon());

        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        world.addEnemy(elanMuske);

        for (BasicEnemy e : world.getEnemies()) {
            e.takeDamage(1000);
        }

        for (int i = 0; i < 1000; i++) {
            world.runBattles();
            character.healCharacter(100);
        }

        assertTrue(!defeatBossesGoal.isGoalMet(this.world));
        assertTrue(!this.world.isGameWon());
    }

    @Test
    @DisplayName("Just not meet defeat bosses goal, elan muske not defeated")
    public void failDefeatBossesElanMuskeNotDefeatedTest() {
        GoalNode defeatBossesGoal = new DefeatBossesGoalNode();
        assertTrue(!defeatBossesGoal.isGoalMet(this.world));

        this.world.setEndGoal(defeatBossesGoal);
        assertTrue(!this.world.isGameWon());

        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        world.addEnemy(doggie);

        for (BasicEnemy e : world.getEnemies()) {
            e.takeDamage(1000);
        }

        for (int i = 0; i < 1000; i++) {
            world.runBattles();
            character.healCharacter(100);
        }

        assertTrue(!defeatBossesGoal.isGoalMet(this.world));
        assertTrue(!this.world.isGameWon());
    }


///////////////////////////////////////////////////////////////////////////////////////
//// Simple Subgoals

    @Test
    @DisplayName("Simple AND goal passing, testing left side")
    public void passSimpleANDGoalLeftTest() {
        List<GoalNode> goals = new ArrayList<>();
        goals.add(new GoldGoalNode(200));
        goals.add(new XPGoalNode(150));


        GoalNode andGoalLeft = new ANDGoalNode(goals);
        assertTrue(!andGoalLeft.isGoalMet(this.world));

        this.character.addGold(100);
        assertTrue(!andGoalLeft.isGoalMet(this.world));
        assertEquals(String.format("☐  Achieve the following:\n  ✅  Earn 200 Gold\n  ☐  Gain 150 XP\n"), andGoalLeft.asString(world));

        this.character.gainExperience(150);
        assertTrue(andGoalLeft.isGoalMet(this.world));

        assertEquals(String.format("✅  Achieve the following:\n  ✅  Earn 200 Gold\n  ✅  Gain 150 XP\n"), andGoalLeft.asString(world));
    }

    @Test
    @DisplayName("Simple AND goal passing, testing right side")
    public void passSimpleANDGoalRightTest() {

        List<GoalNode> goals = new ArrayList<>() {
            {
                add(new GoldGoalNode(200));
                add(new XPGoalNode(150));
            }
        };

        GoalNode andGoalRight = new ANDGoalNode(goals);
        assertTrue(!andGoalRight.isGoalMet(this.world));

        this.character.gainExperience(150);
        assertTrue(!andGoalRight.isGoalMet(this.world));
        
        this.character.addGold(100);
        assertTrue(andGoalRight.isGoalMet(this.world));
    }

    @Test
    @DisplayName("Simple OR goal passing, testing left side")
    public void passSimpleORGoalLeftTest() {
        List<GoalNode> goals = new ArrayList<>() {
            {
                add(new GoldGoalNode(200));
                add(new XPGoalNode(150));
            }
        };

        GoalNode orGoalLeft = new ORGoalNode(goals);
        assertTrue(!orGoalLeft.isGoalMet(this.world));
        assertEquals(String.format("☐  Achieve one of the following:\n  ☐  Earn 200 Gold\n  ☐  Gain 150 XP\n"), orGoalLeft.asString(world));

        this.character.addGold(100);
        assertTrue(orGoalLeft.isGoalMet(this.world));
        assertEquals(String.format("✅  Achieve one of the following:\n  ✅  Earn 200 Gold\n  ☐  Gain 150 XP\n"), orGoalLeft.asString(world));
    }

    @Test
    @DisplayName("Simple OR goal passing, testing right side")
    public void passSimpleORGoalRightTest() {
        
        List<GoalNode> goals = new ArrayList<>() {
            {
                add(new GoldGoalNode(200));
                add(new XPGoalNode(150));
            }
        };

        GoalNode orGoalRight = new ORGoalNode(goals);
        assertTrue(!orGoalRight.isGoalMet(this.world));

        this.character.gainExperience(150);
        assertTrue(orGoalRight.isGoalMet(this.world));
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Complex Subgoals

    // AND Goal with 2 ANDs
    @Test
    @DisplayName("More complex subgoal system")
    public void complexSubgoals() {

        /*
        AND                               - Step 4
        - AND
            - Cycle 2
            - Gold 400                    - Step 3
        - OR
            - AND
                - Gold 200                - Step 1
                - XP 150
            - OR
                - Cycle 4                 - Step 2
                - XP 200
        */
        
        
        List<GoalNode> easyOrGoals = new ArrayList<>();
        easyOrGoals.add(new XPGoalNode(200));
        easyOrGoals.add(new CycleGoalNode(4));

        GoalNode easyORGoal = new ORGoalNode(easyOrGoals);

        List<GoalNode> easyAndGoals = new ArrayList<>();
        easyAndGoals.add(new GoldGoalNode(200));
        easyAndGoals.add(new XPGoalNode(150));

        GoalNode easyAndGoal = new ANDGoalNode(easyAndGoals);

        List<GoalNode> hardOrGoals = new ArrayList<>();
        hardOrGoals.add(easyAndGoal);
        hardOrGoals.add(easyORGoal);

        GoalNode hardOrGoal = new ORGoalNode(hardOrGoals);
        assertTrue(!hardOrGoal.isGoalMet(this.world));

        List<GoalNode> hardAndGoals = new ArrayList<>();
        hardAndGoals.add(new CycleGoalNode(2));
        hardAndGoals.add(new GoldGoalNode(400));

        GoalNode hardAndGoal = new ANDGoalNode(hardAndGoals);

        List<GoalNode> finalsGoals = new ArrayList<>();
        finalsGoals.add(hardAndGoal);
        finalsGoals.add(hardOrGoal);
    
        GoalNode finalGoal = new ANDGoalNode(finalsGoals);
        assertTrue(!hardAndGoal.isGoalMet(this.world));
        assertTrue(!hardOrGoal.isGoalMet(this.world));
        assertTrue(!finalGoal.isGoalMet(this.world));

        // Step 1
        this.character.addGold(100);
        assertTrue(!hardOrGoal.isGoalMet(this.world));

        // Step 2
        for (int i = 0; i < 4; i++) runACycle();
        assertTrue(hardOrGoal.isGoalMet(this.world));
        assertTrue(!finalGoal.isGoalMet(this.world));

        // Step 3
        this.character.addGold(200);
        assertTrue(finalGoal.isGoalMet(this.world));
    }

}
