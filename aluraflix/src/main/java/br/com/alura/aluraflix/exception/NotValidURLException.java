package br.com.alura.aluraflix.exception;

public class NotValidURLException extends RuntimeException {

	private static final long serialVersionUID = 7386344751703885710L;

	public NotValidURLException() {
		super(ExceptionMessages.URL_IS_INVALID);
	}

	public NotValidURLException(String msg) {
		super(msg);
	}

	public NotValidURLException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
