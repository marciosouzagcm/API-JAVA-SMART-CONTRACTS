package blockchain.api_java_smart_contracts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta classe é um controlador REST
public class MySimpleController {

    @GetMapping("/hello") // Mapeia requisições GET para o caminho "/hello"
    public String sayHello() {
        return "Olá do endpoint /hello da sua API Spring Boot!";
    }
}