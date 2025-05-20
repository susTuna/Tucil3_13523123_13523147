package com.java.searching;

import com.java.checker.PuzzleChecker;
import com.java.exception.InvalidConfigurationException;
import java.util.*;

public class IDAStarSolver implements SearchStrategy {
    public SolverResult solve(State start) {
        long t0 = System.currentTimeMillis();
        int threshold = start.heuristic();
        long nodes = 0;
        
        List<Move> path = new ArrayList<>();
        
        int iteration = 0;
        int maxIterations = 100; // Safety limit for extremely difficult puzzles
        
        while (iteration++ < maxIterations) {
            path.clear();

            Map<String, Integer> bestCost = new HashMap<>();
            
            Result result = new Result();
            result.nodesExpanded = nodes;
            
            boolean found = dfs(start, 0, threshold, path, bestCost, result);
            nodes = result.nodesExpanded;
            
            if (found) {
                return new SolverResult(path, nodes, System.currentTimeMillis() - t0);
            }
            
            if (result.nextThreshold == Integer.MAX_VALUE) {
                return new SolverResult(new ArrayList<>(), nodes, System.currentTimeMillis() - t0);
            }
            
            threshold = result.nextThreshold;
            
            System.out.println("IDA* iteration " + iteration + ": increased threshold to " + threshold);
        }
        
        // If we hit iteration limit, return best effort
        return new SolverResult(new ArrayList<>(), nodes, System.currentTimeMillis() - t0);
    }
    
    private static class Result {
        int nextThreshold = Integer.MAX_VALUE;
        long nodesExpanded = 0;
    }
    
    private boolean dfs(State state, int g, int threshold, List<Move> path, 
                      Map<String, Integer> bestCost, Result result) {
        result.nodesExpanded++;
        
        int h = state.heuristic();
        int f = g + h;
        
        // Prune if f-value exceeds threshold
        if (f > threshold) {
            result.nextThreshold = Math.min(result.nextThreshold, f);
            return false;
        }
        
        // Check for goal state
        boolean isGoal;
        try {
            isGoal = PuzzleChecker.checkSolved(state.getBoard());
        } catch (InvalidConfigurationException e) {
            isGoal = false;
        }
        
        if (isGoal) {
            return true;
        }

        String stateKey = state.toString();
        
        Integer previousCost = bestCost.get(stateKey);
        if (previousCost != null && previousCost <= g) {
            return false;
        }
        
        bestCost.put(stateKey, g);
        
        List<Move> successors = state.successors();
        successors.sort(Comparator.comparingInt(move -> g + 1 + move.next.heuristic()));
        
        // Try each successor
        for (Move move : successors) {
            path.add(move);
            
            if (dfs(move.next, g + 1, threshold, path, bestCost, result)) {
                return true;
            }
            
            path.remove(path.size() - 1);
        }
        
        return false;
    }
}