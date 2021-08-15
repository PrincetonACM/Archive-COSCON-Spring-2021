#include <bits/stdc++.h>
using namespace std;
#define INF 0x3f3f3f3f

vector<int> graph[2 * 100005];
struct Edge {
    int u, v, cap;
    int flow;
};
vector<Edge> E;

int N, M;
int s, t;
int dist[2 * 100005]; // record distance from 1 to this node (used to generate level graph in Dinic's algorithm)
int upTo[2 * 100005]; // records the edge we're currently looking at from a vertex

int idd = 0;

// construct a level graph
bool BFS() {
    for (int i = 1; i <= N; i++) dist[i] = -1;

    queue<int> q;
    q.push(s);
    dist[s] = 0;

    while (!q.empty()) {
        int xt = q.front();
        q.pop();
        for (int i = 0; i < graph[xt].size(); i++)
        {
            int currID = graph[xt][i];
            int xt1 = E[currID].v;
            if (dist[xt1] == -1 && E[currID].flow < E[currID].cap)
            {
                q.push(xt1);
                dist[xt1] = dist[xt] + 1;
            }
        }
    }

    return (dist[t] != -1);
}

// find and process flow augmentations for a particular level graph
int DFS(int xt, int minCap) {
    if (minCap == 0) return 0;
    if (xt == t) return minCap;

    while (upTo[xt] < graph[xt].size())
    {
        int currID = graph[xt][upTo[xt]];
        int xt1 = E[currID].v;
        if (dist[xt1] != dist[xt] + 1) // only push flow to higher levels along level graph
        {
            upTo[xt]++;
            continue;
        }
        int aug = DFS(xt1, min(minCap, E[currID].cap - E[currID].flow));
        if (aug > 0)
        {
            E[currID].flow += aug;
            if (currID & 1) currID--; else currID++;
            E[currID].flow -= aug;
            return aug;
        }
        upTo[xt]++;
    }

    return 0;
}

int Dinic() {
    int flow = 0;

    while (true)
    {
        if (!BFS()) break; // run until no level graph can be constructed anymore
        for (int i = 1; i <= N; i++) upTo[i] = 0;
        while (true) {
            int currFlow = DFS(s, INF);
            if (currFlow == 0) break; // if no more flow to push, then we are done
            flow += currFlow;
        }
    }

    return flow;
}

void addEdge(int u, int v, int cap = 1) {
    Edge E1, E2;
    
    E1.u = u, E1.v = v, E1.cap = cap, E1.flow = 0;
    E2.u = v, E2.v = u, E2.cap = 0, E2.flow = 0;
    
    graph[u].push_back(idd++); // construct forward edge
    E.push_back(E1);
    graph[v].push_back(idd++); // construct backward edge (with capacity 0)
    E.push_back(E2);
}

int main() {
    cin >> N >> M;

    // transform the graph
    for (int i = 1; i <= N; i++){
        addEdge(2 * i - 1, 2 * i);
    }
    
    for (int i = 0; i < M; i++){
        int a, b;
        scanf("%d %d", &a, &b);
        addEdge(2 * a, 2 * b - 1);
    }

    // find flow from 2 to N-1
    N *= 2;
    s = 2, t = N - 1;
    
    printf("%d\n", Dinic());
    
    return 0;
}