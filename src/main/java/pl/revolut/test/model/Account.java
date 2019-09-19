package pl.revolut.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountId;

    @JsonProperty(required = true)
    private String emailAddress;

    @JsonProperty(required = true)
    private BigDecimal accountBalance;

    public Account() {}

    public Account(String emailAddress, BigDecimal accountBalance) {
        this.emailAddress = emailAddress;
        this.accountBalance = accountBalance;
    }

    public Account(Long accountId, String emailAddress, BigDecimal accountBalance) {
        this.accountId = accountId;
        this.emailAddress = emailAddress;
        this.accountBalance = accountBalance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", emailAddress='" + emailAddress + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
