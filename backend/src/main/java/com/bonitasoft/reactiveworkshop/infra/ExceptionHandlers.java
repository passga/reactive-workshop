package com.bonitasoft.reactiveworkshop.infra;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.bonitasoft.reactiveworkshop.domain.Error;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = NOT_FOUND)
	public void handleNotFoundException(NotFoundException e) {
		// no content
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = BAD_REQUEST)
	public void handleThrowable(Exception e) {
		log.error("Error while processing", e);
		// no content
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Error> resolveAndWriteException(IllegalArgumentException e) throws IOException {
		Error error = new Error(e.getMessage());
		return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
	}

}
