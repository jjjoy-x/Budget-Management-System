package cpt206cw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BudgetManagerTest {
    private BudgetManager manager;
    private LocalDateTime testDate;
    
    @BeforeEach
    void setUp() {
        manager = new BudgetManager();
        manager.addCategory("Food", 500.0);
        manager.addCategory("Entertainment", 200.0);
        testDate = LocalDateTime.of(2023, 6, 15, 12, 0);
    }
    
    @Test
    void shouldAddIncomeCorrectly() {
        manager.addIncome(1000.0, testDate, "Salary");
        assertEquals(1, manager.getAllTransactions().size());
    }
    
    @Test
    void shouldAddExpenseWithinLimit() throws MonthlyLimitExceededException {
        manager.addExpense(300.0, testDate, Expense.PaymentMethod.CASH, "Food");
        assertEquals(1, manager.getAllTransactions().size());
    }
    
    @Test
    void shouldRejectExpenseOverLimit() {
        assertThrows(MonthlyLimitExceededException.class, () -> {
            manager.addExpense(600.0, testDate, Expense.PaymentMethod.CARD, "Food");
        });
    }
    
    @Test
    void shouldFilterExpensesByCategory() throws MonthlyLimitExceededException {
        manager.addExpense(100.0, testDate, Expense.PaymentMethod.CASH, "Food");
        manager.addExpense(150.0, testDate, Expense.PaymentMethod.ALIPAY, "Entertainment");
        
        assertEquals(1, manager.getExpensesByCategory("Food").size());
        assertEquals(1, manager.getExpensesByCategory("Entertainment").size());
    }
    
    @Test
    void shouldFilterLargeExpenses() throws MonthlyLimitExceededException {
        manager.addExpense(50.0, testDate, Expense.PaymentMethod.CASH, "Food");
        manager.addExpense(150.0, testDate, Expense.PaymentMethod.CARD, "Entertainment");
        
        assertEquals(1, manager.getExpensesExceedingAmount(100.0).size());
    }
    
    @Test
    void shouldFilterTransactionsByDate() {
        LocalDateTime start = testDate.minusDays(1);
        LocalDateTime end = testDate.plusDays(1);
        
        manager.addIncome(1000.0, testDate, "Salary");
        manager.addIncome(2000.0, testDate.plusDays(2), "Bonus"); // Outside range
        
        assertEquals(1, manager.getTransactionsInPeriod(start, end).size());
    }
    
    @Test
    void shouldCalculateNetEarnings() {
        manager.addIncome(1000.0, testDate, "Salary");
        manager.addIncome(500.0, testDate, "Bonus");
        
        try {
            manager.addExpense(300.0, testDate, Expense.PaymentMethod.CASH, "Food");
            manager.addExpense(200.0, testDate, Expense.PaymentMethod.CARD, "Entertainment");
        } catch (MonthlyLimitExceededException e) {
            fail("Should not throw exception");
        }
        
        double net = manager.getNetEarnings(testDate.minusDays(1), testDate.plusDays(1));
        assertEquals(998.0, net); // (1000+500) - (300+202) = 998
    }
    
    @Test
    void shouldHandleCategoryOperations() {
        manager.addCategory("Transport", 300.0);
        assertDoesNotThrow(() -> {
            manager.addExpense(100.0, testDate, Expense.PaymentMethod.CASH, "Transport");
        });
        
        manager.removeCategory("Transport");
        assertThrows(IllegalArgumentException.class, () -> {
            manager.addExpense(100.0, testDate, Expense.PaymentMethod.CASH, "Transport");
        });
    }
}
