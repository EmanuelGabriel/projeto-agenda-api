package br.com.srsolution.agenda.api.dtos.request;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModelParcialRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String nome;

	@Email
	private String email;

}
