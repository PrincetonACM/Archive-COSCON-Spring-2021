def solution():
    s = input()
    n = len(s) # number of students

    # The case where there is an odd number of students
    if n % 2 == 1:
        print(-1)
        return

    numFresh = 0;     # number of freshman
    numFreshCurr = 0; # number of freshmen in the first half

    for i in range(n):
        inc = (s[i] == 'F')
        numFresh += inc
        if (i < n // 2):
            numFreshCurr += inc

    # If there are an odd number of freshmen, the task can't be done
    if numFresh % 2 == 1:
        print(-1)
        return 

    first = -1; sec = n // 2 - 1 # Point to where the first and second cut are
    while sec < n:
        # If the number of freshman in our sliding window is exactly half of them,
        # we've found the solution
        if (numFreshCurr == numFresh // 2):
            print("0 T")
            if first != -1:
                print(first + 1, "C")
                print(sec + 1, "T")
            else:
                print(sec + 1, "C")
            return

        # Otherwise, move the sliding window one unit to the right and update the
        # number of freshman currently in it
        numFreshCurr -= (s[first + 1] == 'F')
        numFreshCurr += (s[sec + 1] == 'F')
        first += 1
        sec += 1

    # If we've reached this point, then the task cannot be done
    print(-1)
    return

if __name__ == '__main__':
    solution()