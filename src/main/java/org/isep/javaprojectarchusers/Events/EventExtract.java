package org.isep.javaprojectarchusers.Events;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class EventExtract {

    public static void extract() throws IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue(new File("src/main/resources/org/isep/javaprojectarchusers/events.json"), new TypeReference<ArrayList<Events>>(){});
    }
}