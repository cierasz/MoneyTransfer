package pl.revolut.test.dao.interfaces;

import pl.revolut.test.exception.AccountException;
import pl.revolut.test.model.Account;
import pl.revolut.test.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface AccountManagerDAO {

    Account getAccountById(Long accountId) throws AccountException;

    List<Account> getAllAccounts() throws AccountException;

    Long addAccount(Account account) throws AccountException;

    int transferAccountBalance(Transaction transaction) throws AccountException, SQLException;

    boolean deleteAccountById(Long accountId) throws AccountException;

}
