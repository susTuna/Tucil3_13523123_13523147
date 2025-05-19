package com.java.gui;

import javax.swing.*;
import java.awt.*;

public class AnimationPanel extends JPanel {
    private JButton prevButton;
    private JButton nextButton;
    private JLabel currentStepLabel;
    
    private RushHourGUI parent;
    
    public AnimationPanel(RushHourGUI parent) {
        this.parent = parent;
        setLayout(new FlowLayout());
        initializeComponents();
    }
    
    private void initializeComponents() {
        prevButton = new JButton("< Previous");
        nextButton = new JButton("Next >");
        currentStepLabel = new JLabel("Step: 0/0");
        
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        
        prevButton.addActionListener(e -> parent.showPreviousStep());
        nextButton.addActionListener(e -> parent.showNextStep());
        
        add(prevButton);
        add(currentStepLabel);
        add(nextButton);
    }
    
    public void setControlsEnabled(boolean prevEnabled, boolean nextEnabled) {
        prevButton.setEnabled(prevEnabled);
        nextButton.setEnabled(nextEnabled);
    }
    
    public void updateStepLabel(int current, int total) {
        currentStepLabel.setText("Step: " + current + "/" + total);
    }
}