package org.isep.javaprojectarchusers;

public class Stock extends Asset{
    private String companyName;

    public Stock(String companyName, double value){
        super(value);
        this.companyName = companyName;
        }

    public Stock(String companyName){
        super(0.0);
        this.companyName = "Uknow";
    }

    @Override
    public String toString(){
        return this.companyName;
    }
}
