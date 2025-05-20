package com.java.gui;

import com.java.searching.SolverResult;
import com.java.searching.Move;
import com.java.searching.State;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class SolvedPanel extends JPanel {
    public static final String SOLVED = "SOLVED";

    private RushHourGUI parent;
    private BoardPanel boardView;
    private JLabel timeLbl, nodesLbl, stepsLbl;
    private JButton replayBtn, saveImgBtn, backBtn;
    private SolverResult result;
    private State initial;

    public SolvedPanel(RushHourGUI gui) {
        this.parent = gui;
        setLayout(new BorderLayout());
        setOpaque(false);

        // video background
        try {
            JFXPanel fx = VideoBackground.create("resources/videos/bg.mp4");
            add(fx, BorderLayout.CENTER);
        } catch (Exception e) {
            setBackground(Color.WHITE);
        }

        boardView = new BoardPanel();
        add(boardView, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(6, 1, 5, 5));
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

        replayBtn .addActionListener(e -> replay());
        saveImgBtn.addActionListener(e -> saveImage());
        backBtn   .addActionListener(e -> parent.showMain());

        right.add(replayBtn);
        right.add(saveImgBtn);
        right.add(backBtn);

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
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paint(img.getGraphics());
        try {
            ImageIO.write(img, "PNG", new File("solution.png"));
            JOptionPane.showMessageDialog(this, "Saved as solution.png");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
