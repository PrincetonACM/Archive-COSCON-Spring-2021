import java.util.Random;
import java.lang.Math;

public class Ref_In_Java {
    private static Random rng; // Our Random Number Generator

    private static boolean f(double v, int n) { 
        return (n % 2 == 0);
    }
    
    private static int g(int k) { 
        return k * (k - 1);
    }

    // You don't need to edit any code past this point

    private static boolean bern() {
        int n = 0;
        double u = 0.5;
        double v = rng.nextDouble(); // here v is uniformly distributed in [0, 1]

        while (v >= u)
        {
            u = v;
            v = rng.nextDouble();
            n = n + 1;
        }
        return f(v, n);
    }

    private static double amirite() {
        boolean flag2 = false;
        int k = 0; double x = 0.0;
        while (!flag2)
        {
            boolean flag1 = false;
            while (!flag1)
            {
                k = 0;
                while (bern())
                    k++;

                int m = g(k);
                flag1 = true;
                for (int i = 0; i < m; i++)
                    flag1 = flag1 && bern();
            }

            x = rng.nextDouble(); // here x is uniformly distributed in [0, 1]
            flag2 = rng.nextDouble() < Math.exp(-0.5 * x * (2 * k + x));
        }

        double y = k + x;
        if (rng.nextDouble() > 0.5)
            return -1 * y;

        return y;
    }

    public static void main(String[] args) {

        rng = new Random(); // Initialize the random number generator

        // Here is some code to help you test your implementation. One way to
        // check empirically how close your distribution is to N(0,1) is to check
        // the frequency of numbers in the range [a, b] and compare it to the 
        // actual value, which is erf(b / sqrt(2)) - erf(a / sqrt(2)). Here erf
        // denotes the error function.
        int n = 1000000;
        double[] data = new double[n];

        for (int i = 0; i < n; i++) {
            data[i] = amirite();
        }
    }
}