package transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@PostMapping("/transactions")
	public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction) { 
		
		return transactionService.createTransaction(transaction); 
	}
	
	@DeleteMapping("/transactions") 
	public ResponseEntity<Void> deleteAllTransactions() {
		return transactionService.deleteTransactions();
	}
}
