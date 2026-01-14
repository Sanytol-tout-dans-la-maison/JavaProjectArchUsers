package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Events {

    @JsonProperty("valueImpact")
    private double valueImpact;
    @JsonProperty("type")
    private EVENT_TYPE eventType;
    @JsonProperty("date")
    private LocalDate date;

    @JsonCreator
    public Events(@JsonProperty("eventType") EVENT_TYPE eventType, @JsonProperty("valueImpact") double valueImpact, @JsonProperty("date") LocalDate date){
        this.eventType = eventType;
        this.valueImpact = valueImpact;
        this.date = date;
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

    @Override
    public String toString() {
        return eventType.toString() + " , " + valueImpact + " , " + date.toString();
    }
}
