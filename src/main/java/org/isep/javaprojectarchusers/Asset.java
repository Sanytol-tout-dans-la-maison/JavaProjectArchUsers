package org.isep.javaprojectarchusers;

public class Asset {
    private double value;

    public Asset(){
        this.value = 0.0;
    }

    public Asset(double value){
        this.value = value;
    }

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
