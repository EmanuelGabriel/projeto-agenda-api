package br.com.srsolution.agenda.api.dtos.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModelInputRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "nome", type = "string", format = "string", description = "nome do cliente", required = true, example = "Fulano")
	@NotBlank
	@Size(min = 5, max = 90)
	private String nome;

	@Schema(name = "email", type = "string", format = "string", description = "email do cliente", required = true, example = "email@email.com.br")
	@Email
	@NotBlank
	private String email;

	@Schema(name = "cpf", type = "string", format = "string", description = "CPF do cliente", required = true, example = "000.000.000-00")
	@CPF
	@NotBlank
	private String cpf;

}
