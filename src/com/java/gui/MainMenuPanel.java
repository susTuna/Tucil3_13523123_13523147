package com.java.gui;

import com.java.model.Board;
import com.java.model.Config;
import com.java.searching.heuristic.HeuristicType;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javafx.embed.swing.JFXPanel;

public class MainMenuPanel extends JPanel {
    public static final String MAIN = "MAIN_MENU";

    private RushHourGUI parent;
    private JButton uploadBtn, calculateBtn;
    private BoardPanel preview;
    private File currentFile;
    private Board loadedBoard;
    private JPanel contentPanel;

    public MainMenuPanel(RushHourGUI gui) {
        this.parent = gui;
        setLayout(new BorderLayout());
        setOpaque(false);

        // Create content panel first, before adding video background
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);  // This is crucial!
        
        // Add top bar with Upload & Calculate buttons - ADD MORE PADDING
        JPanel top = new JPanel();
        top.setOpaque(false);  // Make transparent
        top.setBorder(BorderFactory.createEmptyBorder(135, 0, 20, 0)); // Add padding at top

        uploadBtn = new JButton("Upload a .txt File…");
        uploadBtn.addActionListener(e -> onUpload());
        top.add(uploadBtn);

        calculateBtn = new JButton("Calculate ▶");
        calculateBtn.setEnabled(false);
        calculateBtn.addActionListener(e -> onCalculate());
        top.add(calculateBtn);

        contentPanel.add(top, BorderLayout.NORTH);

        // Center preview - also make transparent with more spacing
        preview = new BoardPanel();
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        // Wrap the preview in another panel with some padding
        JPanel previewWrapper = new JPanel();
        previewWrapper.setOpaque(false);
        previewWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        previewWrapper.add(preview);
        center.add(previewWrapper);
        contentPanel.add(center, BorderLayout.CENTER);
        
        // Try creating video background last (IMPORTANT)
        try {
            // Create a separate panel for the video
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setOpaque(false);
            JFXPanel fx = VideoBackground.create("resources/video/bg.mp4");
            videoPanel.add(fx, BorderLayout.CENTER);
            
            // Add video panel FIRST, then the content panel on top
            setLayout(new OverlayLayout(this)); // Use OverlayLayout for proper stacking
            add(contentPanel);  // Add content last so it's on top
            add(videoPanel);    // Add video first so it's on bottom
        } catch (Exception e) {
            e.printStackTrace();
            setBackground(Color.WHITE);
            add(contentPanel, BorderLayout.CENTER);  // Just add content without video
        }
    }

    private void onUpload() {
        JFileChooser chooser = new JFileChooser("test");
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            try {
                loadedBoard = Config.loadConfig(currentFile.getPath());
                preview.setBoard(loadedBoard);
                calculateBtn.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCalculate() {
        String[] options = {"A★ Search", "Uniform Cost", "Greedy Best-First", "IDA* Search"};
        String algo = (String) JOptionPane.showInputDialog(
            this,
            "Choose algorithm:",
            "Algorithm Selection",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (algo != null) {
            // If not using UCS, ask for heuristic type
            HeuristicType heuristicType = HeuristicType.COMPOSITE; // Default
            
            if (!algo.equals("Uniform Cost")) {
                String[] heuristics = {"Composite", "Manhattan Distance", "Pattern Database", "Enhanced Blocking"};
                String heuristic = (String) JOptionPane.showInputDialog(
                    this,
                    "Choose heuristic:",
                    "Heuristic Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    heuristics,
                    heuristics[0]
                );
                
                if (heuristic != null) {
                    // Set heuristic based on selection
                    switch (heuristic) {
                        case "Manhattan Distance":
                            heuristicType = HeuristicType.MANHATTAN;
                            break;
                        case "Pattern Database":
                            heuristicType = HeuristicType.PATTERN;
                            break;
                        case "Enhanced Blocking":
                            heuristicType = HeuristicType.BLOCKING;
                            break;
                        default:
                            heuristicType = HeuristicType.COMPOSITE;
                            break;
                    }
                } else {
                    return; // User canceled heuristic selection
                }
            }
            
            parent.showLoading();
            new SolverWorker(parent, loadedBoard, algo, heuristicType).execute();
        }
    }

    // Add this method to support loading a puzzle from a file
    public void loadPuzzleFile(File file) {
        currentFile = file;
        try {
            loadedBoard = Config.loadConfig(currentFile.getPath());
            preview.setBoard(loadedBoard);
            calculateBtn.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add this method to access the loaded board
    public Board getLoadedBoard() {
        return loadedBoard;
    }
}