package statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import transactions.Transaction;
import transactions.TransactionCacheConfig;

@Service
public class StatisticsService {
	
	@Autowired
	TransactionCacheConfig transactionCacheConfig;
	
	ResponseEntity <Statistics> computeStatistics(){
		
		List<Transaction> transactions = transactionCacheConfig.getCacheData();
		transactions = filterOldTransactions(transactions);
		if(transactions.isEmpty()) {
			return new ResponseEntity<>(new Statistics(), HttpStatus.NO_CONTENT); //No Statistics to show
		}
		
		Statistics statistics = new Statistics();
		statistics.setSum(getSum(transactions));
		statistics.setCount(transactions.size());
		statistics.setAvg(statistics.getSum().divide(new BigDecimal(statistics.getCount()), 2, RoundingMode.CEILING));
		statistics.setMax(getMax(transactions));
		statistics.setMin(getMin(transactions));
		
		return new ResponseEntity<>(statistics, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param transactions
	 * @return all transaction objects where the time stamp is still no older than 60 seconds
	 */
	private List<Transaction> filterOldTransactions(List<Transaction> transactions) {
		return transactions.stream()
				.filter(t -> t.getTimestamp().toInstant().compareTo(Instant.now().minus(60, ChronoUnit.SECONDS)) >= 0)
				.collect(Collectors.toList());
	}
	
	private BigDecimal getSum(List<Transaction> transactions) {
		BigDecimal sum = new BigDecimal(0);
		for(Transaction t : transactions) {
			sum = sum.add(t.getAmount());
		}
		return toTwoDecimalFormat(sum);
	}
	
	private BigDecimal getMax(List<Transaction> transactions) {
		BigDecimal max = transactions.stream().map(Transaction::getAmount).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
		return toTwoDecimalFormat(max);
	}
	
	private BigDecimal getMin(List<Transaction> transactions) {
		BigDecimal min = transactions.stream().map(Transaction::getAmount).min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
		return toTwoDecimalFormat(min);
	}
	
	private BigDecimal toTwoDecimalFormat(BigDecimal value) {
		return value.setScale(2,RoundingMode.CEILING);
	}
}
