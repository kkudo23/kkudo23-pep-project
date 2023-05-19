package DAO;

import Model.Account;

public interface AccountDAO {
    Account createAccount(Account account);

    Account getAccountByUsername(String username);

    Account getAccountByUsernameAndPassword(String username, String password);

    Account getAccountById(int accountId);

}
