package com.java;

import com.java.checker.PuzzleChecker;
import com.java.exception.*;
import com.java.model.*;
import java.util.Map;

public class Main {
    public static void main(String[] args)
        throws InvalidConfigurationException, PuzzleNotSolvedException
    {
        if (args.length < 1) {
            System.out.println("Usage: java Main <config-file>");
            System.exit(1);
        }
        Board board = Config.loadConfig(args[0]);
        if (board == null) {
            System.err.println("Failed to load board configuration.");
            System.exit(2);
        }

        System.out.println("Initial Board:");
        board.printBoard();

        Map<Character, Piece> pcs = board.getPieces();

        try {
            // 11 steps
            // board.movePiece(pcs.get('F'), 0, -1);
            // board.movePiece(pcs.get('I'), 0, 1);
            // board.movePiece(pcs.get('M'), -1, 0);
            // board.movePiece(pcs.get('F'), 0, -1);
            // board.movePiece(pcs.get('E'), 0, -1);
            // board.movePiece(pcs.get('B'), 1, 0);
            // board.movePiece(pcs.get('G'), 0, 1);
            // board.movePiece(pcs.get('G'), 0, 1);
            // board.movePiece(pcs.get('P'), 1, 0);
            // board.movePiece(pcs.get('P'), 1, 0);
            board.movePiece(pcs.get('P'), 1, 0);
            board.movePiece(pcs.get('P'), -1, 0);

            board.printBoard();
            
        } catch (MoveBlockedException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
        boolean solved = PuzzleChecker.checkSolved(board);
        if (!solved) {
            throw new PuzzleNotSolvedException();
        }
    }
}
