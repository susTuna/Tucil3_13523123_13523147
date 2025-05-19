package com.java.model;

import com.java.exception.MoveBlockedException;
import java.util.*;

public class Board {
    private final int rows, cols;
    private final char[][] grid;
    private final Map<Character, Piece> pieces = new HashMap<>();
    private int exitRow = Integer.MIN_VALUE, exitCol = Integer.MIN_VALUE;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];
        for (int r = 0; r < rows; r++)
            Arrays.fill(grid[r], '.');
    }

    public void setExit(int r, int c) {
        exitRow = r;
        exitCol = c;
    }

    public void parseCell(int r, int c, char ch) {
        if (ch == 'K') {
            setExit(r, c);
        } else if (Character.isLetter(ch)) {
            grid[r][c] = ch;
            if (!pieces.containsKey(ch)) {
                pieces.put(ch, new Piece(ch, r, c, 1, true, ch == 'P'));
            } else {
                Piece p = pieces.get(ch);
                int newSize   = p.getSize() + 1;
                boolean horiz = (r == p.getRow());
                int anchorR   = horiz ? p.getRow() : Math.min(p.getRow(), r);
                int anchorC   = horiz ? Math.min(p.getCol(), c) : p.getCol();
                p.setHorizontal(horiz);
                p.setSize(newSize);
                p.setPosition(anchorR, anchorC);
            }
        }
    }

    public char getCell(int r, int c)     { return grid[r][c]; }
    public int getExitRow()               { return exitRow; }
    public int getExitCol()               { return exitCol; }
    public int getRows()                  { return rows; }
    public int getCols()                  { return cols; }
    public Map<Character, Piece> getPieces() { return pieces; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        final String[] COLORS = {
            "\u001B[32m", // GREEN (0)
            "\u001B[33m", // YELLOW (1)
            "\u001B[34m", // BLUE (2)
            "\u001B[35m", // PURPLE (3)
            "\u001B[36m", // CYAN (4)
            "\u001B[90m", // BRIGHT_BLACK (5)
            "\u001B[92m", // BRIGHT_GREEN (6)
            "\u001B[93m", // BRIGHT_YELLOW (7)
            "\u001B[94m", // BRIGHT_BLUE (8)
            "\u001B[95m", // BRIGHT_PURPLE (9)
            "\u001B[96m", // BRIGHT_CYAN (10)
            "\u001B[97m", // BRIGHT_WHITE (11)
            "\u001B[30m"  // BLACK (12)
        };
        
        final String RESET = "\u001B[0m";
        final String WHITE = "\u001B[37m";
        final String RED = "\u001B[31m";
        
        sb.append(WHITE).append("+").append("-".repeat(this.getCols())).append("+").append(RESET).append("\n");
        
        for (int r = 0; r < this.getRows(); r++) {
            sb.append(WHITE).append("|").append(RESET);
            for (int c = 0; c < this.getCols(); c++) {
                char cell = this.getCell(r, c);
                
                if (cell == '.') {
                    sb.append(WHITE).append(cell).append(RESET);
                } else if (cell == 'P') {
                    sb.append(RED).append(cell).append(RESET);
                } else {
                    int colorIndex = cell % COLORS.length;
                    sb.append(COLORS[colorIndex]).append(cell).append(RESET);
                }
            }
            sb.append(WHITE).append("|").append(RESET).append("\n");
        }
        
        sb.append(WHITE).append("+").append("-".repeat(this.getCols())).append("+").append(RESET);
        
        sb.append("\n").append(WHITE).append("Exit at: (")
          .append(this.getExitRow()).append(", ").append(this.getExitCol()).append(")")
          .append(RESET);
        
        return sb.toString();
    }
    
        
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("+");
        for (int c = 0; c < this.getCols(); c++) {
            sb.append("-");
        }
        sb.append("+\n");
        
        for (int r = 0; r < this.getRows(); r++) {
            sb.append("|");
            for (int c = 0; c < this.getCols(); c++) {
                sb.append(this.getCell(r, c));
            }
            sb.append("|\n");
        }
        
        sb.append("+");
        for (int c = 0; c < this.getCols(); c++) {
            sb.append("-");
        }
        sb.append("+");
        
        sb.append("\nExit at: (")
          .append(exitRow).append(", ").append(exitCol).append(")");
        
        return sb.toString();
    }

    public void printBoard() { System.out.println(this.toString()); }

    public void movePiece(Piece p, int x, int y) throws MoveBlockedException {
        if (p.getSize() > 1) {
            if (p.isHorizontal() && y != 0)
                throw new IllegalArgumentException(
                  "Piece '" + p.getId() + "' is horizontal; cannot move vertically");
            if (p.isVertical() && x != 0)
                throw new IllegalArgumentException(
                  "Piece '" + p.getId() + "' is vertical; cannot move horizontally");
        }

        int nr = p.getRow() - y;
        int nc = p.getCol() + x;
        boolean horiz = p.isHorizontal();
        int sz       = p.getSize();

        List<int[]> dest = new ArrayList<>();
        for (int i = 0; i < sz; i++) {
            int r = nr + (horiz ? 0 : i);
            int c = nc + (horiz ? i : 0);
            if (r < 0 || r >= rows || c < 0 || c >= cols) {
                System.out.println("\nMove out of bounds: (" + r + ", " + c + ")");
                printBoard(); 
                throw new MoveBlockedException(null); 
            }
            char occ = grid[r][c];
            if (occ != '.' && occ != p.getId()) {
                System.out.println("\nMove blocked by piece: " + occ);
                printBoard(); 
                throw new MoveBlockedException(pieces.get(occ));
            }
            dest.add(new int[]{r, c});
        }

        for (int[] cell : p.occupiedCells()) {
            grid[cell[0]][cell[1]] = '.';
        }

        p.setPosition(nr, nc);

        for (int[] cell : dest) {
            grid[cell[0]][cell[1]] = p.getId();
        }
    }
}