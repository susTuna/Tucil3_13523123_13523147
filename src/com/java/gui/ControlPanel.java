package com.java.gui;

import com.java.searching.*;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JButton loadButton;
    private JButton solveButton;
    private JButton saveButton;
    
    private RushHourGUI parent;
    
    public ControlPanel(RushHourGUI parent) {
        this.parent = parent;
        setLayout(new FlowLayout());
        initializeComponents();
    }
    
    private void initializeComponents() {
        algorithmComboBox = new JComboBox<>(new String[]{
            "A* Search", 
            "Uniform Cost Search", 
            "Greedy Best-First Search"
        });
        
        loadButton = new JButton("Load Puzzle");
        solveButton = new JButton("Solve");
        saveButton = new JButton("Save Solution");
        
        solveButton.setEnabled(false);
        saveButton.setEnabled(false);
        
        loadButton.addActionListener(e -> parent.loadPuzzle());
        solveButton.addActionListener(e -> {
            SearchStrategy solver;
            String algorithmName;
            
            int selection = algorithmComboBox.getSelectedIndex();
            if (selection == 0) {
                solver = new AStarSolver();
                algorithmName = "A*";
            } else if (selection == 1) {
                solver = new UCSolver();
                algorithmName = "UCS";
            } else {
                solver = new GBFSolver();
                algorithmName = "GBFS";
            }
            
            parent.solvePuzzle(solver, algorithmName);
        });
        
        saveButton.addActionListener(e -> {
            String algorithmName = algorithmComboBox.getSelectedIndex() == 0 ? "A*" : 
                                  (algorithmComboBox.getSelectedIndex() == 1 ? "UCS" : "GBFS");
            parent.saveSolution(algorithmName);
        });
        
        add(algorithmComboBox);
        add(loadButton);
        add(solveButton);
        add(saveButton);
    }
    
    public void setSolveEnabled(boolean enabled) {
        solveButton.setEnabled(enabled);
    }
    
    public void setSaveEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }
    
    public void setControlsEnabled(boolean enabled) {
        loadButton.setEnabled(enabled);
        solveButton.setEnabled(enabled);
    }
}