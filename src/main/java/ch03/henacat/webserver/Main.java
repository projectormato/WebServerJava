package main.java.ch03.henacat.webserver;

import main.java.ch03.henacat.servletimpl.WebApplication;

import java.net.*;

public class Main {
    public static void main(String[] argv) throws Exception {
        WebApplication app = WebApplication.createInstance("testbbs");
        app.addServlet("/ShowBBS", "main.java.ch03.henacat.testbbs.ShowBBS");
//        app.addServlet("/ShowBBS", "ShowBBS");
        app.addServlet("/PostBBS", "main.java.ch03.henacat.testbbs.PostBBS");
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
