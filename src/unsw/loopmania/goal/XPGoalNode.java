package unsw.loopmania.goal;

import unsw.loopmania.main.LoopManiaWorld;

public class XPGoalNode extends GoalNode{

    private int xpGoal;

    public XPGoalNode (int xpGoal) {
        this.xpGoal = xpGoal;
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        return world.getXPCount() >= xpGoal;
    }

    @Override
    public String asString(LoopManiaWorld world) {
        String goalString = "Gain "+ xpGoal + " XP";
        return (isGoalMet(world)) ? "✅  " + goalString : "☐  " + goalString;
    }
    
}
