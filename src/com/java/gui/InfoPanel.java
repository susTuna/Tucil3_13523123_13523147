package com.java.gui;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel statusLabel;
    private JLabel timeLabel;
    private JLabel nodesLabel;
    private JLabel stepsLabel;
    
    public InfoPanel() {
        setPreferredSize(new Dimension(150, 0));
        setLayout(new GridLayout(4, 1));
        initializeComponents();
    }
    
    private void initializeComponents() {
        statusLabel = new JLabel("Status: Ready");
        timeLabel = new JLabel("Time: -");
        nodesLabel = new JLabel("Nodes: -");
        stepsLabel = new JLabel("Steps: -");
        
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        nodesLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        stepsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        add(statusLabel);
        add(timeLabel);
        add(nodesLabel);
        add(stepsLabel);
    }
    
    public void updateStatus(String status) {
        statusLabel.setText("Status: " + status);
    }
    
    public void updateMetrics(long time, long nodes, int steps) {
        timeLabel.setText("Time: " + time + " ms");
        nodesLabel.setText("Nodes: " + nodes);
        stepsLabel.setText("Steps: " + steps);
    }
    
    public void clearMetrics() {
        timeLabel.setText("Time: -");
        nodesLabel.setText("Nodes: -");
        stepsLabel.setText("Steps: -");
    }
}