package com.java;

import com.java.model.*;
import com.java.searching.*;
import com.java.searching.heuristic.HeuristicType;
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
        System.out.println("Choose algorithm: 1) A* 2) UCS 3) GBFS 4) IDA*");
        String choice = sc.nextLine().trim();
        String algorithmName;

        switch (choice) {
            case "2":
                solver = new UCSolver();
                algorithmName = "UCS";
                break;
            case "3":
                solver = new GBFSolver();
                algorithmName = "GBFS";
                break;
            case "4":
                solver = new IDAStarSolver();
                algorithmName = "IDA*";
                break;
            case "1":
            default:
                solver = new AStarSolver();
                algorithmName = "A*";
                break;
        }

        if (!algorithmName.equals("UCS")){
            System.out.println("Choose heuristic: 1) Composite 2) Manhattan 3) Pattern 4) Enhanced Blocking");
            String heuristicChoice = sc.nextLine().trim();
            
            switch (heuristicChoice) {
                case "2":
                    State.setHeuristic(HeuristicType.MANHATTAN);
                    System.out.println("Using Manhattan Distance Heuristic");
                    break;
                case "3":
                    State.setHeuristic(HeuristicType.PATTERN);
                    System.out.println("Using Pattern Database Heuristic");
                    break;
                case "4":
                    State.setHeuristic(HeuristicType.BLOCKING);
                    System.out.println("Using Enhanced Blocking Heuristic");
                    break;
                case "1":
                default:
                    State.setHeuristic(HeuristicType.COMPOSITE);
                    System.out.println("Using Composite Heuristic");
                    break;
            }
        }

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

        System.out.println("Save solution? (y/n): ");
        String saveChoice = sc.nextLine().trim();

        if (saveChoice.equalsIgnoreCase("y") || saveChoice.equalsIgnoreCase("yes")) {
            System.out.println("Enter filename (without .txt extension):");
            String filename = sc.nextLine().trim();
            Save.saveSolution(res, start, filename, args[0], algorithmName, State.getHeuristicType());
            System.out.println("Solution saved to " + filename + ".txt");
        }

        sc.close();
    }
}
