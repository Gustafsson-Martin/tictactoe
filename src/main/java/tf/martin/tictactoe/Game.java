package main.java.tf.martin.tictactoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

enum Player {
    CROSS,
    CIRCLE;

    private static final Player[] fastValuesCopy = values();
    private static final Map<Player, Piece> pieceMap = new EnumMap<>(Player.class);

    static {
        pieceMap.put(Player.CROSS, Piece.CROSS);
        pieceMap.put(Player.CIRCLE, Piece.CIRCLE);
    }

    public Player next() {
        return fastValuesCopy[(ordinal() + 1) % fastValuesCopy.length];
    }

    public Piece getPiece() {
        return pieceMap.get(this);
    }
}

public class Game {

    static final int ROWS = 3;
    static final int COLUMNS = 3;
    static final Player STARTING_PLAYER = Player.CROSS;

    private Player player = STARTING_PLAYER;
    private Board board;
    private GUI gui;

    Game() {
        this.board = new Board(ROWS, COLUMNS);
        this.gui = new GUI(board, new PlacePieceActionFactory(this), (e -> restart()));
        this.board.registerObserver(gui);
    }

    public boolean placePiece(int row, int col) {
        Cell cell = new Cell(row, col);
        if (board.at(cell) != Piece.NONE) return false;
        board.setPiece(cell, player.getPiece());
        player = player.next();
        return true;
    }

    public void restart() {
        board.reset();
        player = STARTING_PLAYER;
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
