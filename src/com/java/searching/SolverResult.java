package com.java.searching;
import java.util.*;
public class SolverResult {
    public final List<Move> path;
    public final long nodesCreated;
    public final long timeMs;
    public SolverResult(List<Move> path, long nodesCreated, long timeMs){
        this.path=path;this.nodesCreated=nodesCreated;this.timeMs=timeMs;
    }
}
