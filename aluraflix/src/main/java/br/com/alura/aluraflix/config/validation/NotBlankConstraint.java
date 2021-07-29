package br.com.alura.aluraflix.config.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import br.com.alura.aluraflix.exception.ExceptionMessages;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { NotBlankValidator.class })
public @interface NotBlankConstraint {

	String message() default ExceptionMessages.FIELD_CANT_BE_BLANK;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
