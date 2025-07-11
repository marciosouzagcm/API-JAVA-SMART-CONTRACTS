package blockchain.api_java_smart_contracts;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Adicione esta importação
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

// Remova esta importação, pois o ContractVoting não será mais injetado diretamente aqui
// import com.votacao.contracts.ContractVoting;

import blockchain.api_java_smart_contracts.service.BlockchainService;
import blockchain.api_java_smart_contracts.service.BlockchainServiceException;

@SpringBootApplication
@ComponentScan(basePackages = { "blockchain.api_java_smart_contracts", "blockchain.api_java_smart_contracts.config",
        "blockchain.api_java_smart_contracts.service" })
public class ApiJavaSmartContractsApplication {

    private static final Logger logger = LoggerFactory.getLogger(ApiJavaSmartContractsApplication.class);

    @Autowired
    private BlockchainService blockchainService;

    // Injetar o valor da propriedade do application.properties
    @Value("${blockchain.voting.contract-address}")
    private String configuredContractAddress;

    public static void main(String[] args) {
        SpringApplication.run(ApiJavaSmartContractsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            logger.info("-----> MINHA APLICAÇÃO SPRING BOOT INICIOU COM SUCESSO! <-----");

            try {
                // Verificar se o serviço de blockchain foi injetado
                if (blockchainService == null) {
                    logger.error("BlockchainService não foi injetado. Verifique a configuração do Spring.");
                    return; // Aborta se o serviço não estiver disponível
                }

                logger.info("Status da conexão Ethereum: {}", blockchainService.getConnectionStatus());

                // Obtém o endereço do contrato configurado (pode ser vazio, fictício ou real)
                String currentContractAddress = configuredContractAddress;

                // Lógica para deploy ou carregamento do contrato
                if (currentContractAddress == null || currentContractAddress.trim().isEmpty() || "0xENDERECO_FICTICIO".equalsIgnoreCase(currentContractAddress)) {
                    logger.info("Nenhum contrato Voting válido encontrado na configuração. Realizando deploy...");
                    // CHAME O NOVO MÉTODO QUE FAZ O DEPLOY REAL!
                    currentContractAddress = blockchainService.deployContractAndReturnAddress().join(); // .join() espera a conclusão
                    logger.info("Contrato Voting deployado e configurado no endereço: {}", currentContractAddress);
                } else {
                    logger.info("Contrato Voting encontrado no endereço configurado: {}. Carregando...", currentContractAddress);
                    // Carrega o contrato existente se o endereço for fornecido e não for fictício
                    blockchainService.loadContract(currentContractAddress);
                }

                // Agora, interaja com o contrato usando os métodos do blockchainService
                // que, internamente, usará a instância correta (deployada ou carregada).
                BigInteger totalVotos = blockchainService.getTotalVotos().get(); // .get() para esperar o resultado do CompletableFuture
                logger.info("Total de votos no contrato: {}", totalVotos);

            } catch (InterruptedException | ExecutionException | BlockchainServiceException e) {
                logger.error("Erro durante a inicialização/interação com o contrato na blockchain: {}", e.getMessage(), e);
                // Para depuração, também imprima o stack trace completo
                logger.error("Stack trace completo do erro:", e);
            } catch (IllegalStateException e) {
                 logger.error("Erro de estado ilegal: Contrato Voting não foi inicializado corretamente. {}", e.getMessage(), e);
            } catch (Exception e) { // Catch-all para qualquer outra exceção inesperada
                logger.error("Ocorreu um erro inesperado durante a inicialização da aplicação: {}", e.getMessage(), e);
                logger.error("Stack trace completo do erro inesperado:", e);
            }

            logger.info("Aplicação pronta para receber requisições.");
        };
    }
}