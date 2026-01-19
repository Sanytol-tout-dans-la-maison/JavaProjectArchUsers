package org.isep.javaprojectarchusers.Events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Assets.Asset;
import org.isep.javaprojectarchusers.Assets.GeneralAssets;

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
    @JsonProperty("generalAsset")
    private GeneralAssets generalAsset;

    private static ArrayList<Events> eventList = new ArrayList<>();

    @JsonCreator
    public Events(@JsonProperty("eventType") EVENT_TYPE eventType, @JsonProperty("valueImpact") double valueImpact, @JsonProperty("date") String date, @JsonProperty("generalAsset") GeneralAssets generalAsset){
        this.eventType = eventType;
        this.valueImpact = valueImpact;
        this.date = date;
        this.generalAsset = generalAsset;
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

    public @JsonProperty("generalAsset")GeneralAssets getGeneralAsset() {
        return generalAsset;
    }

    public void setGeneralAsset(@JsonProperty("generalAsset")GeneralAssets generalAsset) {
        this.generalAsset = generalAsset;
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
