import java.util.*;
import java.io.*;


public class Card{

  final String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
  final Character[] ranks = {'A','2','3','4','5','6','7','8','9','T','J','Q','K'};

  //All the qualities a card could have
  private char rank;
  private char shortSuit; //The suit expressed in one letter
  private String suit;
  private String color; //red or black, based on suit
  private int rankAsInt; //ranks expressed as integers, used for comparing
  private int suitAsInt; //suits expressed as integers, used for determining what pile to place a card
  private boolean up = false; //true if player can see card otherwise false
  private boolean independent = false; // a card is independent if it is face up and has no cards face up above it
  private int row = -1; //row of the independent cards, for now
  private int col = -1; //column of the independent cards, for now

  public Card(char rank, String suit){
    this.rank = rank;
    this.suit = suit;
    this.color = suit == "Spades" || suit == "Clubs" ? "Black" : "Red";
    this.rankAsInt = Arrays.asList(ranks).indexOf(this.rank)+1;
    this.suitAsInt = Arrays.asList(suits).indexOf(this.suit);
    this.shortSuit = suit.charAt(0);
  }

  public char getRank(){
    return this.rank;
  }

  public String getSuit(){
    return this.suit;
  }

  public String getColor(){
    return this.color;
  }

  public int getRankAsInt(){
    return this.rankAsInt;
  }

  public int getSuitAsInt(){
    return this.suitAsInt;
  }

  public int getRow(){
    return this.row;
  }

  public int getCol(){
    return this.col;
  }

  public void setRow(int row){
    this.row = row;
  }

  public void setCol(int col){
    this.col = col;
  }

  public boolean oneLess(Card c){
    return this.rankAsInt == c.getRankAsInt()-1;
  }

  public boolean oneMore(Card c){
    return this.rankAsInt-1 == c.getRankAsInt();
  }

  public void setSide(boolean up){
    this.up = up;
  }

  public boolean getSide(){
    return this.up;
  }

  public void setIndependent(boolean independent){
    this.up = true;
    this.independent = independent;
  }

  public boolean getIndependent(){
    return this.independent;
  }

  public char getShortSuit(){
    return this.shortSuit;
  }

  public String display(){
    return this.rank+""+this.shortSuit;
  }

  public String toString(){
    return rank + " " + suit + " " + color + " " + up + " " + independent + " " + rankAsInt + " " + suitAsInt + " " + shortSuit;
  }
}
