package com.advanced.projectspring.dto.chat;

public class ChatResponse {
    private String reply;
    private boolean blocked;

    public ChatResponse(String reply, boolean blocked) {
        this.reply = reply;
        this.blocked = blocked;
    }

    public String getReply() {
        return reply;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
