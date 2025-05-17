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
            for (int i = 0; i < A; i++) {
                String line = br.readLine();
                for (int j = 0; j < B; j++) {
                    char ch = line.charAt(j);
                    if (ch == '.') ch = '.';
                    board.setCell(i, j, ch);
                }
            }
            return board;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading configuration: " + e.getMessage());
            return null;
        }
    }
}

