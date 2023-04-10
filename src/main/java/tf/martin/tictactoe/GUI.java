package main.java.tf.martin.tictactoe;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GUI extends JFrame implements BoardObserver {
    private static final int FRAME_MIN_WIDTH = 800;
    private static final int FRAME_MIN_HEIGHT = 600;
    private static final Color GRAY_ISH = new Color(230, 230, 230);
    
    private JButton[][] buttons = new JButton[3][3];
    private JLabel winnerLabel;
    private GridBagConstraints gridBagConstraints = new GridBagConstraints();

    private final Board board;
    private final int rows;
    private final int columns;

    public GUI(Board board, PlacePieceActionFactory buttonPressActionFactory, ActionListener restartButtonAction) {
        super("Tic Tac Toe");
        this.board = board;
        this.rows = board.getRows();
        this.columns = board.getColumns();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(FRAME_MIN_WIDTH, FRAME_MIN_HEIGHT));
        setLayout(new GridBagLayout());

        JPanel buttonPanel = createButtonPanel(buttonPressActionFactory);
        JPanel sidePanel = createSidePanel(restartButtonAction);

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1;
        add(buttonPanel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1;
        add(sidePanel, gridBagConstraints);
        pack();
        setVisible(true);
    }

    public void setText(int row, int col, String text) {
        buttons[row][col].setText(text);
    }

    @Override
    public void onBoardChange() {
        setBoardText();
    }

    @Override
    public void onStateChange() {
        winnerLabel.setText(String.format("State: %s", board.getState()));
    }

    private void setBoardText() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText(board.pieceAtPositionAsString(row, col));
            }
        }
    }

    private JPanel createButtonPanel(PlacePieceActionFactory buttonPressActionFactory) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, columns));
        buttonPanel.setBackground(Color.BLACK);

        createButtons(buttonPressActionFactory);
        for (int row = 0; row < rows; row++) {
            for (int columnn = 0; columnn < columns; columnn++) {
                buttonPanel.add(buttons[row][columnn]);
            }
        }
        return buttonPanel;
    }

    private JPanel createSidePanel(ActionListener restartButtonAction) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(GRAY_ISH);
    
        winnerLabel = new JLabel(String.format("State: %s", board.getState()));
        winnerLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        winnerLabel.setPreferredSize(new Dimension(100, 64));
    
        JButton restartButton = new JButton();
        restartButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        restartButton.addActionListener(restartButtonAction);
        restartButton.setBackground(Color.LIGHT_GRAY);
        restartButton.setText("Restart");
        restartButton.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        restartButton.setPreferredSize(new Dimension(100, 64));
    
        sidePanel.add(winnerLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10))); // add spacing between components
        sidePanel.add(restartButton);
        return sidePanel;
    }

    private void createButtons(PlacePieceActionFactory buttonPressActionFactory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                Font font = new Font("Serif", Font.BOLD, 120);
                button.setFont(font);
                button.setFocusable(false);
                button.addActionListener(buttonPressActionFactory.build(row, col));
                button.setBackground(Color.white);
                button.setBorder(BorderFactory.createLineBorder(Color.black, 8));
                button.setPreferredSize(calculateContentSizeBasedOnFont(button, font));
                buttons[row][col] = button;
            }
        }
    }

    private Dimension calculateContentSizeBasedOnFont(JButton button, Font font) {
        FontMetrics metrics = button.getFontMetrics(font);
        int width = metrics.stringWidth(button.getText());
        int height = metrics.getHeight();
        return new Dimension(width + 20, height + 10);
    }
}
