package com.java.model;

import com.java.model.Board;
import com.java.searching.Move;
import com.java.searching.SolverResult;
import com.java.searching.State;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Save {
    public static void saveSolution(SolverResult result, State initialState, String filename, 
                                    String originalPuzzle, String algorithmName) 
            throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fullFilename = filename + "_" + timestamp + ".txt";
        
        // mkdir like ahh type shi
        Files.createDirectories(Paths.get("test/solutions"));
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test/solutions/" + fullFilename))) {
            writer.write("Rush Hour Solution\n");
            writer.write("=================\n\n");
            writer.write("Original puzzle: " + originalPuzzle + "\n");
            writer.write("Algorithm: " + algorithmName + "\n");
            writer.write("Number of moves: " + result.path.size() + "\n");
            writer.write("Nodes created: " + result.nodesCreated + "\n");
            writer.write("Execution time: " + result.timeMs + " ms\n\n");
            
            writer.write("Solution path:\n");
            writer.write("-------------\n\n");
            
            writer.write("Initial state:\n");
            writer.write(initialState.getBoard().toPlainString());
            writer.write("\n\n");
            
            State currentState = initialState;
            for (int i = 0; i < result.path.size(); i++) {
                Move move = result.path.get(i);
                currentState = move.next;
                
                writer.write("Step " + (i + 1) + ":\n");
                writer.write("Move piece '" + move.id + "' ");
                if (move.dr < 0) writer.write("left " + Math.abs(move.dr));
                if (move.dr > 0) writer.write("right " + move.dr);
                if (move.dc < 0) writer.write("down " + Math.abs(move.dc));
                if (move.dc > 0) writer.write("up " + move.dc);
                writer.write("\n\n");
                
                writer.write(currentState.getBoard().toPlainString());
                writer.write("\n\n");
                
                if (i < result.path.size() - 1) {
                    writer.write("↓\n\n");
                }
            }
        }
    }
}