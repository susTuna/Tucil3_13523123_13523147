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
        if (ch == 'X') {
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

    public void printBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println("Exit at: (" + exitRow + ", " + exitCol + ")");
    }

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
            if (r < 0 || r >= rows || c < 0 || c >= cols)
                throw new MoveBlockedException(null);
            char occ = grid[r][c];
            if (occ != '.' && occ != p.getId())
                throw new MoveBlockedException(pieces.get(occ));
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
