from pymining import itemmining
from pymining import seqmining
import sys

if(len(sys.argv) != 3):
	print("Please provide the data file and the minimum support as input, e.g., python freq_itemset.py ./output.txt 40")
	sys.exit(-1)

f = open(sys.argv[1], 'r')
lines = f.read().splitlines()
seqs = []
for s in lines:
	seq = s.split("---")[1]
	seq = seq[1:-1]
	seqs.append(seq.split(", "))
	
relim_input = itemmining.get_relim_input(seqs)
report = itemmining.relim(relim_input, min_support=int(sys.argv[2]))
for p in report:
	print(p, report[p])
