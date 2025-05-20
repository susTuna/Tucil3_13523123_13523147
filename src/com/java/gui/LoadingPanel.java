package com.java.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.java.gui.CustomFonts;

public class LoadingPanel extends JPanel {
    public static final String LOADING = "LOADING";
    private JLabel label;
    private int dotCount = 0;

    public LoadingPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        Font font = CustomFonts.load("resources/fonts/MyFont.ttf", 32f, Font.PLAIN);
        label = new JLabel("Calculating");
        label.setFont(font);
        add(label);

        Timer timer = new Timer(500, e -> {
            dotCount = (dotCount + 1) % 4;
            label.setText("Calculating" + ".".repeat(dotCount));
        });
        timer.start();
    }
}
