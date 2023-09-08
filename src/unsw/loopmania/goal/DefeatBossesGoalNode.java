package unsw.loopmania.goal;

import unsw.loopmania.main.LoopManiaWorld;

public class DefeatBossesGoalNode extends GoalNode {

    public DefeatBossesGoalNode () {
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        return world.isDoggieDefeated() && world.isElanMuskeDefeated();
    }

    @Override
    public String asString(LoopManiaWorld world) {
        String goalString = "Defeat the Doggie and Elan Muske Bosses";
        return (isGoalMet(world)) ? "✅  " + goalString : "☐  " + goalString;
    }
    
}