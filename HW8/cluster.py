import numpy
import math
import csv

BOS = [42.4, 71.1]
NY = [41.7, 74.0]
DC = [38.9, 77.0]
MIA = [25.8, 80.2]
SLC = [40.8, 111.9]
SEA = [47.6, 122.3]
SF = [37.8, 122.4]
LA = [34.1, 118.2]
DEN = [39.7, 105.0]
ATL = [33.7, 84.3]

nodes = (BOS, NY, DC, MIA, SLC, SEA, SF, LA, DEN, ATL)
distances = [[None for x in range(len(nodes))] for y in range(len(nodes))]
for i in range(len(nodes)):
    for j in range(i, len(nodes)):
        node1 = nodes[i]
        node2 = nodes[j]
        distances[i][j] = round(
            math.sqrt(math.pow(node1[0]-node2[0], 2) + math.pow(node1[1]-node2[1], 2)), 2)

        # print(i, ' ', j, ' ', math.sqrt(
        #     math.pow(node1[0]-node2[0], 2) + math.pow(node1[1]-node2[1], 2)))
print(distances)
with open("distances.csv", "w+") as file:
    csvWriter = csv.writer(file, delimiter=',')
    csvWriter.writerows(distances)
