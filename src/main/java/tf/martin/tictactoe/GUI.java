package main.java.tf.martin.tictactoe;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUI extends JFrame implements BoardObserver {
    final int FRAME_MIN_HEIGHT = 600;
    final int FRAME_MIN_WIDTH = 800;

    JButton[][] buttons = new JButton[3][3];
    JLabel winnerLabel = new JLabel(String.format("Winner: %s", Piece.NONE));

    private final Board board;
    
    public GUI(Board board, PlacePieceActionFactory buttonPressActionFactory, ActionListener restartButtonAction) {
        super("Tic Tac Toe");
    
        this.board = board;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.setBackground(new Color(0, 0, 0));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Serif", Font.BOLD, 120));
                buttons[row][col].setFocusable(false);
                buttons[row][col].addActionListener(buttonPressActionFactory.build(row, col));
                buttons[row][col].setBackground(Color.white);
                buttons[row][col].setText(" ");
                buttons[row][col].setBorder(BorderFactory.createLineBorder(Color.black, 8));
                buttonPanel.add(buttons[row][col]);
            }
        }

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(2, 1));
        sidePanel.setBackground(new Color(230, 230, 230));

        winnerLabel.setFont(new Font("Serif", Font.BOLD, 16));

        JButton restartButton = new JButton();
        restartButton.setFont(new Font("Serif", Font.BOLD, 16));
        restartButton.addActionListener(restartButtonAction);
        restartButton.setBackground(Color.LIGHT_GRAY);
        restartButton.setText("Restart");
        restartButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        sidePanel.add(winnerLabel);
        sidePanel.add(restartButton);

        add(buttonPanel);
        add(sidePanel);
        pack();
        setVisible(true);
    }

    public void setText(int row, int col, String text) {
        buttons[row][col].setText(text);
    }

    @Override
    public void onBoardChange() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText(board.pieceAtPositionAsString(row, col));
            }
        }
    }

    @Override
    public void onPlayerWin(Piece winner) {
        winnerLabel.setText(String.format("Winner: %s", winner));
    }
}
