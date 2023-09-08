package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import unsw.loopmania.goal.GoalType;

public class GoalTypeEnumTest {
    
    @Test
    @DisplayName("Ensure xp goal type returns correct value")
    public void xpGoalTypeEnumValueTest() {
        assertEquals("experience", GoalType.XP.getValue());
    }

    @Test
    @DisplayName("Ensure gold goal type returns correct value")
    public void goldGoalTypeEnumValueTest() {
        assertEquals("gold", GoalType.GOLD.getValue());
    }

    @Test
    @DisplayName("Ensure cycle goal type returns correct value")
    public void cycleGoalTypeEnumValueTest() {
        assertEquals("cycles", GoalType.CYCLE.getValue());
    }

    @Test
    @DisplayName("Ensure and goal type returns correct value")
    public void andGoalTypeEnumValueTest() {
        assertEquals("AND", GoalType.AND.getValue());
    }

    @Test
    @DisplayName("Ensure or goal type returns correct value")
    public void orGoalTypeEnumValueTest() {
        assertEquals("OR", GoalType.OR.getValue());
    }
}
