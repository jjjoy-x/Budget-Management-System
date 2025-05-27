package cpt206cw3;

import java.time.LocalDateTime;

public class Income extends Transaction {
    
    private final String source;
    
    public Income(double amount, LocalDateTime dateTime, String source) {
        super(amount, dateTime);
        this.source = source;
    }
    
    public String getSource() {
        return source;
    }
    
    @Override
    public double getEffectiveAmount() {
        return getAmount(); // Income is always positive
    }
    
    @Override
    public String toString() {
        return String.format("Income[%s, source=%s]", super.toString(), source);
    }
}
