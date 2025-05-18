package com.java.searching;

import com.java.checker.PuzzleChecker;
import com.java.exception.InvalidConfigurationException;
import java.util.*;


public class GBFSolver implements SearchStrategy {
    public SolverResult solve(State start) {
        long t0=System.currentTimeMillis();
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n->n.h));
        Set<State> closed = new HashSet<>();
        open.add(new Node(start,null,null,0,start.heuristic()));
        long nodes=0;
        while(!open.isEmpty()){
            Node cur=open.poll();
            boolean isGoal;
            try {
                isGoal = PuzzleChecker.checkSolved(cur.state.getBoard());
            } catch (InvalidConfigurationException e) {
                isGoal = false;
            }
            if (isGoal) {
                List<Move> path=new ArrayList<>();
                for(Node n=cur;n.move!=null;n=n.parent)path.add(n.move);
                Collections.reverse(path);
                return new SolverResult(path,nodes,System.currentTimeMillis()-t0);
            }
            nodes++;
            closed.add(cur.state);
            for(Move mv:cur.state.successors()){
                if(!closed.contains(mv.next)){
                    open.add(new Node(mv.next,cur,mv,0,mv.next.heuristic()));
                }
            }
        }
        return new SolverResult(List.of(),nodes,System.currentTimeMillis()-t0);
    }
}
