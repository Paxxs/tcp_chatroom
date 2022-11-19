package cn.morfans.tcpChat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//练习
public class Server {
    private Socket socket = null; // 数据传输对象
    private ServerSocket server = null; // 服务端连接对象
    private DataInputStream in = null; // 数据流

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("服务端启动!");
            System.out.println(":: 等待客户端接入...");
            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";
            // 聊天结束条件
            while (!line.equals("Over")) {
                line = in.readUTF();
                System.out.println(line);
            }
            System.out.println("服务端关闭!");
            // close connection
            socket.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args){
        Server s = new Server(5001);
    }
}
