package org.isep.javaprojectarchusers;

import java.util.ArrayDeque;
import java.util.Deque;

public class Block {
    private Deque<Transaction> transactions;

    public Block() {
        this.transactions = new ArrayDeque<Transaction>();
    }

    public Deque<Transaction> getTransactions() {
        return transactions;
    }

    public void add(Transaction transaction) {
        if (transactions.size() == 10) {
            Block newBlock = new Block();
            newBlock.add(transaction);
            Blockchain.addBlock(newBlock);
        } else {
            transactions.add(transaction);
        }
    }

    /*
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Transaction t : transactions) if(t.getIsepCoins() != 0 && t.isPayed() && t.getOriginWallet() != t.getDestinationWallet()) str.append(t.toString());
        return str.toString();
    }
     */

}