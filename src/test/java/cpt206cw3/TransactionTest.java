package cpt206cw3;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

abstract class TransactionTest {
    protected abstract Transaction createTransaction(double amount, LocalDateTime dateTime);
    
     @Test
    void shouldStoreBasicProperties() {
        LocalDateTime now = LocalDateTime.now();
        Transaction t = createTransaction(100.0, now);
        assertNotNull(t.getId());
        assertEquals(100.0, t.getAmount(), 0.001);
        assertEquals(now, t.getDateTime());
    }

    @Test
    void shouldRejectInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> 
            createTransaction(-50.0, LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> 
            createTransaction(0.0, LocalDateTime.now()));
    }

    @Test
    void shouldHandleExtremeDateTime() {
        assertDoesNotThrow(() -> 
            createTransaction(1.0, LocalDateTime.MIN));
        assertDoesNotThrow(() -> 
            createTransaction(1.0, LocalDateTime.MAX));
    }
}
