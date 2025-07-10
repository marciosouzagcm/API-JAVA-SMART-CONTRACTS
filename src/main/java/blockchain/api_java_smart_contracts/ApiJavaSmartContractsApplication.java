package blockchain.api_java_smart_contracts;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan; // Adicionado para escanear componentes

import com.votacao.contracts.ContractVoting; // <-- CORRIGIDO: De Voting para ContractVoting

import blockchain.api_java_smart_contracts.service.BlockchainService;

@SpringBootApplication
// Adicionado ComponentScan para garantir que o Spring encontre todos os seus Beans
@ComponentScan(basePackages = {"blockchain.api_java_smart_contracts", "blockchain.api_java_smart_contracts.config", "blockchain.api_java_smart_contracts.service"})
public class ApiJavaSmartContractsApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApiJavaSmartContractsApplication.class);

    @Autowired
    private BlockchainService blockchainService;

    public static void main(String[] args) {
        SpringApplication.run(ApiJavaSmartContractsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            logger.info("-----> MINHA APLICAÇÃO SPRING BOOT INICIOU COM SUCESSO! <-----");

            try {
                if (blockchainService != null) {
                    logger.info("Status da conexão Ethereum: {}", blockchainService.getConnectionStatus());

                    String contractAddress = blockchainService.getDeployedContractAddress();
                    if (contractAddress == null || contractAddress.isEmpty() || "0xENDERECO_FICTICIO".equals(contractAddress)) {
                        logger.info("Nenhum contrato Voting encontrado ou endereço fictício. Realizando deploy...");
                        // O método deployContract() no BlockchainService ainda retorna um Future de String fixa ("0xENDERECO_FICTICIO").
                        // Para um deploy real, você precisaria de uma lógica mais robusta.
                        contractAddress = blockchainService.deployContract().get();
                        logger.info("Contrato Voting (simulado) deployado no endereço: {}", contractAddress);
                    } else {
                        logger.info("Contrato Voting já existente no endereço: {}", contractAddress);
                    }

                    int totalVotos = blockchainService.getTotalVotos(contractAddress);
                    logger.info("Total de votos (simulado/fixo em 0) no contrato: {}", totalVotos);

                    @SuppressWarnings("unused")
                    ContractVoting votingContract = blockchainService.loadContract(contractAddress); // <-- CORRIGIDO: De Voting para ContractVoting
                    // Lembre-se: o método loadContract() no BlockchainService ainda retorna null,
                    // o que causará um NullPointerException se 'votingContract' for realmente usado.
                }

            } catch (InterruptedException | ExecutionException e) {
                logger.error("Erro durante a inicialização do contrato na blockchain: {}", e.getMessage(), e);
            }

            logger.info("Aplicação pronta para receber requisições.");
        };
    }
}