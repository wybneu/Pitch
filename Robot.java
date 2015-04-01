
import java.util.ArrayList;

public class Robot extends Player {
    char maxSuit;

    Card maxTrumpCardInTrickAll = new Card();
    Card minTrumpCardInTrickAll = new Card();
    Card maxTrumpCardInTrick = new Card();
    Card minTrumpCardInTrick = new Card();
    Card maxTrumpCard = new Card();
    Card minTrumpCard = new Card();

    public Robot() {
        super();
        this.isRobot = true;
        maxSuit = '0';
    }

    public Robot(Card[] card) {
        super(card);
        this.isRobot = true;
        maxSuit = '0';
    }

    public Robot clone() {
        Robot player = new Robot(this.card);
        player.setScore(this.score);
        player.setPips(this.pips);
        player.setTrick(this.trick);
        player.setPostion(this.seat);
        player.setRobot(this.isRobot);
        player.setCard(this.card);
        player.maxSuit = this.maxSuit;
        return player;

    }
    //count the Possibility of success and return this robot's final decision of how many he want to bid
    public int bid(int currentBid, int playersNumber) {
        double[] bid = new double[4];
        double maxBid = 0;
        char[] suit = { 'C', 'D', 'H', 'S' };
        for (int i = 0; i < 4; i++) {
            bid[i] += this.countMax(playersNumber, suit[i]);
            bid[i] += this.countMin(playersNumber, suit[i]);
            bid[i] += this.countJack(suit[i]);
            bid[i] += this.countPip(this.card);
        }

        for (int i = 0; i < 4; i++) {
            if (bid[i] > maxBid) {
                maxBid = bid[i];
                this.maxSuit = suit[i];
            }
        }
        if ((maxBid > currentBid) && (maxBid <= 4) && (maxBid >= 2)) {
            if (currentBid < 2) {
                currentBid = 1;
            }
            return (currentBid + 1);
        }
        return 0;
    }

