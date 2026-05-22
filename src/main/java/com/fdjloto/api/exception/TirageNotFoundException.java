package com.fdjloto.api.exception;

/**
 * Exception métier levée lorsqu'un tirage est introuvable.
 */
public class TirageNotFoundException extends RuntimeException {

    public TirageNotFoundException(String message) {
        super(message);
    }
}
