package main.java.ch01;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Modoki01 {
    // ルートの指定。IDEAの気持ちに合わせて指定している
    private static final String DOCUMENT_ROOT = "./src/HTML";


    // InputStreamからのバイト列を、行単位で読み込むユーティリティメソッド
    private static String readLine(InputStream input) throws Exception {
        int ch;
        String ret = "";
        while ((ch = input.read()) != -1) {
            if (ch == '\r') {
            } else if (ch == '\n') {
                break;
            } else {
                ret += (char)ch;
            }
        }
        if (ch == -1) {
            return null;
        } else {
            return ret;
        }
    }

    // 1列の文字列を、バイト列としてOutputStreamに書き込むユーティリティメソッド
    private static void writeLine(OutputStream output, String str)
            throws  Exception {
        for (char ch : str.toCharArray()) {
            output.write((int)ch);
        }
        output.write((int)'\r');
        output.write((int)'\n');
    }

    // 現在時刻からHTTP標準に合わせてフォーマットされた日付文字列を返す
    private static String getDateStringUtc() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",
                Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + " GMT";
    }

    public static void main(String[] argv) throws Exception {
        // ポートを指定
        try (ServerSocket server = new ServerSocket(8001)) {
            Socket socket = server.accept();

            InputStream input = socket.getInputStream();

            String line;
            String path = null;
            while ((line = readLine(input)) != null) {
                if (line.equals(""))
                    break;
                if (line.startsWith("GET")) {
                    path = line.split(" ")[1];
                }
            }
            // レスポンスヘッダを返す
            OutputStream output = socket.getOutputStream();
            writeLine(output, "HTTP/1.1 200 OK");
            writeLine(output, "Date: " + getDateStringUtc());
            writeLine(output, "Server: Modoki/0.1");
            writeLine(output, "Connection: close");
            writeLine(output, "Content-type: text/html");
            writeLine(output, "");

            // レスポンスボディを返す
            try (FileInputStream fis
                         = new FileInputStream(DOCUMENT_ROOT + path);) {
                int ch;
                while ((ch = fis.read()) != -1) {
                    output.write(ch);
                }
            }
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
