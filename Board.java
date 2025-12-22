import java.util.ArrayList;
import java.util.List;
 
/**
* Represents the Connect Four game board state and handles core game logic.
* The board uses 0 for empty, 1 for Player 1 (Human), and 2 for Player 2 (AI).
*/
public class Board {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    public static final int EMPTY = 0;
    public static final int PLAYER_1 = 1; // Human
    public static final int PLAYER_2 = 2; // AI
 
    private final int[][] grid;
    private int lastMoveCol = -1;
    private int lastMoveRow = -1;
 
    public Board() {
        this.grid = new int[ROWS][COLS];
    }
 
    // Copy constructor for deep cloning, essential for Minimax
    public Board(Board other) {
        this.grid = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, COLS);
        }
        this.lastMoveCol = other.lastMoveCol;
        this.lastMoveRow = other.lastMoveRow;
    }
 
    /**
     * Executes a move on the board for the given player.
     * @param col The column index (0-6) where the piece is dropped.
     * @param player The player ID (PLAYER_1 or PLAYER_2).
     * @return true if the move was successful, false if the column is full.
     */
    public boolean makeMove(int col, int player) {
        if (!isColumnValid(col)) {
            return false;
        }
 
        for (int r = ROWS - 1; r >= 0; r--) {
            if (grid[r][col] == EMPTY) {
                grid[r][col] = player;
                this.lastMoveRow = r;
                this.lastMoveCol = col;
                return true;
            }
        }
        return false; // Should not happen if validity check is correct
    }
 
    public boolean isColumnValid(int col) {
        if (col < 0 || col >= COLS) {
            return false;
        }
        return grid[0][col] == EMPTY; // Check if top row is empty
    }
 
    /**
     * Finds all valid moves (columns that are not full).
     * @return A list of column indices (Moves).
     */
    public List<Move> getValidMoves() {
        List<Move> validMoves = new ArrayList<>();
        for (int c = 0; c < COLS; c++) {
            if (isColumnValid(c)) {
                // A Move object simply stores the column index
                validMoves.add(new Move(c));
            }
        }
        return validMoves;
    }
 
    /**
     * Checks if the last move resulted in a win.
     * This is the heavy lifting of the game logic.
     * @param player The player who just made the last move.
     * @return true if the player won, false otherwise.
     */
    public boolean checkWin(int player) {
        if (lastMoveCol == -1 || lastMoveRow == -1) {
            return false;
        }
 
        // Check four directions: Horizontal, Vertical, Diagonal (\), Diagonal (/)
        return checkDirection(1, 0, player) || // Horizontal
               checkDirection(0, 1, player) || // Vertical
               checkDirection(1, 1, player) || // Diagonal (\)
               checkDirection(1, -1, player); // Diagonal (/)
    }
 
    // Helper method to check for 4 in a row in a specific direction
    private boolean checkDirection(int dRow, int dCol, int player) {
        int count = 1;
 
        // Check forwards/up/right
        for (int i = 1; i < 4; i++) {
            int r = lastMoveRow + dRow * i;
            int c = lastMoveCol + dCol * i;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
 
        // Check backwards/down/left
        for (int i = 1; i < 4; i++) {
            int r = lastMoveRow - dRow * i;
            int c = lastMoveCol - dCol * i;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c] == player) {
                count++;
            } else {
                break;
            }
        }
 
        return count >= 4;
    }
 
    /**
     * Checks if the game is a draw (board is full and no winner).
     */
    public boolean isDraw() {
        return getValidMoves().isEmpty();
    }
 
    public int getCell(int r, int c) {
        return grid[r][c];
    }
 
    public int getRows() {
        return ROWS;
    }
 
    public int getCols() {
        return COLS;
    }
}
