package com.java;

import javax.swing.SwingUtilities;
import com.java.gui.RushHourGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RushHourGUI().setVisible(true);
        });
    }
}


// package com.java;

// import com.java.model.*;
// import com.java.searching.*;
// import java.util.List;
// import java.util.Scanner;

// public class Main {
//     public static void main(String[] args)
//       throws Exception
//     {
//         if(args.length<1){
//             System.out.println("Usage: java Main <config-file>");
//             System.exit(1);
//         }
//         Board board=Config.loadConfig(args[0]);
//         System.out.println("Initial Board:");
//         board.printBoard();

//         State start=new State(board);
//         SearchStrategy solver;
//         Scanner sc=new Scanner(System.in);
//         System.out.println("Choose algorithm: 1) A* 2) UCS 3) GBFS");
//         String choice=sc.nextLine().trim();
//         String algorithmName;
//         if(choice.equals("1")) {

//             solver=new AStarSolver();
//             algorithmName = "A*";
//         }
//         else if(choice.equals("2")) {
//             solver=new UCSolver();
//             algorithmName = "UCS";
//         }
//         else {
//             solver=new GBFSolver();
//             algorithmName = "GBFS";
//         }

//         SolverResult res = solver.solve(start);
//         List<Move> path=res.path;

//         State cur=start;
//         for(Move mv:path){
//             cur=mv.next;
//             cur.getBoard().printBoard();
//         }
//         System.out.println("Elapsed ms: "+res.timeMs);
//         System.out.println("Nodes created: "+res.nodesCreated);
//         System.out.println("Shortest steps: "+path.size());

//         System.out.println("Save solution? (y/n): ");
//         String saveChoice = sc.nextLine().trim();

//         // Default is not to save unless user explicitly types 'y' or 'yes'
//         if (saveChoice.equalsIgnoreCase("y") || saveChoice.equalsIgnoreCase("yes")) {
//             System.out.println("Enter filename (without .txt extension):");
//             String filename = sc.nextLine().trim();
//             Save.saveSolution(res, start, filename, args[0], algorithmName);
//             System.out.println("Solution saved to " + filename + ".txt");
//         }

//         // Close scanner in all cases
//         sc.close();
//     }
// }
