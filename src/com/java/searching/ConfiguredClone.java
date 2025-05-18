package com.java.searching;
import com.java.model.*;
import java.util.Map;

public class ConfiguredClone {
    public static Board cloneBoard(Board b) {
        int R=b.getRows(), C=b.getCols();
        Board copy=new Board(R,C);
        for(Map.Entry<Character,Piece> e:b.getPieces().entrySet()){
            Piece p=e.getValue();
            for(int[] cell:p.occupiedCells()){
                copy.parseCell(cell[0],cell[1],p.getId());
            }
        }
        copy.setExit(b.getExitRow(),b.getExitCol());
        return copy;
    }
}
