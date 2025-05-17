package com.java.checker;

import com.java.exception.InvalidConfigurationException;
import com.java.model.Board;
import com.java.model.Piece;

public class PuzzleChecker {
    /**
     * Returns true if:
     *  - P actually covers the exit (rare, since exit is outside), or
     *  - there's a straight, empty corridor from P to X in P's orientation.
     *
     * Throws if the primary piece is oriented wrong for that exit.
     */
    public static boolean checkSolved(Board board)
      throws InvalidConfigurationException
    {
        Piece p = board.getPieces().get('P');
        if (p == null)
            throw new InvalidConfigurationException("No primary piece 'P' found");

        int er = board.getExitRow(), ec = board.getExitCol();
        int R  = board.getRows(), C  = board.getCols();

        boolean exitLeft   = ec < 0;
        boolean exitRight  = ec > C - 1;
        boolean exitTop    = er < 0;
        boolean exitBottom = er > R - 1;

        if (( (exitLeft || exitRight) && p.isVertical() ) ||
            ( (exitTop  || exitBottom) && p.isHorizontal() ))
        {
            throw new InvalidConfigurationException(
              "Primary Vehicles Cannot Exit the Compound"
            );
        }

        for (int[] cell : p.occupiedCells()) {
            if (cell[0] == er && cell[1] == ec) {
                System.out.println("Puzzle solved!");
                return true;
            }
        }

        if ((exitLeft || exitRight) && p.isHorizontal() && p.getRow() == er) {
            int start = exitLeft ? 0 : p.getCol() + p.getSize();
            int end   = exitLeft ? p.getCol() : C;
            for (int c = start; c < end; c++) {
                if (board.getCell(er, c) != '.') return false;
            }
            System.out.println("Puzzle solved!");
            return true;
        }

        if ((exitTop || exitBottom) && p.isVertical() && p.getCol() == ec) {
            int start = exitTop    ? 0 : p.getRow() + p.getSize();
            int end   = exitTop    ? p.getRow() : R;
            for (int r = start; r < end; r++) {
                if (board.getCell(r, ec) != '.') return false;
            }
            System.out.println("Puzzle solved!");
            return true;
        }

        return false;
    }
}
