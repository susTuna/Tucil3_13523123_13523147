package com.java.searching.heuristic;

import com.java.searching.State;

public enum HeuristicType {
    COMPOSITE, // Original heuristic (Distance + Blockers)
    MANHATTAN, // Manhattan distance heuristic
    PATTERN, // Pattern database heuristic
    BLOCKING // Enhanced blocking heuristic
}
