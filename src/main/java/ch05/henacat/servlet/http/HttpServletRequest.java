package main.java.ch05.henacat.servlet.http;

import java.io.UnsupportedEncodingException;

public interface HttpServletRequest {
    String getMethod();
    String getParameter(String name);
    String[] getParameterValues(String name);
    Cookie[] getCookies();
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;
    HttpSession getSession(boolean create);
}

