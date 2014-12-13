public class Card
{
  private boolean isFaceUp;
  private int rank;
  private String suit;
  
  public Card(int rank, String suit)
  {
    this.rank = rank;
    this.suit = suit;
    isFaceUp = false;
  }
  
  public int getRank()
  {
    return rank;
  }
  
  public String getSuit()
  {
    return suit;
  }
  
  public boolean isFaceUp()
  {
    return isFaceUp;
  }
  
  public void turnUp()
  {
    isFaceUp = true;
  }
  
  public void turnDown()
  {
    isFaceUp = false;
  }

  public String getFileName()
  {
    if (isFaceUp)
      return rank + suit + ".gif";
    else
      return "back.gif";
  }
}