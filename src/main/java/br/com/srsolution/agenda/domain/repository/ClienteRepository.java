package br.com.srsolution.agenda.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.srsolution.agenda.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	List<Cliente> findByAtivo(Boolean ativo);

	List<Cliente> findByNome(String nome);

	Cliente findByEmail(String email);

	Cliente findByCpf(String cpf);

}
