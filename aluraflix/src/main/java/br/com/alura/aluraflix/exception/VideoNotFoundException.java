package br.com.alura.aluraflix.exception;

public class VideoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -618274813773767842L;

	public VideoNotFoundException() {
		super("Video not found");
	}

	public VideoNotFoundException(String msg) {
		super(msg);
	}
}
