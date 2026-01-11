package org.isep.javaprojectarchusers;

import java.io.IOException;
import java.util.ArrayList;

public class LoginExtraction {
    public static void extract(ArrayList<String> email, ArrayList<String> password) throws IOException {
        ArrayList<String> data = FileReaderWithBuffer.readCSV("src/main/resources/org/isep/javaprojectarchusers/login.csv");
        for(int i = 0; i < data.size(); i += 2){
            email.add(data.get(i));
            password.add(data.get(i+1));
        }
    }
}
