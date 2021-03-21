package br.com.srsolution.agenda.api.dtos.response;

import java.io.Serializable;

import br.com.srsolution.agenda.domain.model.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteModelResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private String nome;
	private String email;
	private String cpf;
	private Boolean ativo;

	public ClienteModelResponse() {
	}

	public ClienteModelResponse(Cliente cliente) {
		this.codigo = cliente.getCodigo();
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
		this.cpf = cliente.getCpf();
		this.ativo = cliente.getAtivo();
	}

}
