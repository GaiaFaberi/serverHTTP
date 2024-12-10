package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread {
    private Socket s;

    public MyThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String responseBody;
            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];
            String header;
            File file;
            InputStream input;
            byte[] buf;
            int n;

            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());

            System.out.println("Richiesta terminata");

            switch (resource) {
                case "/":
                
                case "/index.html":

                     file = new File("htdocs/index.html");
                     input = new FileInputStream(file);

                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Length: " + file.length() + "\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("\n");
                    buf = new byte[8192];
                   
                    while((n = input.read(buf)) != -1){
                        out.write(buf, 0, n);
                    }
                    input.close();
                    break;
                
                case "/file.txt":
                 file = new File("htdocs/file.txt");
                 input = new FileInputStream(file);

                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("Content-Type: text/plain\n");
                out.writeBytes("\n");
                buf = new byte[8192];
                
                while((n = input.read(buf)) != -1){
                    out.write(buf, 0, n);
                }
                input.close();
                break;
                
                default:
                    out.writeBytes("HTTP/1.1 404 Not Found\n");
                    out.writeBytes("Content-Length: 0"  + "\n");
                    out.writeBytes("\n");

              
            }

            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getConentType(File f){
        String[] s = f.getName().split("\\.");
        String ext = s[s.length - 1];
        switch(ext){
            case "html":
            case "htm":
                return "text/html";
            case "pnj":
                return "image/png";
            case "css":
                return "text/css";
            default:
                return "";
        }
        

    }
}