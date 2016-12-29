import java.util.*;
import java.io.*;

public class Main{
  public static void main(String args[]){
    int mode = 3; //3 card solitaire
    int deckSize;
    double winCount = 0.0;
    double numTrials = 100000.0;
    int helloCount = 0;
    int lgCount = 0;

    for (int i = 0; i < numTrials; i++){
      Stack<Card> deck = new Stack<Card>();
      Stack<Card> unused = new Stack<Card>();
      Card[][] board = new Card[20][7];
      List<Stack<Card>> piles = new ArrayList<Stack<Card>>();
      Board b = new Board();
      boolean lastGasp = false;


      b.initialize(deck, unused, mode, piles, board);
      do{
        do{
          deckSize = b.getDeckSize(deck);
          b.run(deck, unused, mode, piles, board);
          b.resetDeck(deck, unused);
          // if(deckSize != b.getDeckSize(deck)) && b.checkPilesToBoard())
          //   b.printSetup
          // b.printIndTopCards();
          // System.out.println();
          // b.printFirstFaceUp();
        }while(deckSize !=  b.getDeckSize(deck));
        //lastGasp = b.checkPilesToBoard(piles, board);
        //b.setBoardMove(lastGasp);
        //if(lastGasp == true && b.getBroken())
          //b.printSetup(deck, unused, mode, piles, board);
        //System.out.println("It's your last gasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssspppp!!!! "+lastGasp);
        //lgCount++;
        // b.printIndTopCards();
        // System.out.println();
        // b.printFirstFaceUp();
      }while(lastGasp == true);

      //b.printSetup(deck, unused, mode, piles, board);
      if (win(piles)) winCount++;
      //displayResults(win(piles));
      helloCount+= b.getHelloCount();
    }
    System.out.println("You won: " + winCount/numTrials*100+ " Percent of the time");
    //b.printSetup(deck, unused, mode, piles, board);
    // SolitaireGame trial = new SolitaireGame(b);
    //
    // trial.run();
  }

  public static boolean win(List<Stack<Card>> piles){
    for(int i = 0; i < 4; i++){
      if(piles.get(i).size() != 13)
        return false;
    }
    return true;
  }

  public static void displayResults(boolean win){
    if (win == true)
      System.out.println("Congratualtions you win!!");
    else
      System.out.println("You have no more moves. Game Over");
  }
}
