package main.java.ch05.henacat.servletimpl;

import main.java.ch05.henacat.servlet.http.*;
import java.util.*;
import java.util.concurrent.*;

public class HttpSessionImpl implements HttpSession {
    private String id;
    private Map<String, Object> attributes
            = new ConcurrentHashMap<String, Object>();
    private volatile long lastAccessedTime;

    public String getId() {
        return this.id;
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = new HashSet<String>();
        names.addAll(attributes.keySet());

        return Collections.enumeration(names);
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        if (value == null){
            removeAttribute(name);
            return;
        }
        this.attributes.put(name, value);
    }

    synchronized void access() {
        this.lastAccessedTime = System.currentTimeMillis();
    }

    long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public HttpSessionImpl(String id) {
        this.id = id;
        this.access();
    }
}
