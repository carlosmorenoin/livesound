package com.livesound.live.venue.infrastructure.validation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationError {

	private List<FieldError> fieldErrors = new ArrayList<>();

	public ValidationError() {
		// Used by spring data
	}
	public void addFieldError(String path, String message) {
		FieldError error = new FieldError(path, message);
		this.fieldErrors.add(error);
	}
}
