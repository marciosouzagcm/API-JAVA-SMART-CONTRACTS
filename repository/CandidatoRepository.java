package blockchain.api_java_smart_contracts.repository;

import blockchain.api_java_smart_contracts.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Candidato.
 * Fornece métodos CRUD para operações de banco de dados.
 */
@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    // Métodos de consulta personalizados podem ser adicionados aqui, se necessário
}