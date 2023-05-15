package main.java.tf.martin.tictactoe;

import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player {
    private static final int WIN_SCORE = 1;
    private static final int LOSE_SCORE = -1;
    private static final int TIE_SCORE = 0;
    private static final int MAX_DEPTH = 4;

    private Board board;
    
    Bot(Board board, Piece piece) {
        super(piece);
        this.board = board;
    }

    @Override
    public Cell getMove() { 
        ArrayList<Cell> bestCells = new ArrayList<Cell>();
        int bestScore = Integer.MIN_VALUE;
        for (Cell cell : board.getEmptyCells()) {
            Board newBoard = new Board(board);
            newBoard.setPiece(cell, getPiece());
            int score = minimax(newBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            //System.out.printf("(%d,%d) -> %d\n", cell.row, cell.column, score);
            if (score > bestScore) {
                bestCells = new ArrayList<Cell>();
                bestScore = score;
            }
            if (score == bestScore) {
                bestCells.add(cell);
            } 
        }
        int randomInt = new Random().nextInt(bestCells.size());
        return bestCells.get(randomInt);
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (board.getState() == State.CROSS_WIN) {
            if (getPiece() == Piece.CROSS) return WIN_SCORE;
            return LOSE_SCORE;
        }
        if (board.getState() == State.CIRCLE_WIN) {
            if (getPiece() == Piece.CIRCLE) return WIN_SCORE;
            return LOSE_SCORE;
        }
        if (board.getState() == State.DRAW) return TIE_SCORE;
        if (depth == MAX_DEPTH) return TIE_SCORE;

        int bestScore = 0;
        if (isMaximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (Cell cell : board.getEmptyCells()) {
                Board newBoard = new Board(board);
                newBoard.setPiece(cell, getPiece());
                int score = minimax(newBoard, depth+1, alpha, beta, false);
                bestScore = Math.max(bestScore, score);
                if (bestScore > beta) break;
                alpha = Math.max(alpha, bestScore);
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (Cell cell : board.getEmptyCells()) {
                Board newBoard = new Board(board);
                newBoard.setPiece(cell, getPiece().next());
                int score = minimax(newBoard, depth+1, alpha, beta, true);
                bestScore = Math.min(bestScore, score);
                if (bestScore < alpha) break;
                beta = Math.min(beta, bestScore);
            }
        }
        return bestScore;
    }
}
