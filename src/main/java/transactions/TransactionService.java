package transactions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
	
	@Autowired 
	TransactionCacheConfig transactioCacheConfig;
	
	public ResponseEntity<Void> createTransaction(Transaction transaction) { 
		
		//Checking conditions before saving the transaction information and returning the respective statuses
		if(!checkIsValidJsonObject(transaction)) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
		if(!checkDateIsNotFuture(transaction.getTimestamp())) {
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		if(olderThanSixtySeconds(transaction.getTimestamp())) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		
		return transactioCacheConfig.createCache(transaction);
	}
	
	public ResponseEntity<Void> deleteTransactions(){
		
		return transactioCacheConfig.clearAllCache();
	}

	private boolean checkIsValidJsonObject(Transaction transaction) {
		return (transaction.getAmount() == null || transaction.getTimestamp() == null)? false : true;
	}
	
	private boolean checkDateIsNotFuture(Date date) {
		return (Date.from(Instant.now()).compareTo(date) >= 0)? true : false;
	}
	
	private boolean olderThanSixtySeconds(Date date ) {
		return (Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)).compareTo(date) >= 0)? true : false;
	}

}
