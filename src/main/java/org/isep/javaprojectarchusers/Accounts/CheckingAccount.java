package org.isep.javaprojectarchusers.Accounts;

import org.isep.javaprojectarchusers.Portfolio;

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
    }

    @Override
    public boolean withdraw(double amount) {
        if (-WITHDRAWAL_LIMIT <this.getBalance()-amount){
            return false;
        }
        return super.withdraw(amount);
    }


}
