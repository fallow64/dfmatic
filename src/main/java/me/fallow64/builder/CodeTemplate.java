package me.fallow64.builder;

import me.fallow64.builder.blocks.CodeBlock;
import me.fallow64.builder.blocks.CodeHeader;
import me.fallow64.util.CompressionUtil;
import me.fallow64.util.ItemAPIClient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CodeTemplate {

    private final CodeHeader header;
    private final List<CodeBlock> blocks;
    private final String uncompressedTemplate;
    private final static String templateFormat = "/give minecraft:ender_chest{PublicBukkitValues:{\"hypercube:codetemplatedata\":'{\"author\":\"%s\",\"name\":\"%s\",\"version\":%d,\"code\":\"%s\"}'}}";

    public CodeTemplate(List<CodeBlock> blocks) {
        assert blocks.get(0) instanceof CodeHeader: "First code block should be a code header.";
        this.header = (CodeHeader) blocks.get(0);
        this.blocks = blocks;
        List<String> blockStrings = new ArrayList<>();
        for(CodeBlock block : blocks) {
            blockStrings.add(block.serialize());
        }

        this.uncompressedTemplate = "{\"blocks\": [" + String.join(",", blockStrings) + "]}";
    }
// TODO this code could be cleaner
    public CodeHeader getHeader() {
        return header;
    }

    public List<CodeBlock> getBlocks() {
        return blocks;
    }

    public String compressTemplate() throws IOException {
        return new String(CompressionUtil.toBase64(CompressionUtil.toGZIP(uncompressedTemplate.getBytes(StandardCharsets.UTF_8))));
    }

    public String genGiveCommand(String author, int version) throws IOException {
        String compressedCode = compressTemplate();
        return String.format(templateFormat, author, getHeader().getTemplateName(), version, compressedCode);
    }

    public boolean sendTemplate() {
        try {
            String message = "{\"type\":\"template\",\"source\":\"DFMatic\",\"data\":\"{\\\"name\\\":\\\"" + header.getTemplateName() + "\\\",\\\"data\\\":\\\"" + compressTemplate() + "\\\"}\"}";
            ItemAPIClient client = new ItemAPIClient(new URI("ws://localhost:31371/codeutilities/itemapi"), message);
            client.connect();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
