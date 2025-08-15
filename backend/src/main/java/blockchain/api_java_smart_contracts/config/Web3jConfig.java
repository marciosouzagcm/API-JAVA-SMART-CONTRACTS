package blockchain.api_java_smart_contracts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    // Endereço do seu nó Ethereum (ex: Ganache, Infura, geth local)
    private final String web3jNodeUrl = "http://localhost:8545/"; // Ajuste conforme seu nó

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3jNodeUrl));
    }
}