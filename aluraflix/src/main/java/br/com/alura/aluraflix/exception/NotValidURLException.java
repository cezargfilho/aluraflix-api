package br.com.alura.aluraflix.exception;

public class NotValidURLException extends RuntimeException {

	private static final long serialVersionUID = 7386344751703885710L;

	@Override
	public String getMessage() {
		return "Url is invalid";
	}

	public NotValidURLException() {
		super();
	}

	public NotValidURLException(String msg) {
		super(msg);
	}

	public NotValidURLException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
