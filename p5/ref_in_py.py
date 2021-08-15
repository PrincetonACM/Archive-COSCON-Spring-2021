import random
import numpy as np
import matplotlib.pyplot as plt

# TODO: Fill f and g out. There exists a simple answer for both of them!

def f(v, n):
    return (n % 2 == 0)

def g(k):
    return k * (k - 1)

# You don't need to change any code below this point.

def bern():
   n = 0
   u,v = 0.5,random.uniform(0,1)
   while (v >= u):
       u = v
       v = random.uniform(0,1)
       n = n + 1
   return f(v, n)

def amirite():
    flag2 = False
    while (not flag2):
        flag1 = False
        while (not flag1):
            k = 0
            while (bern()):
               k = k + 1

            m = g(k)
            flag1 = True
            for i in range(m):
               flag1 = flag1 and bern()

        x = random.uniform(0,1)
        flag2 = random.uniform(0,1) < np.exp(-0.5*x*(2*k+x))
        
    y = k + x
    if (random.uniform(0,1) > 0.5):
        return -1*y

    return y


# Here is some test code to help you out.
n = 100000
data = np.zeros(n)
for i in range(n):
    data[i] = amirite()

plt.hist(data, bins=np.arange(data.min(), data.max()+1, 0.1))
plt.show()

# Another way to check empirically how close your distribution is to N(0,1) 
# is to check the frequency of numbers in the range [a, b] and compare it 
# to the actual value, which is erf(b / sqrt(2)) - erf(a / sqrt(2)). Here 
# erf denotes the error function.