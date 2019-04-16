import numpy
import math
import csv


def euclidian_distance(node1, node2):
    return round(
        math.sqrt(math.pow(node1.x-node2.x, 2) + math.pow(node1.y-node2.y, 2)), 2)


class node:
    def __init__(self, name, x, y):
        self.name = name
        self.x = x
        self.y = y

    def __str__(self):
        return self.name

    def distance(self, other):
        return euclidian_distance(self, other)


class cluster:
    def __init__(self, nodes):
        self.nodes = nodes

    def add_node(self, node):
        self.nodes.append(node)

    def distance(self, other):
        distances = []
        for s_node in self.nodes:
            for o_node in other.nodes:
                distances.append(s_node.distance(o_node))

        return min(distances)

    def join(self, other):
        for node in other.nodes:
            self.add_node(node)

    def __str__(self):
        retstr = ""
        for node in self.nodes:
            retstr += node.__str__() + " "
        return retstr


BOS = cluster([node("BOS", 42.4, 71.1)])
NY = cluster([node("NY", 41.7, 74.0)])
DC = cluster([node("DC", 38.9, 77.0)])
MIA = cluster([node("MIA", 25.8, 80.2)])
SLC = cluster([node("SLC", 40.8, 111.9)])
SEA = cluster([node("SEA", 47.6, 122.3)])
SF = cluster([node("SF", 37.8, 122.4)])
LA = cluster([node("LA", 34.1, 118.2)])
DEN = cluster([node("DEN", 39.7, 105.0)])
ATL = cluster([node("ATL", 33.7, 84.3)])

clusters = [BOS, NY, DC, MIA, SLC, SEA, SF, LA, DEN, ATL]
distances = [[None for x in range(len(clusters))]
             for y in range(len(clusters))]
cities_wanted = 3

while len(clusters) > cities_wanted:
    min_dist = math.inf
    min_i = None
    min_j = None
    for i in range(len(clusters)):
        for j in range(i+1, len(clusters)):
            cluster1 = clusters[i]
            cluster2 = clusters[j]
            distance = cluster1.distance(cluster2)
            if distance < min_dist:
                min_dist = distance
                min_i = i
                min_j = j

    print("Combining ", clusters[min_i], "and", clusters[min_j])
    clusters[min_i].join(clusters[min_j])
    del clusters[min_j]

for cluster in clusters:
    print(cluster)

# for i in range(len(nodes)):
#     for j in range(i, len(nodes)):
#         node1 = nodes[i]
#         node2 = nodes[j]
#         distances[i][j] = round(
#             math.sqrt(math.pow(node1[0]-node2[0], 2) + math.pow(node1[1]-node2[1], 2)), 2)

# print(i, ' ', j, ' ', math.sqrt(
#     math.pow(node1[0]-node2[0], 2) + math.pow(node1[1]-node2[1], 2)))
# print(distances)
# with open("distances.csv", "w+") as file:
#     csvWriter = csv.writer(file, delimiter=',')
#     csvWriter.writerows(distances)
