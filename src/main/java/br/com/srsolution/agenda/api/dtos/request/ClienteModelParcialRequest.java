package br.com.srsolution.agenda.api.dtos.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModelParcialRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "nome", type = "string", format = "string", description = "nome do cliente", required = true, example = "Fulano")
	@NotBlank
	private String nome;

	@Schema(name = "email", type = "string", format = "string", description = "e-mail do cliente", required = true, example = "email@email.com.br")
	@Email
	private String email;

}
