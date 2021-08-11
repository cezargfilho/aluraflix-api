package br.com.alura.aluraflix.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.aluraflix.controller.dto.TokenDto;
import br.com.alura.aluraflix.controller.form.LoginForm;
import br.com.alura.aluraflix.service.AuthService;

@RestController
@RequestMapping(value = "auth")
@Profile("prod")
public class AuthenticationController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authManager;

	@PostMapping
	public ResponseEntity<TokenDto> authenticate(@RequestBody @Valid LoginForm form) {
		TokenDto tokenDto = this.authService.authenticate(form, this.authManager);
		return ResponseEntity.ok(tokenDto);

	}

}
