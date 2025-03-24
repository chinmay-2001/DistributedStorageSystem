package com.example.DistributedStorageSystem.Utils;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeUtils {
    public static Map<String,String> FileTypeMap=new HashMap<>();
    static {
        FileTypeMap.put("x-msdownload","exe");
        FileTypeMap.put("x-zip-compressed","zip");
    }

    public static String getFileType(String key){
        if(FileTypeMap.containsKey(key)){
            return FileTypeMap.get(key);
        }else{
            return key;
        }
    }
}
