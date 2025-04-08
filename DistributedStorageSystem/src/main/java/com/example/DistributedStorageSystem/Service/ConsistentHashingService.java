package com.example.DistributedStorageSystem.Service;

import com.example.DistributedStorageSystem.Utils.ConsistentHashing;
import org.springframework.beans.factory.annotation.Autowired;

public class ConsistentHashingService {

    @Autowired
    ConsistentHashing consistentHashing;


    public String getNode(String key){
        return consistentHashing.getNode(key);
    }

    public void addNode(String key){
         consistentHashing.addNode(key);
    }

    public  void deleteNode(String key){
        consistentHashing.removeNode(key);
    }
}
