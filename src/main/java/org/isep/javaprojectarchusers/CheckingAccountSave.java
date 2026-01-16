package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CheckingAccountSave {
    public static void save(ArrayList<CheckingAccount> checkingAccountArrayList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/checkingAccounts.json"), checkingAccountArrayList);
    }
}
