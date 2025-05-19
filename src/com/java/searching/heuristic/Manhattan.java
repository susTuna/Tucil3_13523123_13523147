package com.java.searching.heuristic;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.State;
import java.util.HashSet;
import java.util.Set;

public class Manhattan implements Heuristic {
    public int calculate(State state) {
        Board board = state.getBoard();
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow(), ec = board.getExitCol();

        if (p.isHorizontal()) {
            int pmr = p.getRow();
            int pmc = p.getCol() + (p.getSize() - 1) / 2;

            return Math.abs(ec - pmc) + Math.abs(er - pmr);
        }
        else {
            int pmr = p.getRow() + (p.getSize() - 1) / 2;
            int pmc = p.getCol();

            return Math.abs(ec - pmc) + Math.abs(er - pmr);
        }
    }
}