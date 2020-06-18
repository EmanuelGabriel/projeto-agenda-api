package br.com.srsolution.agenda.api.modelmapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.srsolution.agenda.api.dtos.ClienteDTO;
import br.com.srsolution.agenda.api.dtos.ClienteInputDTO;
import br.com.srsolution.agenda.domain.model.Cliente;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClienteModelMapper {

	private final ModelMapper modelMapper;

	public ClienteDTO toModel(Cliente cliente) {
		return this.modelMapper.map(cliente, ClienteDTO.class);
	}

	public Cliente toDto(ClienteDTO clienteDTO) {
		return this.modelMapper.map(clienteDTO, Cliente.class);
	}

	public Cliente toDto(ClienteInputDTO clienteInputDTO) {
		return this.modelMapper.map(clienteInputDTO, Cliente.class);
	}

	public List<ClienteDTO> toCollectionModel(List<Cliente> clientes) {
		return clientes.stream().map(this::toModel).collect(Collectors.toList());
	}

}
