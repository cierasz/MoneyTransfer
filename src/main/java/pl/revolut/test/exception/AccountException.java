package pl.revolut.test.exception;

public class AccountException extends Exception {

    static final long serialVersionUID = -571150400822803017L;

    public AccountException(String methodName, MoneyTransferAppErrorsMapping moneyTransferAppErrorsMapping) {
        super("Method: " + methodName + "; " + moneyTransferAppErrorsMapping.getDescription());
    }

    public AccountException(String methodName, MoneyTransferAppErrorsMapping moneyTransferAppErrorsMapping, Throwable cause) {
        super("Method: " + methodName + "; " + moneyTransferAppErrorsMapping.getDescription(), cause);
    }

    public AccountException(String methodName, InternalErrors internalErrors) {
        super("Method: " + methodName + "; " + internalErrors.getDescription());
    }

    public AccountException(String methodName, InternalErrors internalErrors, Throwable cause) {
        super("Method: " + methodName + "; " + internalErrors.getDescription(), cause);
    }

}
