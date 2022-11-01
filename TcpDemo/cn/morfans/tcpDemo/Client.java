package cn.morfans.tcpDemo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream out = null;
        try {
            socket = new Socket("10.254.213.241", 8080);
            out = socket.getOutputStream();
            String text = "食不食油饼?";
            out.write(text.getBytes("GBK"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert socket != null;
//            assert out != null;
            try {
//                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
