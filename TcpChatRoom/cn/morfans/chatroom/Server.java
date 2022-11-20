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
    private final SimpleDateFormat sdf;

    public Server(int port) {
        this.port = port;
        this.clientList = new ArrayList<>();
        this.sdf = new SimpleDateFormat("HH:mm:ss");
    }

    public static void main(String[] args) {
        int portNum = 3190;
        switch (args.length) {
            case 1 -> {
                try {
                    portNum = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("错误的端口号！");
                    System.out.println("Usage:");
                    System.out.println("Server [portNum]");
                }
            }
            case 0 -> {
            }
            default -> {
                // 如果有很多参数，报错结束
                System.out.println("Usage:");
                System.out.println("Server [portNum]");
                return;
            }
        }

        Server server = new Server(portNum);
        server.start();
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

    protected void stop() {
        keepGoing = false;
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
     * 服务端发送广播信息，自动判断（群聊）、私聊。
     * 私聊 msg 传入检测格式 UserName:[space]@name[space]信息。判断字段1开头是否为 @
     *
     * @param msg 发送的信息
     * @return 私聊是否发送成功，群聊都会返回 true
     */
    private synchronized boolean broadcast(String msg) {
        String time = sdf.format(new Date());
        String[] wMsg = msg.split(" ", 3);
        boolean isPrivateChat = wMsg[1].charAt(0) == '@';

        if (isPrivateChat) {
            String toUserName = wMsg[1].substring(1);
            msg = wMsg[0] + wMsg[2]; //  合成：UserName: + 信息
            String formatMsg = time + " [PM]" + msg + "\n";
            boolean isExist = false; // 是否找到用户

            // 对着名字查找客户端
            for (int i = clientList.size(); --i >= 0; ) { // 从 0 开始，所以先要 size -1
                ClientThread ct = clientList.get(i);
                if (ct.getUserName().equals(toUserName)) {
                    // 找到了对应的用户
                    sendToClient(ct, i, formatMsg);
                    isExist = true;
                    break;
                }
            }
            return isExist;
        } else { // 群发不保证都发到，故不用返回 false
            // 遍历给所有客户端发送
            String formatMsg = time + "" + msg + "\n";
            System.out.println(formatMsg);
            for (int i = clientList.size(); --i >= 0; ) { // 从 0 开始，所以先要 size -1
                ClientThread ct = clientList.get(i);
                sendToClient(ct, i, formatMsg);
            }
        }
        return true;
    }

    /**
     * 给客户端发送消息，失败则移除客户端
     *
     * @param ct              客户端线程
     * @param clientListIndex ClientList 索引(所在客户端）
     * @param msg             发送的信息
     */
    private void sendToClient(ClientThread ct, int clientListIndex, String msg) {
        if (!ct.sendMsg(msg)) {
            // 如果失败大概率是寄了，直接让其下线吧
            clientList.remove(clientListIndex);
            log("客户端 " + ct.getUserName() + " 断开了，已从列表中移除！");
        }
    }

    /**
     * 从 clientList 中移除客户端，用于线程结束时候移除客户端
     *
     * @param id 客户端唯一id
     */
    private synchronized void removeClient(int id) {
        String disconnectClient = "";
        boolean isExist = false;
        for (int i = 0; i < clientList.size(); i++) {
            ClientThread ct = clientList.get(i);
            if (id == ct.id) {
                disconnectClient = ct.userName;
                clientList.remove(i);
                isExist = true;
                break;
            }
        }
        if (isExist)
            broadcast("*** " + disconnectClient + "离开了聊天室");
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
                        //处理发消息
                        // 群发都会返回 true,返回flase 只有可能是私聊
                        // 格式 UserName:[space]@name[space]信息
                        boolean confirm = broadcast(userName + ": " + message);
                        if (!confirm) {
                            // 私聊出现问题直接回怼客户端
                            String msg = "***" + "抱歉，你pm的用户不存在...";
                            sendMsg(msg);
                        }
                    }
                    case ChatMessage.WHO -> {
                        // 生成在线列表
                        sendMsg(sdf.format(new Date()) + " 当前在线的用户:\n");
                        for (int i = 0; i < clientList.size(); i++) {
                            ClientThread ct = clientList.get(i);
                            sendMsg((i + 1) + ") " + ct.userName + " 上线时间：" + ct.date);
                        }
                    }
                    case ChatMessage.LOGOUT -> {
                        log(userName + "通过下线请求下线！");
                        keepGoing = false;
                    }
                }
            }
            // 结束就关闭了
            // 因为要从 client列表中移除，所以还需要找到其对应的id
            removeClient(id);
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
