package com.sprinthive.pokerhands;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainActivity implements ActionListener {
    private JButton btnSub = new JButton("Submit");
    private JTextField txtInput = new JTextField(10);
    private JPanel cardPanel = new JPanel();
    private JPanel handOne = new JPanel();
    private JPanel handTwo = new JPanel();

    public MainActivity() {
        ImageIcon img = new ImageIcon(MainActivity.class.getResource("/Icon.png"));
        JFrame frame = new JFrame();
        JPanel inputPanel = new JPanel();
        JLabel label = new JLabel("How many cards would you like?(52)");
        inputPanel.add(label);
        inputPanel.add(txtInput);
        btnSub.setFocusable(false);
        inputPanel.add(btnSub);
        frame.add(inputPanel , BorderLayout.NORTH);
        cardPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/2));
        frame.add(cardPanel, BorderLayout.CENTER);

        // Create labels for handOne and handTwo
        JLabel labelHandOne = new JLabel("Hand One");
        JLabel labelHandTwo = new JLabel("Hand Two");

        // Add labels and panels to SOUTH region
        JPanel southPanel = new JPanel(new GridLayout(3, 1)); // 3 rows: label, tie, label
        southPanel.add(labelHandOne);
        southPanel.add(new JLabel("TIE", SwingConstants.CENTER)); // Label for "TIE" centered
        southPanel.add(labelHandTwo);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(img.getImage());
        frame.setTitle("Pokerhands");
        frame.setVisible(true);
        btnSub.addActionListener(this);
    }

    public static void main(String[] args){
        new MainActivity();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Deck deck = new Deck();
        int userInput = Integer.parseInt(txtInput.getText());
        deck.pick(userInput);
        cardPanel.removeAll();
        handOne.removeAll();
        handTwo.removeAll();
        for (String cards : deck.img) {
            ImageIcon cardImage = new ImageIcon(MainActivity.class.getResource("/cards/" + cards + ".png"));
            Image img = cardImage.getImage().getScaledInstance(125, 150, Image.SCALE_DEFAULT);
            cardImage = new ImageIcon(img);
            JLabel cardLabel = new JLabel(cardImage);
            cardLabel.setName(cards);
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
            JLabel cardLabelOne = new JLabel(cardImage); // Create a new instance for handOne
            JLabel cardLabelTwo = new JLabel(cardImage); // Create a new instance for handTwo
            handOne.add(cardLabelOne);
            handTwo.add(cardLabelTwo);
            cardPanel.add(cardLabel);
        }
        cardPanel.revalidate();
        handOne.revalidate();
        handTwo.revalidate();
        cardPanel.repaint();
        handOne.repaint();
        handTwo.repaint();
    }
}