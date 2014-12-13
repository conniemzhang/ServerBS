import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnection extends Thread
{
  private Socket socket;
  private BufferedReader in;  //what you receive from the server
  private PrintWriter out;    //what you send to the server
  private Bsdisplay display;
  private boolean isTurn;
  //Usernames ShowINputDialog, send first, Sorted Cards, Pile size(from anoop), Tells you when you're turn, Card Hand Size, Timer
  public ClientConnection(String host) throws IOException
  {
    this.socket = new Socket(host, 9003);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    display = new Bsdisplay(this);
    start();
  }
  
  public void run()
  {
    try
    {
      while (true)
      {
        String line = in.readLine();
        System.out.println("received:  " + line);
        String[] tokens = line.split(" ");
        if (tokens[0].equals("deal"))
        {
          String cardID = tokens[1]+tokens[2]+".gif";
          System.out.println(cardID);
          display.initHand(cardID);
        }
        
        else if (tokens[0].equals("delt"))
        {
          display.updateHandSize();
          display.displayCards();
        }
        else if (tokens[0].equals("name"))
        {
          display.setName(tokens[1]);
          System.out.println(tokens[1]);
        }  
        else if (tokens[0].equals("pile"))
        {
          display.updatePileSize(tokens[1]);
        }  
        else if (tokens[0].equals("turn"))
        {
          isTurn = true;
          display.getSubmitButton().setEnabled(true);
          if(tokens[1].equals("11"))
            {
              display.showDialog("It's your turn.  Play jacks");  
            }
            else if(tokens[1].equals("1"))
            {
              display.showDialog("It's your turn.  Play aces");  
            }
            else if(tokens[1].equals("12"))
            {
              display.showDialog("It's your turn.  Play queens");  
            }
            else if(tokens[1].equals("13"))
            {
              display.showDialog("It's your turn.  Play kings");  
            }
            else 
            {
              display.showDialog("It's your turn.  Play " + tokens[1] + ".");
            }
        }
        else if(tokens[0].equals("bsfl"))
        {
           display.showDialog("BS Called Incorrectly. Cards given to BSer");  
        }
        else if(tokens[0].equals("bstr"))
        {
           display.showDialog("BS Called Correctly. Previous player was bluffing.");  
        }
        else if(tokens[0].equals("uwon"))
        {
           display.showDialog("CONGRATS U WIN. Click OK to close.");
           System.exit(0);
        }
         else if(tokens[0].equals("winr"))
        {
           display.showDialog(tokens[1] + " Wins!");
           System.exit(0);
        }  

        else if(tokens[0].equals("plyd"))
        {
            
            if(tokens[2].equals("11"))
            {
              display.showDialog("There were " + tokens[1] + " jack" + "s Played!");  
            }
            else if(tokens[2].equals("12"))
            {
              display.showDialog("There were " + tokens[1] + " queen" + "s Played!");  
            }
            else if(tokens[2].equals("1"))
            {
              display.showDialog("There were " + tokens[1] + " aces" + "s Played!");  
            }
            else if(tokens[2].equals("13"))
            {
              display.showDialog("There were " + tokens[1] + " king" + "s Played!");  
            }
            else 
            {
              display.showDialog("There were " + tokens[1] + " " +  tokens[2] + "s Played!");  
            }
              
        }
        
       
        /*if (Math.random() < 0.5)
          out.println("yes");
        else
          out.println("no");*/
      }
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public boolean checkIfTurn()
  {
    return isTurn;
  }
  
  public void sendBS()
  {
    out.println("bsbs");
  }
  
  public void submit(ArrayList<ClickableCard> submittedCards)
  {
    String totalList = "";
    for(int i = 0; i<submittedCards.size(); i++)
    {
      totalList = totalList + submittedCards.get(i).getCardNum() + " " + submittedCards.get(i).getSuit()+ " ";
    }
    display.updateHandSize();
    System.out.println(totalList);
    out.println("play " + totalList);
    display.clearSubmitted();
  } 
  
  public void setUsername(String username)
  {
    out.println("name "+username);
  }
}