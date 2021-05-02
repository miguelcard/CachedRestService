package transactions;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Class to cache the transaction objects for the transaction service
 * And retrieve transaction data for statistics
 * @author migue
 *
 */
@Configuration
public class TransactionCacheConfig {
	//All the transaction instances will be saved in the cache and each one will be deleted after 60 seconds upon being cached
	private Cache<Long, Transaction> cache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
	private AtomicLong atomicLong = new AtomicLong(1);
	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * transaction object cached
	 * @param transaction
	 */
	public ResponseEntity<Void> createCache(Transaction transaction) {
		lock.lock();
		try {
			cache.put(atomicLong.getAndIncrement(), transaction);
		} finally {
			lock.unlock();
		}
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	public ResponseEntity<Void> clearAllCache(){
		cache.invalidateAll();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 
	 * @return list of current transaction objects, used by Statistics Service
	 */
	public List<Transaction> getCacheData(){
		return cache.asMap().values().stream().collect(Collectors.toList());
	}
}
