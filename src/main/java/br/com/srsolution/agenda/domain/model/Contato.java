package br.com.srsolution.agenda.domain.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Contato implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank
	@Size(min = 5, max = 90)
	private String nome;

	@Email
	@NotBlank
	private String email;

	private Boolean favorito;

	@NotBlank
	private String telefone;

	@Embedded
	private Endereco endereco;

}
