package cpt206cw3;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeCaseTest {
    
    @Test
    void shouldRejectEmptyCategoryName() {
        BudgetManager manager = new BudgetManager();
        assertThrows(IllegalArgumentException.class, () -> manager.addCategory("  ", 100));
    }

    @Test
    void shouldRejectZeroLimitCategory() {
        BudgetManager manager = new BudgetManager();
        assertThrows(IllegalArgumentException.class, () -> manager.addCategory("Zero", 0));
    }

    @Test
    void shouldRejectDuplicateCategory() {
        BudgetManager manager = new BudgetManager();
        manager.addCategory("Food", 500);
        assertThrows(IllegalArgumentException.class, () -> manager.addCategory("Food", 200));
    }

    @Test
    void shouldRejectExpenseInNonexistentCategory() {
        BudgetManager manager = new BudgetManager();
        LocalDateTime now = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () ->
            manager.addExpense(100, now, Expense.PaymentMethod.CASH, "Unknown"));
    }

    @Test
    void shouldSupportExtremeAmounts() {
        assertDoesNotThrow(() -> new Income(Double.MAX_VALUE, LocalDateTime.now(), "Huge"));
        assertDoesNotThrow(() -> new Expense(Double.MIN_VALUE, LocalDateTime.now(), Expense.PaymentMethod.CASH, "Small"));
    }
    @Test
    void shouldAutoResetExpenditureWhenMonthChanges() {
        BudgetCategory category = new BudgetCategory("Test", 100.0);
        category.addExpense(50.0);
        category.autoResetIfNewMonth(5);
        assertEquals(0.0, category.getCurrentExpenditure());

        category.addExpense(30.0);
        category.autoResetIfNewMonth(5);
        assertEquals(30.0, category.getCurrentExpenditure());

        category.autoResetIfNewMonth(6);
        assertEquals(0.0, category.getCurrentExpenditure());
}

    
}
