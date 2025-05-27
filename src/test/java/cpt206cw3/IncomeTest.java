package cpt206cw3;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class IncomeTest extends TransactionTest {
    protected Transaction createTransaction(double amount, LocalDateTime dateTime) {
        return new Income(amount, dateTime, "Test Source");
    }
    
    @Test
    void shouldStoreIncomeSource() {
        Income income = new Income(100.0, LocalDateTime.now(), "Salary");
        assertEquals("Salary", income.getSource());
    }
    
    @Test
    void shouldReturnPositiveEffectiveAmount() {
        Income income = new Income(100.0, LocalDateTime.now(), "Gift");
        assertEquals(100.0, income.getEffectiveAmount());
    }
    
}
