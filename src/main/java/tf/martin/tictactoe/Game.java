package main.java.tf.martin.tictactoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum Player {
    CROSS,
    CIRCLE
}

public class Game implements BoardObserver{

    static final int ROWS = 3;
    static final int COLUMNS = 3;

    private Player player = Player.CROSS;
    private Board board;
    private GUI gui;

    Game() {
        this.board = new Board(ROWS, COLUMNS);
        this.gui = new GUI(board, new PlacePieceActionFactory(this), (e -> restart()));
        this.board.registerObserver(gui);
        this.board.registerObserver(this);
    }

    public boolean placePiece(int row, int col) {
        if (board.at(row, col) != Piece.NONE) return false;
        board.setPiece(row, col, currentPiece());
        nextPlayer();
        return true;
    }

    public void restart() {
        board.reset();
    }

    private Piece currentPiece() {
        if (player == Player.CROSS) return Piece.CROSS;
        return Piece.CIRCLE;
    }

    private void nextPlayer() {
        if (player == Player.CROSS) {
            player = Player.CIRCLE;
        } else  {
            player = Player.CROSS;
        }
    }

    @Override
    public void onBoardChange() {}

    @Override
    public void onPlayerWin(Piece winner) {
        board.reset();
    }
}

class PlacePieceActionFactory {
    private Game game;

    PlacePieceActionFactory(Game game) {
        this.game = game;
    };

    public PlacePieceAction build(int row, int col) {
        return new PlacePieceAction(game, row, col);
    }
}

class PlacePieceAction implements ActionListener {

    private int row;
    private int col;
    private Game game;

    PlacePieceAction(Game game, int row, int col) {
        this.game = game;
        this.row = row;
        this.col = col;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.placePiece(row, col);
    }
    
}
