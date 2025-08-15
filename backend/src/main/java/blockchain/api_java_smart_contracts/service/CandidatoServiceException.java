package blockchain.api_java_smart_contracts.service;

// Exceção personalizada para erros específicos do serviço de candidato
public class CandidatoServiceException extends Exception {

    public CandidatoServiceException(String message) {
        super(message);
    }

    public CandidatoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
