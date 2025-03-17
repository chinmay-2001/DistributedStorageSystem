package com.example.DistributedStorageSystem.Utils;

public class ExtractObjectNameFromUrl {
    public static String extractObjectNameFromUrl(String url) {
            return url.substring(url.lastIndexOf("/") + 1);
    }
}
