package cpt206cw3;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class BudgetCLI {
    private final BudgetManager manager;
    private final Scanner scanner;
    private boolean running;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    
    public BudgetCLI() {
        this.manager = new BudgetManager();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        System.out.println("=== Budget Planning and Management System ===");
        System.out.println("Type 'help' for a list of commands");
        
        while (running) {
            System.out.print("\n> ");
            String command = scanner.nextLine().trim().toLowerCase();
            
            try {
                switch (command) {
                    case "help":
                        showHelp();
                        break;
                    case "add income":
                        addIncome();
                        break;
                    case "add expense":
                        addExpense();
                        break;
                    case "add category":
                        addCategory();
                        break;
                    case "remove category":
                        removeCategory();
                        break;
                    case "list categories":
                        listCategories();
                        break;
                    case "list transactions":
                        listTransactions();
                        break;
                    case "list expenses by category":
                        listExpensesByCategory();
                        break;
                    case "list large expenses":
                        listLargeExpenses();
                        break;
                    case "list transactions by date":
                        listTransactionsByDate();
                        break;
                    case "net earnings":
                        calculateNetEarnings();
                        break;
                    case "reset category":
                        resetCategory();
                        break;
                    case "exit":
                        exit();
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  help                      - Show this help message");
        System.out.println("  add income                - Add a new income transaction");
        System.out.println("  add expense               - Add a new expense transaction");
        System.out.println("  add category              - Add a new budget category");
        System.out.println("  remove category           - Remove a budget category");
        System.out.println("  list categories           - List all budget categories");
        System.out.println("  list transactions         - List all transactions");
        System.out.println("  list expenses by category - List expenses for a category");
        System.out.println("  list large expenses       - List expenses exceeding amount");
        System.out.println("  list transactions by date - List transactions in date range");
        System.out.println("  net earnings              - Calculate net earnings for period");
        System.out.println("  reset category            - Reset expenditure for a category");
        System.out.println("  exit                      - Exit the program");
    }
    
    private void addIncome() {
        System.out.println("\nAdd New Income");
        
        double amount = promptDouble("Amount: ");
        LocalDateTime dateTime = promptDateTime();
        String source = promptString("Source (salary/gift/bonus/other): ");
        
        manager.addIncome(amount, dateTime, source);
        System.out.println("Income added successfully.");
    }
    
    private void addExpense() {
        System.out.println("\nAdd New Expense");
        
        if (manager.getAllCategories().isEmpty()) {
            System.out.println("No categories exist. Please create a category first.");
            return;
        }
        
        double amount = promptDouble("Amount: ");
        LocalDateTime dateTime = promptDateTime();
        
        // Show available payment methods
        System.out.println("Available payment methods:");
        for (Expense.PaymentMethod method : Expense.PaymentMethod.values()) {
            System.out.printf("- %s (%.1f%% fee)\n", 
                method.name(), method.getFeePercentage() * 100);
        }
        
        Expense.PaymentMethod method = promptPaymentMethod();
        String category = promptCategory();
        
        try {
            manager.addExpense(amount, dateTime, method, category);
            System.out.println("Expense added successfully.");
        } catch (MonthlyLimitExceededException e) {
            System.out.println("Cannot add expense: " + e.getMessage());
        }
    }
    
    private void addCategory() {
        System.out.println("\nAdd New Category");
        String name = promptString("Category name: ");
        double limit = promptDouble("Monthly limit: ");
        
        manager.addCategory(name, limit);
        System.out.println("Category added successfully.");
    }
    
    private void removeCategory() {
        System.out.println("\nRemove Category");
        if (manager.getAllCategories().isEmpty()) {
            System.out.println("No categories exist.");
            return;
        }
        
        String name = promptCategory();
        manager.removeCategory(name);
        System.out.println("Category removed successfully.");
    }
    
    private void listCategories() {
        System.out.println("\nBudget Categories:");
        List<BudgetCategory> categories = manager.getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No categories exist.");
            return;
        }
        
        for (BudgetCategory category : categories) {
            System.out.printf("- %s (Limit: %.2f, Current: %.2f, %s)\n",
                category.getName(),
                category.getMonthlyLimit(),
                category.getCurrentExpenditure(),
                category.isOverLimit() ? "OVER LIMIT!" : "within limit");
        }
    }
    
    private void listTransactions() {
        System.out.println("\nAll Transactions:");
        List<Transaction> transactions = manager.getAllTransactions();
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    
    private void listExpensesByCategory() {
        System.out.println("\nExpenses by Category");
        if (manager.getAllCategories().isEmpty()) {
            System.out.println("No categories exist.");
            return;
        }
        
        String category = promptCategory();
        List<Expense> expenses = manager.getExpensesByCategory(category);
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded for this category.");
            return;
        }
        
        System.out.println("Expenses for category '" + category + "':");
        for (Expense e : expenses) {
            System.out.printf("- %s (Payment: %s, Effective: %.2f)\n",
                e.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                e.getPaymentMethod(),
                e.getEffectiveAmount());
        }
    }
    
    private void listLargeExpenses() {
        System.out.println("\nLarge Expenses");
        double threshold = promptDouble("Enter amount threshold: ");
        
        List<Expense> expenses = manager.getExpensesExceedingAmount(threshold);
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses exceed " + threshold);
            return;
        }
        
        System.out.println("Expenses exceeding " + threshold + ":");
        for (Expense e : expenses) {
            System.out.printf("- %.2f on %s (%s)\n",
                e.getAmount(),
                e.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                e.getCategory());
        }
    }
    
    private void listTransactionsByDate() {
        System.out.println("\nTransactions by Date Range");
        LocalDateTime start = promptDateTime("Enter start date (yyyy-mm-dd): ");
        LocalDateTime end = promptDateTime("Enter end date (yyyy-mm-dd): ");
        
        List<Transaction> transactions = manager.getTransactionsInPeriod(start, end);
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions in this period.");
            return;
        }
        
        System.out.println("Transactions between " + start.toLocalDate() + " and " + end.toLocalDate() + ":");
        for (Transaction t : transactions) {
            System.out.printf("- %s: %.2f (%s)\n",
                t.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                t.getEffectiveAmount(),
                t instanceof Income ? "Income" : "Expense");
        }
    }
    
    private void calculateNetEarnings() {
        System.out.println("\nNet Earnings Calculation");
        LocalDateTime start = promptDateTime("Enter start date (yyyy-mm-dd): ");
        LocalDateTime end = promptDateTime("Enter end date (yyyy-mm-dd): ");
        
        double net = manager.getNetEarnings(start, end);
        System.out.printf("Net earnings for period: %.2f\n", net);
    }
    
    private void resetCategory() {
        System.out.println("\nReset Category Expenditure");
        if (manager.getAllCategories().isEmpty()) {
            System.out.println("No categories exist.");
            return;
        }
        
        String name = promptCategory();
        for (BudgetCategory cat : manager.getAllCategories()) {
            if (cat.getName().equals(name)) {
                cat.resetExpenditure();
                System.out.println("Expenditure reset for category '" + name + "'");
                return;
            }
        }
    }
    
    private void exit() {
        running = false;
        System.out.println("Exiting Budget Management System. Goodbye!");
    }
    
    // Helper methods for user input
    private double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    
    private String promptString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private LocalDateTime promptDateTime() {
        LocalDate date = promptDate("Date (yyyy-mm-dd): ");
        LocalTime time = promptTime("Time (hh:mm): ");
        return LocalDateTime.of(date, time);
    }
    
    private LocalDateTime promptDateTime(String prompt) {
        LocalDate date = promptDate(prompt);
        return LocalDateTime.of(date, LocalTime.MIDNIGHT);
    }
    
    private LocalDate promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            }
        }
    }
    
    private LocalTime promptTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(scanner.nextLine(), TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use hh:mm.");
            }
        }
    }
    
    private Expense.PaymentMethod promptPaymentMethod() {
        while (true) {
            System.out.print("Payment method (CASH/CARD/ALIPAY/WECHAT): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Expense.PaymentMethod.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid payment method. Please try again.");
            }
        }
    }
    
    private String promptCategory() {
        List<BudgetCategory> categories = manager.getAllCategories();
        
        while (true) {
            System.out.println("Available categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, categories.get(i).getName());
            }
            
            System.out.print("Enter category name or number: ");
            String input = scanner.nextLine().trim();
            
            // Check if input is a number
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < categories.size()) {
                    return categories.get(index).getName();
                }
            } catch (NumberFormatException e) {
                // Not a number, proceed to check name
            }
            
            // Check if input matches a category name
            for (BudgetCategory cat : categories) {
                if (cat.getName().equalsIgnoreCase(input)) {
                    return cat.getName();
                }
            }
            
            System.out.println("Invalid category. Please try again.");
        }
    }
    
    public static void main(String[] args) {
        new BudgetCLI().start();
    }
}
