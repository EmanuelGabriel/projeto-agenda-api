package br.com.srsolution.agenda.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

@Data
@Entity
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank
	@Size(min = 5, max = 100)
	private String nome;

	@Email
	@NotBlank
	private String email;

	@CPF
	@NotBlank
	private String cpf;

	private Boolean ativo;

	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;

}
