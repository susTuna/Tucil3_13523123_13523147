package com.java.gui;

import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.java.searching.*;
import com.java.searching.heuristic.HeuristicType;
import com.java.model.Board;
import com.java.checker.PuzzleChecker;
import com.java.exception.InvalidConfigurationException;

public class SolverWorker extends SwingWorker<SolverResult, Void> {
    private final RushHourGUI gui;
    private final com.java.searching.State start; // Use fully qualified name
    private final String algoName;
    private final HeuristicType heuristicType;

    public SolverWorker(RushHourGUI gui, Board board, String algoName, HeuristicType heuristicType) {
        this.gui = gui;
        this.start = new com.java.searching.State(board); // Fully qualified
        this.algoName = algoName;
        this.heuristicType = heuristicType;
        
        // Set the heuristic type (if not UCS)
        if (!algoName.startsWith("Uniform")) {
            com.java.searching.State.setHeuristic(heuristicType); // Fully qualified
        }
    }

    @Override
    protected SolverResult doInBackground() throws Exception {
        SearchStrategy solver;
        
        if (algoName.startsWith("A★") || algoName.startsWith("A*")) {
            solver = new AStarSolver();
        } else if (algoName.startsWith("Uniform")) {
            solver = new UCSolver();
        } else if (algoName.startsWith("IDA*")) {
            solver = new IDAStarSolver();
        } else {
            solver = new GBFSolver();
        }

        return solver.solve(start);
    }

    @Override
    protected void done() {
        try {
            SolverResult res = get();
            
            // Check if path is empty but determine if that's because:
            // 1. Already solved (should show solution screen)
            // 2. Unsolvable (should show error)
            if (res.path.isEmpty()) {
                try {
                    // Check if initial state is already solved
                    if (PuzzleChecker.checkSolved(start.getBoard())) {
                        // Already solved - proceed to solution screen
                        System.out.println("Puzzle was already in solved state!");
                        
                        // Continue to solution screen
                        java.lang.reflect.Field cardsField = RushHourGUI.class.getDeclaredField("cards");
                        cardsField.setAccessible(true);
                        JPanel cards = (JPanel) cardsField.get(gui);
                        
                        SolvedPanel solvedPanel = (SolvedPanel) cards.getComponent(2);
                        solvedPanel.setResult(start, res);
                        solvedPanel.setAlgorithmInfo(algoName, heuristicType);
                        gui.showSolved();
                        
                        // Add a special message
                        JOptionPane.showMessageDialog(gui, 
                            "The puzzle was already solved! No moves needed.",
                            "Already Solved", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Not already solved, but no path found = unsolvable
                        JOptionPane.showMessageDialog(gui, 
                            "No solution found after exploring " + res.nodesCreated + " nodes.",
                            "Unsolvable Puzzle", JOptionPane.WARNING_MESSAGE);
                        gui.showMain();
                    }
                } catch (InvalidConfigurationException e) {
                    JOptionPane.showMessageDialog(gui,
                        "Invalid puzzle configuration: " + e.getMessage(),
                        "Configuration Error", JOptionPane.ERROR_MESSAGE);
                    gui.showMain();
                }
            } else {
                // Normal case - solution found with moves
                java.lang.reflect.Field cardsField = RushHourGUI.class.getDeclaredField("cards");
                cardsField.setAccessible(true);
                JPanel cards = (JPanel) cardsField.get(gui);
                
                SolvedPanel solvedPanel = (SolvedPanel) cards.getComponent(2);
                solvedPanel.setResult(start, res);
                solvedPanel.setAlgorithmInfo(algoName, heuristicType);
                gui.showSolved();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, "Error: " + ex.getMessage(),
                                         "Solver Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            gui.showMain();
        }
    }
}
