import numpy

# Formate csv data into 2d array
cardata = []
with open('cardata.csv', 'r') as file:
	for line in file:
		cardata.append(line.rstrip().split(',')[3:])
cardata.pop(0)

cardata_array = numpy.asarray(cardata)
print(cardata_array)
# print(cardata)
# print(singular_cardata)

# print(numpy.linalg.eigh(cardata))
print(numpy.matmul(cardata_array, cardata_array.transpose()))