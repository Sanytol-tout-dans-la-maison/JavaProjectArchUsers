package org.isep.javaprojectarchusers;

import org.isep.javaprojectarchusers.ReadWriteFile.FileReaderWithBuffer;

import java.io.IOException;
import java.util.ArrayList;

public class LoginExtraction {
    public static void extract(ArrayList<String> email, ArrayList<String> password, ArrayList<String> key) throws IOException {
        ArrayList<String> data = FileReaderWithBuffer.readCSV("src/main/resources/org/isep/javaprojectarchusers/GUI/login.csv");
        for(int i = 0; i < data.size(); i += 3){
            email.add(data.get(i));
            password.add(data.get(i+1));
            key.add(data.get(i+2));
        }
    }
}
