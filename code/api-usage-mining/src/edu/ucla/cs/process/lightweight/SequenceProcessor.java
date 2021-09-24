package edu.ucla.cs.process.lightweight;

import edu.ucla.cs.model.Method;
import org.apache.commons.lang3.StringUtils;

public class SequenceProcessor extends ProcessStrategy {

	@Override
	void process(String line) {
		Method m = getMethodInstance(line);
		buildSequenceMap(m, line);
	}
	
	protected void buildSequenceMap(Method method, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		String[] ss = s.split("->");
		// skip the first element because it is empty string
		for(int i = 1; i < ss.length; i++){
			if (ss[i].contains("}")){
				String api = ss[i].split("}")[0];
				method.seq.add(api);
				// count how many curly braces
				int n = StringUtils.countMatches(ss[i], '}');
				for(int j = 0; j < n; j++) {
					method.seq.add("}");
				}
			}else{
				method.seq.add(ss[i].trim());
			}
		}
	}
}
