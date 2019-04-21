package main.java.ch01;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) throws Exception{
        try {
            ServerSocket server = new ServerSocket(8001);
            FileOutputStream fos = new FileOutputStream("src/main/java/ch01/server_recv.txt");
            FileInputStream fis = new FileInputStream("src/main/java/ch01/server_send.txt");
            System.out.println("クライアントからの接続を待ちます。");
            Socket socket = server.accept();
            System.out.println("クライアント接続");

            int ch;
            InputStream input = socket.getInputStream();
            //クライアントは、終了のマークとして0を送付してくる
            while ((ch = input.read()) != 0){ //0じゃない限り
                //クライアントから受け取った情報をserver_recv.txtに出力
                fos.write(ch);
            }

            //server_send.txtの内容をクライアントに送付
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1){
                output.write(ch);
            }

            socket.close();
            System.out.println("通信を終了しました");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
