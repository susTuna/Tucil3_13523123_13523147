package com.java.searching.heuristic;

import com.java.model.Board;
import com.java.model.Piece;
import com.java.searching.State;
import java.util.*;
import java.io.*;

public class Pattern implements Heuristic {
    private static Map<String, Integer> patternDatabase = new HashMap<>();
    
    private static List<Set<Character>> patterns = new ArrayList<>();
    
    static {
        initializePatterns();
        loadPatternDatabase();
    }
    
    public int calculate(State state) {
        int total = 0;
        Board board = state.getBoard();
        
        for (Set<Character> pattern : patterns) {
            String patternHash = extractPatternHash(board, pattern);
            
            if (patternDatabase.containsKey(patternHash)) {
                total += patternDatabase.get(patternHash);
            } else {
                total += calculateBaseHeuristic(board, pattern);
            }
        }
        return total;
    }
    
    private static void initializePatterns() {
        patterns.add(new HashSet<>(Arrays.asList('P')));
        Set<Character> exitRegionPattern = new HashSet<>();
        exitRegionPattern.add('P');
        patterns.add(exitRegionPattern);
        Set<Character> cornerPattern = new HashSet<>();
        patterns.add(cornerPattern);
    }
    
    private static String getDbFilePath() {
        String homeDir = System.getProperty("user.home");
        return homeDir + File.separator + "rushHourPatternDB.ser";
    }

    private static void loadPatternDatabase() {
        try {
            File dbFile = new File(getDbFilePath());
            if (dbFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile));
                patternDatabase = (Map<String, Integer>) ois.readObject();
                ois.close();
                System.out.println("Loaded pattern database with " + patternDatabase.size() + " entries");
            } else {
                System.out.println("No pattern database found, using fallback heuristics");
            }
        } catch (Exception e) {
            System.out.println("Error loading pattern database: " + e.getMessage());
        }
    }
    
    private String extractPatternHash(Board board, Set<Character> pattern) {
        StringBuilder hash = new StringBuilder();
        
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow();
        int ec = board.getExitCol();
        
        if (pattern.contains('P')) {
            hash.append("P:")
                .append(p.getRow()).append(",")
                .append(p.getCol()).append(",")
                .append(p.getSize()).append(",")
                .append(p.isHorizontal() ? "H" : "V").append(";");
        }
        
        hash.append("K:").append(er).append(",").append(ec).append(";");
        
        if (pattern == patterns.get(1)) {
            if (p.isHorizontal()) {
                int row = p.getRow();
                for (int c = 0; c < board.getCols(); c++) {
                    char cell = board.getCell(row, c);
                    if (cell != '.' && cell != 'P' && !pattern.contains(cell)) {
                        pattern.add(cell);
                    }
                }
            } 
            else {
                int col = p.getCol();
                for (int r = 0; r < board.getRows(); r++) {
                    char cell = board.getCell(r, col);
                    if (cell != '.' && cell != 'P' && !pattern.contains(cell)) {
                        pattern.add(cell);
                    }
                }
            }
        }
        
        if (pattern == patterns.get(2)) {
            if (board.getCell(0, 0) != '.') pattern.add(board.getCell(0, 0));
            if (board.getCell(0, board.getCols()-1) != '.') pattern.add(board.getCell(0, board.getCols()-1));
            if (board.getCell(board.getRows()-1, 0) != '.') pattern.add(board.getCell(board.getRows()-1, 0));
            if (board.getCell(board.getRows()-1, board.getCols()-1) != '.') 
                pattern.add(board.getCell(board.getRows()-1, board.getCols()-1));
        }
        
        for (char pieceId : pattern) {
            if (pieceId == 'P') continue;
            
            Piece piece = board.getPieces().get(pieceId);
            if (piece != null) {
                hash.append(pieceId).append(":")
                    .append(piece.getRow()).append(",")
                    .append(piece.getCol()).append(",")
                    .append(piece.getSize()).append(",")
                    .append(piece.isHorizontal() ? "H" : "V").append(";");
            }
        }
        return hash.toString();
    }
    
    private int calculateBaseHeuristic(Board board, Set<Character> pattern) {
        Piece p = board.getPieces().get('P');
        int er = board.getExitRow();
        int ec = board.getExitCol();
        
        if (pattern == patterns.get(0)) {
            if (p.isHorizontal()) {
                int frontCol = p.getCol() + p.getSize() - 1;
                return Math.abs(ec - frontCol);
            } else {
                int frontRow = p.getRow() + p.getSize() - 1;
                return Math.abs(er - frontRow);
            }
        }
        else if (pattern == patterns.get(1)) {
            Set<Character> blockers = new HashSet<>();
            
            if (p.isHorizontal()) {
                int row = p.getRow();
                int frontCol = p.getCol() + p.getSize() - 1;
                int start = Math.min(frontCol, ec);
                int end = Math.max(frontCol, ec);
                
                for (int c = start + 1; c < end; c++) {
                    char cell = board.getCell(row, c);
                    if (cell != '.') blockers.add(cell);
                }
            } else {
                int col = p.getCol();
                int frontRow = p.getRow() + p.getSize() - 1;
                int start = Math.min(frontRow, er);
                int end = Math.max(frontRow, er);
                
                for (int r = start + 1; r < end; r++) {
                    char cell = board.getCell(r, col);
                    if (cell != '.') blockers.add(cell);
                }
            }
            
            return blockers.size() * 2;
        }
        else if (pattern == patterns.get(2)) {
            int penalty = 0;
            int rows = board.getRows();
            int cols = board.getCols();
            
            if (board.getCell(0, 0) != '.' && board.getCell(0, 1) != '.' && 
                board.getCell(1, 0) != '.') {
                penalty += 1;
            }
            
            if (board.getCell(0, cols-1) != '.' && board.getCell(0, cols-2) != '.' && 
                board.getCell(1, cols-1) != '.') {
                penalty += 1;
            }
            
            if (board.getCell(rows-1, 0) != '.' && board.getCell(rows-1, 1) != '.' && 
                board.getCell(rows-2, 0) != '.') {
                penalty += 1;
            }
            
            if (board.getCell(rows-1, cols-1) != '.' && board.getCell(rows-1, cols-2) != '.' && 
                board.getCell(rows-2, cols-1) != '.') {
                penalty += 1;
            }
            return penalty;
        }
        
        return 0;
    }
    
    public static void buildPatternDatabase() {
        System.out.println("Building pattern database - this may take a while...");
        
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDbFilePath()));
            oos.writeObject(patternDatabase);
            oos.close();
            System.out.println("Saved pattern database with " + patternDatabase.size() + " entries");
        } catch (IOException e) {
            System.out.println("Error saving pattern database: " + e.getMessage());
        }
    }
    
    private static class PatternState implements Serializable {
        Map<Character, Piece> pieces;
        int er, ec;
        
        public PatternState(Map<Character, Piece> pieces, int er, int ec) {
            this.pieces = pieces;
            this.er = er;
            this.ec = ec;
        }
    }
}