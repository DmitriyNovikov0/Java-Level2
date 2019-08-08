package ru.gb.j_two.network;

import java.net.ServerSocket;
import java.net.Socket;

public interface ServerSocketThreadListener {
    void onServerSocketThreadStart(ServerSocketThread thread);
    void onServerSocketThreadStop(ServerSocketThread thread);

    void onServerSocketCreate(ServerSocketThread thread, ServerSocket server);
    void onServerSocketAcceptTimeout(ServerSocketThread thread, ServerSocket server);
    void onSocketAccepted(ServerSocketThread thread, Socket socket);

    void onServerSocketThreadException(ServerSocketThread thread, Exception e);
}
