#include <iostream>
#include <stdlib.h>
#include <math.h>
#include <vector>
#include <limits>
#include <queue>
#include <iomanip>
using namespace std;

// Represents a directed edge in the adjacency matrix
typedef pair<int, double> OutNeighbor;
typedef pair<double, int> HeapElement;

int main() {
    int N; int M;
    cin >> N >> M;

    vector< vector<OutNeighbor> > modified_interactome(2 * N);
    for (int i = 0; i < 2 * N; i ++) modified_interactome[i] = vector<OutNeighbor>();

    // Read in the input into an adjacency matrix representation of a graph. The idea
    // is to duplicate the graph and connect every vertex to its corresponding neighbors
    // in the other copy (and not to neighbors in its own copy)
    for (int i = 0; i < M; i++) {
        int v1; int v2; double weight;
        scanf("%d", &v1); scanf("%d", &v2); scanf("%lf", &weight);

        weight = -1 * log(1 - weight);

        OutNeighbor n1 = make_pair(v2 + N, weight); // From v1 to v2'
        OutNeighbor n2 = make_pair(v1, 0); // From v2' to v1
        modified_interactome[v1].push_back(n1);
        modified_interactome[v2 + N].push_back(n2);

        OutNeighbor n3 = make_pair(v2, 0); // From v1' to v2
        OutNeighbor n4 = make_pair(v1 + N, weight); // From v2 to v1'
        modified_interactome[v1 + N].push_back(n3);
        modified_interactome[v2].push_back(n4);
    }

    // Execute shortest paths on the graph we have made
    vector<double> dist(2 * N, numeric_limits<double>::infinity());
    dist[0] = 0;

    priority_queue< HeapElement, vector<HeapElement>, greater<HeapElement> > pq;
    pq.push(make_pair(0.0, 0));

    while (!pq.empty()) {
        int closest = pq.top().second;
        pq.pop();

        // If we have reached our destination
        if (closest == N - 1 || closest == 2 * N - 1) {
            cout << fixed << setprecision(7) << -1 * dist[closest] << endl;
            break;
        }

        // Edge-relaxing step
        for (OutNeighbor n : modified_interactome[closest]) {
            int v = n.first; 
            double weight = n.second;

            if (dist[closest] + weight < dist[v]) {
                dist[v] = dist[closest] + weight;
                pq.push(make_pair(dist[v], v));
            }
        }
    }

    return 0;
}
