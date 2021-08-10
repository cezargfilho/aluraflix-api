package br.com.alura.aluraflix.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.aluraflix.model.User;
import br.com.alura.aluraflix.repository.UserRepository;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserRepository userRepository;

	public TokenAuthenticationFilter(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = retrieveToken(request);
		boolean valid = tokenService.isValidToken(token);
		if (valid) {
			authenticateClient(token);
		}

		filterChain.doFilter(request, response);
	}
	
	private void authenticateClient(String token) {
		Long userId = tokenService.getUserId(token);
		Optional<User> optional = userRepository.findById(userId);
		if (!optional.isPresent()) {
			throw new UsernameNotFoundException("Autenticação inválida");
		}
		User user = optional.get();
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	private String retrieveToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.replaceFirst("Bearer ", "").trim();
	}

}
