package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;

public class Events {

    @JsonProperty("valueImpact")
    private double valueImpact;
    @JsonProperty("type")
    private EVENT_TYPE eventType;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("asset")
    private Asset asset;

    private static ArrayList<Events> eventList = new ArrayList<>();

    @JsonCreator
    public Events(@JsonProperty("eventType") EVENT_TYPE eventType, @JsonProperty("valueImpact") double valueImpact, @JsonProperty("date") LocalDate date, @JsonProperty("asset") Asset asset){
        this.eventType = eventType;
        this.valueImpact = valueImpact;
        this.date = date;
        this.asset = asset;
        eventList.add(this);
    }

    public EVENT_TYPE getEventType(){
        return eventType;
    }

    public void setTest(EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public double getValueImpact() {
        return valueImpact;
    }
    public void setValueImpact(double valueImpact) {
        this.valueImpact = valueImpact;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public static ArrayList<Events> getEventList() {
        return eventList;
    }

    @Override
    public String toString() {
        return eventType.toString() + " , " + valueImpact + " , " + date.toString();
    }
}
