package org.isep.javaprojectarchusers.Events;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;


public class EventExtract {

    public static void extract(ArrayList<Events> eventsList) throws IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        eventsList = objectMapper.readValue("src/main/resources/org/isep/javaprojectarchusers/events.csv", new TypeReference<ArrayList<Events>>(){});
    }
}