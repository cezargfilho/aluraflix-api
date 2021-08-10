package br.com.alura.aluraflix.config.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.aluraflix.exception.NotValidURLException;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.InvalidColorCodeException;

@RestControllerAdvice
public class ErrorValidationHandler {

	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<FormErrorDto> handle(MethodArgumentNotValidException exception) {
		List<FormErrorDto> dtos = new ArrayList<>();
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

		fieldErrors.forEach(e -> {
			String msg = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			dtos.add(new FormErrorDto(e.getField(), msg));
		});

		return dtos;
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotValidURLException.class)
	public FormErrorDto handle(NotValidURLException exception) {
		return new FormErrorDto("url", exception.getMessage());
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public ErrorMessageDto handle(EntityNotFoundException exception) {
		return new ErrorMessageDto(exception.getMessage());
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidColorCodeException.class)
	public ErrorMessageDto handle(InvalidColorCodeException exception) {
		return new ErrorMessageDto(exception.getMessage());
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UsernameNotFoundException.class)
	public ErrorMessageDto handle(UsernameNotFoundException exception) {
		return new ErrorMessageDto(exception.getMessage());
	}
}
