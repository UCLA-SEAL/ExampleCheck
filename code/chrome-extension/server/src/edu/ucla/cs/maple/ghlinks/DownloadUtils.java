package edu.ucla.cs.maple.ghlinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadUtils {
	public static String download(String link) {
		URL url= null;
		BufferedReader in = null;
		try {
	        url = new URL(link);
			in = new BufferedReader(
			        new InputStreamReader(url.openStream()));
			String content = "";
			String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		    	content += inputLine + System.lineSeparator();
		    }
			return content;
		} catch (IOException e) {
			// fail safe
			System.err.println("Fails to download from " + link);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return "";
	}
	
	public static void main(String[] args) {
		String link = "https://github.com/krb5/krb5/commit/cf1a0c411b2668c57c41e9c4efd15ba17b6b322c.patch";
		System.out.println(DownloadUtils.download(link));
	}
} 
