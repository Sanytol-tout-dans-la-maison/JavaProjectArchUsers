package org.isep.javaprojectarchusers;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Blockchain {
    @JsonProperty("blockchain")
    private static LinkedList<Block> blockchain = new LinkedList<>();


    public Blockchain(){
    }

    public static void addBlock(@JsonProperty("Block") Block block) {
        blockchain.add(block);
    }

    public static @JsonProperty("Block") Block getLast(){
        if(blockchain.isEmpty()) blockchain.add(new Block());
        return blockchain.getLast();
    }

    public static @JsonProperty("blockchain") LinkedList<Block> getBlockchainList() {
        return blockchain;
    }

    public Blockchain getBlockchain(){
        return this;
    }

    public static void setBlockchain(@JsonProperty("blockchain") LinkedList<Block> blockchain) {
        Blockchain.blockchain = blockchain;
    }

    public static void extractBlockchain() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Blockchain.setBlockchain(objectMapper.readValue(new File("src/main/resources/org/isep/javaprojectarchusers/blockchain.json"), new TypeReference<LinkedList<Block>>(){}));
    }
    public static void saveBlockchain() throws IOException {
        blockchain = Blockchain.getBlockchainList();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/blockchain.json"), blockchain);
    }
}
