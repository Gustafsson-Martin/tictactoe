package main.java.tf.martin.tictactoe;

import java.util.ArrayList;
import java.util.List;

enum Piece {
    NONE,
    CROSS,
    CIRCLE
}

enum State {
    IN_PROGRESS,
    DRAW,
    CROSS_WIN,
    CIRCLE_WIN
}

class Cell {
    public int row;
    public int column;

    Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }
}

public class Board {
    
    private Piece[][] board;
    private int rows;
    private int columns;
    private State state = State.IN_PROGRESS;

    private List<BoardObserver> observers = new ArrayList<>();

    Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new Piece[rows][columns];
        reset();
    }

    public List<Cell> getAllCells() {
        ArrayList<Cell> list = new ArrayList<>(rows*columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                list.add(new Cell(row, column));
            }
        }
        return list;
    }

    public void reset() {
        for (Cell cell : getAllCells()) {
            setPiece(cell, Piece.NONE);
        }
        state = State.IN_PROGRESS;
        notifyObserversOfBoardChange();
        notifyObserversOfStateChange();
    }

    public String pieceAtPositionAsString(int row, int col) {
        if (board[row][col] == Piece.CROSS) return "X";
        if (board[row][col] == Piece.CIRCLE) return "O";
        return " ";
    }

    public void setPiece(int row, int col, Piece piece) {
        if (state != State.IN_PROGRESS) return;
        board[row][col] = piece;
        notifyObserversOfBoardChange();
        updateState(new Cell(row, col));
    }

    public void setPiece(Cell cell, Piece piece) {
        // TODO: Move everything from row, column -> Cell
        board[cell.row][cell.column] = piece;
        notifyObserversOfBoardChange();
        updateState(cell);
    }

    public void registerObserver(BoardObserver observer) {
        observers.add(observer);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public State getState() {
        return state;
    }

    public Piece at(Cell cell) {
        return board[cell.row][cell.column];
    }

    public int emptyCellsCount() {
        int count = 0;
        for (Cell cell : getAllCells()) {
            if (at(cell) == Piece.NONE) count += 1;
        }
        return count;
    }

    private void updateState(Cell cell) {
        State newState = newStateAfterPiecePlacement(cell);
        boolean stateChange = (state != newState);
        state = newState;
        if (stateChange) notifyObserversOfStateChange();
    }

    private State newStateAfterPiecePlacement(Cell cell) {
        if (isWinner(cell.row, cell.column)) {
            if (at(cell) == Piece.CROSS) return State.CROSS_WIN;
            return State.CIRCLE_WIN;
        }
        if (emptyCellsCount() == 0) return State.DRAW;
        return State.IN_PROGRESS;
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

    private void notifyObserversOfStateChange() {
        for (BoardObserver observer : observers) {
            observer.onStateChange();
        }
    }

    
}


