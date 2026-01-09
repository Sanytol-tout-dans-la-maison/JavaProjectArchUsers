package org.isep.javaprojectarchusers;

public class Stock extends Asset{
    private String companyName;

    @Override
    public String toString(){
        return this.companyName;
    }
}
