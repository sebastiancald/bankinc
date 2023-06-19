package co.com.bankinc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ApiExceptionHandler {

	@ExceptionHandler({ BadRequestException.class, org.springframework.dao.DuplicateKeyException.class,
			org.springframework.web.HttpRequestMethodNotSupportedException.class,
			org.springframework.web.bind.MethodArgumentNotValidException.class,
			org.springframework.web.bind.MissingRequestHeaderException.class,
			org.springframework.web.bind.MissingServletRequestParameterException.class,
			org.springframework.web.bind.MethodArgumentNotValidException.class,
			org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
			org.springframework.http.converter.HttpMessageNotReadableException.class,
			org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
			org.hibernate.exception.SQLGrammarException.class
	 })

	public ResponseEntity<ErrorMessage> badRequestException(Exception e) {
		return new ResponseEntity<>(new ErrorMessage(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorMessage> notFoundException(Exception e) {
		return new ResponseEntity<>(new ErrorMessage(e.getMessage(), 404), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> exception(Exception e) {
		return new ResponseEntity<>(new ErrorMessage(e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
