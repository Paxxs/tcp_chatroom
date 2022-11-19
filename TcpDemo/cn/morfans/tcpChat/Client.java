package cn.morfans.tcpChat;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket = null; // 数据传输对象
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println(":: 连接上服务端!");

            in = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line = "";
        // 程序结束条件
        while (!line.equals("over")) {
            try {
                line = in.readUTF();
                System.out.println("输入"+line);
                out.writeUTF(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args){
        Client c = new Client("localhost",5001);
    }
}
