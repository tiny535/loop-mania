package unsw.loopmania.goal;

public enum GoalType {

    XP("experience"), 
    GOLD("gold"),
    CYCLE("cycles"),
    BOSSES("bosses"),
    AND("AND"),
    OR("OR");

    private String jsonString;
 
    GoalType(String jsonString) {
        this.jsonString = jsonString;
    }
 
    public String getValue() {
        return this.jsonString;
    }
}
