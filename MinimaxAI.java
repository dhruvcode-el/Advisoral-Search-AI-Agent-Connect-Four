import java.util.List;
 
/**
* Implements the Minimax algorithm with Alpha-Beta Pruning for the AI agent.
*/
public class MinimaxAI {
    private int searchDepth;
    private static final int WIN_SCORE = 1000000;
 
    public MinimaxAI(int depth) {
        this.searchDepth = depth;
    }
 
    public void setSearchDepth(int depth) {
        this.searchDepth = depth;
    }
 
    /**
     * The main AI decision-making method.
     * It iterates over all possible moves and finds the one that yields the
     * highest Minimax score.
     * @param board The current board state.
     * @return The optimal Move (column index).
     */
    public Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
 
        List<Move> validMoves = board.getValidMoves();
 
        // 1. Iterate over possible moves
        for (Move move : validMoves) {
            // 2. Create a deep copy of the board to simulate the move
            Board newBoard = new Board(board);
            newBoard.makeMove(move.getColumn(), Board.PLAYER_2); // AI is Player 2
 
            // 3. Call minimax for the opponent (Player 1/Minimizer)
            // Initial alpha = -Infinity, beta = +Infinity
            int score = minimax(newBoard, searchDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, Board.PLAYER_1);
 
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
 
        // Return the best move found, prioritizing center columns if scores are equal
        return bestMove;
    }
 
    /**
     * The recursive Minimax function with Alpha-Beta Pruning.
     * 
     * @param board The current board state.
     * @param depth The remaining search depth.
     * @param alpha The alpha cutoff value (best Maximizer score found so far).
     * @param beta The beta cutoff value (best Minimizer score found so far).
     * @param currentPlayer The player whose turn it is (PLAYER_1 or PLAYER_2).
     * @return The evaluated score of the board state.
     */
    private int minimax(Board board, int depth, int alpha, int beta, int currentPlayer) {
        // --- BASE CASES ---
 
        // 1. Win/Loss condition check
        if (board.checkWin(Board.PLAYER_2)) {
            return WIN_SCORE + depth; // AI (Max) win, prioritize faster wins
        }
        if (board.checkWin(Board.PLAYER_1)) {
            return -WIN_SCORE - depth; // Human (Min) win, prioritize delaying loss
        }
 
        // 2. Max depth reached (static evaluation)
        if (depth == 0) {
            return evaluate(board);
        }
 
        // 3. Draw condition
        if (board.isDraw()) {
            return 0;
        }
 
        // --- RECURSIVE CASES ---
 
        if (currentPlayer == Board.PLAYER_2) { // Maximizer (AI)
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getValidMoves()) {
                Board newBoard = new Board(board);
                newBoard.makeMove(move.getColumn(), Board.PLAYER_2);
                int eval = minimax(newBoard, depth - 1, alpha, beta, Board.PLAYER_1);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) { // Alpha-Beta Pruning
                    break;
                }
            }
            return maxEval;
        } else { // Minimizer (Human)
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getValidMoves()) {
                Board newBoard = new Board(board);
                newBoard.makeMove(move.getColumn(), Board.PLAYER_1);
                int eval = minimax(newBoard, depth - 1, alpha, beta, Board.PLAYER_2);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, minEval);
                if (beta <= alpha) { // Alpha-Beta Pruning
                    break;
                }
            }
            return minEval;
        }
    }
 
    /**
     * The Evaluation Function (Heuristic). This is the 'intelligence' of the AI.
     * It scores a non-terminal board state based on how favorable it is for the AI (Player 2).
     * @param board The board to evaluate.
     * @return A score: positive for AI advantage, negative for human advantage, 0 for neutral.
     */
    private int evaluate(Board board) {
        // Scoring is based on forming 'lines' of 2, 3, or blocking opponent's lines.
        int score = 0;
        
        // Simple Heuristic 1: Prioritize center column
        for (int r = 0; r < board.getRows(); r++) {
            if (board.getCell(r, 3) == Board.PLAYER_2) {
                score += 3;
            } else if (board.getCell(r, 3) == Board.PLAYER_1) {
                score -= 3;
            }
        }
 
        // Simple Heuristic 2: Count 2-in-a-rows and 3-in-a-rows (simplified)
        // A more advanced evaluation would analyze all possible 4-in-a-row opportunities,
        // but this skeleton provides a basic starting point.
        int[][] grid = new int[board.getRows()][board.getCols()]; // Access grid for simplicity
        // Populate the grid here for full analysis if not using board.getCell()
 
        // Example scoring for a 3-in-a-row for AI:
        // score += (countThrees(board, Board.PLAYER_2) * 50);
        // Example scoring for blocking a 3-in-a-row for Human:
        // score -= (countThrees(board, Board.PLAYER_1) * 40);
 
        return score;
    }
}
