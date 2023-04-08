package main.java.tf.martin.tictactoe;

interface BoardObserver {
    void onBoardChange();

    void onPlayerWin(Piece winner);
}