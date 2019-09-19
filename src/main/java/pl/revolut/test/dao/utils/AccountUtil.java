package pl.revolut.test.dao.utils;

public class AccountUtil {

    public interface SqlStatement {
        String ADD_ACC = "INSERT INTO ACCOUNT (ACC_EMAIL, ACC_BALANCE) VALUES (?, ?)";
        String GET_ACC_BY_ID = "SELECT * FROM ACCOUNT WHERE ACC_ID = ?";
        String GET_ALL_ACC = "SELECT * FROM ACCOUNT";
        String DELETE_ACC_BY_ID = "DELETE FROM ACCOUNT WHERE ACC_ID = ?";
        String LOCK_ACC_BY_ID = "SELECT * FROM ACCOUNT WHERE ACC_ID IN (?, ?) FOR UPDATE";
        String CHANGE_ACC_BALANCE_BY_ID = "UPDATE ACCOUNT SET ACC_BALANCE = ? WHERE ACC_ID = ?";
    }

}
