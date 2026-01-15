package org.isep.javaprojectarchusers;
import java.util.LinkedList;

public class Blockchain {
    private static LinkedList<Block> blockchain = new LinkedList<>();

    public static void addBlock(Block block) {
        blockchain.add(block);
    }

    public static Block getLast(){
        if(blockchain.isEmpty()) blockchain.add(new Block());
        return blockchain.getLast();
    }
}
