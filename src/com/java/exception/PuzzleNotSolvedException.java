package com.java.exception;

public class PuzzleNotSolvedException extends Exception {
    public PuzzleNotSolvedException() {
        super("Puzzle not solved");
    }
}
