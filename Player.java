
import java.util.ArrayList;

/**
 * File : Player.java
 *  This class is used to store the information of the players.
 */

public class Player {
    public static int maxCard = 6;
    // the possible max number of cards for each player
    public static int maxPlayer = 7; // the possible max number of players
    Card[] card; // player's hands
    int score = 0; // the scores
    int pips; // the pips
    Card[] trick; // the trick
    int seat; // seat= acture seat -1
    boolean isRobot = false;

    public Player clone() {
        Player player = new Player(this.card);
        player.setScore(this.score);
        player.setPips(this.pips);
        player.setTrick(this.trick);
        player.setPostion(this.seat);
        player.setRobot(this.isRobot);
        return player;
    }

    public int getSeat() {
        return seat;
    }

    public void setPostion(int postion) {
        this.seat = postion;
    }

    public boolean isRobot() {
        return isRobot;
    }

    public void setRobot(boolean isRobot) {
        this.isRobot = isRobot;
    }

    public Player() {
        this.card = new Card[0];
        this.score = 0;
        this.trick = new Card[0];
    }

    public Player(Card[] card) {
        if (card.length <= maxCard) {
            this.card = new Card[card.length];
            for (int i = 0; i < card.length; i++) {
                this.card[i] = new Card();
                this.card[i].setRank(card[i].getRank());
                this.card[i].setSuit(card[i].getSuit());
            }
            this.score = 0;
        }
    }

    // adds the dealing cards to hands
    public void addCard(Card card) {
        Card[] tempCard = new Card[this.getCard().length + 1];
        for (int i = 0; i < this.getCard().length; i++) {
            tempCard[i] = this.getCard()[i].clone();
        }
        tempCard[this.getCard().length] = card.clone();
        this.card = tempCard;
    }

    // collects all of the cards in the trick that the player wins
    public void addTricks(ArrayList<Card> card) {
        for (int i = 0; i < card.size(); i++) {
            this.addTrick(card.get(i));
        }
    }
    //add a card to the trick of the player
    public void addTrick(Card card) {
        Card[] tempCard = new Card[this.getTrick().length + 1];
        for (int i = 0; i < this.getTrick().length; i++) {
            tempCard[i] = this.getTrick()[i].clone();
        }
        tempCard[this.getTrick().length] = card.clone();
        this.trick = tempCard;
    }

    public Card[] getTrick() {
        return trick;
    }
    
    public void setTrick(Card[] trick) {
        Card[] trickTemp = new Card[trick.length];
        for (int i = 0; i < trick.length; i++) {
            trickTemp[i] = new Card();
            trickTemp[i].setRank(trick[i].getRank());
            trickTemp[i].setSuit(trick[i].getSuit());
        }
        this.trick=trickTemp;
    }

    // plays one card
    public void removeCard(int position) {
        Card[] tempCard = new Card[this.getCard().length - 1];
        for (int i = 0, j = 0; i < this.getCard().length; i++, j++) {
            if (i == position) {
                i++;
            }
            if (i < this.getCard().length) {
                tempCard[j] = this.getCard()[i].clone();
            }
        }
        this.card = tempCard;
    }
    //get the pips from a arraylist card
    public int countPips(ArrayList<Card> trick) {
        Card[] card=new Card[trick.size()];
        for(int i=0;i<trick.size();i++){
            card[i]=trick.get(i).clone();
        }
        return countPips(card);
    }
    //get the pips of a Card[]
    public int countPips(Card[] card) {
        int pips = 0;
        for (int i = 0; i < card.length; i++) {
            switch (card[i].getRank()) {
            case 'A':
                pips += 4;
                break;
            case 'K':
                pips += 3;
                break;
            case 'Q':
                pips += 2;
                break;
            case 'J':
                pips += 1;
                break;
            case 'T':
                pips += 10;
            }
        }
        return pips;
    }

    // counts the pips and marks trump J
    public void setScoreOfPipsAndJ(char trump) {
        if (this.trick.length == 0) {
            this.score = 0;
        }
        for (int i = 0; i < this.trick.length; i++) {
            if (this.trick[i].equals(new Card(trump, 'J'))) {
                this.score++;
            }
        }
        this.pips = this.countPips(trick);
    }

    // to judge whether the card player choose is vaild one
    public boolean isVaildCard(int cardPostion, Card leadCard, char trump) {
        if (cardPostion < 0 || cardPostion>=this.card.length){return false;}
        if (leadCard.getRank() == '0' && leadCard.getSuit() == '0') {
            return true;
        } else if (this.getCard()[cardPostion].getSuit() == trump) {
            return true;
        } else if (this.hasSameSuit(leadCard)) {
            return (this.getCard()[cardPostion].getSuit() == leadCard.getSuit());
        } else {
            return true;
        }
    }

    // return whether there is any card that has the same color of the leadCard
    public boolean hasSameSuit(Card leadCard) {
        if (leadCard.getRank() == '0' && leadCard.getSuit() == '0') {
            return true;
        }
        for (int i = 0; i < this.getCard().length; i++) {

            if (this.getCard()[i].getSuit() == leadCard.getSuit()) {
                return true;
            }
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Card[] getCard() {
        return card;
    }

    public void setCard(Card[] card) {
        Card[] cardTemp = new Card[card.length];
        for (int i = 0; i < card.length; i++) {
            cardTemp[i] = new Card();
            cardTemp[i].setRank(card[i].getRank());
            cardTemp[i].setSuit(card[i].getSuit());
        }
        this.card = cardTemp;
    }

    public int getPips() {
        return pips;
    }

    public void setPips(int pips) {
        this.pips = pips;
    }
}
