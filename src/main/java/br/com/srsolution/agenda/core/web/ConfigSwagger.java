package br.com.srsolution.agenda.core.web;

import org.springdoc.webmvc.ui.SwaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(description = "Documentação da API Restful Agenda On-line", 
title = "API Restful Agenda On-line", 
version = "1.0.0", 
contact = @Contact(name = "Emanuel Gabriel Sousa", email = "emanuel.gabriel.sousa@protonmail.com", url = "emanuelgabriel.github.io")))
public class ConfigSwagger extends SwaggerConfig {

}

