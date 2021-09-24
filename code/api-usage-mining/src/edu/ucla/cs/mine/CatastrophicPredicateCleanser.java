package edu.ucla.cs.mine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ucla.cs.utils.FileUtils;

public class CatastrophicPredicateCleanser extends ExtendedPredicateMiner{
	
	HashSet<String> troubledLines = new HashSet<String>();
	
	public CatastrophicPredicateCleanser(ArrayList<String> pattern,
			String raw_output, String seq) {
		super(pattern, raw_output, seq);
	}

	@Override
	protected void loadAndFilterPredicate() {
		// find API call sequences that follow the pattern
		SequencePatternVerifier pv = new SequencePatternVerifier(pattern);
		pv.verify(sequence_path);

		// read the full output of the traditional analysis
		File output = new File(path);
		try (BufferedReader br = new BufferedReader(new FileReader(output))) {
			String line;
			while ((line = br.readLine()) != null) {
				if(!line.startsWith("results[")) {
					continue;
				}

				final ExecutorService service = Executors.newSingleThreadExecutor();
				final String temp = line;
				try {
					final Future<Object> f = service.submit(() -> {
						processLine(pv, temp);
						return "yes";
					});

					f.get(2, TimeUnit.SECONDS);
				} catch (final TimeoutException e) {
					// catastrophic backtracking
					System.out.println("Regex runs more than 2 seconds! Ignore it!");
					System.out.println(line);
					troubledLines.add(line);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				} finally {
					service.shutdown();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// remove sequences in the raw output that will induce catastrophic backtracking
		FileUtils.removeLines(path, troubledLines);
	}
	
	public static void main(String[] args) {
		ArrayList<String> pattern = new ArrayList<String>();
		pattern.add("getBytes(String)");
		String path = "/media/troy/Disk2/Boa/apis/String.getBytes/1-clean.txt";
		String sequence_path = "/media/troy/Disk2/Boa/apis/String.getBytes/large-output-resolved.txt";
		CatastrophicPredicateCleanser pm = new CatastrophicPredicateCleanser(pattern,
				path, sequence_path);
		pm.loadAndFilterPredicate();
		System.exit(0);
	}
}
