package ru.geekbrains.april_chat.network;

public interface ChatMessageService {
    void send(String msg);
    void receive(String msg);
    void connect();
}
