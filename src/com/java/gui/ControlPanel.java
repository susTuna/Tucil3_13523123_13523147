package com.java.gui;

import com.java.searching.*;
import com.java.searching.heuristic.HeuristicType;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JComboBox<String> heuristicComboBox;
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
            "Greedy Best-First Search",
            "Iterative Deepening A* Search"
        });
        
        heuristicComboBox = new JComboBox<>(new String[]{
            "Composite",
            "Manhattan Distance", 
            "Patttern Database", 
            "Enhanced Blocking"
        });

        loadButton = new JButton("Load Puzzle");
        solveButton = new JButton("Solve");
        saveButton = new JButton("Save Solution");
        
        solveButton.setEnabled(false);
        saveButton.setEnabled(false);
        
        algorithmComboBox.addActionListener(e -> {
            int index = algorithmComboBox.getSelectedIndex();
            heuristicComboBox.setEnabled(index != 1);
        });

        loadButton.addActionListener(e -> parent.loadPuzzle());
        solveButton.addActionListener(e -> {
            SearchStrategy solver;
            String algorithmName;
            String heuristic;

            int selection = algorithmComboBox.getSelectedIndex();
            switch (selection) {
                case 0:
                    solver = new AStarSolver();
                    algorithmName = "A*";
                    break;
                case 1:
                    solver = new UCSolver();
                    algorithmName = "UCS";
                    break;
                case 2:
                    solver = new GBFSolver();
                    algorithmName = "GBFS";
                    break;
                case 3:
                    solver = new IDAStarSolver();
                    algorithmName = "IDA*";
                    break;
                default:
                    solver = new AStarSolver();
                    algorithmName = "A*";
                    break;
            }

            if (selection != 1) {
                HeuristicType heuristicType;
                switch (heuristicComboBox.getSelectedIndex()) {
                    case 0:
                        heuristicType = HeuristicType.COMPOSITE;
                        break;
                    case 1:
                        heuristicType = HeuristicType.MANHATTAN;
                        break;
                    case 2:
                        heuristicType = HeuristicType.PATTERN;
                        break;
                    case 3:
                        heuristicType = HeuristicType.BLOCKING;
                        break;
                    default:
                        heuristicType = HeuristicType.COMPOSITE;
                        break;
                }
                State.setHeuristic(heuristicType);
            }
            
            parent.solvePuzzle(solver, algorithmName);
        });
        
        saveButton.addActionListener(e -> {
            String algorithmName = (String) algorithmComboBox.getSelectedItem();
            parent.saveSolution(algorithmName);
        });
        
        add(algorithmComboBox);
        add(heuristicComboBox);
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