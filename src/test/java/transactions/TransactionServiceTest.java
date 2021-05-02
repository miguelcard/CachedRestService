package transactions;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.miguel.restservice.RestServiceApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
public class TransactionServiceTest {
	
	@Autowired
	TransactionService tService;
	
	 @Test
	   public void shoulReturnBadRequestIfObjectIncomplete() throws Exception {
		   
	      Transaction txn = new Transaction(null, Date.from(Instant.now()));
	      ResponseEntity<Void> response = tService.createTransaction(txn);
	      assertEquals(400, response.getStatusCodeValue());
	   }
	 
	 @Test
	   public void shoulReturnUnprocessableEntityCodeIfDateIsFuture() throws Exception {
		   
	      Transaction txn = new Transaction(new BigDecimal("12.334"),  Date.from(Instant.now().plus(5,ChronoUnit.SECONDS)));
	      ResponseEntity<Void> response = tService.createTransaction(txn);
	      assertEquals(422, response.getStatusCodeValue());
	   }
	 
	 @Test
	   public void shoulReturnNoContentCodeIfDateIsOlderThanSixtySeconds() throws Exception {
		   
	      Transaction txn = new Transaction(new BigDecimal("12.334"),  Date.from(Instant.now().minus(61,ChronoUnit.SECONDS)));
	      ResponseEntity<Void> response = tService.createTransaction(txn);
	      assertEquals(204, response.getStatusCodeValue());
	   }
	 
	 @Test
	   public void shouldReturnStatusCreatedIfTransactionObjectIsValid() throws Exception {
		   
	      Transaction txn = new Transaction(new BigDecimal("12.334"),  Date.from(Instant.now().minus(5,ChronoUnit.SECONDS)));
	      ResponseEntity<Void> response = tService.createTransaction(txn);
	      assertEquals(201, response.getStatusCodeValue());
	   }
}
