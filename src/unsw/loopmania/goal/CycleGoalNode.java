package unsw.loopmania.goal;

import unsw.loopmania.main.LoopManiaWorld;

public class CycleGoalNode extends GoalNode{
    private int cycleGoal;

    public CycleGoalNode (int cycleGoal) {
        this.cycleGoal = cycleGoal;
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        return world.getCycleCount() >= cycleGoal;
    }

    @Override
    public String asString(LoopManiaWorld world) {
        String goalString = "Reach "+ cycleGoal + " Cycles";
        return (isGoalMet(world)) ? "✅  " + goalString : "☐  " + goalString;
    }
}
