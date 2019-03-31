"""HW6"""
import collections
import os
import matplotlib.pyplot as plt

# Tally occurrences of words in a file
ctr = collections.Counter()
for filename in os.listdir('news'):
    with open('news/' + filename, 'r') as file:
        for word in file.read().split():
            ctr[word] += 1

NUMWORDS = sum(ctr.values())
print(NUMWORDS)
print(ctr.most_common(20))

rc_data = []
ordered_keys = []
i = 0
for word in ctr.most_common():
    ordered_keys.append(word[0])
    rc_data.append(word[1])
    i += 1

# RC plot
# Shows that there are only a few words that are used many times,
# the majority of words is only used a few times.
fig, ax = plt.subplots()
ax.plot(ordered_keys, rc_data)
ax.get_xaxis().set_visible(False)
plt.savefig('rc.png')

# LogR LogC plot
# Shows that the ratio between rank and count is, for the most part,
# the same across all words
fig, ax = plt.subplots()
ax.set_yscale('log')
ax.set_xscale('log')
ax.plot(ordered_keys, rc_data)
ax.get_xaxis().set_visible(False)
plt.savefig('logrlogc.png')
