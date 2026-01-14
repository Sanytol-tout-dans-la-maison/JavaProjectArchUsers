package org.isep.javaprojectarchusers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class EventsManager {
    private static ArrayList<Events> eventList = new ArrayList<>();

    public static void createEventsRandom(LocalDate beginDate, LocalDate endDate) throws IOException {
        ArrayList<Asset> assetList = Asset.getAssetList();
        Random randomGenerator = new Random();
        EVENT_TYPE[] eventTypes = EVENT_TYPE.values();
        ArrayList<Events> eventsArrayList = new ArrayList<>();

        for(LocalDate date = beginDate; date.isBefore(endDate); date.plusDays(1)){
            while(randomGenerator.nextBoolean()){
                EVENT_TYPE eventType = eventTypes[randomGenerator.nextInt(0, eventTypes.length-1)];
                eventList.add(new Events(eventType, randomGenerator.nextDouble(0.1, 0.95), date.toString(), assetList.get(randomGenerator.nextInt(0, assetList.size()-1))));
            }
        }
        EventSave.save(eventList);
    }
}
