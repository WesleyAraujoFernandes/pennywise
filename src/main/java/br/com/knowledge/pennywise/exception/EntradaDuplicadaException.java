package br.com.knowledge.pennywise.exception;

public class EntradaDuplicadaException extends RuntimeException {
    public EntradaDuplicadaException(String message) {
        super(message);
    }

    public EntradaDuplicadaException(String message, Throwable cause) {
        super(message, cause);
    }
}
