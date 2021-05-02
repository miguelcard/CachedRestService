package handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
		
		// If the exception was due to a deserialization error / a not parsable field then it returns a 422 status
		if(ex.getRootCause().getClass().equals(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)) {
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		// else the JSON format is invalid and it returns a 404 status
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
}
