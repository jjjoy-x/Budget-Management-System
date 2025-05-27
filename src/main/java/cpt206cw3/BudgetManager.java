package cpt206cw3;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {

    private final List<Transaction> transactions;
    
    private final Map<String, BudgetCategory> categories;
    
    public BudgetManager() {
        this.transactions = new ArrayList<>();
        this.categories = new HashMap<>();
    }
    
    public void addIncome(double amount, LocalDateTime dateTime, String source) {
        Income income = new Income(amount, dateTime, source);
        transactions.add(income);
    }
    
    public void resetCategoriesIfNewMonth(LocalDateTime now) {
        int month = now.getMonthValue();
        for (BudgetCategory cat : categories.values()) {
            cat.autoResetIfNewMonth(month);
        }
    }

    public void addExpense(double amount, LocalDateTime dateTime, 
                         Expense.PaymentMethod method, String categoryName) 
        throws MonthlyLimitExceededException {
        
        resetCategoriesIfNewMonth(dateTime);
        
        if (!categories.containsKey(categoryName)) {
            throw new IllegalArgumentException("Category does not exist: " + categoryName);
        }
        
        BudgetCategory category = categories.get(categoryName);
        Expense expense = new Expense(amount, dateTime, method, categoryName);
        
        if (category.getCurrentExpenditure() + expense.getAmount() > category.getMonthlyLimit()) {
            throw new MonthlyLimitExceededException(
                categoryName, 
                category.getMonthlyLimit(), 
                category.getCurrentExpenditure() + expense.getAmount());
        }
        
        transactions.add(expense);
        category.addExpense(expense.getAmount());
    }
    
    public void addCategory(String name, double monthlyLimit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (monthlyLimit <= 0) {
            throw new IllegalArgumentException("Monthly limit must be positive");
        }
        if (categories.containsKey(name)) {
            throw new IllegalArgumentException("Category already exists: " + name);
        }
        categories.put(name, new BudgetCategory(name, monthlyLimit));
    }
    
    public void removeCategory(String name) {
        if (!categories.containsKey(name)) {
            throw new IllegalArgumentException("Category does not exist: " + name);
        }
        categories.remove(name);
    }
    
    public List<Expense> getExpensesByCategory(String categoryName) {
        List<Expense> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t instanceof Expense) {
                Expense e = (Expense) t;
                if (e.getCategory().equals(categoryName)) {
                    result.add(e);
                }
            }
        }
        sortByDateTime(result);
        return result;
    }
    
    public List<Expense> getExpensesExceedingAmount(double amount) {
        List<Expense> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t instanceof Expense && t.getAmount() > amount) {
                result.add((Expense) t);
            }
        }
        sortByDateTime(result);
        return result;
    }
    
    public List<Transaction> getTransactionsInPeriod(LocalDateTime start, LocalDateTime end) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.getDateTime().isBefore(start) && !t.getDateTime().isAfter(end)) {
                result.add(t);
            }
        }
        sortByDateTime(result);
        return result;
    }
    
    public double getNetEarnings(LocalDateTime start, LocalDateTime end) {
        double total = 0;
        for (Transaction t : getTransactionsInPeriod(start, end)) {
            total += t.getEffectiveAmount();
        }
        return total;
    }
    
    private <T extends Transaction> void sortByDateTime(List<T> transactions) {
        Collections.sort(transactions, Comparator.comparing(Transaction::getDateTime));
    }
    
    public List<BudgetCategory> getAllCategories() {
        return new ArrayList<>(categories.values());
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
}
