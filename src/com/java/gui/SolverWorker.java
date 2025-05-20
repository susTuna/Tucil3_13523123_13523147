package com.java.gui;

import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.java.searching.*;
import com.java.searching.heuristic.HeuristicType;
import com.java.model.Board;

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
        } else if (algoName.startsWith("IDA★") || algoName.startsWith("IDA*")) {
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
            // Use reflection to access the private cards field
            java.lang.reflect.Field cardsField = RushHourGUI.class.getDeclaredField("cards");
            cardsField.setAccessible(true);
            JPanel cards = (JPanel) cardsField.get(gui);
            
            // Access the solved panel
            SolvedPanel solvedPanel = (SolvedPanel) cards.getComponent(2);
            solvedPanel.setResult(start, res);
            solvedPanel.setAlgorithmInfo(algoName, heuristicType);
            gui.showSolved();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, "Error: " + ex.getMessage(),
                                         "Solver Error", JOptionPane.ERROR_MESSAGE);
            gui.showMain();
        }
    }
}
