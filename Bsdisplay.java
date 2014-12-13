import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Bsdisplay 
{
  public JPanel selCardPanel = new JPanel();
  public ArrayList<ClickableCard> selCard = new ArrayList<ClickableCard>();
  public ArrayList<ClickableCard> hand = new ArrayList<ClickableCard>();
  public JPanel p = new JPanel(new FlowLayout());
  private JScrollPane scrollPane;
  public JPanel bottomPanel = new JPanel();
  public JPanel masterBox = new JPanel();
  private ClientConnection connection;
  private JFrame frame;
  private JPanel infoTower = new JPanel();
  private JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
  private JButton submitButton = new JButton("submit");
  private JLabel handSize;
  private JLabel pileSize;
  private String username;
  
  public static void main(String[] args)
  {
    String IPAddress = load("Connect.txt");
    try{
    ClientConnection connection = new ClientConnection(IPAddress);//"10.13.50.119");
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public Bsdisplay(ClientConnection cc)
  {
    connection = cc;
    frame = new JFrame();
    frame.setResizable(false);
    
    
    JLabel temp1=new JLabel(new ImageIcon(Bsdisplay.class.getResource("1s.gif")));
    JLabel cardBack=new JLabel(new ImageIcon(Bsdisplay.class.getResource("Card Back.jpg")));
    masterBox.setLayout(new BoxLayout(masterBox, BoxLayout.PAGE_AXIS));
    //JPanel p = new JPanel(new FlowLayout()); //PREFERRED!
    scrollPane = new JScrollPane(p);
    scrollPane.setPreferredSize(new Dimension(500, 125));
    
    JPanel midPanel = new JPanel(new FlowLayout()); //midPanel contains selCardPanel, submit button
    midPanel.setBackground(Color.gray);
    
    ///topPanel contains the info box both left and right, timer, score, and card back visual
    infoTower.setLayout(new BoxLayout(infoTower,BoxLayout.PAGE_AXIS));
    handSize = new JLabel("");
    pileSize = new JLabel("");
    infoTower.add(handSize);
    infoTower.add(pileSize);
    
    JLabel title = new JLabel(" WELCOME TO BS");
    
    topPanel.add(infoTower);
    topPanel.add(title);
    topPanel.setPreferredSize(new Dimension(100, 150));
    topPanel.setBackground(Color.lightGray);
    //JPanel clock;
    
    //JPanel selCardPanel = new JPanel();
    selCardPanel.setLayout(new FlowLayout());//panel for the selected cards
    selCardPanel.setBackground(Color.darkGray);
    selCardPanel.setPreferredSize(new Dimension(350, 110));
    selCardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    midPanel.add(selCardPanel);
    //submitButton.setActionCommand("enable");
    submitButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("You clicked the button");
                System.out.println("sending selCards: " + selCard);
                connection.submit(selCard);
                submitButton.setEnabled(false);
            }
    });
    submitButton.setEnabled(false);
    midPanel.add(submitButton);
    
    JButton bsButton = new JButton("Call BS");
    bsButton.setActionCommand("enable");
    bsButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("You clicked the BS button");
                connection.sendBS();
                
            }
    });
    midPanel.add(bsButton);    
    
    //JPanel bottomPanel = new JPanel(); //the container for scrollpanel, selected cards
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
    bottomPanel.add(midPanel);
    bottomPanel.add(Box.createRigidArea(new Dimension(0,5)));
    bottomPanel.add(scrollPane);
    
    masterBox.add(topPanel);
    masterBox.add(bottomPanel);
    frame.getContentPane().add(masterBox);
    
    frame.pack();
    frame.setVisible(true);
    setName(JOptionPane.showInputDialog("Please enter a username"));
  }
    
  public boolean addToSelCards(ClickableCard cardToAdd)
    {
      if(selCard.size()<4 && selCard.size() >=0)
      {
      selCard.add(cardToAdd);
      hand.remove(cardToAdd);
      
      p.remove(cardToAdd);
      selCardPanel.add(cardToAdd);
        
     
      bottomPanel.validate();
      bottomPanel.repaint();
      System.out.println("selCard size "+ selCard.size());
      return false;
      }
      else{
      System.out.println("you cannot select more than 4 cards");
      return true;
                           }

    }
    public boolean addToHand(ClickableCard cardToAdd)
    {
        placeCard(cardToAdd);
        selCard.remove(cardToAdd);
        
        displayCards();
        selCardPanel.remove(cardToAdd);
        
        bottomPanel.validate();
        bottomPanel.repaint();
        System.out.println("hand size: " + hand.size());
        return true;
      
    }
    public void initHand(String cardName)
    {
      String cardID = cardName;
      ClickableCard handCard = new ClickableCard(this, cardID, new ImageIcon(Bsdisplay.class.getResource(cardID)), "scrollpane");
      if(hand.size()==0)
      {
        hand.add(handCard);
      }
      else{
        int before = hand.size();
        for(int i = 0; i<hand.size(); i++)
        {
          if(hand.get(i).getCardNum()>=handCard.getCardNum())
          {
            hand.add(i,handCard);
            i = hand.size()+10;
          }
        }
        if(before == hand.size())
          hand.add(handCard);
      }
        
    }
    
    public void placeCard(ClickableCard card) //returns the cards in sorted order to hand
    {
      String cardID = card.getID();
      //ClickableCard handCard = new ClickableCard(this, cardID, new ImageIcon(Bsdisplay.class.getResource(cardID)), "scrollpane");
      if(hand.size()==0)
      {
        hand.add(card);
      }
      else{
        int before = hand.size();
        for(int i = 0; i<hand.size(); i++)
        {
          if(hand.get(i).getCardNum()>=card.getCardNum())
          {
            hand.add(i,card);
            i = hand.size()+10;
          }
        }
        if(before == hand.size())
          hand.add(card);
      }
        
    }
    public void displayCards() //prints all cards after dealt is called
    {
      Container parent = scrollPane.getParent();
      parent.remove(scrollPane);
      scrollPane = new JScrollPane(p);
      parent.add(scrollPane);
      for(int i = 0; i<hand.size(); i++)
      {
         p.add(hand.get(i));
      }
      bottomPanel.validate();
      bottomPanel.repaint();
      
    }
    public JButton getSubmitButton()
    {
      return submitButton;
    }
      
    public void clearSubmitted()
    {
      selCard = new ArrayList<ClickableCard>();
      selCardPanel.removeAll();
      selCardPanel.validate();
      selCardPanel.repaint();
      
    }
    
    public void setName(String name)
    {
      username = name;
      infoTower.add(new JLabel("You are: " + username));
      infoTower.add(new JLabel("----------------")); //supposed to be a spacer
      topPanel.validate();
      topPanel.repaint();
      connection.setUsername(name);
    }
    
    public void showDialog(String text)
    {
      JOptionPane.showMessageDialog(frame, text);
    }
    
    public void updateHandSize()
    {
      infoTower.remove(handSize);
      handSize = new JLabel("Your Hand: " + hand.size());
      infoTower.add(handSize);
      topPanel.validate();
      topPanel.repaint();
    }
    
    public void updatePileSize(String pile)
    {
      infoTower.remove(pileSize);
      pileSize = new JLabel("Num Cards in Pile: " + pile);
      infoTower.add(pileSize);
      topPanel.validate();
      topPanel.repaint();
    }
      
    public static String load(String fileName)
  {
      try
      {
      BufferedReader in = new BufferedReader(new FileReader(fileName));
      //ArrayList<String> lines = new ArrayList<String>();
      return in.readLine();
      }
      /**while (line != null)
      {
        lines.add(line);
        line = in.readLine();
      }
      in.close();
      String[] array = new String[lines.size()];
      for (int i = 0; i < array.length; i++)
        array[i] = lines.get(i);
      return array;
    }*/
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
        
    
}