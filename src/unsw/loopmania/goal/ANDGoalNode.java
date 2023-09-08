package unsw.loopmania.goal;

import java.util.List;

import unsw.loopmania.main.LoopManiaWorld;

public class ANDGoalNode extends GoalNode{

    private List<GoalNode> goals;

    public ANDGoalNode(List<GoalNode> goals) {
        this.goals = goals;
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        for (GoalNode innerGoal : this.goals) {
            if (!innerGoal.isGoalMet(world)) return false;
        }
        return true;
    }

    @Override
    public String asString(LoopManiaWorld world) {
        String andGoalString = String.format("Achieve the following:");
        for (GoalNode goal : this.goals) {
            andGoalString += String.format("\n  %s", goal.asString(world));
        }
        andGoalString += String.format("\n");
        
        return (isGoalMet(world)) ? "✅  " + andGoalString : "☐  " + andGoalString;
    }
    
}
