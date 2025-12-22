# Advisoral-Search-AI-Agent-Connect-Four
An intelligent Java application featuring a classic board game (Connect Four) powered by the Minimax Search Algorithm with Alpha-Beta Pruning. This project demonstrates core concepts in Artificial Intelligence, including state-space search, heuristic evaluation, and recursive optimization.

# Project Overview
This application allows a human player to compete against an intelligent computer agent. The agent uses the Minimax algorithm enhanced by Alpha-Beta pruning to simulate future game states, choosing moves that maximize its chances of victory while assuming the opponent plays optimally.
# Key AI Concepts Implemented:
1. Adversarial Search: Modeling the game as a tree where players alternate turns.
2. Minimax Logic: Recursive decision-making to minimize potential loss.
3. Alpha-Beta Pruning: Optimization to decrease the number of nodes evaluated by discarding irrelevant branches.
4. Heuristic Evaluation: A scoring function that estimates the "favorability" of non-terminal board states.
# 🚀 Features
1. Adjustable Difficulty: Users can set the search depth (how many moves ahead the AI looks).
2. Asynchronous Processing: AI calculations run on a separate background thread to keep the GUI responsive.
3. Robust Logic: Handles win detection, draw conditions, and invalid move prevention.
4. Clean UI: Built with Java Swing for a clear visual representation of the game board.
# 🧠 Technical Architecture
1. The Search Engine (MinimaxAI.java)
The core "intelligence" of the agent. It explores the game tree recursively.
Alpha ($\alpha$): The best value the maximizer is currently guaranteed.
Beta ($\beta$): The best value the minimizer is currently guaranteed. If $\beta \leq \alpha$, the branch is pruned to save computational resources.
2. The Game State (Board.java)
Manages the $6 \times 7$ grid.
Uses a 2D array representation.
Implements deep cloning for state simulation during search.
Directional scanning for horizontal, vertical, and diagonal win conditions.
3. Evaluation Function
Since the game tree is too large to search to completion, the AI uses a heuristic to score boards at the search limit:
Center Control: Pieces in the center column are weighted more heavily.
Line Scoring: Points are awarded based on sequences of 2 or 3 pieces.
Blocking: High negative weights are assigned to opponent's near-win states.
# 🛠️ Installation & Execution
# Prerequisites 
Java Development Kit (JDK) 11 or higher.
# Steps
1. Clone the repository:
    '''bash
    java ConnectFourApp
3. Compile the source files:
4. Run the application:
