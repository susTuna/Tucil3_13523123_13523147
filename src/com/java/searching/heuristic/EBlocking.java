package com.java.searching.heuristic;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.State;
import java.util.*;

public class EBlocking implements Heuristic {
    @Override
    public int calculate(State state) {
        Board board = state.getBoard();
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow();
        int ec = board.getExitCol();
        
        int targetDistance = distanceToExit(board, p, er, ec);
        
        Set<Character> directBlockers = countDirectBlockers(board, p, er, ec);
        
        int recursiveBlockers = countRecursiveBlockers(board, directBlockers);
        
        int mobility = calculateMobilityScore(board, p, directBlockers);
        
        return (targetDistance * 4) + 
               (directBlockers.size() * 3) + 
               (recursiveBlockers * 2) - 
               (int)(mobility * 0.5);
    }
    
    private int distanceToExit(Board board, Piece p, int er, int ec) {
        if (p.isHorizontal()) {
            int frontCol = p.getCol() + p.getSize() - 1;
            return Math.abs(ec - frontCol);
        } else {
            int frontRow = p.getRow() + p.getSize() - 1;
            return Math.abs(er - frontRow);
        }
    }
    
    private Set<Character> countDirectBlockers(Board board, Piece p, int er, int ec) {
        Set<Character> blockers = new HashSet<>();
        int rows = board.getRows();
        int cols = board.getCols();
        
        if (p.isHorizontal()) {
            int row = p.getRow();
            int frontCol = p.getCol() + p.getSize() - 1;
            
            if (ec > frontCol) {
                for (int c = frontCol + 1; c < cols; c++) {
                    char cell = board.getCell(row, c);
                    if (cell != '.') {
                        blockers.add(cell);
                    }
                }
            } else if (ec < p.getCol()) {
                for (int c = Math.max(0, ec); c < p.getCol(); c++) {
                    char cell = board.getCell(row, c);
                    if (cell != '.') {
                        blockers.add(cell);
                    }
                }
            }
        } else {
            int col = p.getCol();
            int frontRow = p.getRow() + p.getSize() - 1;
            
            if (er > frontRow) {
                for (int r = frontRow + 1; r < rows; r++) {
                    char cell = board.getCell(r, col);
                    if (cell != '.') {
                        blockers.add(cell);
                    }
                }
            } else if (er < p.getRow()) {
                for (int r = Math.max(0, er); r < p.getRow(); r++) {
                    char cell = board.getCell(r, col);
                    if (cell != '.') {
                        blockers.add(cell);
                    }
                }
            }
        }
        
        return blockers;
    }
    
    private int countRecursiveBlockers(Board board, Set<Character> directBlockers) {
        int recursiveCount = 0;
        Map<Character, Piece> pieces = board.getPieces();
        
        for (char blockerId : directBlockers) {
            Piece blocker = pieces.get(blockerId);
            if (blocker == null) continue;
        
            Set<Character> secondaryBlockers = findPiecesBlocking(board, blocker);
            recursiveCount += secondaryBlockers.size();
        }
        
        return recursiveCount;
    }
    
    private Set<Character> findPiecesBlocking(Board board, Piece piece) {
        Set<Character> blockingPieces = new HashSet<>();
        
        if (piece.isHorizontal()) {
            int row = piece.getRow();
            
            if (piece.getCol() > 0) {
                char leftCell = board.getCell(row, piece.getCol() - 1);
                if (leftCell != '.') {
                    blockingPieces.add(leftCell);
                }
            }
            
            int rightPos = piece.getCol() + piece.getSize();
            if (rightPos < board.getCols()) {
                char rightCell = board.getCell(row, rightPos);
                if (rightCell != '.') {
                    blockingPieces.add(rightCell);
                }
            }
        } else {
            int col = piece.getCol();
            
            if (piece.getRow() > 0) {
                char upCell = board.getCell(piece.getRow() - 1, col);
                if (upCell != '.') {
                    blockingPieces.add(upCell);
                }
            }
            
            int bottomPos = piece.getRow() + piece.getSize();
            if (bottomPos < board.getRows()) {
                char downCell = board.getCell(bottomPos, col);
                if (downCell != '.') {
                    blockingPieces.add(downCell);
                }
            }
        }
        
        return blockingPieces;
    }
    
    private int calculateMobilityScore(Board board, Piece p, Set<Character> directBlockers) {
        int mobilityScore = 0;
        Map<Character, Piece> pieces = board.getPieces();
        
        int targetMobility = calculatePieceMobility(board, p);
        mobilityScore += targetMobility * 2;
        
        for (char blockerId : directBlockers) {
            Piece blocker = pieces.get(blockerId);
            if (blocker == null) continue;
            
            int blockerMobility = calculatePieceMobility(board, blocker);
            mobilityScore += blockerMobility;
        }
        
        return mobilityScore;
    }
    
    private int calculatePieceMobility(Board board, Piece piece) {
        int mobility = 0;
        
        if (piece.isHorizontal()) {
            int row = piece.getRow();
        
            for (int c = piece.getCol() - 1; c >= 0; c--) {
                if (board.getCell(row, c) == '.') {
                    mobility++;
                } else {
                    break;
                }
            }
            
            for (int c = piece.getCol() + piece.getSize(); c < board.getCols(); c++) {
                if (board.getCell(row, c) == '.') {
                    mobility++;
                } else {
                    break;
                }
            }
        } else {
            int col = piece.getCol();
            
            for (int r = piece.getRow() - 1; r >= 0; r--) {
                if (board.getCell(r, col) == '.') {
                    mobility++;
                } else {
                    break;
                }
            }
            
            for (int r = piece.getRow() + piece.getSize(); r < board.getRows(); r++) {
                if (board.getCell(r, col) == '.') {
                    mobility++;
                } else {
                    break;
                }
            }
        }
        
        return mobility;
    }
}