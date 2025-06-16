package com.votacao.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import blockchain.api_java_smart_contracts.service.VotingService; // Certifique-se de que este import está correto

@SpringBootApplication(scanBasePackages = {
        "com.votacao.api",
        "blockchain.api_java_smart_contracts.service",
        "blockchain.api_java_smart_contracts.config",
        "blockchain.api_java_smart_contracts.controller"
})
public class ApiJavaSmartContractsApplication implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getLogger(ApiJavaSmartContractsApplication.class.getName());

    // CORRIGIDO: Nome da propriedade alterado para corresponder ao
    // application.properties
    @Value("${blockchain.ethereum.node-url}")
    private String nodeUrl;

    @Value("${blockchain.voting.contract-address}")
    private String contractAddress;

    @Autowired
    @Qualifier("web3jPrincipal") // Este qualificador está OK, pois corresponde ao bean na BlockchainConfig
    private Web3j web3j;

    @Autowired
    @Qualifier("credentialsContract") // CORRIGIDO: Qualificador alterado para corresponder ao bean na
                                      // BlockchainConfig
    private Credentials credentials;

    @Autowired
    private VotingService votingService; // Injeta o serviço de votação

    public static void main(String[] args) {
        SpringApplication.run(ApiJavaSmartContractsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            // Verifica a versão do cliente Ethereum (o nó ao qual você está conectado)
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            System.out.println("Versão do cliente Ethereum: " + clientVersion.getWeb3ClientVersion());
            LOGGER.log(Level.INFO, "Conectado ao nó Ethereum: {0}", nodeUrl);

            // Verifica o saldo da conta que está a ser usada para interagir com o contrato
            if (credentials != null) {
                EthGetBalance balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                        .send();
                LOGGER.log(Level.INFO, "Saldo da conta {0}: {1} Wei",
                        new Object[] { credentials.getAddress(), balance.getBalance() });
            } else {
                LOGGER.log(Level.WARNING, "Credenciais não carregadas, não foi possível verificar o saldo.");
            }

            // Registra o endereço do contrato de votação
            LOGGER.log(Level.INFO, "Endereço do contrato de votação: {0}", contractAddress);

            // Interage com o contrato se o VotingService foi injetado com sucesso
            if (votingService != null) {
                interagirComContrato();
            } else {
                LOGGER.log(Level.SEVERE, "VotingService não foi injetado. Verifique a configuração.");
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro de comunicação com o nó Ethereum: " + e.getMessage(), e);
        } catch (Exception e) { // Captura qualquer outra exceção não IO
            LOGGER.log(Level.SEVERE,
                    "Erro inesperado na inicialização ou interação com a blockchain: " + e.getMessage(), e);
        }
    }

    private void interagirComContrato() {
        try {
            LOGGER.log(Level.INFO, "Tentando adicionar candidato 'Candidato Teste'...");
            // Exemplo: adicionando um candidato de teste
            votingService.adicionarCandidato("Candidato Teste");
            System.out.println("Candidato 'Candidato Teste' adicionado com sucesso!");
            LOGGER.log(Level.INFO, "Candidato 'Candidato Teste' adicionado com sucesso!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao interagir com o contrato: " + e.getMessage(), e);
            e.printStackTrace(); // Imprime a stack trace para depuração
        }
    }
}