package pl.revolut.test.dao;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import pl.revolut.test.dao.interfaces.AccountManagerDAO;
import pl.revolut.test.dao.utils.AccountUtil;
import pl.revolut.test.exception.AccountException;
import pl.revolut.test.exception.InternalErrors;
import pl.revolut.test.exception.MoneyTransferAppErrorsMapping;
import pl.revolut.test.model.Account;
import pl.revolut.test.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements AccountManagerDAO {

    private static Logger log = Logger.getLogger(AccountDAO.class);

    @Override
    public Account getAccountById(Long accountId) throws AccountException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        log.info("Preparing to reach the account with id " + accountId);
        try {
            connection = DatasetDAO.getConnection();
            preparedStatement = connection.prepareStatement(AccountUtil.SqlStatement.GET_ACC_BY_ID);
            preparedStatement.setLong(1, accountId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account(
                        resultSet.getLong("ACC_ID"),
                        resultSet.getString("ACC_EMAIL"),
                        resultSet.getBigDecimal("ACC_BALANCE"));
            }
            if (account == null) {
                log.warn("No account found by id: " + accountId);
            }
            return account;
        } catch (SQLException e) {
            log.error(InternalErrors.ACCOUNT_GET_DATASET_ERROR, e);
            throw new AccountException("getAccountById", InternalErrors.ACCOUNT_GET_DATASET_ERROR);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<Account> getAllAccounts() throws AccountException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Account> accounts = new ArrayList<>();
        try {
            connection = DatasetDAO.getConnection();
            preparedStatement = connection.prepareStatement(AccountUtil.SqlStatement.GET_ALL_ACC);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(
                        new Account(
                                resultSet.getLong("ACC_ID"),
                                resultSet.getString("ACC_EMAIL"),
                                resultSet.getBigDecimal("ACC_BALANCE")
                        )
                );
            }
            if (accounts.isEmpty()) {
                log.warn("No accounts found");
            } else {
                log.info("Found " + accounts.size() + " accounts");
            }
            return accounts;
        } catch (SQLException e) {
            log.error(InternalErrors.ACCOUNT_GET_DATASET_ERROR, e);
            throw new AccountException("getAccountById", InternalErrors.ACCOUNT_GET_DATASET_ERROR);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Long addAccount(Account account) throws AccountException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        log.info("Preparing to add an account with an email " + account.getEmailAddress());
        try {
            connection = DatasetDAO.getConnection();
            preparedStatement = connection.prepareStatement(AccountUtil.SqlStatement.ADD_ACC);
            preparedStatement.setString(1, account.getEmailAddress());
            preparedStatement.setBigDecimal(2, account.getAccountBalance());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                long newAccId = resultSet.getLong(1);
                log.info("The account has been created with an id: " + newAccId);
                return newAccId;
            } else {
                log.error(InternalErrors.ACCOUNT_ID_DATASET_ERROR);
                throw new AccountException("addAccount", InternalErrors.ACCOUNT_ID_DATASET_ERROR);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == MoneyTransferAppErrorsMapping.ACCOUNT_UNIQUE_ERROR.getCode()) {
                log.error(MoneyTransferAppErrorsMapping.ACCOUNT_UNIQUE_ERROR, e);
                throw new AccountException("addAccount", MoneyTransferAppErrorsMapping.ACCOUNT_UNIQUE_ERROR);
            } else {
                log.error(InternalErrors.ACCOUNT_CREATE_DATASET_ERROR, e);
                throw new AccountException("addAccount", InternalErrors.ACCOUNT_CREATE_DATASET_ERROR);
            }
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public boolean deleteAccountById(Long accountId) throws AccountException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        log.info("Preparing to delete the account with id " + accountId);
        try {
            connection = DatasetDAO.getConnection();
            preparedStatement = connection.prepareStatement(AccountUtil.SqlStatement.DELETE_ACC_BY_ID);
            preparedStatement.setLong(1, accountId);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error(InternalErrors.ACCOUNT_DELETE_DATASET_ERROR + accountId.toString(), e);
            throw new AccountException("deleteAccountById", InternalErrors.ACCOUNT_DELETE_DATASET_ERROR);
        } finally {
            DbUtils.closeQuietly(connection);
            DbUtils.closeQuietly(preparedStatement);
        }
    }

    @Override
    public int transferAccountBalance(Transaction transaction) throws AccountException, SQLException {
        int result = 0;
        Connection connection = null;
        PreparedStatement lockAccount = null;
        PreparedStatement updateAccount = null;
        ResultSet resultSet = null;

        try {
            connection = DatasetDAO.getConnection();
            connection.setAutoCommit(false);

            lockAccount = connection.prepareStatement(AccountUtil.SqlStatement.LOCK_ACC_BY_ID);
            lockAccount.setLong(1, transaction.getSourceAccountId());
            lockAccount.setLong(2, transaction.getDestAccountId());
            resultSet = lockAccount.executeQuery();
            Account sourceAccount = getAccountForMoneyTransfer(resultSet);
            Account destAccount = getAccountForMoneyTransfer(resultSet);

            if (sourceAccount == null || destAccount == null) {
                log.error(InternalErrors.ACCOUNT_TRANSFER_PREPARE_ERROR);
                throw new AccountException("transferAccountBalance", InternalErrors.ACCOUNT_TRANSFER_PREPARE_ERROR);
            }

            BigDecimal sourceAccountBalanceDiff = sourceAccount.getAccountBalance().subtract(transaction.getAmount());
            if (sourceAccountBalanceDiff.compareTo(BigDecimal.ZERO) < 0) {
                log.error(InternalErrors.ACCOUNT_TRANSFER_NOT_ENOUGH_MONEY_ERROR);
                throw new AccountException("transferAccountBalance", InternalErrors.ACCOUNT_TRANSFER_NOT_ENOUGH_MONEY_ERROR);
            }

            updateAccount = connection.prepareStatement(AccountUtil.SqlStatement.CHANGE_ACC_BALANCE_BY_ID);
            prepareBatchForMoneyTransfer(updateAccount, sourceAccountBalanceDiff, transaction.getSourceAccountId());
            prepareBatchForMoneyTransfer(updateAccount, destAccount.getAccountBalance().add(transaction.getAmount()), transaction.getDestAccountId());

            int[] numberOfUpdated = updateAccount.executeBatch();
            result = numberOfUpdated.length;
            connection.commit();
            log.info("Successful transaction between " + sourceAccount.toString() + " and " + destAccount.toString() + ". Amount: " + transaction.getAmount());
        } catch (SQLException e) {
            if (connection != null) {
                log.warn("Trying to rollback transaction due to: ", e);
                connection.rollback();
            }

        } finally {
            DbUtils.closeQuietly(connection);
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(lockAccount);
            DbUtils.closeQuietly(updateAccount);
        }
        return result;
    }

    private void prepareBatchForMoneyTransfer(PreparedStatement updateAccount, BigDecimal sourceAccountBalanceDiff, Long sourceAccountId) throws SQLException {
        updateAccount.setBigDecimal(1, sourceAccountBalanceDiff);
        updateAccount.setLong(2, sourceAccountId);
        updateAccount.addBatch();
    }

    private Account getAccountForMoneyTransfer(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Account(
                    resultSet.getLong("ACC_ID"),
                    resultSet.getString("ACC_EMAIL"),
                    resultSet.getBigDecimal("ACC_BALANCE"));
        }
        return null;
    }
}
