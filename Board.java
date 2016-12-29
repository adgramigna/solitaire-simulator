import java.util.*;
import java.io.*;

public class Board{

  final Character[] ranks = {'K', 'Q', 'J', 'T'/* because "10" is two characters and it messes up the board format*/, '9', '8', '7', '6', '5', '4', '3', '2', 'A'};
  final String[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};

  private String topCards;
  private boolean topCardOff = false;
  private boolean boardMove = true;
  private boolean broken = false;

  private int helloCount = 0;
  private int numMoves;
  private int faceUpBoardCt = 0;

  private List<Card> indTopCards = new ArrayList<Card>();
  private List<Card> firstfaceUp = new ArrayList<Card>();


  public Board(){}

  //makes 52 cards and puts them in the deck
  public void makeDeck(Stack<Card> deck){
    for(String suit: suits){
      for(char rank: ranks){
        deck.push(new Card(rank,suit));
      }
    }
  }

  //Adds a new stack for each finish pile
  public void makePiles(List<Stack<Card>> piles){
    for(String suit:suits){
      piles.add(new Stack<Card>());
    }
  }

  public int getHelloCount(){
    return helloCount;
  }

  //Sets up board initally
  public void setup(Stack<Card> deck, Card[][] board){
    int cols = board[0].length;
    for(int i = 0; i<cols; i++){
      for(int j = cols-i; j > 0; j--){
        Card c = deck.peek();
        if(i+j == cols){
          c.setSide(true); //specifies which sides are face up (that the player can see)
          c.setIndependent(true);
          firstfaceUp.add(c);
          faceUpBoardCt++;
        }
        board[i][cols-j] = deck.pop();
        //System.out.println(board[i][cols-j].toString());
      }
    }
  }

  public void drawCard(Stack<Card> deck, Stack<Card> unused){
    unused.push(deck.pop());
  }

  public void makeTopCards(Stack<Card> deck, Stack<Card> unused, int mode){
      topCards = "";
      int draws = deck.size()<mode ? deck.size() : mode; //size of the deck if true otherwise the mode;
      for(int i = 0; i < draws; i++){
        Card c = deck.peek();
        c.setSide(true);
        if (i == draws-1){
          c.setIndependent(true);
          indTopCards.add(c);
        }
        drawCard(deck, unused);
        topCards += c.display();
      }
      while(topCards.length() < 6)
        topCards += " ";
  }

  public void printDeck(Stack<Card> deck, Stack<Card> unused, int mode){
    if(!deck.isEmpty())
      System.out.print("**");
    else
      System.out.print("O");
    System.out.print(" ");
    System.out.print(topCards);
    System.out.print(" ");
  }

  public void printPiles(List<Stack<Card>> piles){
    for(int i = 0; i<piles.size(); i++){
      System.out.print(" ");
      if(piles.get(i).isEmpty())
        System.out.print("_");
      else
        System.out.print(piles.get(i).peek().display());
    }
    System.out.println("\n");
  }

  public void printBoard(int rows, int cols, Card[][] board){
    for(int row = 0; row < rows; row++){
      boolean placed = false;
      for(int col = 0; col < cols; col++){
        if(board[row][col] != null && board[row][col].getSide() == true){
          System.out.print(board[row][col].display());
          placed = true;
        }
        if(board[row][col] != null && board[row][col].getSide() == false){
            System.out.print("**");
            placed = true;
        }
        if(board[row][col] == null)
          System.out.print("  ");
        if(col == cols-1)
          System.out.println();
      }
      if(placed == false)
        break;
    }
  }

