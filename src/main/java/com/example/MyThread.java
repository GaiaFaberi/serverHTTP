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

            
            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resource = request[1];
            String version = request[2];
            String header;
            
            byte[] buf;
            int n;

            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());

            System.out.println("Richiesta terminata");
            File file;

            if(resource.endsWith("/"))
                resource = resource + "index.html";

            
            if(resource.equals("/")){
                file = new File("htdocs/ProgettoFinaleFaberi/index.html");
            }else{
             file = new File("htdocs/ProgettoFinaleFaberi" + resource);
            }

            if(file.isDirectory()){
                out.writeBytes("HTTP/1.1 301 Moved Permanently\n");
                out.writeBytes("Location:  " + resource + "\n");
                out.writeBytes("Content- Length: 0 " + "\n");
                
                out.writeBytes("\n");
            }else if(file.exists()){
                InputStream input = new FileInputStream(file);
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: " + MyThread.getContentType(resource) + "\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("\n");
                buf = new byte[8192];

                while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
                input.close();
            }else{
                out.writeBytes("HTTP/1.1 404 Not Found\n");
                out.writeBytes("Content-Length: 0" + "\n");
                out.writeBytes("\n");
            }
            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getContentType(String resource) {
            String[] s = resource.split("\\.");
        String ext = s[s.length - 1];
        switch (ext) {
            case "html":
            case "htm":
                return "text/html";
            case "jpg":
                return "image/jpg";
            case "css":
                return "text/css";
            default:
                return "";
        }

    }
}