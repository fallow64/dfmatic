package me.fallow64.util;

import me.fallow64.builder.CodeTemplate;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

// TODO make not jank
public class ItemAPIClient extends WebSocketClient {

    private final List<CodeTemplate> templates;
    private final boolean quitOnSend;

    public ItemAPIClient(URI serverUri, List<CodeTemplate> templates, boolean quitOnSend) {
        super(serverUri);
        this.templates = templates;
        this.quitOnSend = quitOnSend;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        sendTemplates(templates);
    }

    public void sendTemplates(List<CodeTemplate> templates) {
        for(CodeTemplate template : templates) {
            String name = template.getHeader().getTemplateName();
            String compressedTemplate = template.compressTemplate();
            send("{\"type\":\"template\",\"source\":\"DFMatic\",\"data\":\"{\\\"name\\\":\\\"" + name + "\\\",\\\"data\\\":\\\"" + compressedTemplate + "\\\"}\"}");
            try {Thread.sleep(100); } catch(Exception ignored) {}
        }
    }

    @Override
    public void onMessage(String message) {
        if(message.startsWith("{\"status\":\"success\"")) {
            System.out.println("Sent to CodeUtils successfully!");
        }
        if(message.startsWith("{\"status\":\"error\",\"error\":")) {
            System.err.println("code utils: " + message);
        }
        if(quitOnSend) System.exit(0);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onError(Exception ex) {}
}
