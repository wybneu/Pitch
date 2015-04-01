
/**
 * File : Card.java
 * 
 * This class is used to store the suit and rank of the cards.
 */

public class Card {

    char suit; // the suit of the cards
    char rank; // the rank of the cards
    public static String[] suitName = { "CLUBS", "DIAMONDS", "HEARTS", "SPADES" };

    // return the full name of suit with upper letter
    public static String getSuitName(char suit) {
        switch (suit) {
        case 'C':
            return suitName[0];
        case 'D':
            return suitName[1];
        case 'H':
            return suitName[2];
        case 'S':
            return suitName[3];
        }
        return null;

    }

    public Card() {
        this.setSuit('0');
        this.setRank('0');
    }

    // card initialization with number
    public Card(int i) {
        switch (i / 13) {
        case 0:
            suit = 'C';
            break;
        case 1:
            suit = 'D';
            break;
        case 2:
            suit = 'H';
            break;
        case 3:
            suit = 'S';
            break;
        }
        switch (i % 13) {
        case 0:
            rank = '2';
            break;
        case 1:
            rank = '3';
            break;
        case 2:
            rank = '4';
            break;
        case 3:
            rank = '5';
            break;
        case 4:
            rank = '6';
            break;
        case 5:
            rank = '7';
            break;
        case 6:
            rank = '8';
            break;
        case 7:
            rank = '9';
            break;
        case 8:
            rank = 'T';
            break;
        case 9:
            rank = 'J';
            break;
        case 10:
            rank = 'Q';
            break;
        case 11:
            rank = 'K';
            break;
        case 12:
            rank = 'A';
            break;
        }

    }

    public Card(char suit, char rank) {
        this.setRank(rank);
        this.setSuit(suit);
    }

    public char getSuit() {
        return suit;
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }

    public char getRank() {
        return rank;
    }

    public void setRank(char rank) {
        this.rank = rank;
    }

    public boolean equals(Card card) {
        if ((this.getRank() == card.getRank())
                && (this.getSuit() == card.getSuit())) {
            return true;
        }
        return false;
    }

    public String toString() {
        String temp = "" + this.getRank() + this.getSuit();
        return temp;

    }

    public Card clone() {
        Card temp = new Card(this.getSuit(), this.getRank());
        return temp;
    }

    // judge whether this card is greater than the compared card
    // if this card is greater, return True, else return False
    public Boolean greaterThan(Card card, char trump) {
        if (card.getRank() == '0' && card.getSuit() == '0') {
            return true;
        }
        if (this.getRank() == card.getRank()
                && this.getSuit() == card.getSuit()) {
            return false;
        }
        if (this.getSuit() == trump) {
            if (card.getSuit() == trump) {
                return this.getCardNumber(this.getRank()) > card
                        .getCardNumber(card.getRank());
            } else {
                return true;
            }
        } else if (card.getSuit() == trump) {
            return false;
        } else if (this.getSuit() == card.getSuit()) {
            return this.getCardNumber(this.getRank()) > card.getCardNumber(card
                    .getRank());
        } else {
            return false;
        }

    }

    public boolean greaterThan(Card card) {
        if ((card.suit == '0') && (card.rank == '0')) {
            return true;
        }
        return this.getCardNumber(this.getRank()) > card.getCardNumber(card
                .getRank());
    }

    public boolean lessThan(Card card) {
        if ((card.suit == '0') && (card.rank == '0')) {
            return true;
        }
        return this.getCardNumber(this.getRank()) < card.getCardNumber(card
                .getRank());
    }

    // judge whether this card is less than the compared card
    // if this card is less, return True, else return False
    public boolean lessThan(Card card, char trump) {
        if (card.getRank() == '0' && card.getSuit() == '0') {
            return true;
        }
        if (card.getSuit() != trump) {
            return true;
        }
        if (this.getSuit() != trump) {
            return false;
        }
        return this.getCardNumber(this.getRank()) < card.getCardNumber(card
                .getRank());

    }

    // Return different numbers depends on the different ranks.
    // As all the numbers should greater the the number of ASCII for char '0',
    // so the numbers begin with 50.
    public int getCardNumber(char rank) {
        int CardNumber = 0;
        switch (rank) {
        case '0':
            CardNumber =49;
            break;
        case '2':
            CardNumber = 50;
            break;
        case '3':
            CardNumber = 51;
            break;
        case '4':
            CardNumber = 52;
            break;
        case '5':
            CardNumber = 53;
            break;
        case '6':
            CardNumber = 54;
            break;
        case '7':
            CardNumber = 55;
            break;
        case '8':
            CardNumber = 56;
            break;
        case '9':
            CardNumber = 57;
            break;
        case 'T':
            CardNumber = 58;
            break;
        case 'J':
            CardNumber = 59;
            break;
        case 'Q':
            CardNumber = 60;
            break;
        case 'K':
            CardNumber = 61;
            break;
        case 'A':
            CardNumber = 62;
            break;
        }
        return CardNumber;
    }

}
