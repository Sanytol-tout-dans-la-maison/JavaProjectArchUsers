package org.isep.javaprojectarchusers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.isep.javaprojectarchusers.Accounts.CheckingAccount;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CheckingAccountExtract {
    public static void extract(ArrayList<CheckingAccount> checkingAccountList) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        checkingAccountList = objectMapper.readValue(new File("src/main/resources/org/isep/javaprojectarchusers/checkingAccounts.json"), new TypeReference<ArrayList<CheckingAccount>>(){});
    }
}
