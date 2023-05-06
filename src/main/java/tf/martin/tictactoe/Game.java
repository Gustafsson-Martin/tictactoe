package main.java.tf.martin.tictactoe;

import java.util.List;

class CircularList<T> {
    private int index;
    private List<T> items;

    CircularList(List<T> items) {
        this.items = items;
        this.index = 0;
    }

    public void reset() {
        index = 0;
    }

    public T next() {
        index = (index + 1) % items.size();
        return items.get(index);
    }

    public T current() {
        return items.get(index);
    }
}

public class Game {
    static final int ROWS = 3;
    static final int COLUMNS = 3;

    private Board board;
    private GUI gui;
    private Player humanPlayer;
    private Bot bot;
    private CircularList<Player> players;

    Game() {
        this.board = new Board(ROWS, COLUMNS);
        this.humanPlayer = new Player(Piece.CROSS);
        this.bot = new Bot(board, Piece.CIRCLE);
        this.players = new CircularList<Player>(List.of(humanPlayer, bot));
        this.gui = new GUI(board, humanPlayer, (e -> startNewGame()));
        this.board.registerObserver(gui);
        startNewGame();
    }

    private void startNewGame() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                board.reset();
                players.reset();
                while (board.getState() == State.IN_PROGRESS) {
                    Player player = players.current();
                    boolean moveSuccess = makeMove(player.getMove(), player.getPiece());
                    if (moveSuccess) players.next();
                }
            }
        });
        thread.start();
    }

    public boolean makeMove(Cell cell, Piece piece) {
        if (board.at(cell) != Piece.NONE) return false;
        board.setPiece(cell, piece);
        return true;
    }
}
