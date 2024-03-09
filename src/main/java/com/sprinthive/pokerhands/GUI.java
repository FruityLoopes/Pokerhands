package com.sprinthive.pokerhands;

import com.sprinthive.pokerhands.handrank.HandRank;
import com.sprinthive.pokerhands.handrank.BetterPokerHandRanker;
import com.sprinthive.pokerhands.Card;
import com.sprinthive.pokerhands.CardRank;
import com.sprinthive.pokerhands.Suit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private JPanel MainPanel;
    private JLabel label;
    private JTextField txtInput;
    private JButton btnSubmit;
    private JPanel cardPanel;
    private JPanel handsPanel;
    private JPanel handOne;
    private JPanel handTwo;
    private JLabel lblHandOne;
    private JLabel lblHandTwo;
    private JLabel lblRank;

    public GUI() {

        JFrame frame = new JFrame();

        //setting up how the frame is going to start and look
        frame.add(MainPanel, BorderLayout.CENTER);
        ImageIcon img = new ImageIcon(GUI.class.getResource("/Icon.png"));
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(img.getImage());
        frame.setTitle("Pokerhands");
        frame.setVisible(true);

        //setting all the layouts for the panels being used
        cardPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 2));
        cardPanel.setLayout(new GridLayout(0, 15, 0, 0));
        handOne.setLayout(new GridLayout(1, 5, 0, 0));
        handTwo.setLayout(new GridLayout(1, 5, 0, 0));
        btnSubmit.addActionListener(new ActionListener() { //submit button that uses your input to get the amount of cards they wanted
            @Override
            public void actionPerformed(ActionEvent e) {
                lblHandOne.setText("");

                Deck deck = new Deck();
                int userInput = Integer.parseInt(txtInput.getText());
                deck.pick(userInput); //running pick method to get the amount og cards they wanted

                //clearing panels
                cardPanel.removeAll();
                handOne.removeAll();
                handTwo.removeAll();
                for (String cards : deck.img) { //for loop to load all images for card faces

                    //calling loadimage method to set dimensions and load the image
                    ImageIcon cardImage = new ImageIcon(loadImage(cards, 100, 120));
                    JLabel cardLabel = new JLabel(cardImage);

                    cardLabel.setName(cards); //setting the name to name of cards

                    cardLabel.addMouseListener(new MouseAdapter() { //when a card is clicked it will then move it to handOne then handTwo while also if being clicked again will return to cardPanel

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);

                            JLabel clickedLabel = (JLabel) e.getSource();
                            System.out.println(clickedLabel.getName());
                            if (handOne.isAncestorOf(clickedLabel)) {
                                handOne.remove(clickedLabel);
                                cardPanel.add(clickedLabel);
                            } else if (handTwo.isAncestorOf(clickedLabel)) {
                                handTwo.remove(clickedLabel);
                                cardPanel.add(clickedLabel);
                            } else if (cardPanel.isAncestorOf(clickedLabel)) {

                                if (handOne.getComponentCount() < 5) {
                                    handOne.add(clickedLabel);
                                } else if (handTwo.getComponentCount() < 5) {
                                    handTwo.add(clickedLabel);
                                }
                                cardPanel.remove(clickedLabel);
                            }
                            List<Card> handOneCards = new ArrayList<>();
                            List<Card> handTwoCards = new ArrayList<>();

                            for (Component component : handOne.getComponents()) {
                                if (component instanceof JLabel) {
                                    JLabel label = (JLabel) component;
                                    String cardName = label.getName();
                                    String[] parts = cardName.split(" of ");
                                    String rankString = parts[0];
                                    String suitString = parts[1];
                                    CardRank rank = CardRank.valueOf(rankString.toUpperCase());
                                    Suit suit = Suit.valueOf(suitString.toUpperCase());
                                    Card card = new Card(rank, suit);
                                    handOneCards.add(card);
                                }
                            }

                            for (Component component : handTwo.getComponents()) {
                                if (component instanceof JLabel) {
                                    JLabel label = (JLabel) component;
                                    String cardName = label.getName();
                                    String[] parts = cardName.split(" of ");
                                    String rankString = parts[0];
                                    String suitString = parts[1];
                                    CardRank rank = CardRank.valueOf(rankString.toUpperCase());
                                    Suit suit = Suit.valueOf(suitString.toUpperCase());
                                    Card card = new Card(rank, suit);
                                    handTwoCards.add(card);
                                }
                            }
                            BetterPokerHandRanker ranker = new BetterPokerHandRanker();

                            List<Card> listOne = new ArrayList<>(handOneCards);
                            List<Card> listTwo = new ArrayList<>(handTwoCards);

                            HandRank handRankOne = ranker.findBestHandRank(listOne);
                            HandRank handRankTwo = ranker.findBestHandRank(listTwo);

                            if (handOne.getComponentCount() == 5) { //only once the hand is full is will display what hand it is
                                lblHandOne.setText(handRankOne.describeHand());
                            }
                            if (handTwo.getComponentCount() == 5) { //only once the hand is full is will display what hand it is
                                lblHandTwo.setText(handRankTwo.describeHand());
                            }

                            if (handOne.getComponentCount() == 5 && handTwo.getComponentCount() == 5) { //will only display the stronger hand once both hands are full
                                if (handRankOne.compareTo(handRankTwo) > 0) {
                                    lblRank.setText(">");
                                } else {
                                    lblRank.setText("<");
                                }
                            }

                            cardPanel.revalidate();
                            handOne.revalidate();
                            handTwo.revalidate();
                            cardPanel.repaint();
                            handOne.repaint();
                            handTwo.repaint();

                        }
                    });

                    cardPanel.add(cardLabel);

                }
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });
    }

    public static void main(String[] args) {
        new GUI();
    }

    public Image loadImage(String imageName, int width, int height) { // method to return image from given the image name, the card name (ace of spades)
        try {
            ImageIcon icon = new ImageIcon(GUI.class.getResource("/cards/" + imageName + ".png"));
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

