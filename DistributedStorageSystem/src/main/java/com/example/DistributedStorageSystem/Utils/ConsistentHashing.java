package com.example.DistributedStorageSystem.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHashing {
    private final SortedMap<Integer, String> circle = new TreeMap();
    private int virtualNode;
    MessageDigest md;


    ConsistentHashing(int virtualNode, List<String> nodes) throws NoSuchAlgorithmException {
        this.virtualNode=virtualNode;
        md=MessageDigest.getInstance("SHA-256");
        for(String node:nodes){
            addNode(node);
        }
    }

    private int Hash(String key){
        byte[] digest=md.digest(key.getBytes(StandardCharsets.UTF_8));
        return Math.abs(Arrays.hashCode(digest));
    }

    public void addNode(String node){
        for(int i=0;i<virtualNode;i++){
            int hash=Hash(node+i);
            circle.put(hash,node);
        }
    }

    public void removeNode(String node){
        for (int i=0;i<virtualNode;i++){
            int hash=Hash(node+i);
            circle.put(hash,node);
        }
    }

    public String getNode(String key){
        if(circle.isEmpty()){
            return null;
        }

        int hash=Hash(key);
        SortedMap<Integer,String> nodeMap=circle.tailMap(hash);
        int nodeHash=nodeMap.isEmpty()?circle.firstKey():nodeMap.firstKey();
        return circle.get(nodeHash);
    }

}

