package main.java.ch03.henacat.webserver;

import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import main.java.ch03.henacat.servletimpl.*;
import main.java.ch03.henacat.util.*;

public class ServerThread implements Runnable {
    private static final String DOCUMENT_ROOT = "/home/keita/IdeaProjects/WebServerJava/src/HTML";
    private static final String ERROR_DOCUMENT = "/home/keita/IdeaProjects/WebServerJava/src/HTML";
    private Socket socket;

    private static void addRequestHeader(Map<String, String> requestHeader,
                                         String line) {
        int colonPos = line.indexOf(':');
        if (colonPos == -1)
            return;

        String headerName = line.substring(0, colonPos).toUpperCase();
        String headerValue = line.substring(colonPos + 1).trim();
        requestHeader.put(headerName, headerValue);
    }

    @Override
    public void run() {
        OutputStream output = null;
        try {
            InputStream input = socket.getInputStream();

            String line;
            String requestLine = null;
            String method = null;
            Map<String, String> requestHeader = new HashMap<String, String>();
            while ((line = Util.readLine(input)) != null) {
                if (line.equals("")) {
                    break;
                }
                if (line.startsWith("GET")) {
                    method = "GET";
                    requestLine = line;
                } else if (line.startsWith("POST")) {
                    method = "POST";
                    requestLine = line;
                } else {
                    addRequestHeader(requestHeader, line);
                }
            }
            if (requestLine == null)
                return;

            String reqUri = MyURLDecoder.decode(requestLine.split(" ")[1],
                    "UTF-8");
            String[] pathAndQuery = reqUri.split("\\?");
            String path = pathAndQuery[0];
            String query = null;
            if (pathAndQuery.length > 1) {
                query = pathAndQuery[1];
            }
            output = new BufferedOutputStream(socket.getOutputStream());

            String appDir = path.substring(1).split("/")[0];
            WebApplication webApp = WebApplication.searchWebApplication(appDir);
            if (webApp != null) {
                ServletInfo servletInfo
                        = webApp.searchServlet(path.substring(appDir.length() + 1));
                if (servletInfo != null) {
                    System.out.println(method + "aaa" + query + "aaa" + servletInfo + "aaa" + requestHeader + "aaa" + input + "aaa" + output);
                    ServletService.doService(method, query, servletInfo,
                            requestHeader, input, output);
                    return;
                }
            }
            String ext = null;
            String[] tmp = reqUri.split("\\.");
            ext = tmp[tmp.length - 1];

            if (path.endsWith("/")) {
                path += "index.html";
                ext = "html";
            }
            FileSystem fs = FileSystems.getDefault();
            Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath;
            try {
                realPath = pathObj.toRealPath();
            } catch (NoSuchFileException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }
            if (!realPath.startsWith(DOCUMENT_ROOT)) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            } else if (Files.isDirectory(realPath)) {
                String host = requestHeader.get("HOST");
                String location = "http://"
                        + ((host != null) ? host : Constants.SERVER_NAME)
                        + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }
            try (InputStream fis
                         = new BufferedInputStream(Files.newInputStream(realPath))) {
                SendResponse.sendOkResponse(output, fis, ext);
            } catch (FileNotFoundException ex) {
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    ServerThread(Socket socket) {
        this.socket = socket;
    }
}
