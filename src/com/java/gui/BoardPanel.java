package com.java.gui;

import com.java.model.Board;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private Board board;
    private Map<Character, Color> pieceColors;
    
    public BoardPanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        initializeColors();
    }
    
    private void initializeColors() {
        pieceColors = new HashMap<>();
        
        // Primary piece is always red
        pieceColors.put('P', Color.RED);
        
        // Basic colors
        pieceColors.put('A', Color.GREEN);
        pieceColors.put('B', Color.BLUE);
        pieceColors.put('C', Color.YELLOW);
        pieceColors.put('D', Color.CYAN);
        pieceColors.put('E', Color.MAGENTA);
        pieceColors.put('F', Color.ORANGE);
        pieceColors.put('G', Color.PINK);
        pieceColors.put('H', Color.LIGHT_GRAY);
        
        // Extended colors - each with a unique RGB value
        pieceColors.put('I', new Color(139, 69, 19));    // Brown
        pieceColors.put('J', new Color(0, 100, 0));      // Dark Green
        pieceColors.put('K', new Color(70, 130, 180));   // Steel Blue
        pieceColors.put('L', new Color(148, 0, 211));    // Dark Violet
        pieceColors.put('M', new Color(255, 140, 0));    // Dark Orange
        pieceColors.put('N', new Color(255, 20, 147));   // Deep Pink
        pieceColors.put('O', new Color(72, 61, 139));    // Dark Slate Blue
        pieceColors.put('Q', new Color(255, 99, 71));    // Tomato
        pieceColors.put('R', new Color(30, 144, 255));   // Dodger Blue
        pieceColors.put('S', new Color(154, 205, 50));   // Yellow Green
        pieceColors.put('T', new Color(255, 218, 185));  // Peach
        pieceColors.put('U', new Color(221, 160, 221));  // Plum
        pieceColors.put('V', new Color(176, 224, 230));  // Powder Blue
        pieceColors.put('W', new Color(210, 180, 140));  // Tan
        pieceColors.put('Y', new Color(255, 182, 193));  // Light Pink
        pieceColors.put('Z', new Color(135, 206, 235));  // Sky Blue
        
        // Exit marker
        pieceColors.put('K', Color.GRAY);
    }
    
    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (board == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Please load a puzzle file", 50, 50);
            return;
        }
        
        int rows = board.getRows();
        int cols = board.getCols();
        
        int cellSize = Math.min(getWidth() / cols, getHeight() / rows);
        int boardWidth = cellSize * cols;
        int boardHeight = cellSize * rows;
        
        int startK = (getWidth() - boardWidth) / 2;
        int startY = (getHeight() - boardHeight) / 2;
        
        g.setColor(Color.BLACK);
        for (int r = 0; r <= rows; r++) {
            g.drawLine(startK, startY + r * cellSize, 
                       startK + cols * cellSize, startY + r * cellSize);
        }
        for (int c = 0; c <= cols; c++) {
            g.drawLine(startK + c * cellSize, startY, 
                       startK + c * cellSize, startY + rows * cellSize);
        }
        
        int exitRow = board.getExitRow();
        int exitCol = board.getExitCol();
        
        g.setColor(Color.RED);
        if (exitCol == cols) { // Exit on right
            g.fillRect(startK + cols * cellSize, startY + exitRow * cellSize,
                       cellSize/2, cellSize);
        } else if (exitCol == -1) { // Exit on left
            g.fillRect(startK - cellSize/2, startY + exitRow * cellSize,
                       cellSize/2, cellSize);
        } else if (exitRow == rows) { // Exit on bottom
            g.fillRect(startK + exitCol * cellSize, startY + rows * cellSize,
                       cellSize, cellSize/2);
        } else if (exitRow == -1) { // Exit on top
            g.fillRect(startK + exitCol * cellSize, startY - cellSize/2,
                       cellSize, cellSize/2);
        }
        
        g.setColor(Color.WHITE);
        g.drawString("EXIT", startK + exitCol * cellSize + cellSize/2 - 15,
                   startY + exitRow * cellSize + cellSize/2 + 5);
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cell = board.getCell(r, c);
                if (cell != '.') {
                    Color pieceColor = pieceColors.getOrDefault(cell, Color.GRAY);
                    g.setColor(pieceColor);
                    g.fillRect(startK + c * cellSize + 2, 
                               startY + r * cellSize + 2, 
                               cellSize - 4, cellSize - 4);
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(cell), 
                                startK + c * cellSize + cellSize/2 - 5, 
                                startY + r * cellSize + cellSize/2 + 5);
                }
            }
        }
    }
}