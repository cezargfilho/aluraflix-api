package br.com.alura.aluraflix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.aluraflix.config.security.TokenService;
import br.com.alura.aluraflix.controller.dto.TokenDto;
import br.com.alura.aluraflix.controller.form.LoginForm;

@Service
@Profile("prod")
public class AuthService {

	private TokenService tokenService;

	@Autowired
	public AuthService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	public TokenDto authenticate(LoginForm form, AuthenticationManager authManager) {

		UsernamePasswordAuthenticationToken login = form.converter();
		try {
			Authentication authentication = authManager.authenticate(login);
			String token = tokenService.generateToken(authentication);
			return new TokenDto(token, "Bearer");

		} catch (AuthenticationException e) {
			throw new UsernameNotFoundException("Credenciais inválidas");
		}
	}

}
