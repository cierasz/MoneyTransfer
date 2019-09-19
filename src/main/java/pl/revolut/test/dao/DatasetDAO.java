package pl.revolut.test.dao;

import org.apache.commons.dbutils.DbUtils;
import org.h2.tools.RunScript;
import pl.revolut.test.dao.interfaces.AccountManagerDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatasetDAO {
    private static final String h2_driver = "org.h2.Driver";
    private static final String h2_conn_url = "jdbc:h2:mem:moneytransfer;DB_CLOSE_DELAY=-1";
    private static final String h2_user = "admin";
    private static final String h2_password = "admin";

    private final AccountManagerDAO accountManagerDAO = new AccountDAO();

    public DatasetDAO() {
        DbUtils.loadDriver(h2_driver);
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(h2_conn_url, h2_user, h2_password);
    }

    public void initData() {
        Connection conn = null;
        try {
            conn = DatasetDAO.getConnection();
            RunScript.execute(conn, new FileReader("src/main/resources/init.sql"));
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public AccountManagerDAO getAccountManagerDAO() {
        return accountManagerDAO;
    }
}
