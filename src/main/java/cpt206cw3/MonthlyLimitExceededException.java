package cpt206cw3;

public class MonthlyLimitExceededException extends Exception {

    private final String categoryName;
    private final double limit;
    private final double attemptedAmount;
    
    public MonthlyLimitExceededException(String categoryName, 
                                       double limit, 
                                       double attemptedAmount) {
        super(String.format("Category '%s' would exceed monthly limit (%.2f) with this expense (%.2f)", 
            categoryName, limit, attemptedAmount));
        this.categoryName = categoryName;
        this.limit = limit;
        this.attemptedAmount = attemptedAmount;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public double getLimit() {
        return limit;
    }
    
    public double getAttemptedAmount() {
        return attemptedAmount;
    }
}
