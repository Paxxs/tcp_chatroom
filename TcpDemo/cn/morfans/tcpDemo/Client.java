package cn.morfans.tcpDemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
//            socket = new Socket("10.254.213.241", 8080);
            socket = new Socket("10.254.213.241", 8080);
            int count = 0;
            while (count++ < 20) {
                System.out.println(":: 客户e端发话");
                out = socket.getOutputStream();
                String text = "你食不食油饼?";
                out.write(text.getBytes("GBK"));
//                out.write(text.getBytes());

//                System.out.println(":: 接受服务端");
//                in = socket.getInputStream();
//                byte[] datas = new byte[100];
//                int num = in.read(datas);
//                String strInput = new String(datas, 0, num);
//                System.out.println(strInput);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert socket != null;
                assert out != null;
                out.close();
                assert in != null;
                in.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
