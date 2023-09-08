package unsw.loopmania.main;

import java.util.List;
import java.util.ArrayList;

public class Deck {
    
    private static final int MAX_CARDS_ON_HAND = 8;

    private List<Card> cardsOnHand;

    public Deck(){
        this.cardsOnHand = new ArrayList<Card>();
    }

    /**
     * Removes and destroys all cards, clearing the deck.
     */
    public void clear() {
        for (Card c : cardsOnHand) c.destroy();
        cardsOnHand.clear();
    }

    public boolean isDeckEmpty(){
        return cardsOnHand.isEmpty();
    }

    public Card getCardByCordinates(int x, int y){
        for (Card c: this.cardsOnHand){
            if ((c.getX() == x) && (c.getY() == y)){
                return c;
            }
        }
        return null;
    }

    public boolean isDeckFull(){
        return this.cardsOnHand.size() >= MAX_CARDS_ON_HAND;
    }

    public Card getOldestCard(){
        return this.cardsOnHand.get(0);
    }

    public void addCard(Card card){
        this.cardsOnHand.add(card);
    }

    public int getSize(){
        return this.cardsOnHand.size();
    }

    public void removeCard(Card card){
        int x = card.getX();
        card.destroy();
        this.cardsOnHand.remove(card);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card c: cardsOnHand){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    public boolean isContained(Card card){
        return this.cardsOnHand.contains(card);
    }
}
