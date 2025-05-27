package cpt206cw3;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Transaction {
    
    private final UUID id;
    private final double amount;
    private final LocalDateTime dateTime;
    
    public Transaction(double amount, LocalDateTime dateTime) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.dateTime = dateTime;
    }
    
    public UUID getId() {
        return id;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public abstract double getEffectiveAmount();
    
    @Override
    public String toString() {
        return String.format("Transaction[id=%s, amount=%.2f, dateTime=%s]", 
            id, amount, dateTime);
    }
}
