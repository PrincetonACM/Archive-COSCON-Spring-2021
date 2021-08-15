import java.io.*;
import java.util.*;
import java.lang.Math;

public class Solution {

    private static int N;
    private static int M;
    private static ArrayList< ArrayList<Integer> > graph;

    private static int s; private static int t;
    private static int[] dist; // record distance from 1 to this node (used to 
                               // generate level graph in Dinic's algorithm)
    private static int[] upTo; // records the edge we're currently looking at from a vertex
    private static int idd;

    private static ArrayList<Edge> E;

    // Represents a directed edge, and stores its capacity and flow
    private static class Edge {
        public int u; // from vertex
        public int v; // to vertex
        public int cap; // capacity
        public int flow; // flow

        public Edge(int iU, int iV, int iCap, int iFlow) {
            this.u = iU; this.v = iV;
            this.cap = iCap; this.flow = iFlow;
        }
    }

    // construct a level graph
    private static boolean BFS() {
        for (int i = 1; i <= N; i++) dist[i] = -1;

        Queue<Integer> q = new LinkedList<Integer>();
        q.add(s);
        dist[s] = 0;

        while (!q.isEmpty()) {
            int xt = q.remove();
            for (int i = 0; i < graph.get(xt).size(); i++)
            {
                int currID = graph.get(xt).get(i);
                int xt1 = E.get(currID).v;
                if (dist[xt1] == -1 && E.get(currID).flow < E.get(currID).cap)
                {
                    q.add(xt1);
                    dist[xt1] = dist[xt] + 1;
                }
            }
        }
    
        return (dist[t] != -1);
    }

    // find and process flow augmentations for a particular level graph
    private static int DFS(int xt, int minCap) {
        if (minCap == 0) return 0;
        if (xt == t) return minCap;
    
        while (upTo[xt] < graph.get(xt).size())
        {
            int currID = graph.get(xt).get(upTo[xt]);
            int xt1 = E.get(currID).v;
            if (dist[xt1] != dist[xt] + 1) // only push flow to higher levels along level graph
            {
                upTo[xt]++;
                continue;
            }
            int aug = DFS(xt1, Math.min(minCap, E.get(currID).cap - E.get(currID).flow));
            if (aug > 0)
            {
                E.get(currID).flow += aug;
                if (currID % 2 == 1) currID--; else currID++;
                E.get(currID).flow -= aug;
                return aug;
            }
            upTo[xt]++;
        }
    
        return 0;
    }

    private static int Dinic() {
        int flow = 0;
    
        while (true)
        {
            if (!BFS()) break; // run until no level graph can be constructed anymore
            for (int i = 1; i <= N; i++) upTo[i] = 0;

            while (true) {
                int currFlow = DFS(s, Integer.MAX_VALUE);
                if (currFlow == 0) break; // if no more flow to push, then we are done
                flow += currFlow;
            }
        }
    
        return flow;
    }

    // Helper function to add edges to our graphs with unit capacity in the forward direction
    // and zero capacity in the backward direction
    private static void addEdge(int u, int v) {
        Edge E1 = new Edge(u, v, 1, 0);
        Edge E2 = new Edge(v, u, 0, 0);
        
        graph.get(u).add(idd++); // construct forward edge
        E.add(E1);
        graph.get(v).add(idd++); // construct backward edge (with capacity 0)
        E.add(E2);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        N = Integer.parseInt(firstMultipleInput[0]);
        M = Integer.parseInt(firstMultipleInput[1]);

        // Initialize the data structures we're going to use
        final int MAX_SIZE = 2 * 100005;
        graph = new ArrayList< ArrayList<Integer> >(MAX_SIZE);
        for (int i = 0; i < MAX_SIZE; i++) graph.add(new ArrayList<Integer>());
        E = new ArrayList<Edge>(MAX_SIZE);
        dist = new int[MAX_SIZE];
        upTo = new int[MAX_SIZE];

        for (int i = 0; i < N; i++) graph.add(new ArrayList<Integer>());

        // transform the graph
        for (int i = 1; i <= N; i++){
            addEdge(2 * i - 1, 2 * i);
        }

        for (int i = 0; i < M; i++){
            String[] line = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            int a = Integer.parseInt(line[0]);
            int b = Integer.parseInt(line[1]);
            addEdge(2 * a, 2 * b - 1);
        }

        bufferedReader.close();

        // find flow from 2 to N-1
        N *= 2;
        s = 2; t = N - 1;

        System.out.println(Dinic());
    }
}
