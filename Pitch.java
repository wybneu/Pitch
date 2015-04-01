
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * File     : Pitch.java
 * Purpose  : This program includes all the processes of Pitch game. It does
 *            the handle shuffling, dealing the cards properly, bidding 
 *            process, playing the game, counting the point and finally printing
 *            the results.
 *            
 * Main design: It uses arrays to store important information, such as deck for
 *              handle shuffling, card for the player's hands and trick for 
 *              tricks and the collected cards one player wins. It also uses 
 *              three classes stand for three different objects: Pitch, Card
 *              and Player.
 */

/**
 * The class of Pitch is used to implement all the processes of the game.
 */
public class Pitch {
    Card[] deck = new Card[52];// stores all the cards
    char trump = '0'; // stores the suit of trump
    Random random = new Random(0);
    int players_number = 0; // total number of players
    Robot[] players; // the players
    int bid = 0; // the current bid
    int highBid = 0; // the highest bid
    int highPlayer = 0; // the player who does the highest bid
    int currentPlayer = 0; // the current player
    int currentCard = 0; // the current card
    int winPlayer = 0; // the player who win one trick
    int pitcher = 0; // the pitcher\

    Scanner ScannerObject = new Scanner(System.in);
    ArrayList<Card> trick = new ArrayList<Card>(); // stores the cards in one
                                                   // trick
    ArrayList<Card> trickAll = new ArrayList<Card>();// to store all the played
                                                     // card
    Card maxCard = new Card(); // stores the maximum cards
    Card minCard = new Card(); // stores the minimum cards

    public Pitch(int seed, int players_number, Integer seat) {
        if (players_number > 7 || players_number < 2) {
            System.out
                    .println("The players' number should be between 2 and 7.");
            System.exit(1);
        }
        // cards, players and tricks initialization
        this.random = new java.util.Random(seed);
        this.players_number = players_number;
        for (int i = 0; i < 52; i++) {
            this.deck[i] = new Card(i);
        }
        players = new Robot[this.players_number];
        for (int i = 0; i < players_number; i++) {
            if (i == (seat - 1)) {
                players[i] = new Robot();
                players[i].setRobot(false);
                this.players[i].setPostion(i);
            } else {
                players[i] = new Robot();
                players[i].setRobot(true);
                this.players[i].setPostion(i);
            }
        }

    }

    private void run() {
        this.deck = this.wash(deck, random);
        this.players = this.deal(players, deck);
        goBid();
        play();
        score();
    }

    // handle shuffling the deck
    public Card[] wash(Card[] deck, Random random) {
        Card[] cardBox = new Card[deck.length];
        for (int i = 0; i < deck.length; i++) {
            cardBox[i] = deck[i].clone();
        }
        Card tempCard = new Card();
        for (int i = 52; i > 0; i--) {
            int temp = random.nextInt(i);
            tempCard.setRank(cardBox[temp].getRank());
            tempCard.setSuit(cardBox[temp].getSuit());
            cardBox[temp].setRank(cardBox[i - 1].getRank());
            cardBox[temp].setSuit(cardBox[i - 1].getSuit());
            cardBox[i - 1].setRank(tempCard.getRank());
            cardBox[i - 1].setSuit(tempCard.getSuit());
        }
        return cardBox;
    }

    // deal the cards
    private Robot[] deal(Robot[] player, Card[] deck) {
        Robot[] players = new Robot[player.length];
        for (int i = 0; i < player.length; i++) {
            players[i] = player[i].clone();
        }

        // the first three cards for all the players
        for (int y = 0; y < players_number; y++) {

            for (int x = 0; x < 3; x++) {
                players[y].addCard(deck[x + (3 * y)]);
            }
        }

        // the second three cards for all the players
        for (int y = 0; y < players_number; y++) {
            for (int x = 0; x < 3; x++) {
                players[y].addCard(deck[x + (3 * y) + players_number * 3]);
            }
        }
        return players;

    }

