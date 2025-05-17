package com.java.exception;

import com.java.model.Piece;

public class MoveBlockedException extends Exception {
    private final Piece blocker;

    public MoveBlockedException(Piece blocker) {
        super("Move blocked by piece '" + (blocker != null ? blocker.getId() : "unknown") + "'");
        this.blocker = blocker;
    }

    /** The piece that's blocking. */
    public Piece getBlocker() {
        return blocker;
    }
}
