from math import log
from collections import defaultdict
import heapq
import sys

def solution():
	first_multiple_input = input().rstrip().split()
	N = int(first_multiple_input[0])
	M = int(first_multiple_input[1])

	# Adjacency-list representation of the interactome we will be reading
	modified_interactome = [[] for i in range(2 * N)] 
	for i in range(M):
		line = sys.stdin.readline().rstrip().split()
		v1 = int(line[0]); v2 = int(line[1])
		w = -1 * log(1 - float(line[2]))

		modified_interactome[v1].append((v2 + N, w))
		modified_interactome[v1 + N].append((v2, 0))
		modified_interactome[v2].append((v1 + N, w))
		modified_interactome[v2 + N].append((v1, 0))

	dist = defaultdict(lambda: float('inf'))
	dist[0] = 0
	pq = [(0.0, 0)]
	heapq.heapify(pq)

	while len(pq) > 0:
		closest = heapq.heappop(pq)[1]

		# If we have reached our destination
		if (closest == N - 1) or (closest == 2 * N - 1):
			print("{:.7f}".format(-1 * dist[closest]))
			break

		# Edge-relaxing step
		for neighbor in modified_interactome[closest]:
			v = neighbor[0] 
			weight = neighbor[1]

			if (dist[closest] + weight < dist[v]):
				dist[v] = dist[closest] + weight
				heapq.heappush(pq, (dist[v], v))

if __name__ == "__main__":
	solution()
