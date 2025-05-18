package com.java;

import com.java.model.*;
import com.java.searching.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
      throws Exception
    {
        if(args.length<1){
            System.out.println("Usage: java Main <config-file>");
            System.exit(1);
        }
        Board board=Config.loadConfig(args[0]);
        System.out.println("Initial Board:");
        board.printBoard();

        State start=new State(board);
        SearchStrategy solver;
        Scanner sc=new Scanner(System.in);
        System.out.println("Choose algorithm: 1) A* 2) UCS 3) GBFS");
        String choice=sc.nextLine().trim();
        if(choice.equals("1")) solver=new AStarSolver();
        else if(choice.equals("2")) solver=new UCSolver();
        else solver=new GBFSolver();

        SolverResult res = solver.solve(start);
        List<Move> path=res.path;

        State cur=start;
        for(Move mv:path){
            cur=mv.next;
            cur.getBoard().printBoard();
        }
        System.out.println("Elapsed ms: "+res.timeMs);
        System.out.println("Nodes created: "+res.nodesCreated);
        System.out.println("Shortest steps: "+path.size());
    }
}
