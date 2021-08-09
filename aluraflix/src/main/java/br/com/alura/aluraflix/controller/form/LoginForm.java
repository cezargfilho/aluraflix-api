package br.com.alura.aluraflix.controller.form;

import javax.validation.constraints.Email;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {

	@Email
	private String email;

	private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(this.email, this.password);
	}

}
