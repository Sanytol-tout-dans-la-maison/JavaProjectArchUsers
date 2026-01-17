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

    /**
     * Limit the user can withdraw at once?
     */
    final double WITHDRAWAL_LIMIT;

    public CheckingAccount(String userName, float OVERDRAW_LIMIT, double balance, Portfolio portfolio, double WITHDRAWAL_LIMIT) {
        super(userName, AccountType.CHECKING, OVERDRAW_LIMIT, balance, portfolio);

        this.WITHDRAWAL_LIMIT = WITHDRAWAL_LIMIT;
        checkingAccountArrayList.add(this);
    }

    public double getINTEREST_RATES() {
        return INTEREST_RATES;
    }

    public void setINTEREST_RATES(double INTEREST_RATES) {
        this.INTEREST_RATES = INTEREST_RATES;
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

    @Override
    public boolean withdraw(double amount) {
        if (-WITHDRAWAL_LIMIT <this.getBalance()-amount){
            return false;
        }
        return super.withdraw(amount);
    }


}
