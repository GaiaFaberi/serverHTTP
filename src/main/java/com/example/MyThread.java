package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread{
    private Socket s;

    public MyThread(Socket s){
        this.s = s;
    }

    public void run(){
        try (BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];
            String header;

            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());

            System.out.println("Richiesta terminata");

            String responseBody = "Ciao a tutti";
            out.writeBytes("HTTP/1.1 200 OK\n");
            out.writeBytes("Content-Type: text/plain\n");
            out.writeBytes("Content-Lenght: " + responseBody.length() + "\n");
            out.writeBytes("\n");
            out.writeBytes(responseBody);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}