package edu.ucla.cs.postprocess;

import org.junit.Test;

import edu.ucla.cs.utils.FileUtils;

public class ConstructURLTest {
	@Test
	public void testCustomizeBoaURLScript() {
		String sample = "/home/troy/research/BOA/Maple/res/test/example-sample.txt";
		ConstructURL constructor = new ConstructURL(sample);
		constructor.load();
		String script = constructor.customize();
		System.out.println(script);
	}
	
	@Test
	public void testSubmitBoaJob() {
		String sample = "/home/troy/research/BOA/Maple/res/test/example-sample.txt";
		ConstructURL constructor = new ConstructURL(sample);
		constructor.load();
		String script = constructor.customize();
		String output = constructor.run(script);
		System.out.println(output);
	}
	
	@Test
	public void testUpdateExampleFile() {
		String sample = "/home/troy/research/BOA/Maple/res/test/example-sample.txt";
		ConstructURL constructor = new ConstructURL(sample);
		constructor.load();
		String output_file = "/home/troy/research/BOA/Maple/boa/output/output-58709.txt";
		String output = FileUtils.readFileToString(output_file);
		constructor.update(output);
	}
}
