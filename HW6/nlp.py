import collections
import os

# Tally occurrences of words in a file
ctr = collections.Counter()
for filename in os.listdir('news'):
    with open('news/' + filename, 'r') as file:
        for word in file.read().split():
            ctr[word] += 1

numWords = sum(ctr.values())
print(numWords)
print(ctr.most_common(20))
