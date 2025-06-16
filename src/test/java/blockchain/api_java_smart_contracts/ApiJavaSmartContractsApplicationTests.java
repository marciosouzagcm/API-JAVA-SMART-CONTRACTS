package blockchain.api_java_smart_contracts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.votacao.api.ApiJavaSmartContractsApplication; // Importe sua classe principal

@SpringBootTest(classes = ApiJavaSmartContractsApplication.class) // Garante que sua aplicação principal seja carregada
class ApiJavaSmartContractsApplicationTests {

    @Test
    void contextLoads() {
        // Este método de teste é usado para verificar se o contexto da aplicação Spring
        // Boot
        // pode ser carregado com sucesso sem erros. Se o contexto carregar, o teste
        // passará.
        // Se houver qualquer problema na configuração ou nos beans da sua aplicação,
        // este teste irá falhar e indicar o erro.
    }
}