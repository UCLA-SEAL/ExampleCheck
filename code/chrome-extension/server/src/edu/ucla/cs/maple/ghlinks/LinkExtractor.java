package edu.ucla.cs.maple.ghlinks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.ucla.cs.maple.server.MySQLAccess;

public class LinkExtractor {

    public static void main(String[] args) {
        String folderName;
        // add "links" column to the patterns table in database
        MySQLAccess dbAccess = new MySQLAccess();
        dbAccess.connect();
        
        // walk through folder names in patterns-with-urls directory 
        // and check database for the API/method
        File pwuDir = new File("patterns-with-urls");
        
        for (final File folder : pwuDir.listFiles()) {
            folderName = folder.getName();
//            if(!folderName.equals("ByteBuffer.get")) {
//            	continue;
//            }
            
            // convert the patterns in pattern.txt to strings of the same
            // format as the ones in the database
            ArrayList<String> filePatterns = extractPatterns(folder);
                
            // query the database for each pattern
            for (int i=0; i < filePatterns.size(); i++) {
                // if this pattern related to this API is in the database, then use
                // the file pattern's index+1 to locate its corresponding links,
                // and add those to the database
            	String[] ss = folderName.split("\\.");
                String className = ss[0];
                String methodName = ss[1];
                
                if (dbAccess.patternExists(className, methodName, filePatterns.get(i))) {
                    // extract the links from the sample-(i+1).txt file and add as a single string
                    // to the database
                	File sampleFile = new File(folder.getAbsolutePath() + File.separator + "sample-" + (i+1) + ".txt");
                	if(sampleFile.exists()) {
                		dbAccess.addValueToColumn(className, methodName, filePatterns.get(i),
                                "links", extractLinks(sampleFile));
                	}
                }
             }           
         }
        dbAccess.close();
    }
    
    private static ArrayList<String> extractPatterns(File _folder) {
        ArrayList<String> patterns = new ArrayList<String>();
        BufferedReader br = null;
        String line = null;
        String p;
        
        try {
            File pFile  = new File(_folder.getAbsolutePath() + File.separator + "pattern.txt");
            if(!pFile.exists()) {
            	// patterns.txt does not even exist
            	return patterns;
            } else {
            	br = new BufferedReader(new FileReader(pFile));
                
                // skip the first line
                br.readLine();
                // convert each pattern to an API call sequence and add to the ArrayList
                while ((line = br.readLine()) != null)
                {
                    p = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
                    patterns.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if(br != null) {
                	br.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return patterns;
    }
    
    private static String extractLinks(File samples) {
        // construct a string of the top three working URLs in this file, separated
        // by backslashes
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line = null;
        String url;
        String method;
        int count = 0;
        
        try {
            // just read the first file, which is pattern.txt
            br = new BufferedReader(new FileReader(samples));
            
            // skip the first line
            br.readLine();
            // get the URL and add to StringBuilder if it exists
            // break the loop once we have three working URLs
            while (((line = br.readLine()) != null) && (count < 3))
            {
                url = line.substring(line.indexOf("[")+1, line.indexOf("]"));
                // rewrite the url to retrieve its raw code from GitHub
                String rawFileLink = url.replace("github.com", "raw.githubusercontent.com");
				rawFileLink = rawFileLink.replace("/blob/", "/");
				String code = DownloadUtils.download(rawFileLink);
                if (code != null) {
                    // add the corresponding method (i.e. the one that the
                    // API call occurs in)
                    // note: the format of each sampled example is changed to 'results[url][method][frequency] = ...' where 'frequency' indicate the number of duplicated examples.
                    method = line.substring(line.indexOf("[", line.indexOf('[') + 1)+1, line.indexOf("]", line.indexOf(']') + 1));
                    
                    // find the line range of the method
                    ASTParser p = getASTParser(code);
        			CompilationUnit cu = (CompilationUnit) p.createAST(null);
        			MatchMethod mm = new MatchMethod(cu, method);
        			cu.accept(mm);
        			if(mm.startLine > 0 && mm.endLine > 0) {
        				count++;
        				// append the line number to the end of the GitHub url
        				url += "#L" + mm.startLine + "-L" + mm.endLine;
        				// use a backslash as a delimiter between urls
                        sb.append(url + "\\\\" + method + "\\\\");
        			}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    private static ASTParser getASTParser(String sourceCode) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setStatementsRecovery(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(sourceCode.toCharArray());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		parser.setCompilerOptions(options);
		return parser;
	}
    
    // from https://stackoverflow.com/questions/4177864/checking-if-a-url-exists-or-not
    private static boolean urlExists(String URLName){
        try {
          HttpURLConnection.setFollowRedirects(false);
          // note : you may also need
          //        HttpURLConnection.setInstanceFollowRedirects(false)
          HttpURLConnection con =
             (HttpURLConnection) new URL(URLName).openConnection();
          con.setRequestMethod("HEAD");
          return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
           e.printStackTrace();
           return false;
        }
    }  
}
