package main.java.tf.martin.tictactoe;

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
        Cell bestCell = null;
        int bestScore = Integer.MIN_VALUE;
        for (Cell cell : board.getAllCells()) {
            if (board.at(cell) == Piece.NONE) {
                Board newBoard = new Board(board);
                newBoard.setPiece(cell, Piece.CIRCLE);
                int score = minimax(newBoard, 0, false);
                System.out.printf("(%d,%d) -> %d\n", cell.row, cell.column, score);
                if (score > bestScore) {
                    bestCell = cell;
                    bestScore = score;
                }
            }
        }
        System.out.println(bestCell);
        return bestCell;
    }

    private int minimax(Board board, int depth, boolean isMaximizingPlayer) {
        if (board.getState() == State.CIRCLE_WIN) return WIN_SCORE;
        if (board.getState() == State.CROSS_WIN) return LOSE_SCORE;
        if (board.getState() == State.DRAW) return TIE_SCORE;
        if (depth == MAX_DEPTH) return TIE_SCORE;

        int bestScore = 0;
        if (isMaximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (Cell cell : board.getAllCells()) {
                if (board.at(cell) == Piece.NONE) {
                    Board newBoard = new Board(board);
                    newBoard.setPiece(cell, Piece.CIRCLE);
                    int score = minimax(newBoard, depth+1, false);
                    bestScore = Math.max(bestScore, score);
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (Cell cell : board.getAllCells()) {
                if (board.at(cell) == Piece.NONE) {
                    Board newBoard = new Board(board);
                    newBoard.setPiece(cell, Piece.CROSS);
                    int score = minimax(newBoard, depth+1, true);
                    bestScore = Math.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }
}
