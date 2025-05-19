package com.java.searching;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.heuristic.*;
import java.util.*;

public class State {
    private final Board board;
    private final String key;
    private static HeuristicType currentHeuristic = HeuristicType.COMPOSITE;
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

    public static void setHeuristic(HeuristicType heuristic) {
        currentHeuristic = heuristic;
    }

    public int heuristic() {
        switch (currentHeuristic) {
            case COMPOSITE:
                return new Composite().calculate(this);
            case MANHATTAN:
                return new Manhattan().calculate(this);
            default:
                return new Composite().calculate(this);
        
        }
    }

    @Override public boolean equals(Object o) {
        return o instanceof State && ((State)o).key.equals(key);
    }
    @Override public int hashCode() {
        return key.hashCode();
    }
}
