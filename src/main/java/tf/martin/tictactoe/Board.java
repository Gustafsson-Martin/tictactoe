package main.java.tf.martin.tictactoe;

import java.util.ArrayList;
import java.util.List;

enum Piece {
    NONE,
    CROSS,
    CIRCLE;

    Piece next() {
        if (this == Piece.CIRCLE) return Piece.CROSS;
        return Piece.CIRCLE;
    }
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

    public Board(Board original) {
        this.rows = original.rows;
        this.columns = original.columns;
        this.board = new Piece[rows][columns];
        this.state = State.IN_PROGRESS;
        for (Cell cell : original.getAllCells()) {
            board[cell.row][cell.column] = original.at(cell);
        }
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

    public List<Cell> getEmptyCells() {
        ArrayList<Cell> list = new ArrayList<>(rows*columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Cell cell = new Cell(row, column);
                if (at(cell) != Piece.NONE) continue;
                list.add(cell);
            }
        }
        return list;
    }

    public void reset() {
        state = State.IN_PROGRESS;
        for (Cell cell : getAllCells()) {
            setPiece(cell, Piece.NONE);
        }
        notifyObserversOfBoardChange();
        notifyObserversOfStateChange();
    }

    public String pieceAtPositionAsString(int row, int col) {
        if (board[row][col] == Piece.CROSS) return "X";
        if (board[row][col] == Piece.CIRCLE) return "O";
        return " ";
    }

    public void setPiece(Cell cell, Piece piece) {
        if (state != State.IN_PROGRESS) return;
        board[cell.row][cell.column] = piece;
        notifyObserversOfBoardChange();
        if (piece != Piece.NONE) updateState(cell);
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
        if (state == State.CIRCLE_WIN || state == State.CROSS_WIN) notifyObserversOfPlayerWin(getWinnerCells(cell));
    }

    private State newStateAfterPiecePlacement(Cell cell) {
        if (isWinner(cell)) {
            if (at(cell) == Piece.CROSS) return State.CROSS_WIN;
            return State.CIRCLE_WIN;
        }
        if (emptyCellsCount() == 0) return State.DRAW;
        return State.IN_PROGRESS;
    }

    private List<Cell> getWinnerCells(Cell cell) {
        List<Cell> winnerCells = new ArrayList<>();
        if (isRowWinner(cell)) {
            for (int column = 0; column < columns; column++) {
                winnerCells.add(new Cell(cell.row, column));
            }
        } else if (isColumnWinner(cell)) {
            for (int row = 0; row < rows; row++) {
                winnerCells.add(new Cell(row, cell.column));
            }
        } else if (isNegativeDiagonalWinner(cell)) {
            for (int row = 0; row < rows; row++) {
                winnerCells.add(new Cell(row, row));
            }
        } else if (isPositiveDiagonalWinner(cell)) {
            for (int row = 0; row < rows; row++) {
                winnerCells.add(new Cell(row, columns-row-1));
            }
        }
        return winnerCells;
    }

    private boolean isWinner(Cell cell) {
        return isRowWinner(cell) || isColumnWinner(cell) || isNegativeDiagonalWinner(cell) || isPositiveDiagonalWinner(cell);
    }

    private boolean isRowWinner(Cell cell) {
        for (int i = 1; i < columns; i++) {
            if (board[cell.row][i-1] != board[cell.row][i]) return false;
        }
        return true;
    }

    private boolean isColumnWinner(Cell cell) {
        for (int i = 1; i < rows; i++) {
            if (board[i-1][cell.column] != board[i][cell.column]) return false;
        }
        return true;
    }

    private boolean isNegativeDiagonalWinner(Cell cell) { 
        if (cell.row != cell.column) return false;
        for (int i = 1; i < rows; i++) {
            if (board[i-1][i-1] != board[i][i]) return false;
        }
        return true;
    }

    private boolean isPositiveDiagonalWinner(Cell cell) { 
        if (cell.row + cell.column != rows - 1) return false;
        for (int i = 1; i < rows; i++) {
            if (board[i-1][rows-i] != board[i][rows-i-1]) return false;
        }
        return true;
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

    private void notifyObserversOfPlayerWin(List<Cell> cells) {
        for (BoardObserver observer : observers) {
            observer.onPlayerWin(cells);
        }
    }

    
}


