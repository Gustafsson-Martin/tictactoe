package main.java.tf.martin.tictactoe;

import java.util.List;

interface BoardObserver {
    void onBoardChange();

    void onStateChange();

    void onPlayerWin(List<Cell> cells);
}