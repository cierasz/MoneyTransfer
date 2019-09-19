package pl.revolut.test.exception;

public enum MoneyTransferAppErrorsMapping {

    ACCOUNT_UNIQUE_ERROR(23505, "Cannot create an account due to unique index: ");

    private final int code;
    private final String description;

    MoneyTransferAppErrorsMapping(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
