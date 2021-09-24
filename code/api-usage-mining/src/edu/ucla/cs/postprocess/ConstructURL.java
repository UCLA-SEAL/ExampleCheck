package edu.ucla.cs.postprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.iastate.cs.boa.BoaException;
import edu.ucla.cs.client.MapleClient;
import edu.ucla.cs.utils.FileUtils;

public class ConstructURL {
	String path;
	ArrayList<AbstractedExample> examples;

	final String template_path = "/home/troy/SO-Empirical-Study/code/Maple/boa/template/url.boa";
	final String output_dir = "/home/troy/SO-Empirical-Study/code/Maple/boa/output";

	public ConstructURL(String path) {
		this.path = path;
		this.examples = new ArrayList<AbstractedExample>();
	}

	public void construct() {
		// load the code examples in the file
		load();

		// customize the template Boa script to query about the given examples
		String script = customize();

		String output = null;
		try {
			// run
			output = run(script);
		} catch (Exception e) {
			if (e instanceof BoaException) {
				// if we submit the same script to Boa twice, Boa will return 0
				// byte output and throw a BoaException due to a cache bug in
				// Boa
				System.err
						.println("BoaException: may have submitted the same script twice!");
				script += System.lineSeparator();
				output = run(script);
			} else {
				e.printStackTrace();
			}
		}

		if (output != null) {
			update(output);
		}
	}

	protected void update(String output) {
		String[] records = output.split(System.lineSeparator());
		HashMap<String, String> map = new HashMap<String, String>();
		for (String record : records) {
			if (record.startsWith("record")) {
				String id = record.substring(record.indexOf('[') + 1,
						record.indexOf(']'));
				String url = record.substring(record.indexOf("] = ") + 4)
						.trim();
				map.put(id, url);
			}
		}

		String s = "";
		for (AbstractedExample example : examples) {
			String id = example.repo_url + " ** " + example.file_path;
			String url = map.get(id);
			if (url != null) {
				s += "results[" + url + "][" + example.method_name + "]["
						+ example.frequency + "] = " + example.seq.trim()
						+ System.lineSeparator();
			}
		}

		// rewrite the given example file with reconstructed urls
		FileUtils.writeStringToFile(s, path);
	}

	protected String run(String script) {
		MapleClient client = new MapleClient();
		int job_id = client.run(script, output_dir);
		// a compilation error or execution error occurs in the Boa script
		if (job_id == -1)
			return "";

		String output = FileUtils.readFileToString(output_dir + File.separator
				+ "output-" + job_id + ".txt");
		return output;
	}

	protected String customize() {
		ArrayList<String> repos = new ArrayList<String>();
		ArrayList<String> paths = new ArrayList<String>();
		for (AbstractedExample example : examples) {
			if(containsForeignLanguage(example.repo_url) || containsForeignLanguage(example.file_path)) {
				continue;
			}
			
			repos.add(example.repo_url);
			paths.add(example.file_path);
		}

		String script = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File(
				template_path)))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("q_urls = {")) {
					script += "q_urls = {";
					for (String url : repos) {
						script += "\"" + url + "\",";
					}
					// remove the last comma
					script = script.substring(0, script.length() - 1) + "};";
				} else if (line.startsWith("q_files = {")) {
					script += "q_files = {";
					for (String path : paths) {
						script += "\"" + path + "\",";
					}
					// remove the last comma
					script = script.substring(0, script.length() - 1) + "};";
				} else if (line.startsWith("q_id := ")) {
					script += "q_id := " + new Random().nextInt() + ";";
				} else {
					script += line;
				}

				script += System.lineSeparator();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return script;
	}

	protected void load() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File(
				path)))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("results[")) {
					String id = line.substring(line.indexOf('[') + 1,
							line.indexOf(']'));
					String[] ss = id.split("!");
					String repo_url = ss[0];
					String file_path = ss[1];
					String class_name = ss[2];
					String method_name = ss[3];
					int count = Integer.parseInt(line.substring(
							line.indexOf("][") + 2, line.indexOf("] =")));
					String seq = line.substring(line.indexOf("] =") + 3);
					AbstractedExample example = new AbstractedExample(repo_url,
							file_path, class_name, method_name, count, seq);
					examples.add(example);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean containsForeignLanguage(String s) {
		char[] chars = s.toCharArray();
		for (char c : chars) {
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || ((c >= '0' && c <= '9'))|| c == '-'
					|| c == ':' || c == '/' || c == '.' || c == '-' || c == '_'
					|| c == '\\' || c == ' ') {
				continue;
			} else {
				return true;
			}
		}

		return false;
	}
}