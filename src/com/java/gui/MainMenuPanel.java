package com.java.gui;

import com.java.model.Board;
import com.java.model.Config;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainMenuPanel extends JPanel {
    public static final String MAIN = "MAIN_MENU";

    private RushHourGUI parent;
    private JButton uploadBtn, calculateBtn;
    private BoardPanel preview;
    private File currentFile;
    private Board loadedBoard;

    public MainMenuPanel(RushHourGUI gui) {
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

        // top bar with Upload & Calculate
        JPanel top = new JPanel();
        top.setOpaque(false);
        uploadBtn = new JButton("Upload a .txt File…");
        uploadBtn.addActionListener(e -> onUpload());
        top.add(uploadBtn);

        calculateBtn = new JButton("Calculate ▶");
        calculateBtn.setEnabled(false);
        calculateBtn.addActionListener(e -> onCalculate());
        top.add(calculateBtn);

        add(top, BorderLayout.NORTH);

        // center preview
        preview = new BoardPanel();
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(preview);
        add(center, BorderLayout.CENTER);
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
        String[] options = {"A★ Search", "Uniform Cost", "Greedy Best-First"};
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
            parent.showLoading();
            new SolverWorker(parent, loadedBoard, algo).execute();
        }
    }
}