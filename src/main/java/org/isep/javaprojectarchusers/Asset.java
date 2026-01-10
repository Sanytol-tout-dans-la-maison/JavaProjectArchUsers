package org.isep.javaprojectarchusers;

public class Asset {
    private double value;

    public String getInfo(){
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString(){
        return this.getInfo();
    }
}
