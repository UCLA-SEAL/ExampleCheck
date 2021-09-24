package edu.ucla.cs.postprocess;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ExtractSequencesForAndrew {
	public static void main(String[] args) throws IOException {
		final String rootDirPath = "/media/troy/Disk2/Boa/apis";
		final String outputDirPath = "/media/troy/Disk2/Boa/call-sequences";
		File rootDir = new File(rootDirPath);
		for(File apiDir : rootDir.listFiles()) {
			String api = apiDir.getName();
			File outputAPIDir = new File(outputDirPath + "/" + api);
			outputAPIDir.mkdir();
			String seqFilePath = apiDir.getAbsolutePath() + "/large-output-resolved.txt";
			File seqFile = new File(seqFilePath);
			String outputSeqFilePath = outputAPIDir.getAbsolutePath() + "/sequences.txt";
			File outputSeqFile = new File(outputSeqFilePath);
			FileUtils.copyFile(seqFile, outputSeqFile);
		}
	}
}
