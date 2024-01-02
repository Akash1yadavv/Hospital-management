package com.hospital.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hospital.service.exception.ContactException;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler{
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> ExceptionpHandler(Exception ex, WebRequest req){
    	ex.printStackTrace();
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage("Something went wrong.");
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> ConstraintViolationExceptionpHandler(ConstraintViolationException ex, WebRequest req){
    	ex.printStackTrace();
    	ErrorDetails err = new ErrorDetails();
    	err.setTimestamp(LocalDateTime.now());
    	err.setMessage(ex.getMessage());
    	err.setDetails(req.getDescription(false));
    	return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    	
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> notFoundExceptionHandler(NotFoundException ex, WebRequest req){

        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);

    }
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleException(HttpMessageNotReadableException ex, WebRequest req) {
		StringBuilder msg = new StringBuilder(ex.getMessage());
		msg.replace(0, 16, "Invalid input");
		ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(msg.toString());
        err.setDetails(req.getDescription(false));

 
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
	
	
	@ExceptionHandler(OAuth2AuthenticationProcessingException.class)
    public ResponseEntity<ErrorDetails> handleException(OAuth2AuthenticationProcessingException ex, WebRequest req) {

		ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return ResponseEntity.badRequest().body(errors);
	}



    @ExceptionHandler(NameException.class)
    public ResponseEntity<NameExceptionDetails> exceptionpHandler(NameException ex, WebRequest req){


        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));


        return new ResponseEntity<NameExceptionDetails>(ex.getDetails(), HttpStatus.BAD_REQUEST);

    }
    
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorDetails> generalExceptionHandler(GeneralException ex,WebRequest req){


        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));


        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(AccessDeniedException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(JwtException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage("Invalid token");
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(NoHandlerFoundException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(DataIntegrityViolationException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        String[] msgs = ex.getRootCause().getMessage().split("'");
        if(msgs.length >= 0)
        err.setMessage(msgs[0].trim()+".");
        else 
        	err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(BadCredentialsException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(AppointmentException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(CategoryException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(DepartmentException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(EmailException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PatientException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(PatientException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SlotException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(SlotException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ContactException.class)
    public ResponseEntity<ErrorDetails> contactExceptionHandler(ContactException ex,WebRequest req){
        ErrorDetails err = new ErrorDetails();
        err.setTimestamp(LocalDateTime.now());
        err.setMessage(ex.getMessage());
        err.setDetails(req.getDescription(false));
        return new ResponseEntity<ErrorDetails>(err,HttpStatus.NOT_FOUND);
    }
}
