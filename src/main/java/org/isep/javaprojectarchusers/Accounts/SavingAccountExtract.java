package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SavingAccountExtract {
    public static void extract(ArrayList<SavingAccount> savingAccountList) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        savingAccountList = objectMapper.readValue(new File("src/main/resources/org/isep/javaprojectarchusers/savingAccounts.json"), new TypeReference<ArrayList<SavingAccount>>(){});
    }
}
