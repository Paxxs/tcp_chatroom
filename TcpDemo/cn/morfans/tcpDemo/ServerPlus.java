package cn.morfans.tcpDemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端和服务端互发消息
 */
public class ServerPlus {

    // 双向通讯
    public static void main(String[] args) {
        InputStream in = null; // 数据输入流
        Socket socket = null; // 数据传输对象
        ServerSocket serverSocket = null; // 服务连接对象

        try {
            serverSocket = new ServerSocket(8080);
            InetAddress address;
            byte[] datas;
            String str;
            int num;
            while (true) {
                socket = serverSocket.accept();
                address = socket.getInetAddress();
                System.out.println(":: 客户端 "+ address.getHostAddress() + " 连接成功");
                in = socket.getInputStream();
                datas = new byte[100];
                num = in.read(datas);
                str = new String(datas, 0, num);
                System.out.println("客户端：" + str);

                if (str.equals("你食不食油饼"))
                {
                    System.out.println(":: 服务端被控终止");
                    break;
                }
            }
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
