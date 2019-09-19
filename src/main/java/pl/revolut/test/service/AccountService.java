package pl.revolut.test.service;

import pl.revolut.test.dao.DatasetDAO;
import pl.revolut.test.exception.AccountException;
import pl.revolut.test.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    private final DatasetDAO datasetDAO = new DatasetDAO();

    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") Long accountId) throws AccountException {
        return datasetDAO.getAccountManagerDAO().getAccountById(accountId);
    }

    @GET
    @Path("/all")
    public List<Account> getAllAccounts() throws AccountException {
        return datasetDAO.getAccountManagerDAO().getAllAccounts();
    }

    @PUT
    @Path("/add")
    public Long addAccount(Account account) throws AccountException {
        final long accountId = datasetDAO.getAccountManagerDAO().addAccount(account);
        return datasetDAO.getAccountManagerDAO().getAccountById(accountId).getAccountId();
    }

    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") Long accountId) throws AccountException {
        boolean deleteStatus = datasetDAO.getAccountManagerDAO().deleteAccountById(accountId);
        if (deleteStatus) {
            return Response.status(200).build();
        } else {
            return Response.status(404).build();
        }
    }
}
