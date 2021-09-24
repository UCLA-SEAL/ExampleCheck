package edu.ucla.cs.utils;

import java.util.ArrayList;

public class SubsequenceCounter {
	ArrayList<String> seq;
	ArrayList<String> subseq;
	int[][] tbl;

	public SubsequenceCounter(ArrayList<String> seq, ArrayList<String> subseq) {
		this.seq = seq;
		this.subseq = subseq;
	}

	public int countMatches() {
		tbl = new int[seq.size() + 1][subseq.size() + 1];

		for (int row = 0; row < tbl.length; row++)
			for (int col = 0; col < tbl[row].length; col++)
				tbl[row][col] = countMatchesFor(row, col);

		return tbl[seq.size()][subseq.size()];
	}

	private int countMatchesFor(int seqDigitsLeft, int subseqDigitsLeft) {
		if (subseqDigitsLeft == 0)
			return 1;

		if (seqDigitsLeft == 0)
			return 0;

		String currSeqDigit = seq.get(seq.size() - seqDigitsLeft);
		String currSubseqDigit = subseq
				.get(subseq.size() - subseqDigitsLeft);

		int result = 0;

		if (currSeqDigit.equals(currSubseqDigit))
			result += tbl[seqDigitsLeft - 1][subseqDigitsLeft - 1];

		result += tbl[seqDigitsLeft - 1][subseqDigitsLeft];

		return result;
	}
}
