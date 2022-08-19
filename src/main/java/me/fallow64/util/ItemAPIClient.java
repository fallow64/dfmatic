package me.fallow64.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ItemAPIClient extends WebSocketClient {

    private String message;

    public ItemAPIClient(URI serverUri, String message) {
        super(serverUri);
        this.message = message;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send(message);
        close();
    }

    @Override
    public void onMessage(String message) {}

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
