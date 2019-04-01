"""HW6"""
import collections
import os
import matplotlib.pyplot as plt
import math


class CounterSource:
    def __init__(self, source):
        self.count = 0
        self.sources = [source]
        self.numSources = 1

    def __lt__(self, other):
        return self.count < other.count

    def __gt__(self, other):
        return self.count > other.count

    def __eq__(self, other):
        return self.count == other.count

    def __ne__(self, other):
        return self.count != other.count

    def add(self, source):
        self.count += 1
        if source not in self.sources:
            self.sources.append(source)
            self.numSources += 1


def count_all_words():
    ctr = {}
    for filename in os.listdir('news'):
        with open('news/' + filename, 'r') as file:
            for word in file.read().split():
                if word in ctr.keys():
                    ctr[word].add(filename)
                else:
                    ctr[word] = CounterSource(filename)
    print("Counted all words.")
    return ctr


def rc():
    """RC"""
    # Tally occurrences of words in a file
    ctr = collections.Counter()
    for filename in os.listdir('news'):
        with open('news/' + filename, 'r') as file:
            for word in file.read().split():
                ctr[word] += 1

    print(sum(ctr.values()))
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


def analyze_file(filename, all_words):
    """TF_IDF"""
    bow_ctr = collections.Counter()
    tf_idf_ctr = collections.Counter()
    target_count = 0
    doc_word_count = 0
    with open('news/' + filename) as file:
        # initialize counters
        for word in all_words.keys():
            bow_ctr[word] = 0
        print("Initialized BoW counters.")

        # compute word counts
        for word in file.read().split():
            bow_ctr[word] += 1
            doc_word_count += 1
        print("Completed BoW counting.")

        # convert to if-idf format
        for word in all_words.keys():
            tf = bow_ctr[word] / float(doc_word_count)
            idf = math.log(511 / float(all_words[word].numSources))
            tf_idf_ctr[word] = tf * idf
        print("Completed tf-idf computation.")

    return bow_ctr, tf_idf_ctr


def vector_compare():
    all_ctr = count_all_words()
    v1_bow, v1_tf_idf = analyze_file('098.txt', all_ctr)
    print(v1_bow.most_common(10))
    print(v1_tf_idf.most_common(10))
    # v2_bow, v2_tf_idf = analyze_file('297.txt', list(count_all_words().keys()))


def main():
    # rc()
    # tf_idf()
    vector_compare()


if __name__ == '__main__':
    main()
