package com.advanced.projectspring.dto.chat;

public class ChatResponse {
    private String reply;
    private boolean blocked;
    private boolean hasChart;
    private Object chartData;

    public ChatResponse(String reply, boolean blocked) {
        this.reply = reply;
        this.blocked = blocked;
    }

    public ChatResponse(String reply, boolean blocked, boolean hasChart, Object chartData) {
        this.reply = reply;
        this.blocked = blocked;
        this.hasChart = hasChart;
        this.chartData = chartData;
    }

    public String getReply() {
        return reply;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isHasChart() {
        return hasChart;
    }

    public Object getChartData() {
        return chartData;
    }
}
