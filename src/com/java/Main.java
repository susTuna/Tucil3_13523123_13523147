package com.java;

import com.java.model.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <config-file>");
            System.exit(1);
        }
        String configFile = args[0];
        Board board = Config.loadConfig(configFile);
        if (board == null) {
            System.err.println("Failed to load board configuration.");
            System.exit(1);
        }
        System.out.println("Initial Board Configuration:");
        board.printBoard();
    }
}