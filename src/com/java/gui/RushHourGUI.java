package com.java.gui;

import com.java.model.*;
import com.java.searching.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RushHourGUI extends JFrame {
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;
    private InfoPanel infoPanel;
    private AnimationPanel animationPanel;
    
    private Board board;
    private State initialState;
    private List<Move> solutionPath;
    private int currentStep = 0;
    private SolverResult result;
    private boolean solutionFound = false;
    
    public RushHourGUI() {
        setTitle("YuukaFinder: Rush Hour Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initializeUI();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        boardPanel = new BoardPanel();
        controlPanel = new ControlPanel(this);
        infoPanel = new InfoPanel();
        animationPanel = new AnimationPanel(this);
        
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.EAST);
        mainPanel.add(animationPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    public void loadPuzzle() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("test"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                board = Config.loadConfig(selectedFile.getPath());
                initialState = new State(board);
                
                controlPanel.setSolveEnabled(true);
                infoPanel.updateStatus("Puzzle loaded");
                infoPanel.clearMetrics();
                
                currentStep = 0;
                solutionPath = null;
                controlPanel.setSaveEnabled(false);
                animationPanel.setControlsEnabled(false, false);
                animationPanel.updateStepLabel(0, 0);
                
                boardPanel.setBoard(board);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading puzzle: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void solvePuzzle(SearchStrategy solver, String algorithmName) {
        if (board == null) {
            JOptionPane.showMessageDialog(this, 
                "Please load a puzzle first", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        infoPanel.updateStatus("Solving...");
        controlPanel.setControlsEnabled(false);
        
        SwingWorker<SolverResult, Void> worker = new SwingWorker<>() {
            @Override
            protected SolverResult doInBackground() throws Exception {
                return solver.solve(initialState);
            }
            
            @Override
            protected void done() {
                try {
                    result = get();
                    solutionPath = result.path;
                    
                    if (solutionPath.isEmpty()) {
                        infoPanel.updateStatus("No solution found");
                        JOptionPane.showMessageDialog(RushHourGUI.this, 
                            "No solution found for this puzzle", 
                            "Solver Result", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        infoPanel.updateStatus("Solution found");
                        infoPanel.updateMetrics(result.timeMs, result.nodesCreated, solutionPath.size());
                        
                        currentStep = 0;
                        solutionFound = true;
                        controlPanel.setSaveEnabled(true);
                        animationPanel.setControlsEnabled(false, true);
                        animationPanel.updateStepLabel(0, solutionPath.size());
                    }
                    
                } catch (InterruptedException | ExecutionException e) {
                    infoPanel.updateStatus("Error solving puzzle");
                    JOptionPane.showMessageDialog(RushHourGUI.this, 
                        "Error solving puzzle: " + e.getMessage(), 
                        "Solver Error", JOptionPane.ERROR_MESSAGE);
                }
                
                controlPanel.setControlsEnabled(true);
            }
        };
        
        worker.execute();
    }
    
    public void showNextStep() {
        if (solutionPath == null || currentStep >= solutionPath.size()) {
            return;
        }
        
        Move move = solutionPath.get(currentStep);
        board = move.next.getBoard();
        currentStep++;
        
        animationPanel.setControlsEnabled(currentStep > 0, currentStep < solutionPath.size());
        animationPanel.updateStepLabel(currentStep, solutionPath.size());
        
        boardPanel.setBoard(board);
    }
    
    public void showPreviousStep() {
        if (currentStep <= 0) {
            return;
        }
        
        currentStep--;
        
        board = initialState.getBoard();
        for (int i = 0; i < currentStep; i++) {
            board = solutionPath.get(i).next.getBoard();
        }
        
        animationPanel.setControlsEnabled(currentStep > 0, currentStep < solutionPath.size());
        animationPanel.updateStepLabel(currentStep, solutionPath.size());
        
        boardPanel.setBoard(board);
    }
    
    public void saveSolution(String algorithmName) {
        if (!solutionFound || result == null) {
            JOptionPane.showMessageDialog(this, 
                "No solution to save", 
                "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename (without .txt extension):", 
            "solution");
            
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                Save.saveSolution(result, initialState, filename, "GUI", algorithmName);
                JOptionPane.showMessageDialog(this, 
                    "Solution saved to test/solutions/" + filename + ".txt", 
                    "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving solution: " + e.getMessage(), 
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new RushHourGUI().setVisible(true);
        });
    }
}