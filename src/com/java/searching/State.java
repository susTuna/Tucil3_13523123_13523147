package com.java.searching;

import com.java.model.Board;
import com.java.model.Piece;
import java.util.*;

public class State {
    private final Board board;
    private final String key;
    public State(Board b) {
        this.board = b;
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < b.getRows(); r++) {
            for (int c = 0; c < b.getCols(); c++) {
                sb.append(b.getCell(r, c));
            }
        }
        sb.append("X").append(b.getExitRow()).append(",").append(b.getExitCol());
        this.key = sb.toString();
    }
    public Board getBoard() { return board; }
    public List<Move> successors() {
        List<Move> moves = new ArrayList<>();
        for (Piece p : board.getPieces().values()) {
            for (int[] d : new int[][]{{0,1},{0,-1},{1,0},{-1,0}}) {
                try {
                    Board copy = ConfiguredClone.cloneBoard(board);
                    copy.movePiece(copy.getPieces().get(p.getId()), d[0], d[1]);
                    moves.add(new Move(d[0], d[1], p.getId(), new State(copy)));
                } catch (Exception ignored) {}
            }
        }
        return moves;
    }
    public int heuristic() {
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
    @Override public boolean equals(Object o) {
        return o instanceof State && ((State)o).key.equals(key);
    }
    @Override public int hashCode() {
        return key.hashCode();
    }
}
