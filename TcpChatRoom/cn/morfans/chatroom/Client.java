package cn.morfans.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
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
            log("连接服务器错误：" + e);
            return false;
        }
        String message = "Connection accepted " + socket.getInetAddress() + " : " + socket.getPort();
        log(message);
        log(":: 创建输出输出流");
        try {
            // 接受服务端消息和输出给服务端
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log("创建输入输出流失败：" + e);
            close();
            return false;
        }
        // 监听服务端
        new ListendFromServer().start();
        try {
            // 最先发送用户名给服务端
            sOutput.writeObject(userName);
        } catch (IOException e) {
            log("登录服务器过程出错：" + e);
            close(); // 这里创建了输入输出流了。所以关闭
            return false;
        }
        return true;
    }

    public void sendToServer(ChatMessage chatMessage){
        try {
            sOutput.writeObject(chatMessage);
        } catch (IOException e) {
            log("发送讯息给服务端出现错误："+e);
//            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            sInput.close();
            sOutput.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String msg) {
        System.out.println(new Date() + " " + msg);
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
        if (!client.start()) // 没有连上服务端就退出
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
                // 特殊消息后边的都是看不到的，随便写
                client.sendToServer(new ChatMessage(ChatMessage.LOGOUT,"正在将你退出登录..."));
                break;
            } else if (msg.equalsIgnoreCase("who")) {
                // 特殊消息后边的都是看不到的，随便写
                client.sendToServer(new ChatMessage(ChatMessage.WHO,"看看谁在线..."));
            } else {
                // 正常发送消息
                client.sendToServer(new ChatMessage(ChatMessage.MESSAGE,msg));
            }
        }

        // 跳出循环就直接寄了
        scanner.close();
        // 销毁客户端
        client.close();
    }// End of Client main

    class ListendFromServer extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    // 获取到信息就直接输出即可
                    System.out.println(msg);

                } catch (IOException | ClassNotFoundException e) {
                    log("[通知] " + "服务器断连：" + e);
                    break;
                }
            }
        }
    } // End of ListendFromServer Class
}
