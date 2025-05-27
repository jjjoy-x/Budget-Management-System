package cpt206cw3;

public class BudgetCategory {
    
    private final String name;
    private final double monthlyLimit;
    private double currentExpenditure;
    
    public BudgetCategory(String name, double monthlyLimit) {
        if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (monthlyLimit <= 0) {
            throw new IllegalArgumentException("Monthly limit must be positive");
        }
        this.name = name.trim();
        this.monthlyLimit = monthlyLimit;
        this.currentExpenditure = 0;
    }
    
    private int lastResetMonth = -1;
    
    public void autoResetIfNewMonth(int currentMonth) {
        if (lastResetMonth != currentMonth) {
            resetExpenditure();
            lastResetMonth = currentMonth;
        }
    }

    public String getName() {
        return name;
    }
    
    public double getMonthlyLimit() {
        return monthlyLimit;
    }
    
    public double getCurrentExpenditure() {
        return currentExpenditure;
    }
    
    public void addExpense(double amount) {
        if (amount <= 0) {
        throw new IllegalArgumentException("Expense amount must be positive");
        }
        currentExpenditure += amount;
    }
    
    public boolean isOverLimit() {
        return currentExpenditure > monthlyLimit;
    }
    
    public void resetExpenditure() {
        currentExpenditure = 0;
    }
    
    @Override
    public String toString() {
        return String.format("BudgetCategory[name=%s, limit=%.2f, current=%.2f]", 
            name, monthlyLimit, currentExpenditure);
    }
}
