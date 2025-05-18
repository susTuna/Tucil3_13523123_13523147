package com.java.searching;

import com.java.checker.PuzzleChecker;
import com.java.exception.InvalidConfigurationException;
import java.util.*;


public class UCSolver implements SearchStrategy {
    @Override
    public SolverResult solve(State start) {
        long t0=System.currentTimeMillis();
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n->n.g));
        Map<State,Integer> bestG=new HashMap<>();
        open.add(new Node(start,null,null,0,0));
        bestG.put(start,0);
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
            for(Move mv:cur.state.successors()){
                int g2=cur.g+1;
                if(bestG.getOrDefault(mv.next,Integer.MAX_VALUE)>g2){
                    bestG.put(mv.next,g2);
                    open.add(new Node(mv.next,cur,mv,g2,0));
                }
            }
        }
        return new SolverResult(List.of(),nodes,System.currentTimeMillis()-t0);
    }
}
