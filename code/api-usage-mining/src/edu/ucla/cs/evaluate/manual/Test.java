package edu.ucla.cs.evaluate.manual;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Test implements Runnable{

	public static void main(String[] args) {
		Thread t1 = new Thread(new Test());
		Thread t2 = new Thread(new Test());
		t1.start();
		t2.start();
	}
	
	public void thread() throws IOException, InterruptedException {
		File f = new File("/home/troy/research/BOA/Maple/README.md");
		BufferedReader br = new BufferedReader(new FileReader(f));
		System.out.println(br.readLine());
		Thread.sleep(1000);
	}

	@Override
	public void run() {
		try {
			thread();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
