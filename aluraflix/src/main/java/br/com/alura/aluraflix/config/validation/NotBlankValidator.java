package br.com.alura.aluraflix.config.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static br.com.alura.aluraflix.utils.ValidationsUtils.isNotNullAndBlank;

public class NotBlankValidator implements ConstraintValidator<NotBlankConstraint, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (isNotNullAndBlank(value) || value == null)
			return true;

		return false;
	}

}
