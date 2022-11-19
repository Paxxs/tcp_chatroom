package cn.morfans.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    int uniqueID;
    private final int port;
    /**
     * 客户端列表
     */
    private final ArrayList<ClientThread> clientList;
    private boolean keepGoing;
    private SimpleDateFormat sdf;

    public Server(int port) {
        this.port = port;
        this.clientList = new ArrayList<>();
        this.sdf = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * 启动服务端
     */
    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (keepGoing) {
                log("监听 :" + port + "端口，等待客户端连接...");
                // 寻找客户端
                Socket socket = serverSocket.accept();
                if (!keepGoing) // 中断
                    break;
                // 找到了就添加到客户端列表
                ClientThread thread = new ClientThread(socket);
                clientList.add(thread);
                thread.start();
            }
            // 结束了就释放资源
            serverSocket.close();
            // 释放所有客户端的连接
            // 使用 foreach loop:for(data_type variable : array_name)
            for (ClientThread c : clientList) {
                c.close();
            }
        } catch (IOException e) {
            log("ServerSocket 过程发生错误！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务端日志输出
     *
     * @param msg 日志内容
     */
    private void log(String msg) {
        String nowTime = sdf.format(new Date());
        System.out.println(nowTime + " " + msg);
    }

    /**
     * 处理客户端的线程
     */
    public class ClientThread extends Thread {
        /**
         * 客户端数据传输对象
         */
        Socket socket;
        /**
         * 客户端唯一id
         */
        int id;
        String userName;
        /**
         * 客户端上线时间
         */
        String date;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        ChatMessage chatMessage;

        /**
         * 构造函数，初始化线程并获得用户上线时间
         *
         * @param socket 客户端连接对象
         */
        public ClientThread(Socket socket) {
            this.socket = socket;
            this.id = ++uniqueID;
            System.out.println("创建线程输入输出流");
            try {
                this.sInput = new ObjectInputStream(socket.getInputStream());
                this.sOutput = new ObjectOutputStream(socket.getOutputStream());
                this.userName = (String) sInput.readObject();
                //TODO: 广播上线通知 userName
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.date = new Date() + "\n";
        }

        /**
         * 获取当前线程的用户名称
         *
         * @return 用户名称
         */
        public String getUserName() {
            return userName;
        }

        public void run() {
            boolean keepGoing = true;
            while (keepGoing) {
                try {
                    chatMessage = (ChatMessage) sInput.readObject();
                } catch (IOException e) {
                    log(userName + " 读入流发生错误:" + e);
//                    throw new RuntimeException(e);
                    break;
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                    break;
                }
                /*
                  解析后的聊天文本
                 */
                String message = chatMessage.getMessage();
                switch (chatMessage.getType()) {
                    case ChatMessage.MESSAGE -> {
                        //TODO　处理发消息
                    }
                    case ChatMessage.WHO -> {
                        // TODO　查询在线的人
                    }
                    case ChatMessage.LOGOUT -> keepGoing = false;
                }
            }
            // 结束就关闭了
            close();
        }

        /**
         * 服务端给客户端发消息
         *
         * @param msg 消息
         * @return 是否发送成功
         */
        public boolean sendMsg(String msg) {
            try {
                sOutput.writeObject(msg);
            } catch (IOException e) {
                log("[客户端线程] 服务器下发消息过程发生错误！");
                throw new RuntimeException(e);
            }
            return true;

        }

        public void close() {
            try {
                sOutput.close();
                sInput.close();
                socket.close();
            } catch (IOException e) {
                log("[客户端线程] 关闭过程发生错误！");
                throw new RuntimeException(e);
            }

        }
    } // End of ClientThread Class
} // End of Server Class
