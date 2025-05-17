package com.java.model;

import java.io.*;

public class Config {
    public static Board loadConfig(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dims = br.readLine().trim().split("\\s+");
            int A = Integer.parseInt(dims[0]);
            int B = Integer.parseInt(dims[1]);
            int C = Integer.parseInt((br.readLine().trim().split("\\s+"))[0]);

            Board board = new Board(A, B);
            for (int r = 0; r < A; r++) {
                String line = br.readLine();
                for (int c = 0; c < line.length(); c++) {
                    board.parseCell(r, c, line.charAt(c));
                }
            }
            
            // Dari spek si ini harusnya piece_count-1, karena tanpa Primary Piece, kalau misal harus sama primary piece berarti piece_count aja
            if (Piece.piece_count-1 != C) { 
                throw new IllegalArgumentException("Mismatch in number of pieces");
            }
            return board;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading configuration: " + e.getMessage());
            return null;
        }
    }
}
