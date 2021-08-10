package br.com.alura.aluraflix.enums;

public enum Role {
	ADMIN("ADMIN"), USER("USER");

	private String role;

	Role(String role) {
		this.role = role;
	}

	public String value() {
		return role;
	}

}
