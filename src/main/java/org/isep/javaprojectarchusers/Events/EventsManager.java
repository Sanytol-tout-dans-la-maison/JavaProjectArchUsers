package org.isep.javaprojectarchusers.Events;

import org.isep.javaprojectarchusers.Assets.Asset;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class EventsManager {
    /**
     * Use this function to generate events randomly between two DateTimes.
     */
    private static ArrayList<Events> eventList = new ArrayList<>();
    private static HashMap<LocalDate,ArrayList<Events>> hashMapEvents = new HashMap<>();

    public static void createEventsRandom(LocalDate beginDate, LocalDate endDate) throws IOException {
        eventList = Events.getEventList();
        ArrayList<Asset> assetList = Asset.getAssetList();
        Random randomGenerator = new Random();
        EVENT_TYPE[] eventTypes = EVENT_TYPE.values();
        for(LocalDate date = beginDate; date.isBefore(endDate); date.plusDays(1)){
            while(randomGenerator.nextBoolean()){
                EVENT_TYPE eventType = eventTypes[randomGenerator.nextInt(0, eventTypes.length-1)];
                eventList.add(new Events(eventType, randomGenerator.nextDouble(0.1, 0.95), date.toString(), assetList.get(randomGenerator.nextInt(0, assetList.size()-1))));
            }
        }
        Events.setEventList(eventList);
        EventSave.save(eventList);
    }

    /**
     * @return a hashmap that associates DateTime with its events. Use this in the code to quickly search for any event at a given date
     */
    public void sortEventsbyDate(){
        eventList = Events.getEventList();
        for(Events e : eventList){
            LocalDate tempDate = LocalDate.parse(e.getDate());
            if(!hashMapEvents.containsKey(tempDate)) hashMapEvents.put((tempDate), new ArrayList<Events>());
            hashMapEvents.get(tempDate).add(e);
        };
    }

    public static HashMap<LocalDate, ArrayList<Events>> getHashMapEvents() {
        return hashMapEvents;
    }
}
