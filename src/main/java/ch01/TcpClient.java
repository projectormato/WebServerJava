package main.java.ch01;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) throws Exception{
        try {
            Socket socket = new Socket("localhost", 8001);
            FileOutputStream fos = new FileOutputStream("src/main/java/ch01/client_recv.txt");
            FileInputStream fis = new FileInputStream("src/main/java/ch01/client_send.txt");

            int ch;
            //client_send.txtの内容をサーバに送信
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1){
                output.write(ch);
            }
            //終了を示すため、0を送信
            output.write(0);

            //サーバからの送信をclient_recv.txtに出力
            InputStream input = socket.getInputStream();
            while ((ch = input.read()) != -1){
                fos.write(ch);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
