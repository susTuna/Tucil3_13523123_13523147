package com.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* 
To DO: 
1. If there's a piece where it's only a dot, the orientation will be assigned in accordance to where to exit is
  e.g. If exit on left/right the orientation will be horizontal, if exit on top/bottom the orientation will be vertical

*/

public class Piece {
    public static int piece_count = 0;
    private final char id;
    private int row, col;
    private int size;
    private boolean horizontal;
    private final boolean primary;

    public boolean isVertical() {
        return !horizontal;
    }

    public Piece(char id, int row, int col, int size, boolean horizontal, boolean primary) {
        this.id         = id;
        this.row        = row;
        this.col        = col;
        this.size       = size;
        this.horizontal = horizontal;
        this.primary    = primary;
        piece_count++;
    }

    public char getId()        { return id; }
    public int  getRow()       { return row; }
    public int  getCol()       { return col; }
    public int  getSize()      { return size; }
    public boolean isHorizontal() { return horizontal; }
    public boolean isPrimary()    { return primary; }

    void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    void setSize(int size) {
        this.size = size;
    }
    void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public List<int[]> occupiedCells() {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            cells.add(new int[]{r, c});
        }
        return cells;
    }

    @Override
    public String toString() {
        return "Piece{id=" + id +
               ", row=" + row + ", col=" + col +
               ", size=" + size +
               ", horiz=" + horizontal +
               ", primary=" + primary + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece p = (Piece) o;
        return id == p.id &&
               row == p.row &&
               col == p.col &&
               size == p.size &&
               horizontal == p.horizontal &&
               primary == p.primary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, col, size, horizontal, primary);
    }
}
