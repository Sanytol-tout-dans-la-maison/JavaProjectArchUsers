package org.isep.javaprojectarchusers.Accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isep.javaprojectarchusers.Portfolio;

import java.util.ArrayList;

/**
 * {@code SavingAccount} is a subclass of {@link Account} that add functionalities specifically for saving.
 * <p>It's main new thing is having {@code calculateRetirement}.</p>
 */
public class SavingAccount extends Account {
    /**This is the rent the account user has to pay per month?*/
    @JsonProperty("RENT")
    private float RENT ;
    private static ArrayList<SavingAccount> savingAccountArrayList = new ArrayList<>();

    public SavingAccount(@JsonProperty("userName") String userName, @JsonProperty("OVERDRAW_LIMIT") float OVERDRAW_LIMIT, @JsonProperty("balance") double balance, @JsonProperty("portfolio") String portfolio, @JsonProperty("RENT") float RENT) {
        super(userName, AccountType.SAVING ,OVERDRAW_LIMIT, balance, portfolio);
        this.RENT = RENT;
        savingAccountArrayList.add(this);
    }

    public @JsonProperty("RENT") float getRENT() {
        return RENT;
    }

    public void setRENT(@JsonProperty("RENT") float RENT) {
        this.RENT = RENT;
    }

    /** What is this class supposed to do? does it have to calculate how much is left for retirement?
     * Do we get users age?
     * @return Retirement money, if I understand this correctly?
     */
    public double calculateRetirement() {

        return 0.0;
    }

    public static ArrayList<SavingAccount> getSavingAccountArrayList() {
        return savingAccountArrayList;
    }
}
