 
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
/**
* Main class for the GUI using Swing (as a simple skeleton).
* In a professional implementation, JavaFX is often preferred.
*/
public class ConnectFourApp extends JFrame {
    private static final int TILE_SIZE = 80;
    private final GameController controller;
    private final BoardPanel boardPanel;
    private final JLabel messageLabel;
    private final JComboBox<Integer> depthChooser;
 
    public ConnectFourApp() {
        super("Connect Four AI (Minimax)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize UI components
        this.messageLabel = new JLabel("Welcome! Choose a difficulty and start.", SwingConstants.CENTER);
        this.boardPanel = new BoardPanel();
        
        // Initialize Controller, providing UI update methods
        this.controller = new GameController(
            this::updateBoardUI, 
            this::updateMessage
        );
 
        // --- Setup Layout ---
        setLayout(new BorderLayout(10, 10));
 
        // Top Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
 
        // Difficulty Chooser
        controlPanel.add(new JLabel("AI Depth:"));
        Integer[] depths = {1, 2, 3, 4, 5, 6}; // Max depth 6 is usually good for Connect4
        depthChooser = new JComboBox<>(depths);
        depthChooser.setSelectedItem(5);
        depthChooser.addActionListener(e -> {
            int depth = (int) depthChooser.getSelectedItem();
            controller.setAIDifficulty(depth);
        });
        controlPanel.add(depthChooser);
 
        // Reset Button
        JButton resetButton = new JButton("New Game");
        resetButton.addActionListener(e -> controller.resetGame());
        controlPanel.add(resetButton);
 
        // Add components to JFrame
        add(controlPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);
 
        // Final setup
        pack();
        setLocationRelativeTo(null); // Center window
        setVisible(true);
 
        // Ensure shutdown hook is called
        Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown));
    }
 
    // --- UI Update Methods ---
 
    private void updateBoardUI(Board board) {
        // Must ensure this runs on the Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            boardPanel.setBoard(board);
            boardPanel.repaint();
        });
    }
 
    private void updateMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText(message);
        });
    }
 
    // --- Inner Class for Drawing the Board ---
    private class BoardPanel extends JPanel {
        private Board currentBoard;
 
        public BoardPanel() {
            setPreferredSize(new Dimension(Board.COLS * TILE_SIZE, Board.ROWS * TILE_SIZE));
            setBackground(Color.DARK_GRAY);
            
            // Add mouse listener for human input
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!controller.isGameActive() || controller.currentPlayer != Board.PLAYER_1) {
                         // Ignore clicks if game is over or it's the AI's turn
                         return;
                    }
                    
                    int col = e.getX() / TILE_SIZE;
                    if (col >= 0 && col < Board.COLS) {
                        controller.handleHumanMove(col);
                    }
                }
            });
        }
 
        public void setBoard(Board board) {
            this.currentBoard = board;
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentBoard == null) return;
 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
            // Draw the Connect Four grid slots
            for (int r = 0; r < currentBoard.getRows(); r++) {
                for (int c = 0; c < currentBoard.getCols(); c++) {
                    int x = c * TILE_SIZE;
                    int y = r * TILE_SIZE;
 
                    // Draw the slot background (blue background for Connect Four game)
                    g2d.setColor(new Color(0, 102, 204)); 
                    g2d.fillOval(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10);
                    
                    // Draw the piece if present
                    int cellValue = currentBoard.getCell(r, c);
                    if (cellValue != Board.EMPTY) {
                        Color pieceColor = (cellValue == Board.PLAYER_1) ? Color.YELLOW : Color.RED;
                        g2d.setColor(pieceColor);
                        g2d.fillOval(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10);
                        
                        // Add a small shadow/border effect
                        g2d.setColor(Color.BLACK);
                        g2d.drawOval(x + 5, y + 5, TILE_SIZE - 10, TILE_SIZE - 10);
                    }
                }
            }
        }
    }
 
    public static void main(String[] args) {
        // Swing applications must be run on the EDT
        SwingUtilities.invokeLater(ConnectFourApp::new);
    }
}
