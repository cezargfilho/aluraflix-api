package br.com.alura.aluraflix.exception;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -618274813773767842L;

	public EntityNotFoundException(String msg) {
		super(msg);
	}
}
