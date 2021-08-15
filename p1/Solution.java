import java.io.*;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferedReader.readLine();
        bufferedReader.close();

        int n = s.length(); // number of students

        // The case where there is an odd number of students
        if (n % 2 == 1)
        { 
            System.out.println("-1");
            return;
        }

        int numFresh = 0;     // number of freshman
        int numFreshCurr = 0; // number of freshmen in the first half

        for (int i = 0; i < n; i++)
        {
            int inc = (s.charAt(i) == 'F') ? 1 : 0;
            numFresh += inc;
            if (i < n / 2)
                numFreshCurr += inc;
        }

        // If there are an odd number of freshmen, the task can't be done
        if (numFresh % 2 == 1)
        {
            System.out.println("-1");
            return;
        }

        int first = -1, sec = n / 2 - 1; // Point to where the first and second cut are
        while (sec < n)
        {
            // If the number of freshman in our sliding window is exactly half of them,
            // we've found the solution
            if (numFreshCurr == numFresh / 2)
            {
                System.out.println("0 T");
                if (first != -1)
                {
                    System.out.print(first + 1);
                    System.out.println(" C");

                    System.out.print(sec + 1);
                    System.out.println(" T");
                }
                else {
                    System.out.print(sec + 1);
                    System.out.println(" C");
                }
                return;
            }

            // Otherwise, move the sliding window one unit to the right and update the
            // number of freshman currently in it
            numFreshCurr -= (s.charAt(first + 1) == 'F') ? 1 : 0;
            numFreshCurr += (s.charAt(sec + 1) == 'F') ? 1 : 0;
            first++;
            sec++;
        }

        // If we've reached this point, then the task cannot be done
        System.out.println("-1");
        return;
    }
}