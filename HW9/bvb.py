import pandas as pd
import numpy as np
import csv

body = []
brain = []

with open('data.csv', 'r') as file:
    reader = csv.reader(file)
    next(reader)
    for row in reader:
        body.append(float(row[0]))
        brain.append(float(row[1]))


print(np.mean(body), ' ', np.std(body))
#print(brain)
print(np.mean(brain), ' ', np.std(brain))
