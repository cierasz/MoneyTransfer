package pl.revolut.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transaction {

    @JsonProperty(required = true)
    private BigDecimal amount;

    @JsonProperty(required = true)
    private Long sourceAccountId;

    @JsonProperty(required = true)
    private Long destAccountId;

    public Transaction() {}

    public Transaction(BigDecimal amount, Long sourceAccountId, Long destAccountId) {
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.destAccountId = destAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getDestAccountId() {
        return destAccountId;
    }

    public void setDestAccountId(Long destAccountId) {
        this.destAccountId = destAccountId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", sourceAccountId=" + sourceAccountId +
                ", destAccountId=" + destAccountId +
                '}';
    }
}
