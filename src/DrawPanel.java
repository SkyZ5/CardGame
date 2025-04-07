import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private ArrayList<Card> deck;

    // Rectangle object represents a rectangle
    private Rectangle button;
    private Rectangle replaceButton;

    public DrawPanel() {
        button = new Rectangle(147, 330, 160, 26);
        replaceButton = new Rectangle(147, 280, 160, 26);
        this.addMouseListener(this);
        deck = Card.buildDeck();
        hand = Card.buildHand(deck);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 125;
        int y = 10;
        for (int i = 0; i < 3; i++) {
            for(int n = 0; n < 3; n ++) {
                Card c = hand.get(i * 3 + n);
                if (c.getHighlight()) {
                    g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
                }
                c.setRectangleLocation(x, y);
                g.drawImage(c.getImage(), x, y, null);
                x = x + c.getImage().getWidth() + 10;
            }
            x = 125;
            y += hand.get(i).getImage().getHeight() + 10;
        }
        // Drawing the bottom button
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("REPLACE CARDS", 150, 300);
        g.drawString(" PLAY AGAIN", 150, 350);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
        g.drawRect((int)replaceButton.getX(),(int)replaceButton.getY(), (int)replaceButton.getWidth(), (int)replaceButton.getHeight());
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (e.getButton() == 3 || e.getButton() == 1) {
            if (button.contains(clicked)) {
                deck = Card.buildDeck();
                hand = Card.buildHand(deck);
            }
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipHighlight();
                }
            }
            if (replaceButton.contains(clicked)){
                for(int i = 0; i < hand.size(); i++){
                    if(hand.get(i).getHighlight()){
                        Card replacementCard = deck.remove((int) (Math.random() * deck.size()));
                        hand.set(i, replacementCard);
                    }
                }
            }
        }


    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}