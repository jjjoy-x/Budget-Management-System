package cpt206cw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BudgetCategoryTest {
    private BudgetCategory category;
    
    @BeforeEach
    void setUp() {
        category = new BudgetCategory("Groceries", 500.0);
    }
    
    @Test
    void shouldInitializeWithZeroExpenditure() {
        assertEquals(0.0, category.getCurrentExpenditure());
    }
    
    @Test
    void shouldAddExpensesCorrectly() {
        category.addExpense(100.0);
        assertEquals(100.0, category.getCurrentExpenditure());
        
        category.addExpense(50.0);
        assertEquals(150.0, category.getCurrentExpenditure());
    }
    
    @Test
    void shouldDetectLimitExceeded() {
        category.addExpense(500.0);
        assertFalse(category.isOverLimit()); // exactly at limit
        
        category.addExpense(0.01);
        assertTrue(category.isOverLimit());
    }
    
    @Test
    void shouldResetExpenditure() {
        category.addExpense(300.0);
        category.resetExpenditure();
        assertEquals(0.0, category.getCurrentExpenditure());
    }
    
    @Test
    void shouldRejectNegativeExpenses() {
        assertThrows(IllegalArgumentException.class, () -> {
            category.addExpense(-50.0);
        });
    }
}
