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
            board.movePiece(pcs.get('C'), 0, 1);
            board.movePiece(pcs.get('D'), 0, 1);
            board.movePiece(pcs.get('P'), 2, 0);
            board.movePiece(pcs.get('F'), 0, -3);
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
