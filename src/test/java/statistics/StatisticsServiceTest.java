package statistics;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.miguel.restservice.RestServiceApplication;

import transactions.Transaction;
import transactions.TransactionCacheConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestServiceApplication.class)
public class StatisticsServiceTest {

	@Autowired
	StatisticsService statisticsService;
	
	@Autowired 
	TransactionCacheConfig cacheConfig;
	
	@Before
	   public void setUp() {
		cacheConfig.clearAllCache();
	   }
	
	@Test
	   public void shoulVerifyStatisticsDataAreCorrect() throws Exception {
		  
		List<Transaction> transactions = getTransactionsList();
		transactions.forEach(t -> cacheConfig.createCache(t));
		
		ResponseEntity <Statistics> stats = statisticsService.computeStatistics();
		assertEquals(format(new BigDecimal("20")), stats.getBody().getSum());
		assertEquals(4, stats.getBody().getCount());
		assertEquals(format(new BigDecimal("5")), stats.getBody().getAvg());
		assertEquals(format(new BigDecimal("8")), stats.getBody().getMax());
		assertEquals(format(new BigDecimal("2")), stats.getBody().getMin());
		
	   }
	
	private List<Transaction> getTransactionsList(){
			List<Transaction> list = new ArrayList<Transaction>();
			list.add(new Transaction(new BigDecimal("2"), Date.from(Instant.now())));
			list.add(new Transaction(new BigDecimal("4"), Date.from(Instant.now())));
			list.add(new Transaction(new BigDecimal("6"), Date.from(Instant.now())));
			list.add(new Transaction(new BigDecimal("8"), Date.from(Instant.now())));
		return list;
	}
	
	private BigDecimal format(BigDecimal value) {
		return value.setScale(2,RoundingMode.CEILING);
	}

}
