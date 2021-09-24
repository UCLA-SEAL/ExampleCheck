package edu.ucla.cs.evaluate.sensitivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import edu.ucla.cs.utils.FileUtils;

public class Sample {
	ArrayList<String> seqs;
	
	public Sample(String path) {
		this.seqs = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
			String line;
			while((line = br.readLine()) != null) {
				if(line.contains("https:")) {
					this.seqs.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> sample(int num) {
		HashSet<Integer> seeds = generateRandomNumber(num, this.seqs.size());
		ArrayList<String> sample = new ArrayList<String>();
		for(int i : seeds) {
			sample.add(seqs.get(i));
		}
		
		return sample;
	}
	
	private HashSet<Integer> generateRandomNumber(int num, int size) {
		HashSet<Integer> res = new HashSet<Integer>();
		Random rand = new Random();
		int count = 0;
		while(count < num) {
			int n = rand.nextInt(size);
			if (res.contains(n)) {
				continue;
			} else {
				res.add(n);
				count ++;
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		String path = "/home/troy/research/BOA/Maple/example/HashMap.get/large-sequence.txt";
		Sample sam = new Sample(path);
		ArrayList<String> sample = sam.sample(10000);
		String output = path.substring(0, path.lastIndexOf(".")) + "-sample.txt";
		File f = new File(output);
		if(!f.exists()) {
			f.createNewFile();
		}
		FileUtils.writeArrayToFile(sample, output);
	}
}
