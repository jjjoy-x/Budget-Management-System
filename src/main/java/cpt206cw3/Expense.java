package cpt206cw3;

import java.time.LocalDateTime;

public class Expense extends Transaction {

    public enum PaymentMethod {
        CASH(0), 
        CARD(0.01), 
        ALIPAY(0.005), 
        WECHAT(0.005);
        
        private final double feePercentage;
        
        PaymentMethod(double feePercentage) {
            this.feePercentage = feePercentage;
        }
        
        public double getFeePercentage() {
            return feePercentage;
        }
    }
    
    private final PaymentMethod paymentMethod;
    private final String category;
    
    public Expense(double amount, LocalDateTime dateTime, 
                  PaymentMethod paymentMethod, String category) {
        super(amount, dateTime);
        this.paymentMethod = paymentMethod;
        this.category = category;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getCategory() {
        return category;
    }
    
    @Override
    public double getEffectiveAmount() {
        double fee = getAmount() * paymentMethod.getFeePercentage();
        return -(getAmount() + fee); // Expense is always negative
    }
    
    @Override
    public String toString() {
        return String.format("Expense[%s, method=%s, category=%s]", 
            super.toString(), paymentMethod, category);
    }
    
}
