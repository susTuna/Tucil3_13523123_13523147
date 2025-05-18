package com.java.searching;

public class Move {
    public final int dr, dc;
    public final char id;
    public final State next;
    public Move(int dr,int dc,char id,State next){
        this.dr=dr;this.dc=dc;this.id=id;this.next=next;
    }
}
