package org.isep.javaprojectarchusers.Events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Assets.Asset;

import java.util.ArrayList;

/**
 * Do not touch this class. If you wish to implement more events, just go to EVENT_TYPE enumerations.
 * Same for ASSET_TYPE.
 */
public class Events {

    @JsonProperty("valueImpact")
    private double valueImpact;
    @JsonProperty("eventType")
    private EVENT_TYPE eventType;
    @JsonProperty("date")
    private String date;
    @JsonProperty("asset")
    private Asset asset;

    private static ArrayList<Events> eventList = new ArrayList<>();

    @JsonCreator
    public Events(@JsonProperty("eventType") EVENT_TYPE eventType, @JsonProperty("valueImpact") double valueImpact, @JsonProperty("date") String date, @JsonProperty("asset") Asset asset){
        this.eventType = eventType;
        this.valueImpact = valueImpact;
        this.date = date;
        this.asset = asset;
        eventList.add(this);
    }

    public @JsonProperty("eventType")EVENT_TYPE getEventType(){
        return eventType;
    }

    public void setEventType(@JsonProperty("eventType")EVENT_TYPE eventType) {
        this.eventType = eventType;
    }

    public @JsonProperty("valueImpact")double getValueImpact() {
        return valueImpact;
    }
    public void setValueImpact(@JsonProperty("valueImpact")double valueImpact) {
        this.valueImpact = valueImpact;
    }

    public @JsonProperty("date")String getDate() {
        return date;
    }

    public void setDate(@JsonProperty("date")String date) {
        this.date = date;
    }

    public @JsonProperty("asset")Asset getAsset() {
        return asset;
    }

    public void setAsset(@JsonProperty("asset")Asset asset) {
        this.asset = asset;
    }

    public static ArrayList<Events> getEventList() {
        return eventList;
    }

    public static void setEventList(ArrayList<Events> eventList) {
        Events.eventList = eventList;
    }

    @Override
    public String toString() {
        return eventType.toString() + " , " + valueImpact + " , " + date.toString();
    }
}
