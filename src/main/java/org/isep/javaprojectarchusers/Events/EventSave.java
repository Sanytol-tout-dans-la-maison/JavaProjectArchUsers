package org.isep.javaprojectarchusers.Events;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EventSave {
    public static void save(ArrayList<Events> eventsList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/events.json"), eventsList);

    }
}
