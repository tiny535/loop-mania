package unsw.loopmania.main;

import java.util.List;

import unsw.loopmania.ally.Ally;
import unsw.loopmania.battle.Fighter;

import java.util.ArrayList;

public class Party {

    public static final int MAX_PARTY_SIZE = 8;
    
    private List<Ally> allies;

    /**
     * Constructor for Party
     */
    public Party(){
        this.allies = new ArrayList<Ally>();
    }

    /**
     * Remove and destroy all party members.
     */
    public void reset() {
        for (Ally a : allies) a.destroy();
        allies.clear();
    }

    /**
     * Checks if party is empty
     */
    public boolean isPartyEmpty(){
        return this.allies.isEmpty();
    }

    public int getSize(){
        return this.allies.size();
    }

    public Ally getAlly(int index){
        return this.allies.get(index);
    }

    public void removeAlly(Fighter allyToBeRemoved){
        this.allies.remove(allyToBeRemoved);
    }

    public boolean checkAllyContained(Fighter ally){
        return this.allies.contains(ally);
    }

    /**
     * Checks if party is full
     * @return
     */
    public boolean isPartyFull() {
        return (allies.size() >= Party.MAX_PARTY_SIZE);
    }

    /**
     * Getter for allies
     * @return
     */
    public List<Ally> getAllies() {
        return allies;
    }

    /**
     * Adds an ally to the party
     * @param ally
     */
    public void addAlly(Ally ally) {
        this.allies.add(ally);
    }
}
