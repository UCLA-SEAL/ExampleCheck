package edu.ucla.cs.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import edu.iastate.cs.boa.BoaClient;
import edu.iastate.cs.boa.BoaException;
import edu.iastate.cs.boa.CompileStatus;
import edu.iastate.cs.boa.ExecutionStatus;
import edu.iastate.cs.boa.InputHandle;
import edu.iastate.cs.boa.JobHandle;
import edu.iastate.cs.boa.NotLoggedInException;

public class MapleClient {
	final String username = "husttroy";
	final String password = "5887526";
	
	public int run(String script, String output_dir) {
		try (final BoaClient client = new BoaClient()) {
			   client.login(username, password);

			   // print all available input datasets
			   InputHandle dataset = client.getDataset("2015 September/GitHub");
			   
			   if(dataset == null) {
				   System.err.println("Requested Boa dataset does not exist!");
				   return -1;
			   }
			   
			   // submit the query
			   JobHandle job = client.query(script, dataset);
			   
			   CompileStatus cstatus = job.getCompilerStatus();
			   while (cstatus == CompileStatus.RUNNING || cstatus == CompileStatus.WAITING) {
				   cstatus = client.getJob(job.getId()).getCompilerStatus();
				   //System.out.println("Compilation Status: " + cstatus);
			   }
			   
			   if (cstatus == CompileStatus.ERROR) {
				   System.err.println("Compilation Error!");
				   List<String> errors = client.getJob(job.getId()).getCompilerErrors();
				   for(String error : errors) {
					   System.err.println(error);
				   }
				   return -1;
			   }
			   			   
			   ExecutionStatus estatus = job.getExecutionStatus();
			   while (estatus == ExecutionStatus.RUNNING || estatus == ExecutionStatus.WAITING) {
				   estatus = client.getJob(job.getId()).getExecutionStatus();
				   //System.out.println("Execution Status: " + estatus);
			   }
			   
			   if (estatus == ExecutionStatus.ERROR) {
				   System.err.println("Execution Error!");
				   return -1;
			   }
			   
			   File oFile = new File(output_dir + "/output-" + job.getId() + ".txt");
			   if(oFile.exists()) {
				   oFile.delete();
			   }
			   oFile.createNewFile();
			   
			   // dump the output to the given output directory
			   job.getOutput(oFile);
			   
			   return job.getId();
		} catch (NotLoggedInException e) {
			e.printStackTrace();
		} catch (BoaException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static void main(String[] args) throws IOException {
		MapleClient client = new MapleClient();
		final String script = "/home/troy/research/BOA/Maple/boa/template/url.boa";
		byte[] encoded = Files.readAllBytes(Paths.get(script));
		String query = new String(encoded, Charset.defaultCharset());
		client.run(query, "/home/troy/research/BOA/Maple/boa/output");
	}
}
