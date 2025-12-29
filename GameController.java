 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
 
/**
* Manages the overall game flow, state, turns, and coordinates between the Board and AI.
*/
public class GameController {
    private Board board;
    private MinimaxAI aiAgent;
    private int currentPlayer;
    private boolean gameActive;
    private final Consumer<Board> boardUpdateCallback;
    private final Consumer<String> messageCallback;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> aiTask;
 
    public GameController(Consumer<Board> boardUpdate, Consumer<String> messageUpdate) {
        this.boardUpdateCallback = boardUpdate;
        this.messageCallback = messageUpdate;
        // Default difficulty/depth
        this.aiAgent = new MinimaxAI(5);
        resetGame();
    }
 
    public void resetGame() {
        this.board = new Board();
        this.currentPlayer = Board.PLAYER_1; // Human starts
        this.gameActive = true;
        this.boardUpdateCallback.accept(board);
        this.messageCallback.accept("Game started! Your turn (Player 1).");
    }
 
    public void setAIDifficulty(int depth) {
        this.aiAgent.setSearchDepth(depth);
        this.messageCallback.accept("AI Difficulty set to Depth: " + depth);
    }
 
    /**
     * Handles a human move initiated from the GUI.
     * @param col The column index chosen by the human.
     */
    public void handleHumanMove(int col) {
        if (!gameActive || currentPlayer != Board.PLAYER_1) {
            messageCallback.accept("It's not your turn or the game is over.");
            return;
        }
 
        if (board.makeMove(col, Board.PLAYER_1)) {
            boardUpdateCallback.accept(board);
            checkGameStatus(Board.PLAYER_1);
 
            if (gameActive) {
                currentPlayer = Board.PLAYER_2;
                messageCallback.accept("AI is thinking...");
                startAITurn();
            }
        } else {
            messageCallback.accept("Invalid move. Column is full.");
        }
    }
 
    private void startAITurn() {
        // Use the ExecutorService to run the computationally heavy AI task asynchronously
        aiTask = executor.submit(() -> {
            try {
                // Find the best move using Minimax
                Move aiMove = aiAgent.findBestMove(board);
                
                // Ensure UI updates happen on the main thread (platform thread)
                // In a real Swing/JavaFX app, you'd use SwingUtilities.invokeLater or Platform.runLater
                // For this skeleton, we'll simulate the execution, but note the cross-thread requirement
                
                if (aiMove != null) {
                    // Simulate a delay for dramatic effect
                    Thread.sleep(500); 
                    
                    if (board.makeMove(aiMove.getColumn(), Board.PLAYER_2)) {
                        boardUpdateCallback.accept(board);
                        checkGameStatus(Board.PLAYER_2);
 
                        if (gameActive) {
                            currentPlayer = Board.PLAYER_1;
                            messageCallback.accept("Your turn (Player 1).");
                        }
                    }
                } else {
                    // Should only happen if the board was full
                    checkGameStatus(Board.PLAYER_2);
                }
            } catch (Exception e) {
                System.err.println("AI Search Error: " + e.getMessage());
                messageCallback.accept("AI encountered an error. Resetting...");
                resetGame();
            }
        });
    }
 
    private void checkGameStatus(int lastPlayer) {
        if (board.checkWin(lastPlayer)) {
            gameActive = false;
            String winner = (lastPlayer == Board.PLAYER_1) ? "Human (Player 1)" : "AI (Player 2)";
            messageCallback.accept("Game Over! " + winner + " wins!");
        } else if (board.isDraw()) {
            gameActive = false;
            messageCallback.accept("Game Over! It's a draw!");
        }
    }
 
    public boolean isGameActive() {
        return gameActive;
    }
 
    public void shutdown() {
        if (aiTask != null) {
            aiTask.cancel(true);
        }
        executor.shutdownNow();
    }
}
