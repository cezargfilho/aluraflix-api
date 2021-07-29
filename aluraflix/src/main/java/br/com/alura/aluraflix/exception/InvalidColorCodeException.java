package br.com.alura.aluraflix.exception;

public class InvalidColorCodeException extends RuntimeException {

	private static final long serialVersionUID = 8881159231143332551L;

	public InvalidColorCodeException(String msg) {
		super(msg);
	}
}
