package transactions;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Transaction {
	
	private BigDecimal amount; //should be able to be parsed to BigDecimal
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone="UTC")
	private Date timestamp; // in ISO 8601 //should be valid

	public Transaction() {
	}
	
	public Transaction(BigDecimal amount, Date timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
}
