package com.java.searching;

public class Node {
    public final State state;
    public final Node parent;
    public final Move move;
    public final int g, h, f;
    public Node(State s, Node p, Move m, int g, int h){
        this.state=s;this.parent=p;this.move=m;this.g=g;this.h=h;this.f=g+h;
    }
}