    // bidding proceeds
    private void goBid() {
        int pass = 0;
        int lowest_bid = 2;
        int highest_bid = 4;

        for (int i = 0; i < players_number; i++) {
            currentPlayer = i;
            System.out.println();
            if (this.players[i].isRobot()) {
                System.out.println("Robot " + (i + 1) + ":");
            } else {
                System.out.println("Player " + (i + 1) + ":");
            }
            System.out.print("Hand:");
            for (int j = 0; j < this.players[i].getCard().length; j++) {
                System.out.print("  " + (j + 1) + ": "
                        + this.players[i].getCard()[j].toString());
            }
            System.out.println();

            // prompt message and scans the bid number
            do {
                System.out.print("High bid so far:  ");
                if (highBid == 0) {
                    System.out.println("None");
                } else
                    System.out.println(highBid);
                System.out.print("Bid? (0=pass): ");
                if (this.players[i].isRobot()) {
                    Robot temp = this.players[i];
                    bid = temp.bid(highBid, this.players_number);

                } else {
                    bid = ScannerObject.nextInt();
                }

                // judge the bid number is valid or invalid
                if (bid != pass && (bid < lowest_bid || bid > highest_bid)) {
                    System.out.println("Invalid bid " + bid
                            + ": Bid must be between 2 and 4, or 0 to pass.");
                } else if ((bid <= highBid) && (bid != pass)) {
                    System.out.println("Invalid bid " + bid
                            + ": Bid must be higher than previous bid of "
                            + highBid + ", or 0 to pass.");
                }
            } while ((bid != pass && (bid < lowest_bid || bid > highest_bid))
                    || ((bid <= highBid) && (bid != pass)));

            // decide the highest bid and the win player
            if (bid > highBid) {
                highBid = bid;
                highPlayer = currentPlayer + 1;
            }
            if ((bid >= lowest_bid) && (bid <= highest_bid)) {
                if (this.players[i].isRobot()) {
                    System.out.println("Robot " + (currentPlayer + 1)
                            + " bids " + bid);
                } else {
                    System.out.println("Player " + (currentPlayer + 1)
                            + " bids " + bid);
                }
            }
            if (bid == pass) {
                if (this.players[i].isRobot()) {
                    System.out.println("Robot " + (currentPlayer + 1)
                            + " passes");
                } else {
                    System.out.println("Player " + (currentPlayer + 1)
                            + " passes");
                }
            }

            // prints the results
            if (currentPlayer == players_number - 1) {
                System.out.println();
                if (highBid == pass) {
                    System.out.println("No one bid, so the game is passed in.");
                    System.exit(1);
                } else if (this.players[highPlayer - 1].isRobot()) {
                    System.out
                            .print("Robot " + highPlayer
                                    + " wins the contract with bid of "
                                    + highBid + ".");
                } else {
                    System.out
                            .print("Player " + highPlayer
                                    + " wins the contract with bid of "
                                    + highBid + ".");
                }
            }
        }
        System.out.println();

    }

    // process of the Pitch game
    private void play() {

        winPlayer = highPlayer;
        pitcher = highPlayer;
        Card largestCard = new Card(); // the largest card
        Card leadCard = new Card(); // the leading card in this trick
        for (int round = 0; round < Player.maxCard; round++) {
            currentPlayer = winPlayer;
            largestCard = new Card();
            leadCard = new Card();

            // Position is the index for the array of players.
            // Time is used to account how many players have played the cards.
            // If the position (Index) is greater than the number of players,
            // the position should equal to 0.
            for (int position = currentPlayer - 1, time = 0; time < this.players_number; position++, time++) {
                if (position > this.players_number - 1) {
                    position = position - this.players_number;
                }
                System.out.println();
                if (this.players[position].isRobot == true) {
                    System.out.println("Robot " + (position + 1) + ":");
                } else {
                    System.out.println("Player " + (position + 1) + ":");
                }
                System.out.print("Hand:");

                // prints all the hands the current player has
                for (int j = 0; j < this.players[position].getCard().length; j++) {
                    System.out.print("  " + (j + 1) + ": "
                            + this.players[position].getCard()[j].toString());
                }
                System.out.println();

                // prints each card has been shown
                if (this.getTrick().size() != 0) {
                    System.out.print("Cards played so far in this trick: ");
                    for (int k = 0; k < this.getTrick().size(); k++) {
                        System.out.print(this.getTrick().get(k) + " ");
                    }
                    System.out.println();
                }
                // shows the prompt of selecting card
                if (round == 0 && time == 0) {
                    System.out
                            .print("Select a card to lead, determining trump (1 - "
                                    + (6 - round) + "): ");
                } else if (round != 0 && time == 0) {
                    System.out.print("Select next card to lead (1 - "
                            + (6 - round) + "): ");
                } else {

                    System.out.print("Select your play (lead: "
                            + Card.getSuitName(leadCard.getSuit())
                            + ", trump: " + Card.getSuitName(trump) + ") (1 - "
                            + (6 - round) + "): ");
                }
                boolean vaild = false;
                go: do {
                    try {
                        if (this.players[position].isRobot()) {
                            currentCard = this.players[position].play(round,
                                    trump, leadCard, trick, time, trickAll) + 1;
                            System.out.print(currentCard);
                        } else {

                            currentCard = ScannerObject.nextInt();

                        }

                        vaild = this.players[position].isVaildCard(
                                (currentCard - 1), leadCard, trump);
                    } catch (Exception e) {
                        vaild = false;

                    }
                    if (!vaild) {
                        System.out
                                .println("You must follow suit if you can, unless you trump!");
                    }
                } while (!vaild);
                // gets the leading
                if (time == 0) {
                    leadCard = this.players[position].getCard()[currentCard - 1];
                }
                // gets the trump
                if (round == 0 && time == 0) {
                    trump = this.players[position].getCard()[currentCard - 1]
                            .getSuit();
                }
                // stores the current card to the current trick array
                this.getTrick().add(
                        this.players[position].getCard()[currentCard - 1]);

                // If the current card is greater than the largest card,
                // the win player should be set as the current player.
                if (this.players[position].getCard()[currentCard - 1]
                        .greaterThan(largestCard, trump)) {
                    largestCard = this.players[position].getCard()[currentCard - 1]
                            .clone();
                    winPlayer = position + 1;
                }
                // removes the current card from the hands of the current player
                this.players[position].removeCard(currentCard - 1);
                // When the trick is over, initializes the trick.
                if (time == this.players_number - 1) {
                    // stroes the current card to the trickall array
                    for (int i = 0; i < this.trick.size(); i++) {
                        this.trickAll.add(this.trick.get(i));
                    }
                    this.players[winPlayer - 1].addTricks(this.trick);
                    trick.clear();
                }
            }
        }

    }

