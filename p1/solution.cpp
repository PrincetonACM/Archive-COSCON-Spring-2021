#include <iostream>
using namespace std;

int main()
{
    string s;
    cin >> s;
    int n = s.size(); // number of students

    // The case where there is an odd number of students
    if (n % 2 == 1)
    { 
        cout << "-1";
        return 0;
    }

    int numFresh = 0;     // number of freshman
    int numFreshCurr = 0; // number of freshmen in the first half

    for (int i = 0; i < n; i++)
    {
        int inc = (s[i] == 'F');
        numFresh += inc;
        if (i < n / 2)
            numFreshCurr += inc;
    }

    // If there are an odd number of freshmen, the task can't be done
    if (numFresh % 2 == 1)
    {
        cout << "-1";
        return 0;
    }

    int first = -1, sec = n / 2 - 1; // Point to where the first and second cut are
    while (sec < n)
    {
        // If the number of freshman in our sliding window is exactly half of them,
        // we've found the solution
        if (numFreshCurr == numFresh / 2)
        {
            cout << "0 T" << endl;
            if (first != -1)
            {
                cout << first + 1 << " C" << endl;
                cout << sec + 1 << " T" << endl;
            }
            else
                cout << sec + 1 << " C" << endl;
            return 0;
        }

        // Otherwise, move the sliding window one unit to the right and update the
        // number of freshman currently in it
        numFreshCurr -= (s[first + 1] == 'F');
        numFreshCurr += (s[sec + 1] == 'F');
        first++;
        sec++;
    }

    // If we've reached this point, then the task cannot be done
    cout << "-1";
    return 0;
}
