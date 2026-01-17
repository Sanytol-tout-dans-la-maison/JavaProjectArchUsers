package org.isep.javaprojectarchusers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginSave {
    public static void save(ArrayList<String> email, ArrayList<String> password, ArrayList<String> keyList) throws IOException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < email.size(); i++){
            builder.append(email.get(i) + "," + password.get(i) + "," + keyList.get(i) + "\n");
        }
        WriteToFile.write("src/main/resources/org/isep/javaprojectarchusers/GUI/login.csv",builder.toString());
    }

}
