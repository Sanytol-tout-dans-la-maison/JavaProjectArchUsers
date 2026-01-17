package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.isep.javaprojectarchusers.Accounts.SavingAccount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SavingAccountSave {
    public static void save(ArrayList<SavingAccount> savingAccountArrayList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("src/main/resources/org/isep/javaprojectarchusers/savingAccounts.json"), savingAccountArrayList);
    }
}
