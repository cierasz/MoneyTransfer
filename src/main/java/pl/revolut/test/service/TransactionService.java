package pl.revolut.test.service;

import pl.revolut.test.dao.DatasetDAO;
import pl.revolut.test.exception.AccountException;
import pl.revolut.test.model.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/transaction")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

    private final DatasetDAO datasetDAO = new DatasetDAO();

    @POST
    public Response transferMoney(Transaction transaction) throws AccountException, SQLException {
        int updateNo = datasetDAO.getAccountManagerDAO().transferAccountBalance(transaction);
        if (updateNo == 2) {
            return Response.status(200).build();
        } else {
            throw new WebApplicationException("Transaction error", 400);
        }
    }

}
