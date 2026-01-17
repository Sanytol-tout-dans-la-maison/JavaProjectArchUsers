package org.isep.javaprojectarchusers.ReadWriteFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FileReaderWithBuffer {

    public static ArrayList<String> readCSV(String file) throws IOException{

        ArrayList<String> tempdata = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String curLine;
        while ((curLine = bufferedReader.readLine()) != null){      //process the line as required
            String[] temp = curLine.split(",");
            for(String a : temp) tempdata.add(a);
        }
        bufferedReader.close();
        return tempdata;
    }
}