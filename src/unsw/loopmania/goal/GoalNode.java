package unsw.loopmania.goal;

import unsw.loopmania.main.LoopManiaWorld;

public abstract class GoalNode {
    
    /**
     * Method returns whether this goal has been met
     * @return true if met, false if not
     */
    public abstract boolean isGoalMet(LoopManiaWorld world);

    /**
     * Returns the goal as a displayable string version
     * E.g. ( <Goal Type> <Left Expression/Node> <Right Expression/Node>)
     * @return String representing the goal
     */
    public abstract String asString(LoopManiaWorld world);
}
