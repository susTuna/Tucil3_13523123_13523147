package com.java.gui;

import com.java.searching.SolverResult;
import com.java.searching.Move;
import com.java.searching.State;
import com.java.searching.heuristic.HeuristicType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javafx.embed.swing.JFXPanel;

public class SolvedPanel extends JPanel {
    public static final String SOLVED = "SOLVED";

    private RushHourGUI parent;
    private BoardPanel boardView;
    private JLabel timeLbl, nodesLbl, stepsLbl;
    private JButton replayBtn, saveImgBtn, backBtn;
    private SolverResult result;
    private State initial;

    private String algorithmName = "Custom";
    private HeuristicType heuristicType = HeuristicType.COMPOSITE;

    public SolvedPanel(RushHourGUI gui) {
        this.parent = gui;
        setLayout(new BorderLayout());
        setOpaque(false);

        // video background
        try {
            JFXPanel fx = VideoBackground.create("resources/video/bg.mp4");
            add(fx, BorderLayout.CENTER);
        } catch (Exception e) {
            setBackground(Color.WHITE);
        }

        boardView = new BoardPanel();
        add(boardView, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(7, 1, 5, 5));
        right.setOpaque(false);
        timeLbl  = new JLabel("Time: –");
        nodesLbl = new JLabel("Nodes: –");
        stepsLbl = new JLabel("Steps: –");
        right.add(timeLbl);
        right.add(nodesLbl);
        right.add(stepsLbl);

        replayBtn  = new JButton("Replay Solution");
        saveImgBtn = new JButton("Save Image");
        backBtn    = new JButton("Back");
        JButton saveBtn = new JButton("Save Solution");

        replayBtn .addActionListener(e -> replay());
        saveImgBtn.addActionListener(e -> saveImage());
        backBtn   .addActionListener(e -> parent.showMain());
        saveBtn.addActionListener(e -> saveSolution());

        right.add(replayBtn);
        right.add(saveImgBtn);
        right.add(backBtn);
        right.add(saveBtn);

        add(right, BorderLayout.EAST);
    }

    /** Called by SolverWorker when done. */
    public void setResult(State start, SolverResult res) {
        this.initial = start;
        this.result  = res;
        timeLbl .setText("Time: "  + res.timeMs    + " ms");
        nodesLbl.setText("Nodes: " + res.nodesCreated);
        stepsLbl.setText("Steps: " + res.path.size());

        boardView.setBoard(start.getBoard());
        parent.showSolved();
        replay();
    }

    private void replay() {
        List<Move> path = result.path;
        Timer timer = new Timer(500, null);
        final int[] idx = {0};
        timer.addActionListener(e -> {
            if (idx[0] >= path.size()) {
                timer.stop();
                return;
            }
            boardView.setBoard(path.get(idx[0]++).next.getBoard());
        });
        timer.start();
    }

    private void saveImage() {
        // Create solutions directory if it doesn't exist
        File solutionsDir = new File("test/solutions");
        if (!solutionsDir.exists()) {
            solutionsDir.mkdirs();
        }
        
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename (without .png extension):", 
            "solution");
            
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                // Create screenshot
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                paint(img.getGraphics());
                
                // Save to the solutions directory with timestamp
                String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
                String filepath = "test/solutions/" + filename + "_" + timestamp + ".png";
                ImageIO.write(img, "PNG", new File(filepath));
                
                JOptionPane.showMessageDialog(this, 
                    "Screenshot saved to: " + filepath, 
                    "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Save failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setAlgorithmInfo(String algo, HeuristicType heuristic) {
        this.algorithmName = algo;
        this.heuristicType = heuristic;
    }

    public void saveSolution() {
        if (result == null || initial == null) {
            JOptionPane.showMessageDialog(this, "No solution to save", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename (without .txt extension):", 
            "solution");
            
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                // Use String version to avoid ambiguity
                com.java.model.Save.saveSolution(
                    result, initial, filename, "GUI", 
                    algorithmName, heuristicType.toString()
                );
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
}
