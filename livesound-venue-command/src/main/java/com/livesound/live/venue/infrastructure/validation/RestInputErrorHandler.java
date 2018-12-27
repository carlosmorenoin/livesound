package com.livesound.live.venue.infrastructure.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestInputErrorHandler {

	private MessageSource messageSource;

	@Autowired
	public RestInputErrorHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationError processValidationError(final MethodArgumentNotValidException e) {
		final ValidationError validationError  = new ValidationError();
		final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		final Locale locale = LocaleContextHolder.getLocale();

		for (FieldError err : fieldErrors) {
			validationError.addFieldError(err.getField(), messageSource.getMessage(err, locale));
		}
		return validationError;
	}
}
