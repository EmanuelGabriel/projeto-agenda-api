package br.com.srsolution.agenda.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.srsolution.agenda.domain.service.usuario.UsuarioService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioService usuarioService;
	
	  private static final String[] PUBLICO_MATCHERS = {
	    		"/v2/api-docs/**",
	    		"/api-docs/**",
	    		"/swagger-ui.html",
	    		"/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/agenda/api-docs",
	    		"/swagger-ui/**"};

	
		@Bean
		public AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		// Gerenciamento de autenticação da aplicação (os usuários do sistema em memória)
		auth
			.inMemoryAuthentication()
			.withUser("admin")
			.password("admin")
			.roles("USER");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// autorização de urls, habilitação de CORS
		http
			.csrf().disable()
			.cors()
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Não vai guardar sessão (sem estado)
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(PUBLICO_MATCHERS);
	}
	
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

}
