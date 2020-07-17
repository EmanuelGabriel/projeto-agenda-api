package br.com.srsolution.agenda.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long codigo;
	private String nome;
	private String email;
	private String senha;
	private Boolean ativo;

}
