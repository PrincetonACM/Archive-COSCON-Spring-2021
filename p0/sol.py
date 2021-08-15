import math
import os
import random
import re
import sys

if __name__ == '__main__':
    n = int(input().strip())

    a = list(map(int, input().rstrip().split()))

    sum = 0
    for element in a: sum += element if (element % 2) == 0 else 0

    print(sum)
