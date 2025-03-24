package com.example.DistributedStorageSystem.Utils;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MimetypeUtil {
    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();

    static {
        MIME_TYPE_MAP.put("pdf", MediaType.APPLICATION_PDF_VALUE);
        MIME_TYPE_MAP.put("zip", "application/zip");
        MIME_TYPE_MAP.put("png", "image/png");
        MIME_TYPE_MAP.put("jpg", "image/jpeg");
        MIME_TYPE_MAP.put("txt", "text/plain");
        MIME_TYPE_MAP.put("x-msdownload","application/x-msdownload");
    }

    public static String getMimeType(String fileExtension) {
        return MIME_TYPE_MAP.getOrDefault(fileExtension.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
