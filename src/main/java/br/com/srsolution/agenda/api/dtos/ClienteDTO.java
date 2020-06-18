package br.com.srsolution.agenda.api.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 5, max = 90)
	private String nome;

	@Email
	@NotBlank
	private String email;

	@CPF
	@NotBlank
	private String cpf;

	private Boolean ativo;

}
