package main.java.ch01;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    public static void main(String[] argv) throws Exception {
        try (ServerSocket server = new ServerSocket(8001)) {
            while (true) {
                Socket socket = server.accept();

                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}
