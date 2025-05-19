package com.java.searching.heuristic;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.State;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pattern implements Heuristic {
    public int calculate(State state) {
        Board board = state.getBoard();
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow(), ec = board.getExitCol();
        
        int distanceToExit = calculateDistanceToExit(p, er, ec);
        int directBlockers = countDirectBlockers(board, p, er, ec);
        int cornerPatternPenalty = detectCornerPattern(board);
        int gridlockPatternPenalty = detectGridlockPattern(board);
        int zigzagPatternPenalty = detectZigzagPattern(board, p);
        int exitPathDensity = calculateExitPathDensity(board, p);
        
        return distanceToExit * 2 + 
               directBlockers * 3 + 
               cornerPatternPenalty * 2 +
               gridlockPatternPenalty * 2 +
               zigzagPatternPenalty * 2 +
               exitPathDensity;
    }
    
    private int calculateDistanceToExit(Piece p, int exitRow, int exitCol) {
        if (p.isHorizontal()) {
            return Math.abs(exitCol - (p.getCol() + p.getSize() - 1));
        } else {
            return Math.abs(exitRow - (p.getRow() + p.getSize() - 1));
        }
    }
    
    private int countDirectBlockers(Board board, Piece p, int exitRow, int exitCol) {
        Set<Character> blockers = new HashSet<>();
        
        if (p.isHorizontal()) {
            int row = p.getRow();
            int frontCol = p.getCol() + p.getSize() - 1;
            int start = Math.min(frontCol, exitCol);
            int end = Math.max(frontCol, exitCol);
            
            for (int c = start + 1; c < end; c++) {
                char cell = board.getCell(row, c);
                if (cell != '.') blockers.add(cell);
            }
        } else {
            int col = p.getCol();
            int frontRow = p.getRow() + p.getSize() - 1;
            int start = Math.min(frontRow, exitRow);
            int end = Math.max(frontRow, exitRow);
            
            for (int r = start + 1; r < end; r++) {
                char cell = board.getCell(r, col);
                if (cell != '.') blockers.add(cell);
            }
        }
        
        return blockers.size();
    }
    
    private int detectCornerPattern(Board board) {
        int penalty = 0;
        int rows = board.getRows();
        int cols = board.getCols();
        
        // Top-left corner check
        if (board.getCell(0, 0) != '.' && board.getCell(0, 1) != '.' && 
            board.getCell(1, 0) != '.') {
            penalty += 2;
        }
        
        // Top-right corner check
        if (board.getCell(0, cols-1) != '.' && board.getCell(0, cols-2) != '.' && 
            board.getCell(1, cols-1) != '.') {
            penalty += 2;
        }
        
        // Bottom-left corner check
        if (board.getCell(rows-1, 0) != '.' && board.getCell(rows-1, 1) != '.' && 
            board.getCell(rows-2, 0) != '.') {
            penalty += 2;
        }
        
        // Bottom-right corner check
        if (board.getCell(rows-1, cols-1) != '.' && board.getCell(rows-1, cols-2) != '.' && 
            board.getCell(rows-2, cols-1) != '.') {
            penalty += 2;
        }
        
        return penalty;
    }
    
    private int detectGridlockPattern(Board board) {
        int penalty = 0;
        int rows = board.getRows();
        int cols = board.getCols();
        
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                char current = board.getCell(r, c);
                if (current == '.') continue;
                
                // Check for plus-sign pattern
                if (board.getCell(r-1, c) != '.' && 
                    board.getCell(r+1, c) != '.' && 
                    board.getCell(r, c-1) != '.' && 
                    board.getCell(r, c+1) != '.') {
                    penalty += 3;
                }
                
                // Check for 2x2 block pattern
                if (c < cols - 2 && r < rows - 2 &&
                    board.getCell(r, c+1) != '.' && 
                    board.getCell(r+1, c) != '.' && 
                    board.getCell(r+1, c+1) != '.') {
                    penalty += 2;
                }
            }
        }
        
        return penalty;
    }
    
    private int detectZigzagPattern(Board board, Piece primaryPiece) {
        int penalty = 0;
        
        if (primaryPiece.isHorizontal()) {
            int row = primaryPiece.getRow();
            boolean lastWasVertical = false;
            int consecutiveVertical = 0;
            
            for (int c = 0; c < board.getCols(); c++) {
                char cell = board.getCell(row, c);
                if (cell != '.' && cell != 'P') {
                    Piece piece = board.getPieces().get(cell);
                    if (piece != null && !piece.isHorizontal()) {
                        if (lastWasVertical) {
                            if (++consecutiveVertical >= 2) {
                                penalty += 2;
                            }
                        } else {
                            lastWasVertical = true;
                            consecutiveVertical = 1;
                        }
                    } else {
                        lastWasVertical = false;
                    }
                } else {
                    lastWasVertical = false;
                }
            }
        } else {
            int col = primaryPiece.getCol();
            boolean lastWasHorizontal = false;
            int consecutiveHorizontal = 0;
            
            for (int r = 0; r < board.getRows(); r++) {
                char cell = board.getCell(r, col);
                if (cell != '.' && cell != 'P') {
                    Piece piece = board.getPieces().get(cell);
                    if (piece != null && piece.isHorizontal()) {
                        if (lastWasHorizontal) {
                            if (++consecutiveHorizontal >= 2) {
                                penalty += 2;
                            }
                        } else {
                            lastWasHorizontal = true;
                            consecutiveHorizontal = 1;
                        }
                    } else {
                        lastWasHorizontal = false;
                    }
                } else {
                    lastWasHorizontal = false;
                }
            }
        }
        
        return penalty;
    }
    
    private int calculateExitPathDensity(Board board, Piece primaryPiece) {
        int density = 0;
        int rows = board.getRows();
        int cols = board.getCols();
        int bufferSize = 2;
        
        if (primaryPiece.isHorizontal()) {
            int row = primaryPiece.getRow();
            int minRow = Math.max(0, row - bufferSize);
            int maxRow = Math.min(rows - 1, row + bufferSize);
            
            for (int r = minRow; r <= maxRow; r++) {
                int distanceWeight = (r == row) ? 2 : 1;
                for (int c = 0; c < cols; c++) {
                    if (board.getCell(r, c) != '.') {
                        density += distanceWeight;
                    }
                }
            }
        } else {
            int col = primaryPiece.getCol();
            int minCol = Math.max(0, col - bufferSize);
            int maxCol = Math.min(cols - 1, col + bufferSize);
            
            for (int c = minCol; c <= maxCol; c++) {
                int distanceWeight = (c == col) ? 2 : 1;
                for (int r = 0; r < rows; r++) {
                    if (board.getCell(r, c) != '.') {
                        density += distanceWeight;
                    }
                }
            }
        }
        
        return density / 4;
    }
}