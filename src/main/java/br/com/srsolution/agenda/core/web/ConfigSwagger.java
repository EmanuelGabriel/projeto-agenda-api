package br.com.srsolution.agenda.core.web;

import org.springdoc.webmvc.ui.SwaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		info = @Info(description = "Documentação da Agenda On-line - API", 
		termsOfService = "Termos de serviço",
title = "Agenda On-line - API", 
version = "1.0.0", 
contact = @Contact(name = "Emanuel Gabriel Sousa", email = "emanuel.gabriel.sousa@protonmail.com", url = "emanuelgabriel.github.io")))

@SecurityScheme(
		name = "agenda_oauth", 
		description = "Fluxo OAuth2",
type = SecuritySchemeType.OAUTH2,
scheme = "bearer",
bearerFormat = "JWT",
flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "agenda/oauth/token", authorizationUrl = "agenda/oauth/token")))
public class ConfigSwagger extends SwaggerConfig {

}

