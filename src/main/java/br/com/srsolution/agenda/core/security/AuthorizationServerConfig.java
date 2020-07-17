package br.com.srsolution.agenda.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	private static final String CHAVE_SECRETA = "@SRSOLUTIONLTDA@2020@EmanuelGabriel@2020@";

	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(CHAVE_SECRETA);
		return accessTokenConverter;
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints
			.tokenStore(this.tokenStore())
			.reuseRefreshTokens(false)
			.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		// configuração dos 'clients' - aplicações que terão acesso ao sistema
		clients
			.inMemory()
			.withClient("agenda-online") // clientId
			.secret("@654321") // clientSecret
			.scopes("read", "write") // scopes - leitura (read) e escrita (write)
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(1800) // duranção de SEGUNDOS da duração do token  - 1800/equivalente a 30 minutos
			.refreshTokenValiditySeconds(90) // 3600 * 24 = 24h PARA EXPIRAR
		
		   .and()
		   	.withClient("agenda-online-mobile")
		   	.secret("@654321")
		   	.scopes("read", "write") // scopes - leitura (read) e escrita (write)
		   	.authorizedGrantTypes("password", "refresh_token")
		   	.accessTokenValiditySeconds(1800) // duranção de SEGUNDOS da duração do token  - 1800/equivalente a 30 minutos
			.refreshTokenValiditySeconds(90); // 3600 * 24 = 24h PARA EXPIRAR
		
	}
	
	
}