  public void printBoardQualities(Card[][] board){
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[0].length; j++){
        if(board[i][j] != null)
          System.out.println(board[i][j].toString());
      }
    }
    System.out.println("\n\n\n");
  }

  public void printSetup(Stack<Card> deck, Stack<Card> unused, int mode, List<Stack<Card>> piles, Card[][] board){
    //printBoardQualities(board);
    printDeck(deck, unused, mode);
    printPiles(piles);
    printBoard(board.length, board[0].length, board);

    // printIndTopCards();
    // System.out.println();
    // printFirstFaceUp();
    // System.out.println();
    // System.out.println(hasKing(firstfaceUp, indTopCards));
  }

  public void printEntireDeck(Stack<Card> deck, Stack<Card> unused){
    while(!deck.isEmpty()){
      Card c = deck.pop();
      System.out.println(c.display());
      unused.push(c);
    }
    while(!unused.isEmpty()){
      deck.push(unused.pop());
    }
  }

  public void updateTopCards(Card c){
    c.setIndependent(true);
    if (!indTopCards.contains(c))
      indTopCards.add(c);
    if(topCards.substring(topCards.length()-4,topCards.length()).equals("    ")){
      topCards = c.display() + "    ";
    }
    else if(topCards.substring(topCards.length()-2,topCards.length()).equals("  ")){
      topCards = topCards.substring(0,topCards.length()-4);
      topCards += "    ";
    }
    else{
      topCards = topCards.substring(0,topCards.length()-2);
      topCards += "  ";
    }
  }

  //public boolean canGoOnBoard(Card )

  public boolean checkPilesToBoardTopCard(List<Stack<Card>> piles, Card[][] board, Card[] topPiles, Card[] indBoard){
    for(Card indt: indTopCards){
      for(Card tp: topPiles){
        for(Card indb: indBoard){
          if(indt != null && tp != null && indb != null){
            if(indt.oneLess(tp) && indt.getColor() != tp.getColor() && tp.oneLess(indb) && tp.getColor() != indb.getColor()){
              board[indb.getRow()+1][indb.getCol()] = piles.get(tp.getSuitAsInt()).pop();
              tp.setRow(indb.getRow()+1);
              tp.setCol(indb.getCol());
              indb.setIndependent(false);
              tp.setIndependent(true);
              faceUpBoardCt++;
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean hasKing(List<Card> firstfaceUp, List<Card> indTopCards){
    for(Card ffu: firstfaceUp)
      if (ffu.getRankAsInt() == 13 && ffu.getRow() != 0) return true;
    for(Card indt: indTopCards)
      if (indt.getRankAsInt() == 13) return true;
    return false;
  }

  public boolean checkPilesToBoardBoard(List<Stack<Card>> piles, Card[][] board, Card[] topPiles, Card[] indBoard){
    for(Card ffu: firstfaceUp){
      for(Card tp: topPiles){
        for(Card indb: indBoard){
          if(ffu != null && tp != null && indb != null){
            if((ffu.getRow() != 0 || hasKing(firstfaceUp, indTopCards)) && ffu.oneLess(tp) && ffu.getColor() != tp.getColor() && tp.oneLess(indb) && tp.getColor() != indb.getColor()){
              board[indb.getRow()+1][indb.getCol()] = piles.get(tp.getSuitAsInt()).pop();
              tp.setRow(indb.getRow()+1);
              tp.setCol(indb.getCol());
              indb.setIndependent(false);
              tp.setIndependent(true);
              faceUpBoardCt++;
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean checkPilesToBoard(List<Stack<Card>> piles, Card[][] board){
    Card[] topPiles = getTopPiles(piles);
    Card[] indBoard = getIndependents(board);
    return checkPilesToBoardTopCard(piles, board, topPiles, indBoard) || checkPilesToBoardBoard(piles, board, topPiles, indBoard);
  }

  public boolean checkCardToPiles(Card c, List<Stack<Card>> piles){
    return (piles.get(c.getSuitAsInt()).isEmpty() && c.getRankAsInt() == 1) || (!piles.get(c.getSuitAsInt()).isEmpty() && c.oneMore(piles.get(c.getSuitAsInt()).peek()));
  }

  public boolean checkBoardToPiles(List<Stack<Card>> piles, Card[][] board){
    for(int row = 0; row<board.length; row++){
      for(int col = 0; col<board[0].length; col++){
        if(board[row][col] != null){
          if(board[row][col].getIndependent()){
            if(checkCardToPiles(board[row][col], piles)){
              Card c = board[row][col];
              piles.get(c.getSuitAsInt()).push(c);
              firstfaceUp.remove(c);
              board[row][col] = null;
              if(row > 0){
                if(!board[row-1][col].getSide())
                  firstfaceUp.add(board[row-1][col]);
                board[row-1][col].setIndependent(true);
              }
              topCardOff = false;
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean checkTopCardToPiles(Stack<Card> unused, List<Stack<Card>> piles){
    if(unused.isEmpty()) return false;
    Card c = unused.peek();
    //System.out.println(c.getIndependent());
    if(checkCardToPiles(c, piles)){
      indTopCards.remove(c);
      piles.get(c.getSuitAsInt()).push(unused.pop());
      topCardOff = true;
      return true;
    }
    return false;
 }

public boolean checkCardToBoard(Card c, Card[][] board){
  Card[] indBoard = getIndependents(board);
  for(int col = 0; col<board[0].length; col++){
    if(board[0][col] == null && c.getRankAsInt() == 13){
      c.setRow(0);
      c.setCol(col);
      firstfaceUp.add(c);
      return true;
    }
  }
  for(Card indb: indBoard){
    if(indb != null){
      if(c.oneLess(indb) && c.getColor() != indb.getColor()){
        c.setRow(indb.getRow()+1);
        c.setCol(indb.getCol());
        return true;
      }
    }
  }
  return false;
}

public boolean checkTopCardToBoard(Stack<Card> unused, Card[][] board){
  if(unused.isEmpty()) return false;
  Card c = unused.peek();
  //System.out.println(c.getIndependent());
  if(checkCardToBoard(c, board)){
    int row = c.getRow();
    int col = c.getCol();
    if(row > 0)
      board[row-1][col].setIndependent(false);
    board[row][col] = c;
    faceUpBoardCt++;
    indTopCards.remove(c);
    unused.pop();
    topCardOff = true;
    return true;
  }
  return false;
}

public Card[] getTopPiles(List<Stack<Card>> piles){
  Card[] topPiles = new Card[suits.length];
  for(int i = 0; i < suits.length; i++){
    if(!piles.get(i).isEmpty())
      topPiles[i]=piles.get(i).peek();
  }
  return topPiles;
 }

 public Card[] getIndependents(Card[][] board){
   Card[] independents = new Card[board[0].length];
   for(int col = 0; col<board[0].length; col++){
     for(int row = 0; row<board.length; row++){
       if(board[row][col] != null){
         if(board[row][col].getIndependent()){
           independents[col] = board[row][col];
           independents[col].setRow(row);
           independents[col].setCol(col);
         }
       }
     }
   }
   return independents;
 }

public boolean checkBoardToBoard(Card[][] board, List<Stack<Card>> piles){
  boolean nullCount = false;
  int nullCol = 1;
  for(int col = 0; col<board[0].length; col++){
      if(board[0][col] == null && nullCount == false){
        nullCount = true;
        nullCol = col;
      }
  }

  Card[] independents = getIndependents(board);

  for(int row = 0; row<board.length; row++){
    for(int col = 0; col<board[0].length; col++){
      if(board[row][col] != null){
        if(board[row][col].getSide() && (row==0 || !board[row-1][col].getSide())){
          if(board[row][col].getRankAsInt() == 13 && row!=0 && nullCount == true){
            board[row-1][col].setIndependent(true);
            firstfaceUp.add(board[row-1][col]);
            for(int i = 0; i < board.length-row; i++){
              board[i][nullCol] = board[row+i][col];
              if(board[i][nullCol] != null){
                board[i][nullCol].setRow(i);
                board[i][nullCol].setCol(nullCol);
              }
              board[row+i][col] = null;
            }
            return true;
          }
          int kingPiles = 0;
          for(int i = 0; i < board[0].length; i++){
            if(board[0][i] == null || (board[0][i].getSide() && board[0][i].getRankAsInt()==13))
              kingPiles++;
          }
          for(int k = 0; k < independents.length; k++){
            if(independents[k] != null){
              if(board[row][col].oneLess(independents[k]) && board[row][col].getColor() != independents[k].getColor()){
                if(kingPiles<4 || row!=0){
                  if(row != 0){
                    board[row-1][col].setIndependent(true);
                    firstfaceUp.add(board[row-1][col]);
                    faceUpBoardCt++;
                  }
                  firstfaceUp.remove(board[row][col]);
                  for(int i = 1; i < board.length-independents[k].getRow()-1; i++){
                    if(i+row < board.length){
                      board[independents[k].getRow()+i][independents[k].getCol()] = board[row+i-1][col];
                      if(board[independents[k].getRow()+i][independents[k].getCol()] != null){
                        board[independents[k].getRow()+i][independents[k].getCol()].setRow(independents[k].getRow()+i);
                        board[independents[k].getRow()+i][independents[k].getCol()].setCol(independents[k].getCol()+i);
                      }
                      board[row+i-1][col] = null;
                    }
                  }
                  board[independents[k].getRow()][independents[k].getCol()].setIndependent(false);
                  topCardOff = false;
                  return true;
                }
              }
              //System.out.println(board[row][col].display()+ " " + independents[k].display()+ " "+ board[row][col].getColor().equals(independents[k].getColor()) + " " +checkCardToPiles(board[row][col],piles) + " " + !board[row][col].getIndependent());

            }
          }
        }
        if(board[row][col].getSide()){
          for(int k = 0; k < independents.length; k++){
            if(independents[k] != null){
              if(board[row][col].getRankAsInt() == independents[k].getRankAsInt() && board[row][col].getColor().equals(independents[k].getColor()) && checkCardToPiles(board[row][col], piles) && !board[row][col].getIndependent()){
                board[row][col].setIndependent(true);
                //System.out.println(board[row][col]+"HELLLLLLLLLOOOO00000000000000000000000000000000000000000000000oooooooooooooooooooooooooooooooooooooooooo00000000000000000000000000000000000000000000000000000OOOO");
                //helloCount++;
                for(int i = 1; i < board.length-independents[k].getRow()-1; i++){
                  if(i+row < board.length){
                  board[independents[k].getRow()+i][independents[k].getCol()] = board[row+i][col];
                  if(board[independents[k].getRow()+i][independents[k].getCol()] != null){
                    board[independents[k].getRow()+i][independents[k].getCol()].setRow(independents[k].getRow()+i);
                    board[independents[k].getRow()+i][independents[k].getCol()].setCol(independents[k].getCol()+i);
                  }

                  board[row+i][col] = null;
                  }
                }
                board[independents[k].getRow()][independents[k].getCol()].setIndependent(false);
                topCardOff = false;
                return false;
              }
            }
          }
        }
      }
    }
  }
  return false;
}

public void resetDeck(Stack<Card>  deck, Stack<Card> unused){
  indTopCards = new ArrayList<Card>();
  while(!unused.isEmpty()){
    deck.push(unused.pop());
  }
}

public boolean getBroken(){
  return this.broken;
}

public int getFaceUpBoardCt(){
  return this.faceUpBoardCt;
}

public int getDeckSize(Stack<Card> deck){
  return deck.size();
}

public void printIndTopCards(){
  for(Card c: indTopCards){
    System.out.println(c.display());
  }
}

public void printFirstFaceUp(){
  for(Card c: firstfaceUp){
    System.out.println(c.display());
  }
}

public void setBoardMove(boolean boardMove){
  this.boardMove = boardMove;
}


  public void initialize(Stack<Card> deck, Stack<Card> unused, int mode, List<Stack<Card>> piles, Card[][] board){
    makeDeck(deck);
    makePiles(piles);
    Collections.shuffle(deck);
    //printEntireDeck(deck, unused);
    setup(deck, board);
    makeTopCards(deck, unused, mode);
    numMoves = 0;
  }

public boolean oneMove(Stack<Card> deck, Stack<Card> unused, int mode, List<Stack<Card>> piles, Card[][] board){
  if(checkBoardToBoard(board, piles)) return true;
  if(checkTopCardToBoard(unused, board)) return true;
  if(checkTopCardToPiles(unused, piles)) return true;
  if(checkBoardToPiles(piles, board)) return true;
  return false;
  }

public void run(Stack<Card> deck, Stack<Card> unused, int mode, List<Stack<Card>> piles, Card[][] board){
  while(!deck.isEmpty() || boardMove == true){
    if(boardMove == false)
      makeTopCards(deck, unused, mode);
    else if (topCardOff == true && !unused.isEmpty())
      updateTopCards(unused.peek());
    if(unused.isEmpty())
      topCards = "      ";
    //printSetup(deck, unused, mode, piles, board);
    boardMove = oneMove(deck, unused, mode, piles, board);
    numMoves++;
    if(numMoves % 5000 == 0){
      broken = true;
      printSetup(deck, unused, mode, piles, board);
    }

    }
  }
}
