#include <iostream>
#include <random>
#include <vector>

using namespace std;

// code to get a uniform random number; don't worry too much about this
random_device rd;
mt19937 gen(rd());
uniform_real_distribution<> dis(0.0, 1.0);

// TODO: Fill f and g out. There exists a simple answer for both of them!

bool f(double v, int n) { 
    return (n % 2 == 0);
}

int g(int k) { 
    return k * (k - 1);
}

// You don't need to change any code below this point.

bool bern()
{
    int n = 0;
    double u = 0.5;
    double v = dis(gen); // here v is uniformly distributed in [0, 1]

    while (v >= u)
    {
        u = v;
        v = dis(gen);
        n = n + 1;
    }
    return f(v, n);
}

int amirite()
{
    bool flag2 = false;
    int k;
    double x;
    while (not flag2)
    {
        bool flag1 = false;
        while (not flag1)
        {
            k = 0;
            while (bern())
                k++;

            int m = g(k);
            flag1 = true;
            for (int i = 0; i < m; i++)
                flag1 = flag1 && bern();
        }

        x = dis(gen); // here x is uniformly distributed in [0, 1]
        flag2 = dis(gen) < exp(-0.5 * x * (2 * k + x));
    }

    double y = k + x;
    if (dis(gen) > 0.5)
        return -1 * y;
    
    return y;
}

int main()
{
    // Here is some code to help you test your implementation. One way to
    // check empirically how close your distribution is to N(0,1) is to check
    // the frequency of numbers in the range [a, b] and compare it to the 
    // actual value, which is erf(b / sqrt(2)) - erf(a / sqrt(2)). Here erf
    // denotes the error function.
    int n = 100000;
    vector<int> data(n, 0);

    for (int i = 0; i < n; i++) {
        data[i] = amirite();
    }
}