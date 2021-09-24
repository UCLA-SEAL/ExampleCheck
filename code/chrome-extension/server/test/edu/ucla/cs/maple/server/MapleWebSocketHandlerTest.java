package edu.ucla.cs.maple.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.junit.Test;

public class MapleWebSocketHandlerTest {

    //CodeSnippet1: https://stackoverflow.com/questions/10506180/
    //CodeSnippet2: https://stackoverflow.com/questions/10065723/
    //CodeSnippet3: https://stackoverflow.com/questions/12100580/
    @Test
    public void testCodeSnippetAnalysis() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("test/CodeSnippet1"));
        String cs = br.readLine();
        MapleWebSocketHandler mwsh = new MapleWebSocketHandler();
        //mwsh.onMessage(cs);
        HttpClient client = new HttpClient();
        client.start();
        //Request request = client.POST("ws://127.0.0.1:8080/");
        
        client.newRequest("ws://127.0.0.1:8080/")
        .send(new Response.CompleteListener()
        {
            @Override
            public void onComplete(Result result)
            {
                System.out.println(result.getResponse().getStatus());
                System.out.println("Request completed");
            }
        }); 

        client.stop();  
    }

}
