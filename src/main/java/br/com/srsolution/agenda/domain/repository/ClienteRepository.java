package br.com.srsolution.agenda.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.srsolution.agenda.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	List<Cliente> findByNomeStartingWithOrderByNome(String nome);

	Cliente findByEmail(String email);

	Cliente findByCpf(String cpf);

	// pode ser feito com a anotação 'Query'
	@Query("SELECT c FROM Cliente c WHERE c.ativo = true")
	List<Cliente> findByAtivo();

	// ou poderia ser feito assim:
	List<Cliente> findByAtivoTrue();

}
