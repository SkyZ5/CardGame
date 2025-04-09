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
    private int cardsRemaining;
    private String message;

    public DrawPanel() {
        button = new Rectangle(147, 330, 160, 26);
        replaceButton = new Rectangle(147, 280, 160, 26);
        this.addMouseListener(this);
        deck = Card.buildDeck();
        hand = Card.buildHand(deck);
        cardsRemaining = deck.size();
        message = "";
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
        boolean canContinue = canContinue();
        if(!canContinue){
            message = "NO MORE MOVES";
        }
        else{
            message = "";
        }
        // Drawing the bottom button
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("REPLACE CARDS", 150, 300);
        g.drawString(" PLAY AGAIN", 150, 350);
        g.drawString(message, 10, 400);
        cardsRemaining = deck.size();
        g.drawString("CARDS REMAINING: " + cardsRemaining, 10, 450);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
        g.drawRect((int)replaceButton.getX(),(int)replaceButton.getY(), (int)replaceButton.getWidth(), (int)replaceButton.getHeight());
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (e.getButton() == 3 || e.getButton() == 1) {
            if (button.contains(clicked)) {
                deck = Card.buildDeck();
                hand = Card.buildHand(deck);
                message = "";
            }
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipHighlight();
                }
            }
            if (replaceButton.contains(clicked)){
                ArrayList<Card> highlighted = new ArrayList<Card>();
                boolean run = false;
                for(int i = 0; i < hand.size(); i++){
                    if(hand.get(i).getHighlight()){
                        highlighted.add(hand.get(i));
                    }
                }
                if(highlighted.size() == 3){
                    boolean containsJack = false;
                    boolean containsQueen = false;
                    boolean containsKing = false;
                    for(Card card : highlighted){
                        if(card.getValue().equals("J")){
                            containsJack = true;
                        }
                        else if(card.getValue().equals("Q")){
                            containsQueen = true;
                        }
                        else if(card.getValue().equals("K")){
                            containsKing = true;
                        }
                    }
                    if(containsJack && containsKing && containsQueen){
                        run = true;
                    }
                }
                else if(highlighted.size() == 2){
                    int total = 0;
                    for(Card card : highlighted){
                        if(card.getValue().equals("A")){
                            total += 1;
                        }
                        else if(card.getValue().equals("02")){
                            total += 2;
                        }
                        else if(card.getValue().equals("03")){
                            total += 3;
                        }
                        else if(card.getValue().equals("04")){
                            total += 4;
                        }
                        else if(card.getValue().equals("05")){
                            total += 5;
                        }
                        else if(card.getValue().equals("06")){
                            total += 6;
                        }
                        else if(card.getValue().equals("07")){
                            total += 7;
                        }
                        else if(card.getValue().equals("08")){
                            total += 8;
                        }
                        else if(card.getValue().equals("09")){
                            total += 9;
                        }
                        else if(card.getValue().equals("10")){
                            total += 10;
                        }
                    }
                    if(total == 11){
                        run = true;
                    }
                }
                if(run) {
                    message = "";
                    for (int i = 0; i < hand.size(); i++) {
                        if (hand.get(i).getHighlight()) {
                            if(!deck.isEmpty()) {
                                Card replacementCard = deck.remove((int) (Math.random() * deck.size()));
                                hand.set(i, replacementCard);
                            }
                            else{
                                hand.get(i).flipCard();
                            }
                        }
                    }
                    if(deck.isEmpty()){
                        int flipped = 0;
                        for(Card card : hand){
                            if(!card.isShow()){
                                flipped ++;
                            }
                        }
                        if(flipped == 9){
                            message = "VICTORY YOU DID IT!";
                        }
                    }
                }
                else{
                    message = "THAT DOESN'T WORK";
                }
            }
        }


    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
    public boolean canContinue(){
        boolean canContinue = false;
        boolean containsJack = false;
        boolean containsQueen = false;
        boolean containsKing = false;
        for(Card card : hand){
            if(card.getValue().equals("J")){
                containsJack = true;
            }
            else if(card.getValue().equals("Q")){
                containsQueen = true;
            }
            else if(card.getValue().equals("K")){
                containsKing = true;
            }
        }
        if(containsJack && containsKing && containsQueen){
            canContinue = true;
        }
        for(Card card : hand){
            int cardValue = getCardValue(card);
            for(Card card1 : hand) {
                if(card1 != card) {
                    int cardValue1 = getCardValue(card1);
                    if(cardValue1 + cardValue == 11){
                        canContinue = true;
                    }
                }
            }
        }

        return canContinue;
    }

    private static int getCardValue(Card card) {
        int cardValue = 0;
        if (card.getValue().equals("A")) {
            cardValue += 1;
        } else if (card.getValue().equals("02")) {
            cardValue += 2;
        } else if (card.getValue().equals("03")) {
            cardValue += 3;
        } else if (card.getValue().equals("04")) {
            cardValue += 4;
        } else if (card.getValue().equals("05")) {
            cardValue += 5;
        } else if (card.getValue().equals("06")) {
            cardValue += 6;
        } else if (card.getValue().equals("07")) {
            cardValue += 7;
        } else if (card.getValue().equals("08")) {
            cardValue += 8;
        } else if (card.getValue().equals("09")) {
            cardValue += 9;
        } else if (card.getValue().equals("10")) {
            cardValue += 10;
        }
        return cardValue;
    }
}