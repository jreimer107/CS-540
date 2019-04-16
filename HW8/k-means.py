import math


def euclidian_distance(node1, node2):
    return round(
        math.sqrt(math.pow(node1.x-node2.x, 2) + math.pow(node1.y-node2.y, 2)), 2)


class node:
    def __init__(self, name, x, y):
        self.name = name
        self.x = x
        self.y = y

    def __str__(self):
        return self.name + ": " + str(self.x) + "," + str(self.y)

    def distance(self, other):
        return euclidian_distance(self, other)


# initialize nodes
BOS = node("BOS", 42.4, 71.1)
NY = node("NY", 41.7, 74.0)
DC = node("DC", 38.9, 77.0)
MIA = node("MIA", 25.8, 80.2)
SLC = node("SLC", 40.8, 111.9)
SEA = node("SEA", 47.6, 122.3)
SF = node("SF", 37.8, 122.4)
LA = node("LA", 34.1, 118.2)
DEN = node("DEN", 39.7, 105.0)
ATL = node("ATL", 33.7, 84.3)
nodes = [BOS, NY, DC, MIA, SLC, SEA, SF, LA, DEN, ATL]

# initialize centers
c1 = node("C1", 50, 90)
c2 = node("C2", 30, 100)

# compute clusters
c1_cluster = []
c2_cluster = []

for node in nodes:
    c1_dist = c1.distance(node)
    c2_dist = c2.distance(node)
    if c1_dist < c2_dist:
        c1_cluster.append(node)
    else:
        c2_cluster.append(node)

print("c1")
for node in c1_cluster:
    print(node)

print("\nc2")
for node in c2_cluster:
    print(node)

# Get new centers
cx = 0
cy = 0
for node in c1_cluster:
    cx += node.x
    cy += node.y

c1.x = round(cx / float(len(c1_cluster)), 2)
c1.y = round(cy / float(len(c1_cluster)), 2)

cx = 0
cy = 0
for node in c2_cluster:
    cx += node.x
    cy += node.y

c2.x = round(cx / float(len(c2_cluster)), 2)
c2.y = round(cy / float(len(c2_cluster)), 2)
print(c1, c2)

# recompute clusters
c1_cluster = []
c2_cluster = []

for node in nodes:
    c1_dist = c1.distance(node)
    c2_dist = c2.distance(node)
    if c1_dist < c2_dist:
        c1_cluster.append(node)
    else:
        c2_cluster.append(node)

print("c1")
for node in c1_cluster:
    print(node)

print("\nc2")
for node in c2_cluster:
    print(node)
