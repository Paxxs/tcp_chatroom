package cn.morfans.chatroom;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    static final int WHO = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;
    private String message;

    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
