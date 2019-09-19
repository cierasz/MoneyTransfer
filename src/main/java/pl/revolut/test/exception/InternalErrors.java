package pl.revolut.test.exception;

public enum InternalErrors {

    ACCOUNT_ID_DATASET_ERROR("Cannot create an account, because it is impossible to reach the new id."),
    ACCOUNT_CREATE_DATASET_ERROR("Cannot create an account due to problems with dataset: "),
    ACCOUNT_DELETE_DATASET_ERROR("Cannot delete account id: "),
    ACCOUNT_TRANSFER_PREPARE_ERROR("Cannot prepare both accounts for money transfer."),
    ACCOUNT_TRANSFER_NOT_ENOUGH_MONEY_ERROR("Cannot prepare both accounts for money transfer."),
    ACCOUNT_GET_DATASET_ERROR("Cannot reach the account due to problems with dataset: ");

    private String description;

    InternalErrors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
