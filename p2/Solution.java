import java.util.PriorityQueue;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;

public class Solution {
    private static class OutNeighbor {
        public int neighbor;
        public double weight;

        public OutNeighbor(int n, double w) {
            this.neighbor = n;
            this.weight = w;
        }
    }

    private static class WeightedDigraph {
        public ArrayList<ArrayList<OutNeighbor>> adjacencyList;

        public WeightedDigraph(int N) {
            this.adjacencyList = new ArrayList<ArrayList<OutNeighbor>>(N);

            for (int i = 0; i < N; i++) {
                adjacencyList.add(new ArrayList<OutNeighbor>());
            }
        }

        public void addEdge(int source, int dst, double weight) {
            OutNeighbor n = new OutNeighbor(dst, weight);
            adjacencyList.get(source).add(n); //for directed graph
        }
    }

    private static class QueueElement implements Comparable {
        public double weight;
        public int v;

        public QueueElement(double w, int u) {
            this.weight = w;
            this.v = u;
        }

        public int compareTo(Object other) {
            QueueElement otherElement = (QueueElement) other;
            return (this.weight <= otherElement.weight) ? -1 : 1;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] inputs = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int N = Integer.parseInt(inputs[0]);
        int M = Integer.parseInt(inputs[1]);

        WeightedDigraph modified_interactome = new WeightedDigraph(2 * N);

        // Read in the input into an adjacency matrix representation of a graph. The idea
        // is to duplicate the graph and connect every vertex to its corresponding neighbors
        // in the other copy (and not to neighbors in its own copy)
        for (int i = 0; i < M; i++) {
            int v1; int v2; double weight;
            String[] nums = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            v1 = Integer.parseInt(nums[0]);
            v2 = Integer.parseInt(nums[1]);
            weight = Double.parseDouble(nums[2]);

            weight = -1 * Math.log(1 - weight);

            modified_interactome.addEdge(v1, v2 + N, weight);
            modified_interactome.addEdge(v1 + N, v2, 0);
            modified_interactome.addEdge(v2, v1 + N, weight);
            modified_interactome.addEdge(v2 + N, v1, 0);
        }

        // Execute shortest paths on the graph we have made
        double[] dist = new double[2 * N];
        for (int i = 0; i < dist.length; i++) dist[i] = Double.POSITIVE_INFINITY;
        dist[0] = 0;

        PriorityQueue<QueueElement> pq = new PriorityQueue<QueueElement>();
        pq.add(new QueueElement(0.0, 0));

        while (!pq.isEmpty()) {
            QueueElement head = pq.peek();
            pq.remove(head);
            int closest = head.v;

            // If we have reached our destination
            if (closest == N - 1 || closest == 2 * N - 1) {
                System.out.printf("%.7f\n", -1 * dist[closest]);
                break;
            }

            // Edge-relaxing step
            for (OutNeighbor n : modified_interactome.adjacencyList.get(closest)) {
                int v = n.neighbor; 
                double weight = n.weight;

                if (dist[closest] + weight < dist[v]) {
                    dist[v] = dist[closest] + weight;
                    pq.add(new QueueElement(dist[v], v));
                }
            }
        }
    }
}
