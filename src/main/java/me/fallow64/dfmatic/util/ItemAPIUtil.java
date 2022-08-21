package me.fallow64.dfmatic.util;

import me.fallow64.dfmatic.builder.CodeTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ItemAPIUtil {

    private static ItemAPIClient itemAPIClient;

    public static boolean sendTemplates(List<CodeTemplate> templates, boolean quitOnSend) {
        try {
            if (itemAPIClient == null) {
                itemAPIClient = new ItemAPIClient(new URI("ws://localhost:31371/codeutilities/itemapi"), templates, quitOnSend);
                itemAPIClient.connect();
            } else {
                itemAPIClient.sendTemplates(templates);
            }
            return true;
        } catch(URISyntaxException ignored) {
            return false;
        }
    }

}
