#include <bits/stdc++.h>
using namespace std;
int N, M, T;
vector <int> edge[100005];
int dp[100005]; // An array to help us when we do the DP step

// Implements the dynamic programming step of the algorithm: given a fixed 
// range, returns the maximum number of tables that Twelve can swipe swag from
int num(int range) {
    memset(dp, 0, sizeof(dp)); // Clear everything in the dp array

    dp[1] = min(range + 1, N);
    for (int i = 1; i <= N; i++) {
        if (dp[i] == 0) continue;
        for (int e : edge[i]) {
            int over = e + range - N;
            if (over < 0) over = 0;
            if (e - i > 2 * range + 1) {
                dp[e] = max(dp[e], dp[i] + 2 * range + 1 - over);
            } else {
                dp[e] = max(dp[e], dp[i] + e - i - over);
            }
        }
    }

    int best = 1;
    for (int i = 1; i <= N; i++) {
        best = max(best, dp[i]);
    }
    
    return best;
}

int main() {
    // Read in the input
    cin >> N >> M >> T;
    for (int i = 0; i < M; i++) {
        int a, b;
        scanf("%d %d", &a, &b);
        edge[a].push_back(b);
    }

    if (T <= 1) {
        printf("0\n");
        return 0;
    }

    // Binary search on the range of Twelve's arms, using the DP step
    // as a subroutine
    int l = 0, r = N;
    int range;
    while (l < r) {
        range = l + (r - l) / 2;
        if (num(range) < T) {
            l = range + 1;
        } 
        else {
            r = range;
        }
    }

    printf("%d\n", l);
    return 0;
}