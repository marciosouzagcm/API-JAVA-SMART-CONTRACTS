package blockchain.api_java_smart_contracts.repository;

import blockchain.api_java_smart_contracts.model.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade Eleitor.
 * Fornece métodos CRUD para operações de banco de dados.
 */
@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
    // Métodos de consulta personalizados podem ser adicionados aqui, se necessário
}