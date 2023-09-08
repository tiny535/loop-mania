package unsw.loopmania.goal;

import java.util.List;

import unsw.loopmania.main.LoopManiaWorld;

public class ORGoalNode extends GoalNode{

    private List<GoalNode> goals;

    public ORGoalNode(List<GoalNode> goals) {
        this.goals = goals;
    }

    @Override
    public boolean isGoalMet(LoopManiaWorld world) {
        for (int i = 0; i < this.goals.size(); i++) {
            if (goals.get(i).isGoalMet(world)) return true;
        }
        return false;
    }
    
    @Override
    public String asString(LoopManiaWorld world) {
        String orGoalString = String.format("Achieve one of the following:");
        for (GoalNode goal : this.goals) {
            orGoalString += String.format("\n  %s", goal.asString(world));
        }
        orGoalString += String.format("\n");

        return (isGoalMet(world)) ? "✅  " + orGoalString : "☐  " + orGoalString;
    }
}