    // time is how many people has given out his card.
    // time start from 0; 0 for he is the first one who give card
    // 6 is he is the last one who give card
    // return the positon of card
    public int play(int round, char trump1, Card leadCard,
            ArrayList<Card> trick, int time, ArrayList<Card> trickAll) {

        char trump;

        // if there is no trump, he give his prefer trump
        if (trump1 == '0') {
            trump = this.maxSuit;
            return this.getMaxTrumpCard(trump);
        } else {
            trump = trump1;
        }

        // if he is the one who give the lead card
        if (time == 0) {
            if (this.getNumberOfCard(trump) >= 2) {
                return this.getMaxTrumpCard(trump);
            } else if ((this.getMaxNoneTrumpRank(trump) != 0)) {
                return this.getMaxNoneTrumpRank(trump);
            } else {
                return this.getMaxCard();
            }
        }
        // if he is the last one who give out card

        maxTrumpCardInTrickAll = new Card();
        minTrumpCardInTrickAll = new Card();
        maxTrumpCardInTrick = new Card();
        minTrumpCardInTrick = new Card();
        maxTrumpCard = new Card();
        minTrumpCard = new Card();
        Card maxLeadInTrick = new Card();
        Card minLeadInTrick = new Card();
        Card maxLeadCard = new Card();
        Card minLeadCard = new Card();

        maxTrumpCardInTrickAll = this.getMax(trickAll, trump);
        minTrumpCardInTrickAll = this.getMin(trickAll, trump);
        maxTrumpCardInTrick = this.getMax(trick, trump);
        minTrumpCardInTrick = this.getMin(trick, trump);
        maxTrumpCard = this.getMax(this.card, trump);
        minTrumpCard = this.getMin(this.card, trump);
        maxLeadInTrick = this.getMax(trick, leadCard.getSuit());
        minLeadInTrick = this.getMin(trick, leadCard.getSuit());
        maxLeadCard = this.getMax(this.card, leadCard.getSuit());
        minLeadCard = this.getMin(this.card, leadCard.getSuit());

        Card leastTrumpCard; //the card which is the nearest card to the except
        Card leastLeadCard;
        Card trumpJ = new Card(trump, 'J');
        if (time == 6) {
            if ((this.hasTrump(trick, trump))
                    && (this.hasTrump(this.card, trump))
                    && minTrumpCardInTrick.lessThan(minTrumpCardInTrickAll,
                            trump)
                    && minTrumpCardInTrick.lessThan(minTrumpCard, trump)
                    && maxTrumpCard.greaterThan(maxTrumpCardInTrick)) {
                leastTrumpCard = maxTrumpCard;
                for (int i = 0; i < this.card.length; i++) {
                    if (this.card[i].greaterThan(maxTrumpCardInTrick, trump)
                            && this.card[i].lessThan(leastTrumpCard)) {
                        leastTrumpCard = this.card[i].clone();
                    }
                }
                return this.getPosition(leastTrumpCard);
            } else if ((!this.hasTrump(trick, trump))
                    && (this.hasTrump(trickAll, trump))
                    && (minTrumpCard.lessThan(minTrumpCardInTrickAll))) {
                return this.getPosition(minTrumpCard);
            } else if ((this.hasCard(trumpJ))
                    && (trumpJ.greaterThan(maxTrumpCardInTrick))) {
                return this.getPosition(trumpJ);
            } else if (this.hasSameSuit(leadCard)
                    && (maxLeadCard.greaterThan(maxLeadInTrick))) {
                leastLeadCard = maxLeadCard;
                for (int i = 0; i < this.card.length; i++) {
                    if (this.card[i].greaterThan(maxLeadInTrick, trump)
                            && this.card[i].lessThan(leastLeadCard)) {
                        leastLeadCard = this.card[i].clone();
                    }
                }
                return this.getPosition(leastLeadCard);
            } else if ((this.countPips(trick) >= 10)
                    && (this.hasTrump(this.card, trump))
                    && (((this.hasTrump(trick, trump)) && (this.maxTrumpCard
                            .greaterThan(maxTrumpCardInTrick))) || (!this
                            .hasTrump(trick, trump)))) {
                if (!this.hasTrump(trick, trump)) {
                    return this.getPosition(minTrumpCard);
                } else {
                    leastTrumpCard = maxTrumpCard;
                    for (int i = 0; i < this.card.length; i++) {
                        if (this.card[i]
                                .greaterThan(maxTrumpCardInTrick, trump)
                                && this.card[i].lessThan(leastTrumpCard)) {
                            leastTrumpCard = this.card[i].clone();
                        }
                    }
                    return this.getPosition(leastTrumpCard);
                }
            } else if ((leadCard.suit == trump)
                    && (this.getNumberOfCard(trump) >= 2)
                    && (minTrumpCard.lessThan(minTrumpCardInTrickAll))
                    && minTrumpCard.lessThan(minTrumpCardInTrick)) {
                leastTrumpCard = maxTrumpCard;
                for (int i = 0; i < this.card.length; i++) {
                    if (this.card[i].greaterThan(minTrumpCard)
                            && this.card[i].lessThan(leastTrumpCard)) {
                        leastTrumpCard = this.card[i].clone();
                    }
                }
                return this.getPosition(leastTrumpCard);
            } else if (this.hasSameSuit(leadCard)) {
                return this.getPosition(minLeadCard);
            } else if (this.hasNoneTrumpCard(trump)) {
                return this.getPosition(this.getMinRankNoneTrumpCard(trump));
            } else {
                return this.getPosition(this.getMinRankCard());
            }
        } else {
            int pipNumber = this.countPips(trick);
            if (((pipNumber >= 10)
                    || (this.hasTrump(trick, trump)
                            && this.minTrumpCardInTrick
                                    .lessThan(minTrumpCardInTrickAll) && this.minTrumpCardInTrick
                            .lessThan(minTrumpCard)) || this.hasTrumpJ(trick,
                    trump))
                    && (this.hasTrump(card, trump) && (this.maxTrumpCard
                            .greaterThan(this.maxTrumpCardInTrick)))) {
                return this.getPosition(maxTrumpCard);
            } else if (this.hasSameSuit(leadCard)
                    && maxLeadCard.greaterThan(maxLeadInTrick)) {
                return this.getPosition(maxLeadCard);
            } else if ((leadCard.getSuit() == trump)
                    && (this.getNumberOfCard(trump) >= 2)
                    && (this.minTrumpCard.lessThan(this.minTrumpCardInTrick)
                            && (((this.hasTrump(trickAll, trump)) && this.minTrumpCard
                                    .lessThan(this.minTrumpCardInTrickAll))) || (!this
                            .hasTrump(trickAll, trump)))) {
                leastTrumpCard = maxTrumpCard;
                for (int i = 0; i < this.card.length; i++) {
                    if (this.card[i].getSuit() == trump) {
                        if (this.card[i].greaterThan(minTrumpCard)
                                && this.card[i].lessThan(leastTrumpCard)) {
                            leastTrumpCard = this.card[i].clone();
                        }
                    }
                }
                return this.getPosition(leastTrumpCard);
            } else if (this.hasSameSuit(leadCard)) {
                return this.getPosition(minLeadCard);
            } else if (this.hasNoneTrumpCard(trump)) {
                return this.getPosition(this.getMinRankNoneTrumpCard(trump));
            } else {
                return this.getPosition(this.getMinRankCard());
            }
        }

    }
    //find whether the arraylist card has a trump J
    public boolean hasTrumpJ(ArrayList<Card> trick, char trump) {
        for (int i = 0; i < trick.size(); i++) {
            if (trick.get(i).equals(new Card(trump, 'J')))
                return true;
        }
        return false;
    }
    // find whether the card[] has a trump J
    public boolean hasTrumpJ(Card[] card, char trump) {
        for (int i = 0; i < card.length; i++) {
            if (card[i].equals(new Card(trump, 'J')))
                return true;
        }
        return false;
    }
    //find whether this robot has a card which is not a trump
    public boolean hasNoneTrumpCard(char trump) {
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getSuit() != trump)
                return true;
        }
        return false;
    }
    //find the the robot card which is not a trump card and the first minimum rank card
    public Card getMinRankNoneTrumpCard(char trump) {
        Card minCard = new Card();
        for (int i = 0; i < this.card.length; i++) {
            if (!(this.card[i].getSuit() == trump)) {
                if (minCard.getRank() == '0') {
                    minCard = this.card[i].clone();
                }
                if (this.card[i].getCardNumber(this.card[i].getRank()) < minCard
                        .getCardNumber(minCard.getRank())) {
                    minCard = this.card[i].clone();
                }
            }
        }
        return minCard;

    }
    //find the robot card which is the first minimum rank card
    public Card getMinRankCard() {
        Card minCard = this.card[0].clone();
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getCardNumber(this.card[i].getRank()) < minCard
                    .getCardNumber(minCard.getRank())) {
                minCard = this.card[i].clone();
            }
        }
        return minCard;
    }
    
    //find the whether the robot has a special card
    public boolean hasCard(Card card) {
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].equals(card))
                return true;
        }
        return false;
    }
    //find whether a card[] has the trump card
    public boolean hasTrump(Card[] card, char trump) {
        for (int i = 0; i < card.length; i++) {
            if (card[i].getSuit() == trump)
                return true;
        }
        return false;
    }
    //find whether a arraylist card has trump card
    public boolean hasTrump(ArrayList<Card> card, char trump) {
        for (int i = 0; i < card.size(); i++) {
            if (card.get(i).getSuit() == trump)
                return true;
        }
        return false;
    }
    //find the position of a special card in the robot hand card
    public int getPosition(Card card) {
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].equals(card)) {
                return i;
            }
        }
        return -1;
    }
    //find the largest card in a card[] in a special suit  
    public Card getMax(Card[] card, char suit) {
        Card max = new Card();
        for (int i = 0; i < card.length; i++) {
            if (card[i].getSuit() == suit) {
                if (card[i].greaterThan(max)) {
                    max = card[i].clone();
                }
            }
        }
        return max;
    }
  //find the largest card in a ArrayList card in a special suit 
    public Card getMax(ArrayList<Card> card, char suit) {
        Card max = new Card();
        for (int i = 0; i < card.size(); i++) {
            if (card.get(i).getSuit() == suit) {
                if (card.get(i).greaterThan(max)) {
                    max = card.get(i).clone();
                }
            }
        }
        return max;
    }
  //find the minimum card in a  card[] in a special suit 
    public Card getMin(Card[] card, char suit) {
        Card min = new Card();
        for (int i = 0; i < card.length; i++) {
            if (card[i].getSuit() == suit) {
                if (card[i].lessThan(min)) {
                    min = card[i].clone();
                }
            }
        }
        return min;
    }
  //find the minimum card in a  ArrayList Card in a special suit 
    public Card getMin(ArrayList<Card> card, char suit) {
        Card max = new Card();
        for (int i = 0; i < card.size(); i++) {
            if (card.get(i).getSuit() == suit) {
                if (card.get(i).lessThan(max)) {
                    max = card.get(i).clone();
                }
            }
        }
        return max;
    }

    // get the maxRank card's position which is not a trump card
    public int getMaxNoneTrumpRank(char trump) {
        char maxCard = '0';
        int position = 0;
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getSuit() != trump) {
                if (this.card[i].getCardNumber(this.card[i].getRank()) > this.card[i]
                        .getCardNumber(maxCard)) {
                    maxCard = this.card[i].getRank();
                    position = i;
                }
            }
        }
        return position;

    }

    // return the position max card of the player's cards which suit is the suit
    // 0 for no such suit
    public int getMaxTrumpCard(char suit) {
        char maxCard = '0';
        int position = 0;
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getSuit() == suit) {
                if (this.card[i].getCardNumber(this.card[i].getRank()) > this.card[i]
                        .getCardNumber(maxCard)) {
                    maxCard = this.card[i].getRank();
                    position = i;
                }
            }
        }
        return position;
    }

    // get the postion of the max rank card in all the suit
    public int getMaxCard() {
        char maxCard = '0';
        int position = 0;
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getCardNumber(this.card[i].getRank()) > this.card[i]
                    .getCardNumber(maxCard)) {
                maxCard = this.card[i].getRank();
                position = i;
            }
        }
        return position;
    }
    //get the number of a special suit in the robot hand card
    public int getNumberOfCard(char suit) {
        int number = 0;
        for (int i = 0; i < this.card.length; i++) {
            if (this.getCard()[i].getSuit() == suit) {
                number++;
            }
        }
        return number;
    }

    // return the min card posiiton of player's cards which suit is the suit
    // 0 for no such suit
    public int getMinTrumpCard(char suit) {
        char minCard = 'Z';
        int position = 0;
        for (int i = 0; i < this.card.length; i++) {
            if (this.card[i].getSuit() == suit) {
                if (this.card[i].getCardNumber(this.card[i].getRank()) < this.card[i]
                        .getCardNumber(minCard)) {
                    minCard = this.card[i].getRank();
                    position = i;
                }
            }
        }
        return position;
    }
    // count the possibility of getting the largest trump
    public double countMax(int playersNumber, char suit) {

        Double prob = 1.0;
        if (!this.hasSameSuit(new Card(suit, '0'))) {
            return 0D;
        }
        int r = 0;// r keep the value of max of the suit card which can not be .
        char lastCardRank = 49;
        for (int i = 0; i < this.card.length; i++) {
            if (this.getCard()[i].getSuit() == suit) {
                if (this.getCard()[i].getCardNumber(lastCardRank) < this
                        .getCard()[i]
                        .getCardNumber(this.getCard()[i].getRank())) {
                    lastCardRank = this.getCard()[i].getRank();
                }
            }
        }
        r = 62 - lastCardRank;
        for (int i = 0; i < r; i++) {
            prob *= (double) (52 - 6 * playersNumber - i) / (46 - i);
        }
        return prob;
    }
 // count the possibility of getting the minimum trump
    public double countMin(int playersNumber, char suit) {

        Double prob = 1.0;
        if (!this.hasSameSuit(new Card(suit, '0'))) {
            return 0D;
        }
        int r = 0;// r keep the value of max of the suit card which can not be .
        char firstCardRank = 63;
        for (int i = 0; i < this.card.length; i++) {
            if (this.getCard()[i].getSuit() == suit) {
                if (firstCardRank > this.getCard()[i].getRank()) {
                    firstCardRank = this.getCard()[i].getRank();
                }
            }
        }
        r = firstCardRank - 50;
        for (int i = 0; i < r; i++) {
            prob *= (double) (52 - 6 * playersNumber - i) / (46 - i);
        }
        return prob;
    }
 // count the possibility of getting the trump J
    public double countJack(char suit) {
        Double prob = 0.0D;
        for (int i = 0; i < this.card.length; i++) {
            if (this.getCard()[i].getSuit() == suit) {
                if (this.getCard()[i].getRank() == 'J') {
                    prob = 1.0D;
                }
            }
        }
        return prob;
    }
 // count the possibility of getting the most Pips
    private double countPip(Card[] card) {
        Double prob = 0.0D;
        int pips = countPips(card);
        prob = (double) ((double) pips / (double) 25);
        return prob;
    }

}
