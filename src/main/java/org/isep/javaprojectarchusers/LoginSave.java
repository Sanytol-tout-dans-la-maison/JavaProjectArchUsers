package org.isep.javaprojectarchusers;

import java.io.IOException;
import java.util.ArrayList;

public class LoginSave {
    public static void save(ArrayList<String> email, ArrayList<String> password) throws IOException {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < email.size(); i++){
            builder.append(email.get(i) + "," + password.get(i) + "\n");
        }
        WriteToFile.write("src/main/resources/org/isep/javaprojectarchusers/login.csv",builder.toString());
    }

}
