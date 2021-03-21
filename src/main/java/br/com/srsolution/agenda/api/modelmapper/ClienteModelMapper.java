package br.com.srsolution.agenda.api.modelmapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.srsolution.agenda.api.dtos.request.ClienteModelInputRequest;
import br.com.srsolution.agenda.api.dtos.request.ClienteModelParcialRequest;
import br.com.srsolution.agenda.api.dtos.response.ClienteModelResponse;
import br.com.srsolution.agenda.domain.model.Cliente;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClienteModelMapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ModelMapper modelMapper;

	public ClienteModelResponse toModel(Cliente cliente) {
		return this.modelMapper.map(cliente, ClienteModelResponse.class);
	}

	public Cliente toDto(ClienteModelParcialRequest request) {
		return this.modelMapper.map(request, Cliente.class);
	}

	public Cliente toDto(ClienteModelInputRequest clienteModelInputRequest) {
		return this.modelMapper.map(clienteModelInputRequest, Cliente.class);
	}

	public List<ClienteModelResponse> toCollectionModelResponse(List<Cliente> clientes) {
		return clientes.stream().map(this::toModel).collect(Collectors.toList());
	}

}
