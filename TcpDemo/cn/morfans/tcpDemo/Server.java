package cn.morfans.tcpDemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        // 数据输入流
        InputStream in = null;
        // 数据传输对象
        Socket socket = null;
        // 创建服务端连接对象
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);

            socket = serverSocket.accept();
            System.out.println("连接上了");
            in = socket.getInputStream();

            // 传输的字节
            byte[] datas = new byte[100];
            /*
            read(byte[] datas) 从输入流所绑定的位置读取一组字节，存在在一个字节数组中
            返回实际读到的多少个字节

            如果读取中没有到数据末尾/或没有数据可读，read 方法会阻塞
             */
            int num = in.read(datas);
            /*
            字符串构造方法，将字节数组 datas 从 0 开始，转换 num 个字节为字符串
             */
            String str = new String(datas,0,num);
            System.out.println("来自客户端："+str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert in != null;
                in.close();
                serverSocket.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
