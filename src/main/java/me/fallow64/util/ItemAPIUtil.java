package me.fallow64.util;

import me.fallow64.builder.CodeTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ItemAPIUtil {

    private static ItemAPIClient itemAPIClient;

    public static boolean sendTemplate(CodeTemplate template) throws IOException, URISyntaxException {
        String name = template.getHeader().getTemplateName();
        String compressedTemplate = template.compressTemplate();
        String message = "{\"type\":\"template\",\"source\":\"DFMatic\",\"data\":\"{\\\"name\\\":\\\"" + name + "\\\",\\\"data\\\":\\\"" + compressedTemplate + "\\\"}\"}";

        if(itemAPIClient == null) {
            itemAPIClient = new ItemAPIClient(new URI("ws://localhost:31371/codeutilities/itemapi"), message);
            itemAPIClient.connect();
        } else {
            itemAPIClient.send(message);
        }
        return true;
    }

}
