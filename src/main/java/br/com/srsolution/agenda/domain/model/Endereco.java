package br.com.srsolution.agenda.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String logradouro;

	@NotBlank
	private String numero;

	private String complemento;

	@NotBlank
	private String bairro;

	@NotBlank
	private String cep;

	@NotBlank
	private String cidade;

	@NotBlank
	private String estado;

}
