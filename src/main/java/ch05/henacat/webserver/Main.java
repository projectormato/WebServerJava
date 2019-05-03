package main.java.ch05.henacat.webserver;

import main.java.ch05.henacat.servletimpl.WebApplication;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] argv) throws Exception {
        WebApplication app = WebApplication.createInstance("sessiontest");
        app.addServlet("/SessionTest", "main.java.ch05.henacat.sessiontest.SessionTest");
        try (ServerSocket server = new ServerSocket(8001)) {
            for (;;) {
                Socket socket = server.accept();
                ServerThread serverThread = new ServerThread(socket);
                Thread thread = new Thread(serverThread);
                thread.start();
            }
        }
    }
}
