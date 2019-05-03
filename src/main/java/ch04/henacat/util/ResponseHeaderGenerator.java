package main.java.ch04.henacat.util;

import java.io.*;

public interface ResponseHeaderGenerator {
    void generate(OutputStream output) throws IOException;
}