package main.java.tf.martin.tictactoe;

import java.util.ArrayList;
import java.util.List;

enum Piece {
    NONE,
    CROSS,
    CIRCLE
}

public class Board {
    
    private Piece[][] board;

    private List<BoardObserver> observers = new ArrayList<>();

    Board(int rows, int columns) {
        board = new Piece[rows][columns];
        reset();
    }

    public void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = Piece.NONE;
            }
        }
        notifyObserversOfBoardChange();
    }

    public String pieceAtPositionAsString(int row, int col) {
        if (board[row][col] == Piece.CROSS) return "X";
        if (board[row][col] == Piece.CIRCLE) return "O";
        return " ";
    }

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
        notifyObserversOfBoardChange();
        Piece winner = winningPlayer(row, col);
        if (winner != Piece.NONE) {
            notifyObserversOfPlayerWin(winner);
        }
    }

    public Piece at(int row, int col) {
        return board[row][col];
    }

    public void registerObserver(BoardObserver observer) {
        observers.add(observer);
    }

    private Piece winningPlayer(int row, int col) {
        if (isWinner(row, col)) {
            return board[row][col];
        }
        return Piece.NONE;
    }

    private boolean isWinner(int row, int col) {
        return isRowWinner(row) || isColumnWinner(col) || isDiagonalWinner(row, col);
    }

    private boolean isRowWinner(int row) {
        for (int i = 1; i < board[row].length; i++) {
            if (board[row][i-1] != board[row][i]) return false;
        }
        return true;
    }

    private boolean isColumnWinner(int col) {
        for (int i = 1; i < board.length; i++) {
            if (board[i-1][col] != board[i][col]) return false;
        }
        return true;
    }

    private boolean isDiagonalWinner(int row, int col) {
        boolean winnerFound = false;
        // Is on the postive diagonal 
        if (row == col) {
            winnerFound = true;
            for (int i = 1; i < board.length; i++) {
                if (board[i-1][i-1] != board[i][i]) winnerFound = false;
            }
        }
        // Is on the negative diagonal 
        if (row + col == 2) {
            winnerFound = true;
            for (int i = 1; i < board.length; i++) {
                if (board[i-1][3-i] != board[i][2-i]) winnerFound = false;
            }
        }
        return winnerFound;
    }

    private void notifyObserversOfBoardChange() {
        for (BoardObserver observer : observers) {
            observer.onBoardChange();
        }
    }

    private void notifyObserversOfPlayerWin(Piece winner) {
        for (BoardObserver observer : observers) {
            observer.onPlayerWin(winner);
        }
    }

    
}


