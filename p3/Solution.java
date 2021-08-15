import java.util.*;
import java.io.*;
import java.lang.Math;

public class Solution {

    private static int N;
    private static int M;
    private static int T;
    private static ArrayList< ArrayList<Integer> > edge;
    private static int[] dp;

    // Implements the dynamic programming step of the algorithm: given a fixed 
    // range, returns the maximum number of tables that Twelve can swipe swag from
    private static int num(int range) {
        Arrays.fill(dp, 0); // Clear everything in the dp array

        dp[1] = Math.min(range + 1, N);
        for (int i = 1; i <= N; i++) {
            if (dp[i] == 0) continue;
            for (int e : edge.get(i)) {
                int over = e + range - N;
                if (over < 0) over = 0;
                if (e - i > 2 * range + 1) {
                    dp[e] = Math.max(dp[e], dp[i] + 2 * range + 1 - over);
                } else {
                    dp[e] = Math.max(dp[e], dp[i] + e - i - over);
                }
            }
        }

        int best = 1;
        for (int i = 1; i <= N; i++) {
            best = Math.max(best, dp[i]);
        }

        return best;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        N = Integer.parseInt(firstMultipleInput[0]);
        M = Integer.parseInt(firstMultipleInput[1]);
        T = Integer.parseInt(firstMultipleInput[2]);

        final int MAX_SIZE = 100005;
        dp = new int[MAX_SIZE];
        edge = new ArrayList<ArrayList<Integer>>(MAX_SIZE);
        for (int i = 0; i < MAX_SIZE; i++) edge.add(new ArrayList<Integer>());

        // Read in all the edges
        for (int i = 0; i < M; i++) {
            String[] line = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
            int a = Integer.parseInt(line[0]); int b = Integer.parseInt(line[1]);

            edge.get(a).add(b);
        }

        bufferedReader.close();

        if (T <= 1) {
            System.out.println(0); return;
        }

        // Binary search on the range of Twelve's arms, using the DP step
        // as a subroutine
        int l = 0; int r = N;
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

        System.out.println(l);
    }
}