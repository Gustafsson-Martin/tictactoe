package main.java.tf.martin.tictactoe;

public class Player {
    private Piece piece;
    private volatile Cell move = null;

    Player(Piece piece) {
        this.piece = piece;
    }

    public synchronized void setMove(Cell move) {
        this.move = move;
        notify();
    }

    public synchronized Cell getMove() {
        while (this.move == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                continue;
            }
        }
        Cell rv = move;
        this.move = null;
        return rv;
    }

    public Piece getPiece() {
        return piece;
    }
}