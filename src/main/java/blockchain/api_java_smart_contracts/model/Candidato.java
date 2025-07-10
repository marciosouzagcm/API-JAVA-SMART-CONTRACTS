package blockchain.api_java_smart_contracts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column; // Para configurações de coluna, como nullable, length, etc.
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger; // Manter para interações com a blockchain
import java.util.Objects;

/**
 * Entidade JPA para representar um Candidato.
 * Mapeia para a tabela 'candidatos' no banco de dados.
 */
@Entity // 1. Marca esta classe como uma entidade JPA, mapeando-a para uma tabela no BD
@Table(name = "candidatos") // Mapeia explicitamente para a tabela 'candidatos'
public class Candidato {

    @Id // 2. Indica que 'id' é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. Configura a geração automática do ID pelo banco de dados
    private Long id; // 4. Geralmente, IDs de banco de dados são Long ou Integer

    @NotBlank(message = "O nome do candidato é obrigatório")
    @Column(nullable = false, unique = true) // 5. 'nome' não pode ser nulo e deve ser único
    private String nome;

    @NotBlank(message = "A descrição do candidato é obrigatória")
    @Column(nullable = false) // 6. Descrição do candidato, não pode ser nula
    private String descricao; // Adicionando um campo 'descricao' para o candidato no DB

    // 7. O campo 'votos' deve ser gerenciado na blockchain, não persistido no banco de dados
    //    Este campo seria preenchido quando você busca os votos do contrato inteligente,
    //    mas não deve ser uma coluna da tabela 'candidato' no DB.
    //    Se você precisar persistir os votos totais, considere uma tabela separada ou um campo
    //    não mapeado para o JPA (`@Transient`). Para este exemplo, vamos removê-lo da persistência.
    // @Transient // Se você quisesse manter o campo 'votos' na classe, mas não no DB
    private transient BigInteger votosBlockchain; // 8. Renomeado para evitar confusão e indicar que vem da blockchain

    public Candidato() {
        // Construtor padrão sem argumentos é essencial para JPA
    }

    // 9. Ajustado o construtor para refletir os campos persistidos (id, nome, descricao)
    //    O ID geralmente é gerado pelo banco de dados, então não o passamos no construtor de criação.
    public Candidato(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.votosBlockchain = BigInteger.ZERO; // Inicializa votos da blockchain
    }

    // Construtor para casos onde você tem o ID (ex: ao buscar do DB)
    public Candidato(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.votosBlockchain = BigInteger.ZERO;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Getter e Setter para votos da blockchain
    public BigInteger getVotosBlockchain() {
        return votosBlockchain;
    }

    public void setVotosBlockchain(BigInteger votosBlockchain) {
        this.votosBlockchain = votosBlockchain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidato candidato = (Candidato) o;
        // Para entidades JPA, o 'equals' deve ser baseado no ID para consistência
        // Uma entidade é considerada igual a outra se tiverem o mesmo ID (após serem persistidas).
        // Isso é importante para coleções e para o comportamento do JPA.
        return id != null && Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {
        // Para entidades JPA, o 'hashCode' também deve ser baseado no ID.
        // Se o ID ainda não foi gerado (entidade nova), use um valor constante.
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Candidato{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", votosBlockchain=" + votosBlockchain +
                '}';
    }
}