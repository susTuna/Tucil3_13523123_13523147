package com.java.gui;

import javax.swing.SwingWorker;
import javax.swing.JOptionPane;

import com.java.searching.*;
import com.java.model.Board;

public class SolverWorker extends SwingWorker<SolverResult, Void> {
    private final RushHourGUI gui;
    private final State       start;
    private final String      algoName;

    public SolverWorker(RushHourGUI gui, Board board, String algoName) {
        this.gui      = gui;
        this.start    = new State(board);
        this.algoName = algoName;
    }

    @Override
    protected SolverResult doInBackground() throws Exception {
        SearchStrategy solver;
        if (algoName.startsWith("A★"))       solver = new AStarSolver();
        else if (algoName.startsWith("Uniform")) solver = new UCSolver();
        else                                   solver = new GBFSolver();

        return solver.solve(start);
    }

    @Override
    protected void done() {
        try {
            SolverResult res = get();
            gui.getContentPane(); 
            ((SolvedPanel)gui.cards.getComponent(2)).setResult(start, res);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, "Error: " + ex.getMessage(),
                                          "Solver Error", JOptionPane.ERROR_MESSAGE);
            gui.showMain();
        }
    }
}