    public ArrayList<Card> getTrick() {
        return trick;
    }

    public void setTrick(ArrayList<Card> trick) {
        this.trick = trick;
    }

    // counts the points
    private void score() {
        int highestPlayer = 0; // the index of the player with highest score
        int highestScore = 0; // stores the highest score
        boolean tie = false;
        System.out.println();

        // judges if there is a tie for the most pips,
        // or no one captures any pips
        for (int i = 0; i < players_number; i++) {
            this.players[i].setScoreOfPipsAndJ(trump);
            if (this.players[i].getPips() == highestScore) {
                tie = true;
            }
            if (this.players[i].getPips() > highestScore) {
                highestScore = this.players[i].getPips();
                highestPlayer = i + 1;
                tie = false;
            }
        }

        // If just one player captures the most pips, he will get one point.
        if (tie == false) {
            this.players[highestPlayer - 1].score++;
        }

        // finds the highest ranked trump card and the lowest ranked trump card
        for (int i = 0; i < this.players_number * Player.maxCard; i++) {
            if (this.deck[i].greaterThan(maxCard, trump)) {
                maxCard = this.deck[i].clone();
            }
            // ----------------------------------------------------wrong
            if (this.deck[i].lessThan(minCard, trump)) {
                minCard = this.deck[i].clone();
            }
        }

        // The player who captured the highest ranked trump card or the lowest
        // ranked trump card will get one point
        for (int i = 0; i < this.players_number; i++) {
            for (int j = 0; j < this.players[i].trick.length; j++) {
                if (this.players[i].getTrick()[j].equals(maxCard)) {
                    this.players[i].score++;
                }
                if (this.players[i].getTrick()[j].equals(minCard)) {
                    this.players[i].score++;
                }
            }
        }

        // judges if the pitcher fulfill the contract
        if (this.players[highPlayer - 1].getScore() < highBid) {
            this.players[highPlayer - 1].setScore(0 - highBid);
            System.out.println("Player " + highPlayer
                    + " failed to make the bid of " + highBid + " points");
        } else {
            System.out.println("Player " + highPlayer
                    + " fulfilled the contract with "
                    + this.players[highPlayer - 1].getScore() + " points");
        }

        // prints the scores of all the players
        System.out.println("After this game, the scores stand at:");
        for (int i = 0; i < this.players_number; i++) {
            System.out.println("    Player " + (i + 1) + ": "
                    + this.players[i].getScore() + " points");
        }
    }

    public static void main(String[] args) {
        Integer seed = 0, players_number = 0, seat = 0;
        try {
            seed = Integer.parseInt(args[0]);
            players_number = Integer.parseInt(args[1]);
            seat = Integer.parseInt(args[2]);
            if (players_number < 2 || players_number > 7
                    || players_number < seat) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Usage: java Pitch <seed> <players> <seat>");
            System.out.println("where <seed> is a random seed integer");
            System.out.println("and <players> is the number of players");
            System.out.println("and <seat> is your seat number");
            System.exit(1);
        }
        Pitch pitch = new Pitch(seed, players_number, seat);
        pitch.run();
    }

}
