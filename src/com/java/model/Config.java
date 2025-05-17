package com.java.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    public static Board loadConfig(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dims = br.readLine().trim().split("\\s+");
            int A = Integer.parseInt(dims[0]);
            int B = Integer.parseInt(dims[1]);
            br.readLine(); 

            Board board = new Board(A, B);
            for (int r = 0; r < A; r++) {
                String line = br.readLine();
                for (int c = 0; c < B; c++) {
                    board.setCell(r, c, line.charAt(c));
                }
            }
            return board;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading configuration: " + e.getMessage());
            return null;
        }
    }
}
