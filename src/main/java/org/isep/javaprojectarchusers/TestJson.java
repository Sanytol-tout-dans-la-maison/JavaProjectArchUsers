package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TestJson {
    public static void main() throws IOException {
        ArrayList<Asset> assetList = Asset.getAssetList();
        Events event = new Events(EVENT_TYPE.Crash, 0.7, LocalDate.of(2026,01,12), assetList.getFirst());
        Events event2 = new Events(EVENT_TYPE.Covid_19, 0.5, LocalDate.of(2019,10,1), assetList.getFirst());
        ArrayList<Events> list= new ArrayList<Events>();
        list.add(event);
        list.add(event2);
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
        objectMapper.writeValue(file, list);
//        //File file = new File("src/main/resources/org/isep/javaprojectarchusers/events.json");
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ArrayList<Events> copyList = objectMapper.readValue(file, new TypeReference<ArrayList<Events>>(){});
        for (Events e : copyList) System.out.println(e);

    }
}
