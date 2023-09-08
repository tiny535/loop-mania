package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import unsw.loopmania.main.GameMode;

public class GameModeEnumTest {
    
    @Test
    @DisplayName("Normal starting gold")
    public void normalGameModeStartingGoldTest() {
        assertEquals(100, GameMode.NORMAL.getStartingGold());
    }

    
    @Test
    @DisplayName("Beserker starting gold")
    public void beserkerGameModeStartingGoldTest() {
        assertEquals(100, GameMode.BERSERKER.getStartingGold());
    }

    
    @Test
    @DisplayName("Survival starting gold")
    public void survivalGameModeStartingGoldTest() {
        assertEquals(100, GameMode.SURVIVAL.getStartingGold());
    }
}
