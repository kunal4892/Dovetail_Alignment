Class Project for Bio Informatics- *CAP5510*

* This assignment focuses on implementing a DP solution for calculating similarity scores
  for very large character sequences that provides insights on phylogenetic similarity.
* The more the similarity score, better the probability of being related. 
* The sequences are in the order of a few thousand characters.

# Dovetail Alignment
* Generate Nucleotide Sequences randomly.
* Mutate the generated sequences by a given probability.
* Align the mutated strings using the dovetail alignment and combine strings
with the highest alignment score until just one string remain or the best alignment
score within the remaining strings is negative.
