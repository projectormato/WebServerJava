package main.java.ch05.henacat.servletimpl;

import main.java.ch05.henacat.servlet.http.HttpServlet;

public class ServletInfo {
    WebApplication webApp;
    String urlPattern;
    String servletClassName;
    HttpServlet servlet;

    public ServletInfo(WebApplication webApp, String urlPattern,
                       String servletClassName) {
        this.webApp = webApp;
        this.urlPattern = urlPattern;
        this.servletClassName = servletClassName;
    }
}

