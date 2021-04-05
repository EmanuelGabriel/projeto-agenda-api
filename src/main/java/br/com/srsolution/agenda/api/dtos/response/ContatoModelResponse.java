package br.com.srsolution.agenda.api.dtos.response;

import java.io.Serializable;

import br.com.srsolution.agenda.domain.model.Contato;
import br.com.srsolution.agenda.domain.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContatoModelResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String email;
	private Boolean favorito;
	private String telefone;
	private Endereco endereco;

	public ContatoModelResponse(Contato contato) {
		this.nome = contato.getNome();
		this.email = contato.getEmail();
		this.favorito = contato.getFavorito();
		this.telefone = contato.getTelefone();
		this.endereco = contato.getEndereco();
	}

}
