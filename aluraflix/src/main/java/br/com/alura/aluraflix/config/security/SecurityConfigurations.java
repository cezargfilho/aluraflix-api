package br.com.alura.aluraflix.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.aluraflix.enums.Role;
import br.com.alura.aluraflix.repository.UserRepository;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	private AuthenticationService autenticacaoService;
	private TokenService tokenService;
	private UserRepository userRepository;
	
	@Autowired
	public SecurityConfigurations(AuthenticationService autenticacaoService, TokenService tokenService, UserRepository userRepository) {
		this.autenticacaoService = autenticacaoService;
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers(HttpMethod.GET, "/videos/free").permitAll()
		.antMatchers(HttpMethod.DELETE, "/videos/*").hasRole(Role.ADMIN.value())
		.antMatchers(HttpMethod.DELETE, "/categorias/*").hasRole(Role.ADMIN.value())
		.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(
				new TokenAuthenticationFilter(this.tokenService, this.userRepository),
				UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers(
				"/**.html", "/v2/api-docs", "/webjars/**", 
				"/configuration/**", "/swagger-resources/**");
	}

}
