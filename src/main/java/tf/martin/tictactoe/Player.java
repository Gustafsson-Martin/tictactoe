package main.java.tf.martin.tictactoe;

public class Player {
    private Piece piece;
    private volatile Cell move = null;

    Player(Piece piece) {
        this.piece = piece;
    }

    public void setMove(Cell move) {
        this.move = move;
    }

    public Cell getMove() {
        while (this.move == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
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