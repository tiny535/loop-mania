package unsw.loopmania.main;

public enum GameMode {
    NORMAL(100),
    BERSERKER(100),
    SURVIVAL(100);
    
    private int startingGold;

    private GameMode(int startingGold) {
        this.startingGold = startingGold;
    }

    public int getStartingGold() {
        return startingGold;
    }
}
