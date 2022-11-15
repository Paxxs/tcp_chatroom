package cn.morfans.tcpDemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        OutputStream out = null; // 输出流

        Socket socket = null; // 数据传输对象
        ServerSocket serverSocket = null; // 服务连接对象

        try {
            serverSocket = new ServerSocket(8080);
            String name; // 客户端名称
            byte[] datas; // 一次性读取载体大小
            int num; // 真正读取大小
            String strInput; // 客户端发来的信息文本
            while (true) {
                socket = serverSocket.accept();
                name = generateClientName(socket);
                System.out.println(":: " + name + " 连接成功!");
                in = socket.getInputStream();
                datas = new byte[100];
                num = in.read(datas);
                strInput = new String(datas, 0, num);
                System.out.println(name + ": " + strInput);

                // 回复客户端
                out = socket.getOutputStream();
                String text = "你干嘛！！";
                out.write(text.getBytes());

                if (strInput.equals("你食不食油饼")) {
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
                assert out != null;
                out.close();
                serverSocket.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 生成客户端名称
     * @param s socket
     * @return 名称
     */
    private static String generateClientName(Socket s) {
        InetAddress address = s.getInetAddress(); // 客户端地址
        return String.format("客户端[%s]", address.getHostAddress()); // 客户端名称
    }
}
