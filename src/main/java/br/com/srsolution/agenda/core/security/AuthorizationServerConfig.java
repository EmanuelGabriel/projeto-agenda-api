package br.com.srsolution.agenda.core.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Value("${security.jwt.signing-key}")
	private String assinatura_chave_secreta;

	@Value("${security.clients.angular.clientid}")
	private String chave_encoded_clientId;

	@Value("${security.clients.angular.secret}")
	private String chave_encoded_secret;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(assinatura_chave_secreta);
		return accessTokenConverter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		var tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
			.tokenStore(this.tokenStore())
			//.tokenEnhancer(tokenEnhancerChain)
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false)
			//.userDetailsService(userDetailsService)
			.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		// configuração dos 'Clients' - aplicações que terão acesso ao sistema
		clients.inMemory()
				.withClient(chave_encoded_clientId) // ClientId
				.secret(chave_encoded_secret) // clientSecret
				.scopes("read", "write") // ESCRITA E LEITURA
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800) // duranção de SEGUNDOS da duração do token 1800/equivalente a 30 minutos
				.refreshTokenValiditySeconds(90) // 3600 * 24 = 24h PARA EXPIRAR

					.and()
					
				.withClient("@gend@-m0bil3")
				.secret("m0b1l30")
				.scopes("read", "write") // scopes - leitura											
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800) // duranção de SEGUNDOS da duração do token
				.refreshTokenValiditySeconds(90); // 3600 * 24 = 24h PARA EXPIRAR

	}

}
