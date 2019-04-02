import numpy
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
import pandas as pd

# Formate csv data into matrix
data = pd.read_csv('cardata.csv', ',').iloc[:, 2:13].values

#Centralize and normalize data
data_std = StandardScaler().fit_transform(data)

# Get covariance matrix from standardized data
cov = numpy.cov(data_std.T)

# Get eigenvectors and eigenvalues
eigen = numpy.linalg.eig(cov)
e_values = eigen[0]
e_vectors = eigen[1]

# Sort eigenvalues by descending value
e_values[::-1].sort()

# Get first to PCAs from some of standardized data
pca = PCA(n_components=2).fit_transform(data_std)

#Filter out data 
minivan_x = []
minivan_y = []
sedan_x = []
sedan_y = []
suv_x = []
suv_y = []
with open('cardata.csv', 'r') as file:
    next(file)
    line_num = 0
    for line in file:
        words = line.rstrip().split(',')
        if words[1] == 'minivan':
            minivan_x.append(pca[line_num][0])
            minivan_y.append(pca[line_num][0])
        elif words[1] == 'sedan':
            sedan_x.append(pca[line_num][0])
            sedan_y.append(pca[line_num][0])
        elif words[1] == 'suv':
            suv_x.append(pca[line_num][0])
            suv_y.append(pca[line_num][0])
        line_num += 1


plt.scatter(minivan_x, minivan_y, label='minivan', marker='^', linewidths=0, s=1)
plt.scatter(sedan_x, sedan_y, label='sedan', marker='^', linewidths=0, s=1)
plt.scatter(suv_x, suv_y, label='suv', marker='v', linewidths=0, s=1)
plt.xlabel('PCA1: Retail($)')
plt.ylabel('PCA2: Dealer($)')
plt.legend()
plt.savefig('pca.png')
