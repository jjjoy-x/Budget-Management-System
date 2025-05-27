package cpt206cw3;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest extends TransactionTest {
    protected Transaction createTransaction(double amount, LocalDateTime dateTime) {
        return new Expense(amount, dateTime, Expense.PaymentMethod.CASH, "Test Category");
    }
    
    @Test
    void shouldCalculateCorrectFees() {
        LocalDateTime now = LocalDateTime.now();
        
        // Test cash (0% fee)
        Expense cashExpense = new Expense(100.0, now, Expense.PaymentMethod.CASH, "Food");
        assertEquals(-100.0, cashExpense.getEffectiveAmount());
        
        // Test card (1% fee)
        Expense cardExpense = new Expense(100.0, now, Expense.PaymentMethod.CARD, "Food");
        assertEquals(-101.0, cardExpense.getEffectiveAmount());
        
        // Test Alipay (0.5% fee)
        Expense alipayExpense = new Expense(100.0, now, Expense.PaymentMethod.ALIPAY, "Food");
        assertEquals(-100.5, alipayExpense.getEffectiveAmount());
        
        // Test Wechat (0.5% fee)
        Expense weChatExpense = new Expense(100.0, now, Expense.PaymentMethod.WECHAT, "Food");
        assertEquals(-100.5, weChatExpense.getEffectiveAmount());
    }
    
    @Test
    void shouldStorePaymentMethodAndCategory() {
        Expense expense = new Expense(50.0, LocalDateTime.now(), 
                                    Expense.PaymentMethod.WECHAT, "Transport");
        
        assertEquals(Expense.PaymentMethod.WECHAT, expense.getPaymentMethod());
        assertEquals("Transport", expense.getCategory());
    }
    
}
