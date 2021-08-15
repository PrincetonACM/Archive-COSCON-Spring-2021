#include <bits/stdc++.h>

using namespace std;

typedef pair <int, int> pii;

int N, M;
vector <int> forwardEdge[300005]; // adjacency list for edges
vector <int> mainPath; // stores vertices in the central path
bool vis[300005];

unordered_map <int, int> mainPathIdx; // key = vertex, value = central path index number
unordered_map <int, bool> cut; // key = vertex, value = whether or not this node cuts the graph into 2 pieces when removed
unordered_map <int, int> numIn; // key = vertex, value = number of innodes that reach it

vector <pii> ans;
int highest;

// First DFS pass
bool dfs(int v) {
    vis[v] = true;
    mainPath.push_back(v); // tentatively add v to the central path

    if (v == N) {
        return true;
    }

    for (int e : forwardEdge[v]) {
        if (!vis[e]) {
            if (dfs(e)) return true;
        }
    }

    mainPath.pop_back(); // none of its edges reach N, so remove this from the central path
    return false;
}

// Second DFS Pass
void searchForward(int v) {
    vis[v] = true;
    if (mainPathIdx.count(v)) {
        // update highest node reached along central path
        highest = max(highest, mainPathIdx[v]);
        // record the number of times a node along the central path is reached
        numIn[v] += 1;
        return;
    }
    for (int e: forwardEdge[v]) {
        if (!vis[e] || mainPathIdx.count(e)) {
            searchForward(e);
        }
    }
}

int main() {
    cin >> N >> M;

    // Read in our inputs
    for (int i = 0; i < M; i++) {
        int a, b;
        scanf("%d %d", &a, &b);
        forwardEdge[a].push_back(b);
    }

    int res = dfs(1);
    if (!res) { // if there is no path from 1 to N
        printf("%d\n", M);
        for (int i = 1; i <= N; i++) {
            for (int e: forwardEdge[i]) {
                printf("%d %d\n", i, e);
            }
        }
        return 0;
    }

    memset(vis, false, sizeof(vis));
    // order vertices along main path in the order they are reached from 1 to N
    for (int i = 0; i < mainPath.size(); i++) {
        mainPathIdx[mainPath[i]] = i;
        vis[mainPath[i]] = true;
        cut[mainPath[i]] = false;
    }

    highest = 0;
    // Second DFS. Go through all nodes in the main path 1 by 1, and run the DFS on them.
    // Make sure not to revisit already visited vertices.
    for (int i = 0; i < mainPath.size(); i++) {
        int v = mainPath[i];
        if (highest <= mainPathIdx[v]) {
            cut[v] = true; // this is a special node
        }
        if (i > 0) { // Check if mainPath[i-1] to mainPath[i] forms a special edge
            if (cut[mainPath[i - 1]] && cut[v] && numIn[v] == 1) {
                ans.push_back(make_pair(mainPath[i-1], v));
            }
        }
        highest = max(highest, mainPathIdx[v]);
        vis[v] = true;
        for (int e: forwardEdge[v]) {
            if (!vis[e] || mainPathIdx.count(e)) {
                searchForward(e);
            }
        }
    }

    // Sort results for output
    sort(ans.begin(), ans.end());

    printf("%d\n", ans.size());
    for (int i = 0; i < ans.size(); i++) {
        printf("%d %d\n", ans[i].first, ans[i].second);
    }
    return 0;
}