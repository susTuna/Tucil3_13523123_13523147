package com.java.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int rows, cols;
    private final char[][] grid;
    private final Map<Character, Piece> pieces = new HashMap<>();
    private int exitRow, exitCol;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        // initialize empty cells
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = '.';
    }

    public void setCell(int r, int c, char ch) {
        grid[r][c] = ch;
        if (Character.isLetter(ch) && ch != 'K') {
            pieces.putIfAbsent(ch, new Piece(ch, r, c, 1, true, false));
        }
        if (ch == 'K') {
            exitRow = r;
            exitCol = c;
        }
    }

    public char getCell(int r, int c) {
        return grid[r][c];
    }

    public Map<Character, Piece> getPieces() {
        return pieces;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getExitCol() {
        return exitCol;
    }

    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}

