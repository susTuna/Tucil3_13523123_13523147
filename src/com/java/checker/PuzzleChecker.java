package com.java.checker;

import com.java.exception.InvalidConfigurationException;
import com.java.model.Board;
import com.java.model.Piece;

public class PuzzleChecker {
    /**
     * @return true if P sits on X or has a clear straight-line exit path
     * @throws InvalidConfigurationException if P is wrongly oriented for that exit
     */
    public static boolean checkSolved(Board board)
      throws InvalidConfigurationException
    {
        Piece p = board.getPieces().get('P');
        if (p == null) {
            throw new InvalidConfigurationException("No primary piece 'P' found");
        }

        int er = board.getExitRow(), ec = board.getExitCol();
        int R  = board.getRows(), C = board.getCols();

        boolean exitOnVerticalEdge   = (ec == 0 || ec == C - 1);
        boolean exitOnHorizontalEdge = (er == 0 || er == R - 1);

        if ((exitOnVerticalEdge   && p.isVertical())   ||
            (exitOnHorizontalEdge && p.isHorizontal()))
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

        if (exitOnVerticalEdge && p.isHorizontal() && p.getRow() == er) {
            int left  = Math.min(p.getCol(), ec);
            int right = Math.max(p.getCol() + p.getSize() - 1, ec);
            for (int c = left + 1; c < right; c++) {
                if (board.getCell(er, c) != '.') {
                    return false;
                }
            }
            System.out.println("Puzzle solved!");
            return true;
        }

        if (exitOnHorizontalEdge && p.isVertical() && p.getCol() == ec) {
            int top    = Math.min(p.getRow(), er);
            int bottom = Math.max(p.getRow() + p.getSize() - 1, er);
            for (int r = top + 1; r < bottom; r++) {
                if (board.getCell(r, ec) != '.') {
                    return false;
                }
            }
            System.out.println("Puzzle solved!");
            return true;
        }

        return false;
    }
}
