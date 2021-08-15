import java.io.*;
import java.util.*;
import java.lang.Math;

public class Solution {

    private static int N;
    private static int M;
    private static ArrayList< ArrayList<Integer> > forwardEdge; // adjacency list for edges
    private static ArrayList<Integer> mainPath; // stores vertices in the central path

    private static boolean[] vis;
    private static Stack<Integer> dfsStack;
    private static int[] parents;

    private static HashMap<Integer, Integer> mainPathIdx; // key = vertex, value = central path index number
    private static HashMap<Integer, Boolean> cut; // key = vertex, value = whether or not this node cuts the graph into 2 pieces when removed
    private static HashMap<Integer, Integer> numIn; // key = vertex, value = number of innodes that reach it
    private static ArrayList<Pair> ans;

    private static int highest;

    // A simple class to represent a pair of objects
    private static class Pair implements Comparable {
        public int first;
        public int second;

        public Pair(int f, int s) {
            this.first = f; this.second = s;
        }

        public int compareTo(Object that) {
            Pair other = (Pair) that;

            int compareFirst = this.first - other.first;
            if (compareFirst != 0) {
                return compareFirst;
            }
            else return (this.second - other.second);
        }
    }

    // First DFS pass
    // We use a stack based implementation because a recursive approaches generate a stack overflow
    private static boolean DFS(int v) {
        dfsStack = new Stack<Integer>();
        dfsStack.push(v);
        boolean found = false;

        while (!dfsStack.empty()) {
            int current = dfsStack.pop();
            vis[current] = true;

            if (current == N) {
                found = true; break;
            }

            for (int e : forwardEdge.get(current)) {
                if (!vis[e]) {
                    parents[e] = current;
                    dfsStack.push(e);
                }
            }
        }

        // if there is a path from 1 to N, then we generate it by following parent pointers
        if (found) {
            int index = N;
            mainPath.add(index);

            while (index != 1) {
                mainPath.add(parents[index]);
                index = parents[index];
            }

            Collections.reverse(mainPath);
            return true;
        }

        return false;
    }

    // Second DFS Pass
    private static void searchForward(int v) {
        vis[v] = true;
        if (mainPathIdx.containsKey(v)) {
            // update highest node reached along central path
            highest = Math.max(highest, mainPathIdx.get(v));
            // record the number of times a node along the central path is reached
            numIn.put(v, numIn.containsKey(v) ? numIn.get(v) + 1 : 1);
            return;
        }
        for (int e : forwardEdge.get(v)) {
            if (!vis[e] || mainPathIdx.containsKey(e)) {
                searchForward(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        N = Integer.parseInt(firstMultipleInput[0]);
        M = Integer.parseInt(firstMultipleInput[1]);

        // Initialize the data structures we're going to use
        final int MAX_SIZE = 300005;
        forwardEdge = new ArrayList< ArrayList<Integer> >(MAX_SIZE);
        mainPath = new ArrayList<Integer>();
        for (int i = 0; i < MAX_SIZE; i++) forwardEdge.add(new ArrayList<Integer>());

        vis = new boolean[MAX_SIZE];

        mainPathIdx = new HashMap<Integer, Integer>();
        cut = new HashMap<Integer, Boolean>();
        numIn = new HashMap<Integer, Integer>();
        ans = new ArrayList<Pair>();

        for (int i = 0; i < M; i++){
            String[] line = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            int a = Integer.parseInt(line[0]);
            int b = Integer.parseInt(line[1]);
            forwardEdge.get(a).add(b);
        }

        bufferedReader.close();

        // Check if vertex N is reachable from vertex 1
        parents = new int[MAX_SIZE];
        boolean res = DFS(1);
        if (!res) { // if there is no path from 1 to N
            System.out.println(M);
            for (int i = 1; i <= N; i++) {
                for (int e : forwardEdge.get(i)) {
                    System.out.println(i + " " + e);
                }
            }
            return;
        }

        Arrays.fill(vis, false);
        // order vertices along main path in the order they are reached from 1 to N
        for (int i = 0; i < mainPath.size(); i++) {
            int index = mainPath.get(i);

            mainPathIdx.put(index, i);
            vis[index] = true;
            cut.put(index, false);
        }

        highest = 0;
        // Second DFS. Go through all nodes in the main path 1 by 1, and run the DFS on them.
        // Make sure not to revisit already visited vertices.
        for (int i = 0; i < mainPath.size(); i++) {
            int v = mainPath.get(i);

            if (highest <= mainPathIdx.get(v)) {
                cut.put(v, true); // this is a special node
            }

            if (i > 0) {
                // Check if mainPath[i-1] to mainPath[i] forms a special edge
                if (cut.get(mainPath.get(i - 1)) && cut.get(v) && (numIn.get(v) == 1)) {
                    ans.add(new Pair(mainPath.get(i - 1), v));
                }
            }

            highest = Math.max(highest, mainPathIdx.get(v));
            vis[v] = true;
            for (int e: forwardEdge.get(v)) {
                if (!vis[e] || mainPathIdx.containsKey(e)) {
                    searchForward(e);
                }
            }
        }
        
        // Sort results for output
        Collections.sort(ans);

        System.out.println(ans.size());
        for (int i = 0; i < ans.size(); i++) {
            System.out.println(ans.get(i).first + " " + ans.get(i).second);
        }
    }
}
