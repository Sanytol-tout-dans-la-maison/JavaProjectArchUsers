package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Portfolio;

import java.util.ArrayList;

/**
 * {@code CheckingAccount} is a subclass of {@link Account} that add functionalities specifically for paying.
 * <p>For now nothing special except for interest rates and withdrawal limit.</p>
 */
public class CheckingAccount extends Account {
    /**idk what this is*/

    /**Limit the user can withdraw at once?*/
    double WITHDRAWAL_LIMIT;

    private static ArrayList<CheckingAccount> checkingAccountArrayList = new ArrayList<>();

    public CheckingAccount(@JsonProperty("userName") String userName, @JsonProperty("OVERDRAW_LIMIT") float OVERDRAW_LIMIT, @JsonProperty("balance") double balance, @JsonProperty("portfolio") String portfolio, @JsonProperty("INTEREST_RATES") double INTEREST_RATES, @JsonProperty("WITHDRAWAL_LIMIT") double WITHDRAWAL_LIMIT) {
        super(userName, AccountType.CHECKING, OVERDRAW_LIMIT, balance, portfolio);
        this.WITHDRAWAL_LIMIT = WITHDRAWAL_LIMIT;
        checkingAccountArrayList.add(this);
    }


    public void setWITHDRAWAL_LIMIT(double WITHDRAWAL_LIMIT) {
        this.WITHDRAWAL_LIMIT = WITHDRAWAL_LIMIT;
    }

    @Override
    public float getOVERDRAW_LIMIT() {
        return super.getOVERDRAW_LIMIT();
    }

    public static ArrayList<CheckingAccount> getCheckingAccountArrayList() {
        return checkingAccountArrayList;
    }
}
