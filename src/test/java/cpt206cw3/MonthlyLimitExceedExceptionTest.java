package cpt206cw3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonthlyLimitExceededExceptionTest {
    @Test
    void shouldContainCorrectDetailsInMessage() {
        String category = "Food";
        double limit = 500.0;
        double attempted = 600.0;
        
        MonthlyLimitExceededException exception = 
            new MonthlyLimitExceededException(category, limit, attempted);
        
        assertTrue(exception.getMessage().contains(category));
        assertTrue(exception.getMessage().contains(String.valueOf(limit)));
        assertTrue(exception.getMessage().contains(String.valueOf(attempted)));
        
        assertEquals(category, exception.getCategoryName());
        assertEquals(limit, exception.getLimit());
        assertEquals(attempted, exception.getAttemptedAmount());
    }
    
    @Test
    void shouldHandleExtremeValues() {
        String message = new MonthlyLimitExceededException("Test", 
            Double.MAX_VALUE, Double.MAX_VALUE).getMessage();
        assertNotNull(message);
    }
}
