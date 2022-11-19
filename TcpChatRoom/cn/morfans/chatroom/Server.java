package cn.morfans.chatroom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Server {
    int uniqueID;

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
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
                throw new RuntimeException(e);
            }

        }
    } // End of ClientThread Class
} // End of Server Class
