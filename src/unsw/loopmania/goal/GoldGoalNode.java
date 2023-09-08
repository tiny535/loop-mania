package unsw.loopmania.goal;

import unsw.loopmania.main.LoopManiaWorld;

public class GoldGoalNode extends GoalNode{

    private int goldGoal;

    public GoldGoalNode (int goldGoal) {
        this.goldGoal = goldGoal;
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        return world.getGoldCount() >= goldGoal;
    }

    @Override
    public String asString(LoopManiaWorld world) {
        String goalString = "Earn "+ goldGoal + " Gold";
        return (isGoalMet(world)) ? "✅  " + goalString : "☐  " + goalString;
    }
}
