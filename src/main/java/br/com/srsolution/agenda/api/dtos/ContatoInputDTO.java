package br.com.srsolution.agenda.api.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.srsolution.agenda.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContatoInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 5, max = 90)
	private String nome;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String telefone;

	@NotNull
	private Endereco endereco;

}
