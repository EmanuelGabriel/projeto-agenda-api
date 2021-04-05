package br.com.srsolution.agenda.api.dtos.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import br.com.srsolution.agenda.domain.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModelResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private String nome;
	private String email;
	private String cpf;
	private Boolean ativo;
	private LocalDateTime dataCadastro;

	public ClienteModelResponse(Cliente cliente) {
		this.codigo = cliente.getCodigo();
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
		this.cpf = cliente.getCpf();
		this.ativo = cliente.getAtivo();
		this.dataCadastro = cliente.getDataCadastro();
	}

}
