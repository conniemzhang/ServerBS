import java.awt.event.*;
import javax.swing.*;

public class ClickableCard extends JLabel implements MouseListener
{
  //Card identification, Mouselistener
  private String ID;
  private Bsdisplay display;
  private boolean isInScrollPane;

  
  public ClickableCard(Bsdisplay display, String ID, ImageIcon image, String panel)
  {
    super(image);
    this.ID = ID;
    this.display = display;
    addMouseListener(this);
    if(panel.equals("scrollpane"))
         isInScrollPane = true;
      
  }
  public int getCardNum()
  {
    return Integer.parseInt(ID.substring(0, ID.length()-5));
  }
  public String getID()
  {
    return ID;
  }
  
  public void mouseClicked(MouseEvent e) {
    //System.out.println("Clicked "+ID);
    if(isInScrollPane == true)
    {
      isInScrollPane = display.addToSelCards(this);
      //System.out.println("put in selCard");
    }
    else
    {
      
      isInScrollPane = display.addToHand(this);
      //System.out.println("put back in hand");
    }
  }
  
  public void mousePressed(MouseEvent e) 
  {
  }
  
  public void mouseReleased(MouseEvent e) 
  {
  }
  
  public void mouseEntered(MouseEvent e) 
  {
  }
  
  public void mouseExited(MouseEvent e) 
  {
  }
  
  public String toString()
  {
    return ID + ":" + hashCode();
  }

  public String getSuit()
  {
    return ID.substring(ID.length() - 5, ID.length()-4);
  }

}