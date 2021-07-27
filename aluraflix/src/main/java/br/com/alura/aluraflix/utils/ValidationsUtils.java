package br.com.alura.aluraflix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.exception.InvalidColorCodeException;
import br.com.alura.aluraflix.exception.NotValidURLException;

public abstract class ValidationsUtils {

	private ValidationsUtils() {
	}

	public static String isUrlValid(String url) {
		if (new UrlValidator().isValid(url)) {
			return url;
		}
		throw new NotValidURLException(ExceptionMessages.URL_IS_INVALID);
	}

	public static boolean isNotBlank(String string) {
		if (string.isBlank()) {
		}
		return false;
	}

	public static String validateColor(String color) {
		Pattern collorPattern = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
		Matcher matcher = collorPattern.matcher(color);
		if (matcher.matches()) {
			return color;
		}
		throw new InvalidColorCodeException(ExceptionMessages.INVALID_COLOR_CODE);
	}

}
