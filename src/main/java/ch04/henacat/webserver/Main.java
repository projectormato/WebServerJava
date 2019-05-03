package main.java.ch04.henacat.webserver;

import main.java.ch04.henacat.servletimpl.WebApplication;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] argv) throws Exception {
        WebApplication app = WebApplication.createInstance("cookietest");
        app.addServlet("/CookieTest", "main.java.ch04.henacat.cookietest.CookieTest");
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
