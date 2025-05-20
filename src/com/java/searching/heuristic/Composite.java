package com.java.searching.heuristic;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.State;
import java.util.HashSet;
import java.util.Set;

public class Composite implements Heuristic {
    public int calculate(State state) {
        Board board = state.getBoard();
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow(), ec = board.getExitCol();
        int br = p.getRow(), bc = p.getCol(), sz = p.getSize();
        boolean horiz = p.isHorizontal();
        int frontR = horiz ? br : br + sz - 1;
        int frontC = horiz ? bc + sz - 1 : bc;
        int dist = Math.abs(horiz ? (ec - frontC) : (er - frontR));
        Set<Character> blockers = new HashSet<>();
        if (horiz) {
            int start = Math.min(frontC, ec), end = Math.max(frontC, ec);
            for (int c = start + 1; c < end; c++) {
                char ch = board.getCell(br, c);
                if (ch != '.') blockers.add(ch);
            }
        } else {
            int start = Math.min(frontR, er), end = Math.max(frontR, er);
            for (int r = start + 1; r < end; r++) {
                char ch = board.getCell(r, bc);
                if (ch != '.') blockers.add(ch);
            }
        }
        return dist + blockers.size();
    }
}