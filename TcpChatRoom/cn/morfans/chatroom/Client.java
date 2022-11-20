package cn.morfans.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String serverAddr;
    private final int port;
    private String userName;

    private Socket socket;
    private ObjectOutputStream sOutput;
    private ObjectInputStream sInput;

    public Client(String serverAddr, int port, String userName) {
        this.serverAddr = serverAddr;
        this.userName = userName;
        this.port = port;
    }

    public boolean start() {
        try {
            socket = new Socket(serverAddr, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void main(String[] args) {
        int portNum = 3190;
        String server = "localhost";
        String user = "";
        /*
         * 格式：Client [username] [portNumber] [serverAddress]
         */
        switch (args.length) {
            case 3:
                server = args[2];
            case 2:
                try {
                    portNum = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("Invalid port number. ");
                    System.out.println("Usage: Client [username] [portNum] [serverAddr]");
                    return;
                }
            case 1:
                user = args[0];
            case 0:
                break; //参数就全默认值，用户名靠询问
            default:
                System.out.println("Usage: Client [username] [portNum] [serverAddr]");
                return;
        } // End of Switch
        // 如果没有用户名则询问
        Scanner scanner = new Scanner(System.in);
        if (user.equals("")) {
            System.out.println("请输入用户名：");
            user = scanner.nextLine();
        }
        Client client = new Client(server, portNum, user);

        if (!client.start())
            return;

        System.out.println("\n 😀你好哟，" + user + "! 欢迎来到 Paxos 的聊天室~");
        System.out.println("使用指北：");
        System.out.println("1. 直接发送信息是群聊");
        System.out.println("2. 私聊 pm，使用 '@用户名<space>你要发送的信息'");
        System.out.println("3. 查询在线用户，使用 'WHO'");
        System.out.println("3. 退出聊天，使用 'LOGOUT' 退出登录");

        while (true) {
            System.out.println("输入消息>");
            String msg = scanner.nextLine();
            if (msg.equalsIgnoreCase("logout")) {
                // TODO 发送退出通知到服务端
                break;
            } else if (msg.equalsIgnoreCase("who")) {
                // TODO 发送查询在线列表
            } else {
                // TODO 正常发送消息
            }
        }

        // 跳出循环就直接寄了
        scanner.close();
        // 销毁客户端
    }// End of Client main
}
